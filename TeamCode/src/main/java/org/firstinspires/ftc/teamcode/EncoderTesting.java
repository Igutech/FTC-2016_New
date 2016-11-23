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

        AutonomousUtils.driveEncoderFeet(2f, .5f, true);
        AutonomousUtils.resetEncoders();
        AutonomousUtils.driveMotorFeet(1.5f, .5f, Motor.LEFT);
        AutonomousUtils.resetEncoders();
        AutonomousUtils.driveEncoderFeet(2f, .5f, true);
    }
}
