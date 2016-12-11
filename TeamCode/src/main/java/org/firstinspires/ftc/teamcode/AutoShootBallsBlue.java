package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;

/**
 * Created by Kevin on 11/19/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoShootBallsBlue", group="Igutech")
public class AutoShootBallsBlue extends LinearOpMode {
    public void runOpMode() {
        float flywheelSpeed = -.54f;
        double engaged = .63d;
        double disengaged = 0.05d;

        Hardware hardware = new Hardware(hardwareMap);
        hardware.init();
        new AutonomousUtils(hardware);

        waitForStart();
        hardware.flywheel.setPower(Globals.flywheelWheelSpeed);
        hardware.right.setPower(0.25f);
        hardware.left.setPower(0.25f);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hardware.right.setPower(0);
        hardware.left.setPower(0);


        try {
            Thread.sleep(2000);
            hardware.WEST.setPosition(Globals.westEnabled);
            Thread.sleep(500);
            hardware.WEST.setPosition(Globals.westDisabled);
            hardware.flywheel.setPower(0f);
            Thread.sleep(2000);
            hardware.WEST.setPosition(Globals.westEnabled);
            Thread.sleep(500);
            hardware.WEST.setPosition(Globals.westDisabled);
            hardware.flywheel.setPower(0f);
        } catch (Exception e) {

        }
    }
}
