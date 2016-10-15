package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;

/**
 * Created by Kevin on 10/9/2016.
 */
public class ColorDetectionThread implements Runnable {

    BeaconState beaconState;

    private float confidenceThreshold;

    private float redThresh;
    private float blueThresh;
    private float greenThresh;

    public ColorDetectionThread(float confidenceThreshold, float redThresh, float blueThresh, float greenThresh) {
        this.setConfidenceThreshold(confidenceThreshold);
        this.redThresh = redThresh;
        this.blueThresh = blueThresh;
        this.greenThresh = greenThresh;
    }

    @Override
    public void run() {
        beaconState = BeaconState.UNSURE;

        boolean done = false;
        while (!done) {
            ColorSensorData data = AutonomousUtils.getColorSensorData(0);

            boolean white = true;
            /*
            if (AutonomousUtils.getBeaconConfidence(data) < confidenceThreshold) {
                if (data.getRed() > redThresh && data.getBlue() > blueThresh && data.getGreen() > greenThresh) {
                    white = true;
                }
            }
            */

            if (white) {
                ColorSensorData data1 = AutonomousUtils.getColorSensorData(1);
                ColorSensorData data2 = AutonomousUtils.getColorSensorData(2);

                BeaconState state1 = AutonomousUtils.getBeaconState(data1);
                BeaconState state2 = AutonomousUtils.getBeaconState(data2);

                if (AutonomousUtils.getBeaconConfidence(data1) < confidenceThreshold) {
                    state1 = BeaconState.UNSURE;
                }

                if (AutonomousUtils.getBeaconConfidence(data2) < confidenceThreshold) {
                    state2 = BeaconState.UNSURE;
                }

                if (state1.equals(BeaconState.RED) && state2.equals(BeaconState.BLUE)) {
                    beaconState = BeaconState.REDBLUE;
                    done = true;
                } else if (state1.equals(BeaconState.BLUE) && state2.equals(BeaconState.RED)) {
                    beaconState = BeaconState.BLUERED;
                    done = true;
                } else if (state1.equals(BeaconState.RED) && state2.equals(BeaconState.RED)) {
                    beaconState = BeaconState.BOTHRED;
                    done = true;
                } else if (state1.equals(BeaconState.BLUE) && state2.equals(BeaconState.BLUE)) {
                    beaconState = BeaconState.BOTHBLUE;
                    done = true;
                }

                if (state1.equals(BeaconState.UNSURE) || state2.equals(BeaconState.UNSURE)) {
                    beaconState = BeaconState.UNSURE;
                    done = true;
                }
            }
        }
    }

    public BeaconState getState() {
        return beaconState;
    }

    public void setConfidenceThreshold(float threshold) {
        this.confidenceThreshold = threshold;
    }
}
