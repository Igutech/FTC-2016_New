package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Kevin on 10/5/2016.
 */
public class Hardware {

    static final int LED_CHANNEL = 0;

    public DcMotor  left   = null;
    public DcMotor  right  = null;
    public DcMotor  brushes= null;
    public DeviceInterfaceModule dim1 = null;
    public DeviceInterfaceModule dim2 = null;
    public ColorSensor colorSensor1 = null;
    public ColorSensor colorSensor2 = null;

    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    public Hardware(HardwareMap map) {
        this.hwMap = map;
    }

    public void init() {

        // Define and Initialize Motors
        left   = this.hwMap.dcMotor.get("left");
        right  = this.hwMap.dcMotor.get("right");
        brushes= this.hwMap.dcMotor.get("brushes");
        left.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        right.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        //Define and initialize Sensors and DIM modules
        dim1 = this.hwMap.deviceInterfaceModule.get("dim1");
        dim2 = this.hwMap.deviceInterfaceModule.get("dim2");
        colorSensor1 = this.hwMap.colorSensor.get("sensor_color1"); //Currently the back color sensor
        colorSensor2 = this.hwMap.colorSensor.get("sensor_color2");

        // Set all motors to zero power
        left.setPower(0);
        right.setPower(0);

        // Set all motors to run with encoders.
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}
