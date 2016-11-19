package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Teleop;

/**
 * Created by Kevin on 10/19/2016.
 */
public class FlyWheel extends Module {
    public FlyWheel(Teleop t) {
        super(t);
    }

    private float targetSpeed;
    private float speed;
    private boolean toggled;
    private boolean triggered;
    private ElapsedTime period = new ElapsedTime();
    private FlyWheelMonitor monitor;
    WESTTimerThread westTimer;

    public void init() {
        targetSpeed = .54f;
        toggled = false;
        triggered = false;
        period.reset();

        //start monitor thread
        monitor = new FlyWheelMonitor(this);
        Thread t = new Thread(monitor);
        t.start();

        westTimer = new WESTTimerThread(hardware);
        Thread t2 = new Thread(westTimer);
        t2.start();
    }


    public void loop() {

        if (teleop.getGamepad()[2].right_trigger > .2) {
            triggered = true;
        }
        if (teleop.getGamepad()[2].right_trigger < .2) {
            triggered = false;
        }
        if (teleop.getGamepad()[2].dpad_left) {
            toggled = true;
        }
        if (teleop.getGamepad()[2].dpad_right) {
            toggled = false;
        }

        if (teleop.getGamepad()[2].dpad_down) {
            if (period.milliseconds() >= 20) {
                targetSpeed -= 0.01f;
                if (targetSpeed < 0f) {
                    targetSpeed = 0f;
                }
                period.reset();
            }
        }

        if (teleop.getGamepad()[2].dpad_up) {
            if (period.milliseconds() >= 20) {
                targetSpeed += 0.01f;
                if (targetSpeed > 1f) {
                    targetSpeed = 1f;
                }
                period.reset();
            }
        }

        if (teleop.getGamepad()[2].y) {
            targetSpeed = .54f;
        }

        if (monitor.getStatus()) {
            teleop.telemetry.addData(" ", "READY TO FIRE");
        } else {
            teleop.telemetry.addData(" ", "DO NOT FIRE");
        }

        if (targetSpeed > speed) {
            if (period.milliseconds() >= 20) {
                speed += 0.01f;
                if (speed > 1f) {
                    speed = 1f;
                }
                period.reset();
            }
        }

        if (targetSpeed < speed) {
            if (period.milliseconds() >= 20) {
                speed -= 0.01f;
                if (speed < 0f) {
                    speed = 0f;
                }
                period.reset();
            }
        }

        teleop.telemetry.addData("Target targetSpeed", targetSpeed);
        teleop.telemetry.addData("Actual Speed", speed);
        teleop.telemetry.addData("LastPosition", monitor.getLastPosition());

        if (toggled) {
            hardware.flywheel.setPower(-speed);
            teleop.telemetry.addData("FlyWheel", "Enabled");
        } else {
            hardware.flywheel.setPower(0);
            teleop.telemetry.addData("FlyWheel", "Disabled");
            speed = 0f;
        }
        if (triggered) {
            westTimer.trigger();
        }
    }

    public float getTargetSpeed() {
        return targetSpeed;
    }

    public float getSpeed() {
        return speed;
    }
}
