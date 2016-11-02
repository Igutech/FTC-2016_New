package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;

import java.util.ArrayList;

/**
 * Created by Kevin on 10/19/2016.
 */
public class CalibrationThread implements Runnable {

    private long targetTime;
    private ArrayList<ColorSensorData> data;

    private ElapsedTime time;

    private float redtotal = 0;
    private float bluetotal = 0;
    private float greentotal = 0;

    private int totalmeaurements = 0;

    private float red;
    private float blue;
    private float green;

    public CalibrationThread(long time) {
        this.targetTime = time;
    }

    @Override
    public void run() {

        time.reset();

        boolean done = false;
        while (!done) {

            ColorSensorData data1 = AutonomousUtils.getColorSensorData(1);
            ColorSensorData data2 = AutonomousUtils.getColorSensorData(2);

            data.add(data1);
            data.add(data2);

            if (time.milliseconds() > this.targetTime) {
                done=true;
            }
        }

        for (ColorSensorData sensorData : data) {
            redtotal += sensorData.getRed();
            bluetotal += sensorData.getBlue();
            greentotal += sensorData.getGreen();

            totalmeaurements++;
        }

        red = redtotal/totalmeaurements;
        blue = bluetotal/totalmeaurements;
        green = greentotal/totalmeaurements;

    }

    public float getRed() {
        return red;
    }

    public float getBlue() {
        return blue;
    }

    public float getGreen() {
        return green;
    }
}
