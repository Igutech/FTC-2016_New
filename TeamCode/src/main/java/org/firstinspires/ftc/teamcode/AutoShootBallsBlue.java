package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;

/**
 * Created by Kevin on 11/19/2016.
 */
@Disabled
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutoShootBallsBlue", group="Igutech")
public class AutoShootBallsBlue extends LinearOpMode {
    public void runOpMode() {
        float flywheelSpeed = -.54f;
        double engaged = .63d;
        double disengaged = 0.05d;

        Hardware hardware = new Hardware(this);
        hardware.init();
        new AutonomousUtils(hardware);

        waitForStart();
        hardware.flywheel.setPower(Globals.flywheelWheelSpeed);

        AutonomousUtils.pidGyro(3.18f, .25f, 0);

        try {
            Thread.sleep(2000);
            hardware.WEST.setPosition(Globals.westEnabled);
            Thread.sleep(500);
            hardware.WEST.setPosition(Globals.westDisabled);
            Thread.sleep(2000);
            hardware.WEST.setPosition(Globals.westEnabled);
            Thread.sleep(500);
            hardware.WEST.setPosition(Globals.westDisabled);
            hardware.flywheel.setPower(0f);
        } catch (Exception e) {

        }
    }
}
