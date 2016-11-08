package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.modules.MultiplexColorSensor;

/**
 * Created by Kevin on 10/5/2016.
 */
public class Hardware {

    static final int LED_CHANNEL = 0;

    public DcMotor  left   = null;
    public DcMotor  right  = null;
    public DcMotor  brushes= null;
    public DeviceInterfaceModule dim1;
    public Servo leftbeacon;
    public Servo rightbeacon;
    public Servo WEST;
    public DcMotor flywheel;

    public LightSensor lightright;
    public LightSensor lightleft;

    public MultiplexColorSensor muxColor;

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

        flywheel = this.hwMap.dcMotor.get("flywheel");

        flywheel.setDirection(DcMotorSimple.Direction.FORWARD);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        flywheel.setPower(0);

        //Define and initialize Sensors and DIM modules
        dim1 = this.hwMap.deviceInterfaceModule.get("dim1");

        leftbeacon = this.hwMap.servo.get("leftbeacon");
        rightbeacon = this.hwMap.servo.get("rightbeacon");

        WEST = this.hwMap.servo.get("WEST");

        lightright = this.hwMap.lightSensor.get("lightright");
        lightleft = this.hwMap.lightSensor.get("lightleft");

        int[] ports = {1, 2};

        muxColor = new MultiplexColorSensor(hwMap, "mux", "ada", ports, 48, MultiplexColorSensor.GAIN_16X);

        // Set all motors to zero power
        left.setPower(0);
        right.setPower(0);

        // Set all motors to run with encoders.
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set all servos to start positions
        leftbeacon.setPosition(0.985);
        rightbeacon.setPosition(0.05);
        WEST.setPosition(0);

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
