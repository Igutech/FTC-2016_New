package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;

/**
 * Created by Kevin on 10/9/2016.
 */
public class ColorDetectionThread implements Runnable {

    BeaconState beaconState;

    private float confidenceThreshold;

    public ColorDetectionThread(float confidenceThreshold) {
        this.setConfidenceThreshold(confidenceThreshold);
    }

    @Override
    public void run() {
        beaconState = BeaconState.UNSURE;

        BeaconState firstpass = BeaconState.UNSURE;
        BeaconState secondpass = BeaconState.UNSURE;

        boolean done = false;
        while (!done) {
            ColorSensorData data = AutonomousUtils.getColorSensorData();
            if (AutonomousUtils.getBeaconConfidence(data) > confidenceThreshold) { //this logic doesn't quite work yet, needs some more refining.
                if (firstpass.equals(BeaconState.UNSURE)) {
                    firstpass = AutonomousUtils.getBeaconState(data);
                } else {
                    secondpass = AutonomousUtils.getBeaconState(data);
                    done=true;
                }
            }

            if (firstpass != BeaconState.UNSURE && secondpass != BeaconState.UNSURE) {
                if (firstpass == BeaconState.RED && secondpass == BeaconState.BLUE) {
                    beaconState = BeaconState.RED;
                }
                if (firstpass == BeaconState.BLUE && secondpass == BeaconState.RED) {
                    beaconState = BeaconState.BLUE;
                }

                if (firstpass == BeaconState.RED && secondpass == BeaconState.RED) {
                    beaconState = BeaconState.BOTHRED;
                }
                if (firstpass == BeaconState.BLUE && secondpass == BeaconState.BLUE) {
                    beaconState = BeaconState.BOTHBLUE;
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
