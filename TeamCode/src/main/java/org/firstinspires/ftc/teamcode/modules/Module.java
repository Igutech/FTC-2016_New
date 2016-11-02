package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Teleop;

/**
 * Created by Kevin on 10/5/2016.
 */
public class Module {

    Teleop teleop;
    Hardware hardware;

    public Module(Teleop t) {
        this.teleop = t;
        this.hardware = t.getHardware();
    }

    public void init() {  }
    public void initLoop() {  }
    public void start() {  }
    public void loop() {  }
    public void stop() {  }
}
