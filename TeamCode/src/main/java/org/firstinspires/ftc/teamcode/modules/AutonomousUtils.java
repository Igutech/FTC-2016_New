package org.firstinspires.ftc.teamcode.modules;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.BeaconState;
import org.firstinspires.ftc.teamcode.ColorSensorData;
import org.firstinspires.ftc.teamcode.Hardware;

import java.util.Timer;

/**
 * Created by Kevin on 10/6/2016.
 */
public class AutonomousUtils {

    public static void driveEncoderFeet(float feet, float power) {
        driveEncoderTicks((int)(feet*460f), power);
    }

    public static void driveEncoderFeet(float feet, float power, boolean rampUp) {
        driveEncoderTicks((int)(feet*460f), power, rampUp);
    }

    public static void driveEncoderTicks(int ticks, float power) { //460 ticks per foot
        driveEncoderTicks(ticks, power, true);
    }

    public static void driveEncoderTicks(int ticks, float power, boolean rampUp) { //460 ticks per foot
        try {
            Hardware.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Hardware.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            while (Hardware.left.getMode() != DcMotor.RunMode.RUN_TO_POSITION && Hardware.right.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
                Thread.sleep(10);
            }
            Hardware.right.setTargetPosition(ticks);
            Hardware.left.setTargetPosition(ticks);

            if (rampUp) {
                Hardware.right.setPower(0);
                Hardware.left.setPower(0);
            } else {
                Hardware.right.setPower(power);
                Hardware.left.setPower(power);
            }

            long startTime = System.currentTimeMillis()/1000;
            while (Hardware.left.getCurrentPosition() < Hardware.left.getTargetPosition() || Hardware.right.getCurrentPosition() < Hardware.right.getTargetPosition()) {
                if (rampUp) {
                    long currentTime = System.currentTimeMillis() / 1000;
                    long elapsed = Math.abs(currentTime - startTime);
                    double speed = (double) elapsed;
                    if (speed > 1) {
                        speed = 1;
                    }
                    Hardware.right.setPower(speed);
                    Hardware.left.setPower(speed);
                } else {
                    Thread.sleep(10);
                }
            }

            Hardware.right.setPower(0);
            Hardware.left.setPower(0);

            Hardware.left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            Hardware.right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetEncoders() {
        Hardware.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Hardware.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public static ColorSensorData getColorSensorData(int id) {
        if (id == 0) {
            //return new ColorSensorData(Hardware.colorSensor0.red(), Hardware.colorSensor0.green(), Hardware.colorSensor0.blue(), Hardware.colorSensor0.alpha());
            return null;
        }
        if (id == 1) {
            return new ColorSensorData(Hardware.colorSensor1.red(), Hardware.colorSensor1.green(), Hardware.colorSensor1.blue(), Hardware.colorSensor1.alpha());
        }
        if (id == 2) {
            return new ColorSensorData(Hardware.colorSensor2.red(), Hardware.colorSensor2.green(), Hardware.colorSensor2.blue(), Hardware.colorSensor2.alpha());
        }
        return null;
    }

    private static float standardDeviation(ColorSensorData data) {
        float red = data.getRed();
        float green = data.getGreen();
        float blue = data.getBlue();

        float mean = (red+green+blue)/3;

        float sum = Math.abs(red-mean)+Math.abs(green-mean)+Math.abs(blue-mean);
        float sumOverTotal = sum/3;
        float stdev = (float) Math.sqrt((double)sumOverTotal);
        return stdev;
    }

    public static BeaconState getBeaconState(ColorSensorData data) {
        if (data.getRed() > data.getBlue()) {
            return BeaconState.RED;
        } else {
            return BeaconState.BLUE;
        }
    }

    public static float getBeaconConfidence(ColorSensorData data) {
        return standardDeviation(data);
    }
}
