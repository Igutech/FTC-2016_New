package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Teleop;

/**
 * Created by Kevin on 10/5/2016.
 */
public class DriveTrain extends Module {

    public DriveTrain (Teleop t) {
        super(t);
    }
    @Override
    public void loop() {
        float joyThr = teleop.getGamepad()[1].left_stick_y;
        float joyYaw = teleop.getGamepad()[1].right_stick_x;

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


        hardware.right.setPower(rightPow);
        hardware.left.setPower(leftPow);
    }
}
