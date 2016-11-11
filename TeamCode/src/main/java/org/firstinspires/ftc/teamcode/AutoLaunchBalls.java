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
        double disengaged = 0.05d;

        Hardware hardware = new Hardware(hardwareMap);
        hardware.init();
        new AutonomousUtils(hardware);

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
        hardware.flywheel.setPower(0);

        AutonomousUtils.driveEncoderFeet(-.5f, .5f, false);
        hardware.waitForTick(50);
        hardware.right.setPower(-.5f);

        while (hardware.right.getCurrentPosition() > -920) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        hardware.right.setPower(0f);
        hardware.waitForTick(50);
        hardware.brushes.setPower(0);

        AutonomousUtils.resetEncoders();
        hardware.right.setPower(-.5f);
        hardware.left.setPower(-.5f);


        while (hardware.right.getCurrentPosition() > -1301 && hardware.left.getCurrentPosition() > -1301) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        hardware.right.setPower(0f);
        hardware.left.setPower(0f);
        hardware.waitForTick(50);

    }

}
