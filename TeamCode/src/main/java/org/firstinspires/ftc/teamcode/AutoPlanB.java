package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;
import org.firstinspires.ftc.teamcode.modules.WESTTimerThread;

/**
 * Created by Logan on 11/2/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoPlanB", group="Igutech")
public class AutoPlanB extends LinearOpMode {
    public LinearOpMode  getOp() {
        return (LinearOpMode)this;
    }
    public void runOpMode() {
        boolean delay = false;
        WESTTimerThread westTimer;
        boolean confirmed = false;

        while (!confirmed) {
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
                telemetry.addData("Delay", "10s");
            } else {
                telemetry.addData("Delay", "0s");
            }

            if (confirmed) {
                telemetry.addData("LOCKED IN", "TO BEGIN, PRESS START ON PHONE");
                telemetry.addData("Calibrating. Please wait...", "");
            }
            telemetry.update();
        }

        final Hardware hardware = new Hardware(this);
        hardware.init();
        new AutonomousUtils(hardware);

        westTimer = new WESTTimerThread(hardware);
        Thread t2 = new Thread(westTimer);
        t2.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean done = false;
                while (!done) {
                    hardware.updateAutonomous(getOp());
                    AutonomousUtils.hardware = hardware;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    if (!getOp().opModeIsActive()) {
                        done = true;
                    }
                }
            }
        }).start();

        telemetry.addData("Calibration complete", "");
        telemetry.update();


        waitForStart();
        hardware.preStartOperations();


        //Autonomous Begins Here

        if (delay) {
            try {
                Thread.sleep(10000); //10 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        hardware.flywheel.setPower(-Globals.flywheelWheelSpeed);
        hardware.waitForTick(0);
        AutonomousUtils.pidGyro(2.18f, .25f, 0);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        AutonomousUtils.resetEncoders();

        //FIRE BALLS
        westTimer.trigger();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        westTimer.trigger();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        hardware.flywheel.setPower(0);
        AutonomousUtils.pidGyro(3f, .5f, 0);

    }

}
