package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Teleop;

/**
 * Created by Kevin on 10/5/2016.
 */
public class DriveTrain extends Module {

    boolean switchone = false;
    boolean reversed = false;

    public DriveTrain (Teleop t) {
        super(t);
    }
    @Override
    public void loop() {
        float joyThr = teleop.getGamepad()[1].left_stick_y;
        float joyYaw = teleop.getGamepad()[1].right_stick_x;
        float slomo = 1-teleop.getGamepad()[1].right_trigger;

        if (joyThr > .90f) {
            joyThr = .90f;
        } else if (joyThr < -.90f) {
            joyThr = -.90f;
        }

        float rightPow = joyThr + (joyYaw * .5f);
        float leftPow = joyThr + (-joyYaw * .5f);

        if (rightPow > 1) {
            leftPow -= (rightPow - 1.0);
            rightPow = 1.0f;
        }
        if (leftPow > 1) {
            rightPow -= (leftPow - 1.0);
            leftPow = 1.0f;
        }
        if (rightPow < -1) {
            leftPow += (-1.0 - rightPow);
            rightPow = -1.0f;
        }
        if (leftPow < -1) {
            rightPow += (-1.0 - leftPow);
            leftPow = -1.0f;
        }

        if (slomo < .5f) {
            slomo = .2f;
        }

        if (!switchone) {
            if (teleop.getGamepad()[1].back) {
                switchone = true;//toggle to reverse driving
                if(reversed){
                    reversed = false;
                } else {
                    reversed = true;
                }
            }
        }else {
            if (!teleop.getGamepad()[1].back) {
                switchone =false;
            }
        }


        if(!reversed) {
            hardware.right.setPower(rightPow*slomo);
            hardware.left.setPower(leftPow*slomo);
        } else {
            hardware.left.setPower(-rightPow*slomo);
            hardware.right.setPower(-leftPow*slomo);
        }


        teleop.telemetry.addData("slowmo factor", slomo);
    }
}
