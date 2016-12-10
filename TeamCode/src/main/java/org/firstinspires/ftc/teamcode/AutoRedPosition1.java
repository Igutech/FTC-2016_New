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
import java.util.HashMap;

/**
 * Created by Logan on 11/2/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoRedPosition1", group="Igutech")
public class AutoRedPosition1 extends LinearOpMode {
    public void runOpMode() {
        float firstDistance, secondDistance;
        firstDistance = .5f;
        secondDistance = 7.656f - firstDistance;
        float flywheelSpeed = 1f;
        double engaged = .6d;
        double disengaged = 0.05d;
        boolean confirmed = false;
        boolean Color = true;

        /*while (!confirmed && opModeIsActive()) {
            if (gamepad1.b) {
                Color = true;
            }
            if (gamepad1.x) {
                Color = false;
            }

            if (gamepad1.start) {
                confirmed = true;
            }

            if (Color) {
                telemetry.addData("Color", "RED");
            } else {
                telemetry.addData("Color", "BLUE");
            }

            if (confirmed) {
                telemetry.addData("LOCKED IN", "TO BEGIN, PRESS START ON PHONE");
            }
            telemetry.update();*/

            Hardware hardware = new Hardware(hardwareMap);
            hardware.init();
            new AutonomousUtils(hardware);

            waitForStart();

            if(Color) { //red side program
                hardware.flywheel.setPower(Globals.flywheelWheelSpeed);
                hardware.waitForTick(0);
                AutonomousUtils.pidGyro(3.18f, .25f, 0);
                AutonomousUtils.resetEncoders();

                //FIRE BALLS
                hardware.WEST.setPosition(Globals.westEnabled + .1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.WEST.setPosition(Globals.westDisabled);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.WEST.setPosition(Globals.westEnabled + .1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.WEST.setPosition(Globals.westDisabled);
                hardware.flywheel.setPower(0);

                AutonomousUtils.driveEncoderFeetBackwards(.3f, .25f, false);
                AutonomousUtils.resetEncoders();
                AutonomousUtils.gyroTurn(null, TurnType.POINT, 0.25f, 70);
                AutonomousUtils.resetEncoders();
                hardware.waitForTick(100);
                AutonomousUtils.pidGyro(3.52f, 0.25f, 70);
                hardware.waitForTick(100);
                AutonomousUtils.gyroTurn(Motor.LEFT, TurnType.SWING, -0.25f, -10);
                AutonomousUtils.gyroTurn(Motor.LEFT, TurnType.SWING, .125f, -3);
                AutonomousUtils.resetEncoders();

                hardware.left.setPower(-0.125);
                hardware.right.setPower(-0.125);
                while (AutonomousUtils.getLightSensorData(0).getData() < Globals.lightThreshold) {
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
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BeaconState state = colorDetectionThread.getState();
                telemetry.addData("State", state);
                telemetry.update();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                float distance = 0;
                if (state == BeaconState.REDBLUE) {
                    //push first button
                    distance = .075f;
                    AutonomousUtils.driveEncoderFeetBackwards(distance, .25f, false);
                    hardware.beaconleft.setPosition(Globals.beaconLeftEnabled);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    hardware.beaconleft.setPosition(Globals.beaconLeftDisabled);
                } else if (state == BeaconState.BLUERED) {
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
                AutonomousUtils.pidGyro(4f + distance, 0.25f, 0);

                hardware.left.setPower(-0.125);
                hardware.right.setPower(-0.125);
                while (AutonomousUtils.getLightSensorData(0).getData() < Globals.lightThreshold) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                hardware.left.setPower(0);
                hardware.right.setPower(0);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state = colorDetectionThread.getState();
                telemetry.addData("State", state);
                telemetry.update();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                distance = 0;
                if (state == BeaconState.REDBLUE) {
                    //push first button
                    distance = .075f;
                    AutonomousUtils.driveEncoderFeetBackwards(distance, .25f, false);
                    hardware.beaconleft.setPosition(Globals.beaconLeftEnabled);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    hardware.beaconleft.setPosition(Globals.beaconLeftDisabled);
                } else if (state == BeaconState.BLUERED) {
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

                AutonomousUtils.gyroTurn(Motor.LEFT, TurnType.SWING, -0.25f, -135);
                AutonomousUtils.resetEncoders();
                AutonomousUtils.pidGyro(4.5f, .5f, -135);
            }

        }

    }
