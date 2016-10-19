package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Teleop;

/**
 * Created by Kevin on 10/19/2016.
 */
public class Brushes extends Module {
    public Brushes (Teleop t) {
        super(t);
    }

    public void loop() {
        if (teleop.getGamepad()[2].left_bumper) {
            hardware.brushes.setPower(1);
        } else if (teleop.getGamepad()[2].right_bumper) {
            hardware.brushes.setPower(-1);
        } else {
            hardware.brushes.setPower(0);
        }
    }
}
