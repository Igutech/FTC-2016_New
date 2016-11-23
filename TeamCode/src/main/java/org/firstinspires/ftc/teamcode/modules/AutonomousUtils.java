package org.firstinspires.ftc.teamcode.modules;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Autonomous;
import org.firstinspires.ftc.teamcode.BeaconState;
import org.firstinspires.ftc.teamcode.ColorSensorData;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.LightSensorData;
import org.firstinspires.ftc.teamcode.Motor;

import java.util.Timer;

/**
 * Created by Kevin on 10/6/2016.
 */
public class AutonomousUtils {

    private static Hardware hardware;

    public AutonomousUtils(Hardware hardware) {
        this.hardware = hardware;
    }

    public static void driveEncoderFeetBackwards(float feet, float power) {
        driveEncoderTicksBackwards((int)(feet*460f), power);
    }

    public static void driveEncoderFeetBackwards(float feet, float power, boolean rampUp) {
        driveEncoderTicksBackwards((int) (feet * 460f), power, rampUp);
    }

    public static void driveEncoderTicksBackwards(int ticks, float power) { //460 ticks per foot
        driveEncoderTicksBackwards(ticks, power, false);
    }

    public static void driveEncoderTicksBackwards(int ticks, float power, boolean rampUp) { //460 ticks per foot
        try {
            hardware.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hardware.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            while (hardware.left.getMode() != DcMotor.RunMode.RUN_TO_POSITION && hardware.right.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
                Thread.sleep(10);
            }
            hardware.right.setTargetPosition(ticks);
            hardware.left.setTargetPosition(ticks);

            if (rampUp) {
                hardware.right.setPower(0);
                hardware.left.setPower(0);
            } else {
                hardware.right.setPower(power);
                hardware.left.setPower(power);
            }

            long startTime = System.currentTimeMillis()/1000;
            while (hardware.left.getCurrentPosition() < hardware.left.getTargetPosition() || hardware.right.getCurrentPosition() < hardware.right.getTargetPosition()) {
                if (rampUp) {
                    long currentTime = System.currentTimeMillis() / 1000;
                    long elapsed = Math.abs(currentTime - startTime);
                    double speed = (double) elapsed;
                    if (speed > 1) {
                        speed = 1;
                    }
                    hardware.right.setPower(speed);
                    hardware.left.setPower(speed);
                } else {
                    Thread.sleep(10);
                }
            }

            hardware.right.setPower(0);
            hardware.left.setPower(0);

            hardware.left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            hardware.right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void driveEncoderFeet(float feet, float power) {
        driveEncoderTicks((int)(feet*460f), power);
    }

    public static void driveEncoderFeet(float feet, float power, boolean rampUp) {
        //driveEncoderTicks((int) (feet * 460f), power, rampUp);

        if (rampUp) {
            if (feet > 1) {
                driveEncoderTicks(150, power/2, true);
                driveEncoderTicks((int) (feet * 460f)-200, power, true);
                driveEncoderTicks((int) (feet * 460f)-50, power/2, true);
                driveEncoderTicks((int) (feet * 460f), power/5, false);
            }
        } else {
            driveEncoderTicks((int) (feet * 460f), power, false);
        }
    }

    public static void driveEncoderTicks(int ticks, float power) { //460 ticks per foot
        driveEncoderTicks(ticks, power, false);
    }

    public static void driveEncoderTicks(int ticks, float power, boolean rampUp) { //460 ticks per foot
        try {
            if (!hardware.left.getMode().equals(DcMotor.RunMode.RUN_TO_POSITION)) {
                hardware.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                hardware.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            hardware.right.setTargetPosition(-ticks);
            hardware.left.setTargetPosition(-ticks);

            hardware.right.setPower(-power);
            hardware.left.setPower(-power);

            while (hardware.left.getCurrentPosition() > hardware.left.getTargetPosition() || hardware.right.getCurrentPosition() > hardware.right.getTargetPosition()) {
                Thread.sleep(10);
            }
            if (!rampUp) {

                hardware.right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                hardware.left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                hardware.right.setPower(0);
                hardware.left.setPower(0);

                Thread.sleep(100);

                hardware.right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                hardware.left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                hardware.left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hardware.right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void driveMotorFeetBackwards(float feet, float speed, Motor motor) {
        driveMotorBackwards(feet*460f, speed, motor);
    }

    public static void driveMotorBackwards(float ticks, float speed, Motor motor) {
        driveMotor(-ticks, -speed, motor);
    }

    public static void driveMotorFeet(float feet, float speed, Motor motor) {
        driveMotor(feet*460f, speed, motor);
    }

    public static void driveMotor(float ticks, float speed, Motor motor) {
        DcMotor target = null;

        if (motor.equals(Motor.LEFT)) {
            target = hardware.left;
        } else {
            target = hardware.right;
        }

        target.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        target.setTargetPosition((int) -ticks);
        target.setPower(-speed);

        while (target.isBusy()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        target.setPower(0);
        target.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public static void resetEncoders() {
        hardware.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.waitForTick(250);
        hardware.left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public static ColorSensorData getColorSensorData(int id) {
        if (id == 0) {
            //return new ColorSensorData(Hardware.colorSensor0.red(), Hardware.colorSensor0.green(), Hardware.colorSensor0.blue(), Hardware.colorSensor0.alpha());
            return null;
        }
        if (id == 1) {
            int[] crgb = hardware.muxColor.getCRGB(1);
            return new ColorSensorData(crgb[1], crgb[2], crgb[3], crgb[0]);
        }
        if (id == 2) {
            int[] crgb = hardware.muxColor.getCRGB(2);
            return new ColorSensorData(crgb[1], crgb[2], crgb[3], crgb[0]);
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

    public static LightSensorData getLightSensorData(int id) {
        LightSensorData data = null;
        if (id == 0) {
            data = new LightSensorData(hardware.lightleft.getLightDetected());
        }

        if (id == 1) {
            data = new LightSensorData(hardware.lightright.getLightDetected());
        }

        return data;
    }
}
