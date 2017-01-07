package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;
import org.firstinspires.ftc.teamcode.modules.WESTTimerThread;

import java.util.HashMap;

/**
 * Created by Logan on 11/2/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoBluePosition1", group="Igutech")
public class AutoBluePosition1 extends LinearOpMode {
    public void runOpMode() {
        boolean Color = true;
        WESTTimerThread westTimer;

        boolean competition = true; //TODO: CHANGE  THIS AT COMPETITION!

        Hardware hardware = new Hardware(hardwareMap);
        hardware.init();
        new AutonomousUtils(hardware);

        westTimer = new WESTTimerThread(hardware);
        Thread t2 = new Thread(westTimer);
        t2.start();

        waitForStart();

        if(Color) { //red side program
            hardware.flywheel.setPower(-Globals.flywheelWheelSpeed);
            AutonomousUtils.pidGyro(3.18f, .25f, 0);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AutonomousUtils.resetEncoders();



            //FIRE BALLS
            westTimer.trigger();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            westTimer.trigger();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hardware.flywheel.setPower(0);

            AutonomousUtils.driveEncoderFeetBackwards(.3f, .25f, false);
            AutonomousUtils.resetEncoders();
            AutonomousUtils.gyroTurn(null, TurnType.POINT, 0.25f, -80);
            AutonomousUtils.resetEncoders();
            hardware.waitForTick(100);
            AutonomousUtils.pidGyro(3.6f, 0.25f, -80);
            hardware.waitForTick(100);
            AutonomousUtils.gyroTurn(Motor.RIGHT, TurnType.SWING, -0.25f, 10);
            AutonomousUtils.gyroTurn(Motor.RIGHT, TurnType.SWING, .125f, 3);
            AutonomousUtils.resetEncoders();
            AutonomousUtils.driveEncoderFeetBackwards(.15f, .25f, false);
            AutonomousUtils.resetEncoders();

            hardware.left.setPower(-0.125);
            hardware.right.setPower(-0.125);
            while (AutonomousUtils.getLightSensorData(1).getData() < Globals.lightThreshold && opModeIsActive()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            hardware.left.setPower(0);
            hardware.right.setPower(0);

            ColorDetectionThread colorDetectionThread = new ColorDetectionThread(1.5f, 0f, 0f, 0f, BeaconState.BLUE);
            Thread t = new Thread(colorDetectionThread);
            t.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BeaconState state = colorDetectionThread.getState();
            telemetry.addData("State", state);
            telemetry.update();

            float distance = 0;
            if (state == BeaconState.REDBLUE) {
                //push first button
                distance = .25f;
                AutonomousUtils.driveEncoderFeet(distance, .25f, false);
                hardware.beaconright.setPosition(Globals.beaconRightEnabled);
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.beaconright.setPosition(Globals.beaconRightDisabled);
            } else if (state == BeaconState.BLUERED) {
                //push second button
                hardware.beaconright.setPosition(Globals.beaconRightEnabled);
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.beaconright.setPosition(Globals.beaconRightDisabled);
            }

            AutonomousUtils.resetEncoders();
            AutonomousUtils.pidGyro(4f - distance, 0.25f, 0);

            if (competition) {
                AutonomousUtils.pidGyro(4f + distance, 0.25f, 2);
            } else {
                AutonomousUtils.pidGyro(2f + distance, 0.25f, 2);
            }

            hardware.left.setPower(-0.125);
            hardware.right.setPower(-0.125);
            while (AutonomousUtils.getLightSensorData(1).getData() < Globals.lightThreshold && opModeIsActive()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            hardware.left.setPower(0);
            hardware.right.setPower(0);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            colorDetectionThread = new ColorDetectionThread(1.5f, 0f, 0f, 0f, BeaconState.BLUE);
            state = colorDetectionThread.getState();
            telemetry.addData("State", state);
            telemetry.update();

            distance = 0;
            if (state == BeaconState.REDBLUE) {
                //push first button
                distance = .25f;
                AutonomousUtils.resetEncoders();
                AutonomousUtils.driveEncoderFeet(distance, .25f, false);
                hardware.beaconright.setPosition(Globals.beaconRightEnabled);
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.beaconright.setPosition(Globals.beaconRightDisabled);
            } else if (state == BeaconState.BLUERED) {
                //push second button
                hardware.beaconright.setPosition(Globals.beaconRightEnabled);
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.beaconright.setPosition(Globals.beaconRightDisabled);
            }
            AutonomousUtils.resetEncoders();
            AutonomousUtils.pidGyro(distance, .25f, 0);

            AutonomousUtils.gyroTurn(Motor.RIGHT, TurnType.SWING, -0.25f, 125);
            AutonomousUtils.resetEncoders();
            AutonomousUtils.pidGyro(4.5f, .5f, 125);
        }

    }

}

