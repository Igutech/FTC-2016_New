package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Hardware;

/**
 * Created by Logan on 11/16/2016.
 */

public class WESTTimerThread implements Runnable {

    Hardware hardware;
    private boolean triggered;

    public WESTTimerThread (Hardware hardware) {
        this.hardware = hardware;
        triggered = false;
    }

    @Override
    public void run() {
        while(true) {
            if (triggered) {
                hardware.WEST.setPosition(.6);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                hardware.WEST.setPosition(.05);
                triggered = false;
            }
        }
    }

    public void trigger() {
        triggered = true;
    }
}
