package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;

/**
 * Created by Kevin on 11/21/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="EncoderTesting", group="Igutech")
public class EncoderTesting extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Hardware hardware = new Hardware(hardwareMap);
        hardware.init();

        new AutonomousUtils(hardware);


        waitForStart();

        AutonomousUtils.driveEncoderFeet(4f, .8f, true);
    }
}
