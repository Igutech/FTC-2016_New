package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Logan on 11/18/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Diagnose Issues", group="Igutech")
@Disabled
public class SelfTest extends LinearOpMode {
    public void runOpMode() {

        Hardware hardware = new Hardware(this);
        hardware.init();
        new AutonomousUtils(hardware);
        telemetry.addData("Hardware init properly", "");
        telemetry.update();

        waitForStart();

        //START MOTOR TESTING

        hardware.right.setPower(-0.3f);
        hardware.left.setPower(-0.3f);
        hardware.flywheel.setPower(-0.5f);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        hardware.right.setPower(0f);
        hardware.left.setPower(0f);
        hardware.flywheel.setPower(0f);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean rightWheel = hardware.right.getCurrentPosition() != 0;
        boolean leftWheel = hardware.left.getCurrentPosition() != 0;
        boolean flyWheel = hardware.flywheel.getCurrentPosition() != 0;


        boolean encoderFunctioning = false;

        if (rightWheel && leftWheel && flyWheel) {
            encoderFunctioning = true;
        }

        //END MOTOR TESTING

        //START GYRO TESTING

        int gyroPositionBefore = AutonomousUtils.getGyroSensorData().getIntegratedZ();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int gyroPositionAfter = AutonomousUtils.getGyroSensorData().getIntegratedZ();

        int difference = Math.abs(gyroPositionAfter - gyroPositionBefore);

        boolean gyroNominal = true;

        if (difference > 5) {
            gyroNominal = false;
        }

        //END GYRO TESTING

        //START COLOR SENSOR TESTING

        ArrayList<Integer> sensors = new ArrayList<Integer>();
        sensors.add(0);
        sensors.add(1);
        sensors.add(2);
        sensors.add(3);

        ArrayList<ColorSensorData> sensorData = new ArrayList<ColorSensorData>();

        for (Integer i : sensors) {
            sensorData.add(AutonomousUtils.getColorSensorData(i));
        }

        int loopcount = 0;

        HashMap<Integer, Boolean> ok = new HashMap<Integer, Boolean>();

        for (ColorSensorData data : sensorData) {
            float red = data.getRed();
            float green = data.getGreen();
            float blue = data.getBlue();

            telemetry.addData("Shine light at " + loopcount, "");
            telemetry.addData("Press X when ready", "");
            telemetry.update();

            while(!gamepad1.x) { //wait for x to be pressed
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while(gamepad1.x) { //wait for x to be pressed
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ColorSensorData newData = AutonomousUtils.getColorSensorData(loopcount);
            float newRed = newData.getRed();
            float newGreen = newData.getGreen();
            float newBlue = newData.getBlue();

            float redDifference = Math.abs(red - newRed);
            float greenDifference = Math.abs(green - newGreen);
            float blueDifference = Math.abs(blue - newBlue);

            boolean redOk = redDifference > 10;
            boolean greenOk = greenDifference > 10;
            boolean blueOk = blueDifference > 10;

            if (!(redOk && greenOk && blueOk)) {
                ok.put(loopcount, false);
            } else {
                ok.put(loopcount, true);
            }

            loopcount++;
        }

        boolean colorOK = true;

        for (Integer i : sensors) {
            if (!ok.get(i)) {
                colorOK = false;
            }
        }

        //END COLOR SENSOR TESTING



        //PRINT DATA
        telemetry.addData("All encoders", encoderFunctioning);
        if (!encoderFunctioning) {
            telemetry.addData("Right Wheel", rightWheel);
            telemetry.addData("Left Wheel", leftWheel);
            telemetry.addData("FlyWheel", flyWheel);
        }

        telemetry.addData("Gyro", gyroNominal);

        telemetry.addData("All color", colorOK);
        if (!colorOK) {
            for (Integer i : sensors) {
                telemetry.addData("Color " + i, ok.get(i));
            }
        }

        telemetry.update();


        while(opModeIsActive()) { //wait until opmode is stopped
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
