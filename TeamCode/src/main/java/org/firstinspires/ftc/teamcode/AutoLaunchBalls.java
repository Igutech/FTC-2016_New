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

public class AutoLaunchBalls extends LinearOpMode {
    public void runOpMode() {
        float firstDistance, secondDistance;
        firstDistance = .5f;
        secondDistance = 7.656f-firstDistance;
        float flywheelSpeed = .75f;

        Hardware hardware = new Hardware(hardwareMap);
        hardware.init();

        AutonomousUtils.driveEncoderFeet(firstDistance, .5f);
        hardware.waitForTick(500);
        hardware.flywheel.setPower(flywheelSpeed);
        hardware.waitForTick(3000);
        hardware.flywheel.setPower(0);
        hardware.waitForTick(500);
        AutonomousUtils.driveEncoderFeet(secondDistance, .5f);


    }

}
