package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Globals;
import org.firstinspires.ftc.teamcode.Teleop;

/**
 * Created by Kevin on 10/19/2016.
 */
public class BallCap extends Module {
    public BallCap(Teleop t) {
        super(t);
    }

    boolean enabled;
    boolean toggled;
    int state;

    public void init() {
        enabled = false;
        toggled = false;
        hardware.lock.setPosition(Globals.lockEnabled);
        state = 1;
    }

    public void loop() {

        if (teleop.getGamepad()[1].a) {
            enabled = true;
        }



        if (state == 1 && teleop.getGamepad()[2].a) {
            state = 2;
        }
        if (state == 2 && !teleop.getGamepad()[2].a) {
            state = 3;
        }
        if (state == 3 && teleop.getGamepad()[2].a) {
            state = 4;
        }
        if (state == 4 && !teleop.getGamepad()[2].a) {
            state = 1;
        }




        if (enabled) {

            if (Math.abs(teleop.getGamepad()[2].right_stick_y) >= .1) {
                float speed = teleop.getGamepad()[2].right_stick_y;
                if (speed < 0) {
                    speed *= .65f;
                }
                hardware.ballcapper.setPower(speed);
                hardware.lock.setPosition(Globals.lockDisabled);
            } else {

                hardware.ballcapper.setPower(0);
                hardware.lock.setPosition(Globals.lockEnabled);
            }

            teleop.telemetry.addData("Motor Power: ", teleop.getGamepad()[2].right_stick_y);


        }
    }
}
