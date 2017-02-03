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

    boolean holdEnabled = false;

    public void init() {
        enabled = false;
        toggled = false;
        hardware.lock.setPosition(Globals.lockEnabled);
        hardware.ballcapper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.ballcaphold.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.ballcaphold.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hardware.ballcapper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.ballcaphold.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void loop() {

        if (teleop.getGamepad()[1].a) {
            enabled = true;
        }

        if (teleop.getGamepad()[2].left_trigger > .70) {
            holdEnabled = true;
        } else {
            holdEnabled = false;
        }


        if (enabled || true) {

            if (enabled) {
                if (holdEnabled) {
                    hardware.ballcaphold.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    hardware.ballcaphold.setTargetPosition(Globals.ballcapholdEnabled);  //set it to go to clamp
                    if(hardware.ballcaphold.getCurrentPosition() > Globals.ballcapholdSlowZone){ //if it's far enough, apply more powah!
                        hardware.ballcaphold.setPower(Globals.ballcapclampSpeed);
                    }else{
                        hardware.ballcaphold.setPower(Globals.ballcapholdSpeed);
                    }

                } else {
                    hardware.ballcaphold.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    if(hardware.ballcaphold.getCurrentPosition()>100){
                        hardware.ballcaphold.setPower(-.15f);
                    } else {
                        hardware.ballcaphold.setPower(0);
                    }
                }
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
                if (speed < -.5) { //the user needs to push the stick further than halfway in order ot unravel
                    speed= -.10f; //when using steel cable, this needs to run slowly to uravel the  cable
                }else if (speed < 0){ //if the stick is not all the way pressed, this executes, coasting
                    speed = 0; //this just induces coasting as standard
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
