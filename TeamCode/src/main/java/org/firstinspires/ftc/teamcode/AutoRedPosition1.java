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
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoRedPosition1", group="Igutech")
public class AutoRedPosition1 extends LinearOpMode {
    public void runOpMode() {
        boolean Color = true;
        boolean delay = false;
        WESTTimerThread westTimer;
        boolean confirmed = false;

        boolean competition = false; //TODO: CHANGE  THIS AT COMPETITION!

        while (!confirmed && opModeIsActive()) {
            if (gamepad1.b && !confirmed) {
                delay = true;
            }
            if (gamepad1.x && !confirmed) {
                delay = false;
            }

            if (gamepad1.start) {
                confirmed = true;
            }

            if (delay) {
                telemetry.addData("Delay", "0s");
            } else {
                telemetry.addData("Delay", "10s");
            }

            if (confirmed) {
                telemetry.addData("LOCKED IN", "TO BEGIN, PRESS START ON PHONE");
                telemetry.addData("Calibrating. Please wait...", "");
            }
            telemetry.update();
        }


        /*
         * NEW SYSTEM! Read below!
         *
         * When initializing an autonomous program, you need to do what you see in the following:
         *  - Initialize hardware with a reference to a linear OpMode
         *  - Call constructor with a reference to the hardware object
         *   * This allows the AutonomousUtils class to use interrupts
         *  - DO NOT initialize or attempt to use any methods inside AutonomousUtils unless from within a LienarOpMode.
         *   * This causes a null pointer because it cannot find a LinearOpMode object
         *
         *   >> DO NOT USE ANYTHING RELATED TO AUTONOMOUSUTILS IN TELEOP OR ANY REGULAR OPMODE! <<
         */

        Hardware hardware = new Hardware(this);
        hardware.init();
        new AutonomousUtils(hardware);

        westTimer = new WESTTimerThread(hardware);
        Thread t2 = new Thread(westTimer);
        t2.start();

        telemetry.addData("Calibration complete", "");
        telemetry.update();


        waitForStart();
        hardware.preStartOperations();

        if(Color) { //red side program

            if (delay) {
                try {
                    Thread.sleep(10000); //10 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            hardware.flywheel.setPower(-Globals.flywheelWheelSpeed);
            hardware.waitForTick(0);
            AutonomousUtils.pidGyro(1f, .25f, 0);
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
            AutonomousUtils.pidGyro(2.18f, .25f, 0);
            AutonomousUtils.resetEncoders();

            AutonomousUtils.driveEncoderFeetBackwards(.3f, .25f, false);
            AutonomousUtils.resetEncoders();
            AutonomousUtils.gyroTurn(null, TurnType.POINT, 0.25f, 80);
            AutonomousUtils.resetEncoders();
            hardware.waitForTick(100);
            AutonomousUtils.pidGyro(3.6f, 0.25f, 80);
            hardware.waitForTick(100);
            AutonomousUtils.gyroTurn(Motor.LEFT, TurnType.SWING, -0.25f, -10);
            AutonomousUtils.gyroTurn(Motor.LEFT, TurnType.SWING, .125f, -3);
            AutonomousUtils.resetEncoders();
            AutonomousUtils.driveEncoderFeetBackwards(.15f, .25f, false);

            hardware.left.setPower(-0.125);
            hardware.right.setPower(-0.125);
            while (AutonomousUtils.getLightSensorData(0).getData() < Globals.lightThreshold && opModeIsActive()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            hardware.left.setPower(0);
            hardware.right.setPower(0);

            ColorDetectionThread colorDetectionThread = new ColorDetectionThread(1.5f, 0f, 0f, 0f, BeaconState.RED);
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
            if (state == BeaconState.BLUERED) {
                //push first button
                distance = .25f;
                AutonomousUtils.resetEncoders();
                AutonomousUtils.driveEncoderFeet(distance, .25f, false);
                hardware.beaconleft.setPosition(Globals.beaconLeftEnabled);
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.beaconleft.setPosition(Globals.beaconLeftDisabled);
            } else if (state == BeaconState.REDBLUE) {
                //push second button
                hardware.beaconleft.setPosition(Globals.beaconLeftEnabled);
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.beaconleft.setPosition(Globals.beaconLeftDisabled);
            }

            AutonomousUtils.resetEncoders();

            if (competition) {
                AutonomousUtils.pidGyro(4f + distance, 0.25f, -2);
            } else {
                AutonomousUtils.pidGyro(2f + distance, 0.25f, -2);
            }


            hardware.left.setPower(-0.125);
            hardware.right.setPower(-0.125);
            while (AutonomousUtils.getLightSensorData(0).getData() < Globals.lightThreshold && opModeIsActive()) {
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

            colorDetectionThread = new ColorDetectionThread(1.5f, 0f, 0f, 0f, BeaconState.RED);
            t = new Thread(colorDetectionThread);
            t.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            state = colorDetectionThread.getState();
            telemetry.addData("State", state);
            telemetry.update();

            distance = 0;
            if (state == BeaconState.BLUERED) {
                //push first button
                distance = .25f;
                AutonomousUtils.resetEncoders();
                AutonomousUtils.driveEncoderFeet(distance, .25f, false);
                hardware.beaconleft.setPosition(Globals.beaconLeftEnabled);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.beaconleft.setPosition(Globals.beaconLeftDisabled);
            } else if (state == BeaconState.REDBLUE) {
                //push second button
                hardware.beaconleft.setPosition(Globals.beaconLeftEnabled);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.beaconleft.setPosition(Globals.beaconLeftDisabled);
            }
            AutonomousUtils.resetEncoders();
            AutonomousUtils.pidGyro(distance, .25f, 0);

            AutonomousUtils.gyroTurn(Motor.LEFT, TurnType.SWING, -0.25f, -125);
            AutonomousUtils.resetEncoders();
            AutonomousUtils.pidGyro(4.5f, .5f, -125);
        }

    }

}
