package org.firstinspires.ftc.teamcode.modules;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Autonomous;
import org.firstinspires.ftc.teamcode.BeaconState;
import org.firstinspires.ftc.teamcode.ColorSensorData;
import org.firstinspires.ftc.teamcode.GyroSensorData;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.LightSensorData;
import org.firstinspires.ftc.teamcode.Motor;
import org.firstinspires.ftc.teamcode.TurnType;

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
                driveEncoderTicks(150, power/2, true, true);
                driveEncoderTicks((int) (feet * 460f)-200, power, true, false);
                driveEncoderTicks((int) (feet * 460f)-50, power/2, true, false);
                driveEncoderTicks((int) (feet * 460f), power/5, false, false);
            }
        } else {
            driveEncoderTicks((int) (feet * 460f), power, false, true);
        }
    }

    public static void driveEncoderTicks(int ticks, float power) { //460 ticks per foot
        driveEncoderTicks(ticks, power, false, true);
    }

    public static void driveEncoderTicks(int ticks, float power, boolean rampUp, boolean firstTime) { //460 ticks per foot
        try {
            if (firstTime) {
                if (!hardware.left.getMode().equals(DcMotor.RunMode.RUN_TO_POSITION)) {
                    hardware.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    hardware.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                }
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
        driveMotorBackwards(feet * 460f, speed, motor);
    }

    public static void driveMotorBackwards(float ticks, float speed, Motor motor) {
        driveMotor(-ticks, -speed, motor);
    }

    public static void driveMotorFeet(float feet, float speed, Motor motor) {
        driveMotor(feet * 460f, speed, motor);
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

    public static void gyroTurn(Motor motor, TurnType turntype, float speed, int angle) {


        if (turntype.equals(TurnType.SWING)) {

            DcMotor target = null;
            if (motor.equals(Motor.RIGHT)) {
                target = hardware.right;
            } else {
                target = hardware.left;
            }

            boolean done = false;
            Condition condition;
            if (angle < AutonomousUtils.getGyroSensorData().getIntegratedZ()) {
                condition = Condition.LT;
            } else {
                condition = Condition.GT;
            }
            while (!done) {
                GyroSensorData data = AutonomousUtils.getGyroSensorData();
                target.setPower(speed);
                boolean result;
                if (condition.equals(Condition.LT)) {
                    result = data.getIntegratedZ() < angle;
                } else {
                    result = data.getIntegratedZ() > angle;
                }
                if (result) {
                    done = true;
                }
            }
            target.setPower(0f);
        } else {
            boolean done = false;
            Condition condition;
            if (angle < AutonomousUtils.getGyroSensorData().getIntegratedZ()) {
                condition = Condition.LT;
            } else {
                condition = Condition.GT;
            }
            while (!done) {
                GyroSensorData data = AutonomousUtils.getGyroSensorData();
                hardware.left.setPower(speed/2);
                hardware.right.setPower(-speed/2);

                boolean result;
                if (condition.equals(Condition.LT)) {
                    result = data.getIntegratedZ() < angle;
                } else {
                    result = data.getIntegratedZ() > angle;
                }
                if (result) {
                    done = true;
                }
            }
            hardware.left.setPower(0f);
            hardware.right.setPower(0f);
        }
    }

    public static void pidGyro(float feet, float speed, int angle, boolean rampUp) {
        if (rampUp && feet > 1f) {
            pidGyro(150, speed/2, angle);
            pidGyro(feet-.5f, speed, angle);
            pidGyro(feet-.1f, speed/2, angle);
            pidGyro(feet, speed/5, angle);
        } else {
            pidGyro(feet, speed, angle);
        }
    }


    public static void pidGyro(float feet, float speed, int angle) {
        speed = -speed;
        int ticks = (int) -(feet * 460f);

        final float GAIN = 0.0025f;
        boolean done = false;

        while (!done) {
            float error = angle - AutonomousUtils.getGyroSensorData().getIntegratedZ();
            float output = error*GAIN;
            float leftOutput = speed + output;
            float rightOutput = speed - output;

            //limit values to +1/-1

            leftOutput = leftOutput > 1f ? 1f : leftOutput; //if leftoutput > 1, set to 1, if not, keep normally.
            leftOutput = leftOutput < -1f ? -1f : leftOutput; //almost same as previous.

            rightOutput = rightOutput > 1f ? 1f : rightOutput;   //these are called ternaries.
            rightOutput = rightOutput < -1f ? -1f : rightOutput;


            hardware.left.setPower(leftOutput);
            hardware.right.setPower(rightOutput);

            float average = (hardware.right.getCurrentPosition() + hardware.left.getCurrentPosition())/2;
            done = average < ticks;
        }
        hardware.left.setPower(0f);
        hardware.right.setPower(0f);
    }

    public static void resetEncoders() {
        hardware.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (hardware.left.getMode().equals(DcMotor.RunMode.STOP_AND_RESET_ENCODER) || hardware.right.getMode().equals(DcMotor.RunMode.STOP_AND_RESET_ENCODER)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        hardware.left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

    public static GyroSensorData getGyroSensorData() {
        return new GyroSensorData(hardware.gyro.getIntegratedZValue(), hardware.gyro.rawX(), hardware.gyro.rawY(), hardware.gyro.rawZ());
    }

    private enum Condition {
        GT,
        LT
    }
}
