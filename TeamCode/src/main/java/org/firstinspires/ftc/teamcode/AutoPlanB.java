package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;
import org.firstinspires.ftc.teamcode.modules.WESTTimerThread;

/**
 * Created by Logan on 11/2/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoPlanB", group="Igutech")
public class AutoPlanB extends LinearOpMode {
    public void runOpMode() {
        boolean delay = false;
        WESTTimerThread westTimer;
        boolean confirmed = false;

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


        //Autonomous Begins Here

        if (delay) {
            try {
                Thread.sleep(10000); //10 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        hardware.flywheel.setPower(-Globals.flywheelWheelSpeed);
        hardware.waitForTick(0);
        AutonomousUtils.pidGyro(2.18f, .25f, 0);
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
        AutonomousUtils.pidGyro(3f, .5f, 0);

    }

}
