package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Teleop;

/**
 * Created by Kevin on 10/19/2016.
 */
public class BallCap extends Module {
    public BallCap(Teleop t) {
        super(t);
    }

    boolean enabled;

    public void init() {
        enabled = false;
    }

    public void loop() {

        if (teleop.getGamepad()[1].a) {
            enabled = true;
        }

        if (enabled) {
            if (Math.abs(teleop.getGamepad()[2].right_stick_y) >= .1) {
                float speed = teleop.getGamepad()[2].right_stick_y;
                if (speed < 0) {
                    speed *= .65f;
                }
                hardware.ballcapper.setPower(speed);
            } else {
                hardware.ballcapper.setPower(0);
            }
        }
    }
}
