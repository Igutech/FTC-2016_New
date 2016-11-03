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

    private float speed;
    private boolean toggled;
    private ElapsedTime period = new ElapsedTime();

    public void init() {
        speed = 1f;
        toggled = false;
        period.reset();
    }

    public void loop() {


        if (teleop.getGamepad()[2].dpad_left) {
            toggled = true;
        }
        if (teleop.getGamepad()[2].dpad_right) {
            toggled = false;
        }

        if (teleop.getGamepad()[2].dpad_down) {
            if (period.milliseconds() >= 20) {
                speed -= 0.01f;
                if (speed < 0f) {
                    speed = 0f;
                }
                period.reset();
            }
        }

        if (teleop.getGamepad()[2].dpad_up) {
            if (period.milliseconds() >= 20) {
                speed += 0.01f;
                if (speed > 1f) {
                    speed = 1f;
                }
                period.reset();
            }
        }

        if (toggled) {
            hardware.flywheel.setPower(speed);
            teleop.telemetry.addData("FlyWheel", "Enabled");
        } else {
            hardware.flywheel.setPower(0);
            teleop.telemetry.addData("FlyWheel", "Disabled");
        }
        teleop.telemetry.addData("FlyWheel Speed", speed);
        teleop.telemetry.update();
    }
}