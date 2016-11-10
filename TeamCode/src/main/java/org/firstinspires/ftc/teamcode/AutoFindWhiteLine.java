package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;

/**
 * Created by Logan on 11/2/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Find White Line", group="Igutech")
public class AutoFindWhiteLine extends LinearOpMode {
    public void runOpMode() {
        Hardware hardware = new Hardware(hardwareMap);
        hardware.init();
        new AutonomousUtils(hardware);

        waitForStart();

        hardware.right.setPower(-0.1);
        hardware.left.setPower(-0.1);

        /*while(opModeIsActive()) {
            telemetry.addData("Light Sensor", AutonomousUtils.getLightSensorData(0).getData());
            telemetry.update();
        }*/


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

    }

}
