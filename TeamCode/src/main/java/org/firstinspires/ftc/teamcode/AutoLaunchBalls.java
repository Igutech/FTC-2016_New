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
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Auto Launch Balls", group="Igutech")
public class AutoLaunchBalls extends LinearOpMode {
    public void runOpMode() {
        float firstDistance, secondDistance;
        firstDistance = .5f;
        secondDistance = 7.656f-firstDistance;
        float flywheelSpeed = 1f;
        double engaged = .6d;
        double disengaged = 0d;

        Hardware hardware = new Hardware(hardwareMap);
        hardware.init();

        waitForStart();

        hardware.brushes.setPower(1f);
        hardware.flywheel.setPower(0.53f);
        hardware.waitForTick(10);
        hardware.waitForTick(5000);
        hardware.WEST.setPosition(engaged);
        hardware.waitForTick(1500);
        hardware.WEST.setPosition(disengaged);
        hardware.waitForTick(1500);
        hardware.flywheel.setPower(0.55f);
        hardware.waitForTick(3000);
        hardware.WEST.setPosition(engaged);
        hardware.waitForTick(1500);
        hardware.WEST.setPosition(disengaged);
        hardware.waitForTick(8000);
        hardware.WEST.setPosition(engaged);
        hardware.waitForTick(1500);
        hardware.WEST.setPosition(disengaged);
        hardware.waitForTick(2000);
        hardware.flywheel.setPower(0);


    }

}
