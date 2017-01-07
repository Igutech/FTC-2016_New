package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Teleop;

/**
 * Created by Kevin on 10/19/2016.
 */
public class Lights extends Module {
    public Lights(Teleop t) {
        super(t);
    }

    private boolean switchone = false;
    private boolean status = false;

    public void loop() {
        if (!switchone) {
            if (teleop.getGamepad()[2].back) {
                switchone = true;//toggle to reverse driving
                if(status){
                    status = false;
                } else {
                    status = true;
                }
            }
        }else {
            if (!teleop.getGamepad()[2].back) {
                switchone =false;
            }
        }


        if (status) {
            hardware.lights.setPower(.7);
        } else {
            hardware.lights.setPower(0);
        }
    }
}
