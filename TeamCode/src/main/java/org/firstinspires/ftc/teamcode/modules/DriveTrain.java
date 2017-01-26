package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Teleop;

/**
 * Created by Kevin on 10/5/2016.
 */
public class DriveTrain extends Module {

    boolean switchone = false;
    boolean switchtwo = false;
    boolean reversed = false;
    boolean slomo = false;
    float slowmo;
    float boostFactor = 0.8f; //this controlls the percentage of the joystick to throttle, due to run with encoders, 100percent corresponded to 80 percent actual power

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


        if (!switchone) {
            if (teleop.getGamepad()[1].left_bumper) {
                switchone = true;//toggle to reverse driving
                if(reversed){
                    reversed = false;
                } else {
                    reversed = true;
                }
            }
        }else {
            if (!teleop.getGamepad()[1].left_bumper) {
                switchone =false;
            }
        }

        if (!switchtwo) {
            if (teleop.getGamepad()[1].right_bumper) {
                switchtwo = true;
                if(slomo){
                    slomo = false;
                } else {
                    slomo = true;
                }
            }
        }else {
            if (!teleop.getGamepad()[1].left_bumper) {
                switchtwo = false;
            }
        }

        if(slomo) {
            slowmo = .2f;
        } else {
            slowmo = 1;

        }
        if(!reversed) {
            hardware.right.setPower(rightPow*slowmo*boostFactor);
            hardware.left.setPower(leftPow*slowmo*boostFactor);
        } else {
            hardware.left.setPower(-rightPow*slowmo*boostFactor);
            hardware.right.setPower(-leftPow*slowmo*boostFactor);
        }


        teleop.telemetry.addData("slowmo?", switchtwo);
    }
}
