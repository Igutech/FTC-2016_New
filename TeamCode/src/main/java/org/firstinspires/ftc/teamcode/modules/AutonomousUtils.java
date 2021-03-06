package org.firstinspires.ftc.teamcode.modules;

import android.graphics.Color;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autonomous;
import org.firstinspires.ftc.teamcode.BeaconChangeInfo;
import org.firstinspires.ftc.teamcode.BeaconState;
import org.firstinspires.ftc.teamcode.ColorSensorData;
import org.firstinspires.ftc.teamcode.Globals;
import org.firstinspires.ftc.teamcode.GyroSensorData;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.LightSensorData;
import org.firstinspires.ftc.teamcode.Motor;
import org.firstinspires.ftc.teamcode.Teleop;
import org.firstinspires.ftc.teamcode.TurnType;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Timer;

/**
 * Created by Kevin on 10/6/2016.
 */
public class AutonomousUtils {

    private static GyroTarget gyroTarget;
    public static Hardware hardware;
    private static long timer;
    private static long elapsedtime;
    private static boolean gyroInterrupt = false;

    public AutonomousUtils(Hardware hardware) {
        AutonomousUtils.hardware = hardware;
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
            while (hardware.opModeIsActive() && hardware.left.getMode() != DcMotor.RunMode.RUN_TO_POSITION && hardware.right.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
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
            while (hardware.opModeIsActive() && hardware.left.getCurrentPosition() < hardware.left.getTargetPosition() || hardware.right.getCurrentPosition() < hardware.right.getTargetPosition()) {
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
            Thread.currentThread().interrupt();
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
                resetEncoders();
                if (!hardware.left.getMode().equals(DcMotor.RunMode.RUN_TO_POSITION)) {
                    hardware.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    hardware.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                }
            }
            hardware.right.setTargetPosition(-ticks);
            hardware.left.setTargetPosition(-ticks);

            hardware.right.setPower(-power);
            hardware.left.setPower(-power);

            while (hardware.opModeIsActive() && hardware.left.getCurrentPosition() > hardware.left.getTargetPosition() || hardware.right.getCurrentPosition() > hardware.right.getTargetPosition()) {
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
            Thread.currentThread().interrupt();
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

        while (hardware.opModeIsActive() && target.isBusy()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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
            while (hardware.opModeIsActive() && !done) {
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
            while (hardware.opModeIsActive() && !done) {
                GyroSensorData data = AutonomousUtils.getGyroSensorData();
                if (condition.equals(Condition.GT)) {
                    hardware.left.setPower(speed/2);
                    hardware.right.setPower(-speed/2);
                } else {
                    hardware.left.setPower(-speed/2);
                    hardware.right.setPower(speed/2);
                }

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
            pidGyro(feet, speed / 5, angle);
        } else {
            pidGyro(feet, speed, angle);
        }
    }


    public static void pidGyro(float feet, float speed, int angle) {
        speed = -speed;
        int ticks = (int) -(feet * 460f);
        resetEncoders();
        final float GAIN = 0.0025f;
        boolean done = false;

        while (hardware.opModeIsActive() && !done) {
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

    public static void pidGyro(float speed, int angle) {
        speed = -speed;
        resetEncoders();
        final float GAIN = 0.0025f;
        gyroInterrupt = false;

        while (hardware.opModeIsActive() && !gyroInterrupt) {
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
        }
    }

    public static void interruptGyro() {
        gyroInterrupt = true;
    }

    public static void resetEncoders() {
        hardware.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        hardware.left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static ColorSensorData getColorSensorData(int id) {
        if (id == 0) {
            int[] crgb = hardware.muxColor.getCRGB(0);
            return new ColorSensorData(crgb[1], crgb[2], crgb[3], crgb[0]);
        }
        if (id == 1) {
            int[] crgb = hardware.muxColor.getCRGB(1);
            return new ColorSensorData(crgb[1], crgb[2], crgb[3], crgb[0]);
        }
        if (id == 2) {
            int[] crgb = hardware.muxColor.getCRGB(2);
            return new ColorSensorData(crgb[1], crgb[2], crgb[3], crgb[0]);
        }
        if (id == 3) {
            int[] crgb = hardware.muxColor.getCRGB(3);
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

        if (id == 2) {
            data = new LightSensorData(hardware.ballColor.getLightDetected());
        }

        return data;
    }

    public static class NoBallDetector implements Runnable {
        float totalDistance = 0f;
        int totalReadings = 0;
        @Override
        public void run() {
            totalDistance+=hardware.ballUltrasonic.getUltrasonicLevel();
            totalReadings++;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        public boolean getNoBall() {
            if (totalDistance/totalReadings > Globals.ballUltrasonicThreshLow && totalDistance/totalReadings < Globals.ballUltrasonicThreshHigh) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static BallColor getBallColor(LightSensorData data) {
        NoBallDetector detector = new NoBallDetector();
        Thread t = new Thread(detector);
        t.start();
        hardware.ballColor.enableLed(false);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (detector.getNoBall()) {
            return BallColor.NOBALL;
        }
        double colordiff;
        colordiff = -hardware.ballColor.getRawLightDetected();
        hardware.ballColor.enableLed(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        colordiff = colordiff + hardware.ballColor.getLightDetected();
        if(colordiff > 0.10)
        {
            return BallColor.RED;
        }
        return BallColor.BLUE; //TODO: Do stuff here if there is a ball.
    }

    public enum BallColor {
        RED,
        BLUE,
        NOBALL;
    }

    public static GyroSensorData getGyroSensorData() {
        if (gyroTarget.equals(GyroTarget.SENSOR1)) {
            return new GyroSensorData(hardware.gyro.getIntegratedZValue(), hardware.gyro.rawX(), hardware.gyro.rawY(), hardware.gyro.rawZ());
        } else if (gyroTarget.equals(GyroTarget.SENSOR2)) {
            return new GyroSensorData(hardware.gyro2.getIntegratedZValue(), hardware.gyro2.rawX(), hardware.gyro2.rawY(), hardware.gyro2.rawZ());
        } else {
            return null;
        }
    }

    /**
     * Gets specific gyro sensor data
     * @param id Which gyro to retrieve (1 or 2)
     * @return GyroSensorData object containing information about the gyro
     */
    public static GyroSensorData getGyroSensorData(int id) {
        if (id == 1) {
            return new GyroSensorData(hardware.gyro.getIntegratedZValue(), hardware.gyro.rawX(), hardware.gyro.rawY(), hardware.gyro.rawZ());
        } else if (id == 2) {
            return new GyroSensorData(hardware.gyro2.getIntegratedZValue(), hardware.gyro2.rawX(), hardware.gyro2.rawY(), hardware.gyro2.rawZ());
        } else {
            return null;
        }
    }

    /**
     * Set which gyro to use by default
     * @param target the gyro to use by default
     */
    public static void setGyroTarget(GyroTarget target) {
        gyroTarget = target;
    }

    public static GyroTarget getGyroTarget() {
        return gyroTarget;
    }

    public static float getLightThreshold() {
        String location = "/FIRST/lightcalibration.txt";
        try {
            FileInputStream file = new FileInputStream(location);
            return new DataInputStream(file).readFloat();

        } catch (Exception e) {
            return 0.2f; //a fallback value
        }
    }

    public static void setLightThreshold(float threshold) {
        String location = "/FIRST/lightcalibration.txt";
        try {
            FileOutputStream file = new FileOutputStream(location);
            new PrintStream(file).print(threshold);
        } catch (Exception e) {

        }
    }

    private static float motorPower;
    private static float lastPower;
    private static float konstant = 0.005f;
    private static float konstant2 = 0.0005f;
    private static int step = 0;
    private static int decpos;
    private static int drivingerror;
    private static int reverseddriving = 1;

    public static void smartDrive(int goalEncoderTicks){
        int step = 0;
        hardware.left.setTargetPosition(goalEncoderTicks);
        hardware.right.setTargetPosition(goalEncoderTicks);
        if(goalEncoderTicks < 0){
            reverseddriving = -1;
        }else{
            reverseddriving =1;
        }
        goalEncoderTicks = Math.abs(goalEncoderTicks);
        while(hardware.opModeIsActive() && step !=8){
            switch(step){
                case 0:
                    resetEncoders();
                    timer = System.currentTimeMillis();
                    step = 1;
                    break;
                case 1:
                    elapsedtime = System.currentTimeMillis() - timer;
                    motorPower= elapsedtime * konstant;
                    if(Math.abs(hardware.left.getCurrentPosition()) >= (goalEncoderTicks/2)){lastPower=motorPower; step = 2;}
                    if(motorPower >= 1){step = 5;}
                    break;
                case 2:
                    timer = System.currentTimeMillis();
                    step = 3;
                    break;
                case 3:
                    elapsedtime = System.currentTimeMillis() - timer;
                    motorPower = lastPower - (elapsedtime * konstant2);
                    if(motorPower <= 0){step = 4;}
                    break;
                case 4:
                    motorPower = 0;
                    step = 7;
                    break;
                case 5:
                    decpos = (goalEncoderTicks/2) + Math.abs(hardware.left.getCurrentPosition());
                    step = 6;
                    break;
                case 6:
                    motorPower = 1f;
                    if(Math.abs(hardware.left.getCurrentPosition()) >= decpos){lastPower=1; step = 2;}
                    break;
                case 7:
                    drivingerror = goalEncoderTicks-hardware.left.getCurrentPosition();
                    step = 8;
                    break;
                default:
                    //SOMETHING MAJORLY WENT WRONG TO GET TO THIS DEFAULT CASE!
            }
            hardware.left.setPower(motorPower*0.40*reverseddriving);
            hardware.right.setPower(motorPower*0.40*reverseddriving);

        }
        //return drivingerror;
    }

    public BeaconChangeInfo detectBeaconChange(BeaconState team, BeaconState originalState, BeaconState targetState, int pollTime) {

        float differenceThresh = 100;

        int targetSensor = team.equals(BeaconState.RED) ? 0:2;
        BeaconChangeInfo information;
        if (originalState.equals(BeaconState.RED)) {
            information = new BeaconChangeInfo(getColorSensorData(targetSensor).getRed(), targetState);
        } else {
            information = new BeaconChangeInfo(getColorSensorData(targetSensor).getBlue(), targetState);
        }

        ElapsedTime elapsedTime = new ElapsedTime();
        elapsedTime.reset();
        while (hardware.opModeIsActive() && elapsedTime.milliseconds() < pollTime) {
            if (originalState.equals(BeaconState.RED)) {
                information.addNewValue(getColorSensorData(targetSensor).getRed());
            } else {
                information.addNewValue(getColorSensorData(targetSensor).getBlue());
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        float sum = 0;
        for (float value : information.getValues()) {
            sum += value;
        }
        float avg = sum/information.getTotalValues();
        if ((avg+differenceThresh) < information.initialvalue) {
            information.setSuccessful(true);
        } else {
            information.setSuccessful(false);
        }

        return information;
    }

    private enum Condition {
        GT,
        LT
    }

    public enum GyroTarget {
        SENSOR1,
        SENSOR2
    }


    public static void powerGyroTurn(int angle, int slowdownThreshold, float speed, Motor leftorright){
        float turnSpeed;
        float halfMultipler = 1f;
        int goalGyroPos;
        int slowDegrees;
        int isNegative;
        Motor motor;
        int startGyroPos;
        Boolean complete = true;
        goalGyroPos = angle;
        slowDegrees = slowdownThreshold;
        halfMultipler = 1f;
        turnSpeed = speed;
        motor = leftorright;
        if(goalGyroPos <0){isNegative =-1;}else{isNegative=1;}
        startGyroPos = getGyroSensorData().getIntegratedZ();
        slowDegrees = startGyroPos + slowDegrees;
        goalGyroPos = startGyroPos + goalGyroPos;

        while(hardware.opModeIsActive() && complete){
            if(motor == Motor.LEFT){
                hardware.left.setPower(turnSpeed*isNegative*halfMultipler);
            }
            if(motor == Motor.RIGHT){
                hardware.right.setPower(-turnSpeed*isNegative*halfMultipler);
            }
            if(isNegative >0){
                if(slowDegrees <= getGyroSensorData().getIntegratedZ()){
                    halfMultipler = 0.2f;
                }
                if(goalGyroPos <= getGyroSensorData().getIntegratedZ()){
                    complete = false;
                }
            }else{
                if(slowDegrees >= getGyroSensorData().getIntegratedZ()){
                    halfMultipler = 0.2f;
                }
                if(goalGyroPos >= getGyroSensorData().getIntegratedZ()){
                    complete = false;
                }
            }
        }
        hardware.left.setPower(0);
        hardware.right.setPower(0);
    }

    public static void stopDriving(){
        hardware.left.setPower(0);
        hardware.right.setPower(0);
    }

    public static void tankDriving(float val){
        hardware.left.setPower(-val);
        hardware.right.setPower(-val);
    }
}
