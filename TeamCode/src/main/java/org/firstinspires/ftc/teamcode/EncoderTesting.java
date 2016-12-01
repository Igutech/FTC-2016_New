package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;

/**
 * Created by Kevin on 11/21/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="EncoderTesting", group="Igutech")
@Disabled
public class EncoderTesting extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Hardware hardware = new Hardware(hardwareMap);
        hardware.init();

        new AutonomousUtils(hardware);


        waitForStart();

        for (int i = 0; i < 20; i++) {
            AutonomousUtils.driveEncoderFeet(2f, .5f, true);
            AutonomousUtils.resetEncoders();
            hardware.waitForTick(250);
            AutonomousUtils.driveEncoderFeet(-2f, -.5f, true);
            AutonomousUtils.resetEncoders();
            hardware.waitForTick(250);
        }

    }
}
