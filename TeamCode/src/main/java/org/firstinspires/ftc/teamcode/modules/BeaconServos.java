package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Teleop;

/**
 * Created by Kevin on 10/22/2016.
 */
public class BeaconServos extends Module {
    public BeaconServos(Teleop t) {
        super(t);
    }

    @Override
    public void loop() {

        if (teleop.getGamepad()[2].b) {
            hardware.beaconservo.setPosition(.32);
        } else {
            hardware.beaconservo.setPosition(.98);
        }
    }
}
