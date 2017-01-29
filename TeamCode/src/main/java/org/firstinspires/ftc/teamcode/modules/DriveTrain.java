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
    float slowmoval;
    float boostFactor = 0.78f; //this controlls the percentage of the joystick to throttle, due to run with encoders, 100percent corresponded to 80 percent actual power

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
            if (!teleop.getGamepad()[1].right_bumper) {
                switchtwo = false;
            }
        }

        if(slomo) {
            slowmoval = .2f;
        } else {
            slowmoval = 1;

        }
        if(!reversed) {
            hardware.right.setPower(rightPow*slowmoval*boostFactor);
            hardware.left.setPower(leftPow*slowmoval*boostFactor);
            hardware.lightleft.enableLed(true);
            hardware.lightright.enableLed(true);
        } else {
            hardware.left.setPower(-rightPow*slowmoval*boostFactor);
            hardware.right.setPower(-leftPow*slowmoval*boostFactor);
            int runtimer  = (int)Math.round(teleop.getRuntime());
            if ((runtimer & 1) == 0){ //this checks the last bit of the number to see if it's even!
                hardware.lightleft.enableLed(false); //it would be hella dank to have sound that plays too
                hardware.lightright.enableLed(false); //tilman will implement this in his leisure
            }else{
                hardware.lightleft.enableLed(true);
                hardware.lightright.enableLed(true);
            }

        }


        teleop.telemetry.addData("slowmo?", slowmoval);
    }
}
