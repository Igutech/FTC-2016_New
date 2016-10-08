package org.firstinspires.ftc.teamcode.modules;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.BeaconState;
import org.firstinspires.ftc.teamcode.ColorSensorData;
import org.firstinspires.ftc.teamcode.Hardware;

/**
 * Created by Kevin on 10/6/2016.
 */
public class AutonomousUtils {
    public static void driveEncoderTicks(int ticks, float power) { //460 ticks per foot
        try {
            Hardware.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Hardware.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            while (Hardware.left.getMode() != DcMotor.RunMode.RUN_TO_POSITION && Hardware.right.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
                Thread.sleep(10);
            }
            Hardware.right.setTargetPosition(ticks);
            Hardware.left.setTargetPosition(ticks);

            Hardware.right.setPower(power);
            Hardware.left.setPower(power);

            while (Hardware.left.getCurrentPosition() < Hardware.left.getTargetPosition() || Hardware.right.getCurrentPosition() < Hardware.right.getTargetPosition()) {
                Thread.sleep(10);
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

    public static ColorSensorData getColorSensorData() {
        return new ColorSensorData(Hardware.colorSensor.red(), Hardware.colorSensor.green(), Hardware.colorSensor.blue(), Hardware.colorSensor.alpha());
    }

    public static float standardDeviation(ColorSensorData data) {
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
