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
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoPressButtonRed", group="Igutech")
public class AutoPressButtonRed extends LinearOpMode {
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
                hardware.brushes.setPower(1f);
                hardware.flywheel.setPower(0.53f);
                hardware.waitForTick(10);
                hardware.waitForTick(5000);
                hardware.WEST.setPosition(engaged);
                hardware.waitForTick(1500);
                hardware.WEST.setPosition(disengaged);
                hardware.waitForTick(1500);
                hardware.flywheel.setPower(0.55f);
                hardware.waitForTick(3000);
                hardware.WEST.setPosition(engaged);
                hardware.waitForTick(1500);
                hardware.WEST.setPosition(disengaged);
                hardware.flywheel.setPower(0);
                hardware.brushes.setPower(0);

                hardware.left.setPower(-.5);
                hardware.right.setPower(-.5);

                while (hardware.right.getCurrentPosition() > -300 && hardware.left.getCurrentPosition() > -300) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                telemetry.addData("Done with first move", "");
                telemetry.update();
                hardware.left.setPower(0);
                hardware.right.setPower(0);
                hardware.waitForTick(100);
                hardware.right.setPower(-.5f);

                while (hardware.right.getCurrentPosition() > -920) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                hardware.right.setPower(0f);
                hardware.waitForTick(50);

                hardware.right.setPower(-.5f);
                hardware.left.setPower(-.5f);


                while (hardware.right.getCurrentPosition() > -2221 && hardware.left.getCurrentPosition() > -1301) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                hardware.right.setPower(0f);
                hardware.left.setPower(0f);
                hardware.waitForTick(50);
                hardware.left.setPower(-.5f);

                while (hardware.left.getCurrentPosition() > -2221) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                hardware.left.setPower(0f);
                hardware.waitForTick(50);

                hardware.right.setPower(-0.1);
                hardware.left.setPower(-0.1);

                while (AutonomousUtils.getLightSensorData(0).getData() < 0.33) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                hardware.right.setPower(0);
                hardware.left.setPower(0);

                ColorDetectionThread colorDetectionThread = new ColorDetectionThread(1.5f, 0f, 0f, 0f);
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

                if (state == BeaconState.REDBLUE) {
                    //push first button
                } else if (state == BeaconState.BLUERED) {
                    //push second button
                }

                hardware.waitForTick(5000);
            }
            else if(!Color) { //blue side program
                hardware.brushes.setPower(1f);
                hardware.flywheel.setPower(0.53f);
                hardware.waitForTick(10);
                hardware.waitForTick(5000);
                hardware.WEST.setPosition(engaged);
                hardware.waitForTick(1500);
                hardware.WEST.setPosition(disengaged);
                hardware.waitForTick(1500);
                hardware.flywheel.setPower(0.55f);
                hardware.waitForTick(3000);
                hardware.WEST.setPosition(engaged);
                hardware.waitForTick(1500);
                hardware.WEST.setPosition(disengaged);
                hardware.flywheel.setPower(0);

                //AutonomousUtils.driveEncoderFeet(-.5f, .5f, false);
                hardware.waitForTick(50);
                hardware.left.setPower(-.5f);

                while (hardware.left.getCurrentPosition() > -920) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                hardware.left.setPower(0f);
                hardware.waitForTick(50);
                hardware.brushes.setPower(0);

                AutonomousUtils.resetEncoders();
                AutonomousUtils.driveEncoderFeet(-2.8f, .5f, false);


                /*while (hardware.right.getCurrentPosition() > -1301 && hardware.left.getCurrentPosition() > -1301) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/

                hardware.right.setPower(0f);
                hardware.left.setPower(0f);
                hardware.waitForTick(50);
                AutonomousUtils.resetEncoders();
                hardware.right.setPower(-.5f);

                while (hardware.right.getCurrentPosition() > -920) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                hardware.right.setPower(0f);
                hardware.waitForTick(50);

                hardware.right.setPower(-0.1);
                hardware.left.setPower(-0.1);

                while (AutonomousUtils.getLightSensorData(0).getData() < 0.33) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                hardware.right.setPower(0);
                hardware.left.setPower(0);

                ColorDetectionThread colorDetectionThread = new ColorDetectionThread(1.5f, 0f, 0f, 0f);
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

                if (state == BeaconState.REDBLUE) {
                    //push second button
                } else if (state == BeaconState.BLUERED) {
                    //push first button
                }

                hardware.waitForTick(5000);
            }

        }

    }
