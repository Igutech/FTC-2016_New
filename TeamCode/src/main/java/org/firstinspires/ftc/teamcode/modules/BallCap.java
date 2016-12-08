package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Teleop;

/**
 * Created by Kevin on 10/19/2016.
 */
public class BallCap extends Module {
    public BallCap(Teleop t) {
        super(t);
    }

    public void loop() {
        if (Math.abs(teleop.getGamepad()[2].right_stick_y) >= .1) {
            float speed = teleop.getGamepad()[2].right_stick_y;
            hardware.ballcapper.setPower(speed);
        } else {
            hardware.ballcapper.setPower(0);
        }
    }
}
