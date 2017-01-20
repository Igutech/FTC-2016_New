package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;

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
    float speed;
    int state;

    boolean holdEnabled = false;

    public void init() {
        enabled = false;
        toggled = false;
        hardware.lock.setPosition(Globals.lockEnabled);
        state = 1;
        hardware.ballcapper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void loop() {

        if (teleop.getGamepad()[1].a) {
            enabled = true;
            hardware.ballcapper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        if (teleop.getGamepad()[2].left_trigger > .70) {
            holdEnabled = true;
        } else {
            holdEnabled = false;
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




        if (enabled || true) {

            if (enabled) {
                if (holdEnabled) {
                    hardware.ballcaphold.setTargetPosition(Globals.ballcapholdEnabled);
                } else {
                    hardware.ballcaphold.setTargetPosition(Globals.ballcapholdDisabled);
                }

                hardware.ballcaphold.setPower(Globals.ballcapholdSpeed);
            } else {
                hardware.ballcaphold.setPower(0);
            }

            if (teleop.getGamepad()[2].a) {
                hardware.release.setPosition(Globals.releaseEnabled);
                enabled = true;
            } else {
                hardware.release.setPosition(Globals.releaseDisabled);
            }

            if (Math.abs(teleop.getGamepad()[2].right_stick_y) >= .1) {
                speed = teleop.getGamepad()[2].right_stick_y;
                if (speed < 0) {
                    speed= 0; //induces coasting
                }
                hardware.ballcapper.setPower(speed);
                hardware.lock.setPosition(Globals.lockDisabled);
            } else {

                hardware.ballcapper.setPower(0);
                hardware.lock.setPosition(Globals.lockEnabled);
            }


        }
    }
}
