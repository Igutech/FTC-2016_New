package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Globals;
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
            hardware.beaconright.setPosition(Globals.beaconRightEnabled);
        } else {
            hardware.beaconright.setPosition(Globals.beaconRightDisabled);
        }

        if (teleop.getGamepad()[2].x) {
            hardware.beaconleft.setPosition(Globals.beaconLeftEnabled);
        } else {
            hardware.beaconleft.setPosition(Globals.beaconLeftDisabled);
        }
    }
}
