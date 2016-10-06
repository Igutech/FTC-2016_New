package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware;

/**
 * Created by Kevin on 10/6/2016.
 */
public class AutonomousUtils {
    public static void driveEncoderTicks(int ticks, float power) { //460 ticks per foot
        try {
            Hardware.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Hardware.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            while (Hardware.left.getMode() != DcMotor.RunMode.RUN_TO_POSITION && Hardware.right.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
                Thread.sleep(10);
            }
            Hardware.right.setTargetPosition(ticks);
            Hardware.left.setTargetPosition(ticks);

            Hardware.right.setPower(power);
            Hardware.left.setPower(power);

            while (Hardware.left.getCurrentPosition() < Hardware.left.getTargetPosition() || Hardware.right.getCurrentPosition() < Hardware.right.getTargetPosition()) {
                Thread.sleep(10);
            }

            Hardware.right.setPower(0);
            Hardware.left.setPower(0);

            Hardware.left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            Hardware.right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetEncoders() {
        Hardware.left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Hardware.right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


    }
}
