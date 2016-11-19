package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;

/**
 * Created by Logan on 11/18/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoShootBalls", group="Igutech")
public class AutoShootBalls extends LinearOpMode {
    public void runOpMode() {
        float flywheelSpeed = -.54f;
        double engaged = .50d;
        double disengaged = 0.05d;

        Hardware hardware = new Hardware(hardwareMap);
        hardware.init();
        new AutonomousUtils(hardware);

        waitForStart();

        hardware.brushes.setPower(1f);
        hardware.flywheel.setPower(flywheelSpeed);
        hardware.waitForTick(10);
        hardware.waitForTick(5000);
        hardware.WEST.setPosition(engaged);
        hardware.waitForTick(1500);
        hardware.WEST.setPosition(disengaged);
        hardware.waitForTick(5000);
        hardware.WEST.setPosition(engaged);
        hardware.waitForTick(1500);
        hardware.WEST.setPosition(disengaged);
        hardware.waitForTick(5000);
        hardware.WEST.setPosition(engaged);
        hardware.waitForTick(1500);
        hardware.WEST.setPosition(disengaged);
        hardware.flywheel.setPower(0);
        hardware.brushes.setPower(0);

        hardware.left.setPower(-.3);
        hardware.right.setPower(-.3);
        hardware.waitForTick(2000);
        hardware.left.setPower(0);
        hardware.right.setPower(0);
        hardware.waitForTick(100);
        hardware.left.setPower(-.5);
        hardware.waitForTick(500);
        hardware.left.setPower(0);
    }
}
