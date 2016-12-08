package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorMRGyro;
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
    public Servo beaconservo;
    public Servo WEST;
    public DcMotor flywheel;
    public DcMotor ballcapper;

    public LightSensor lightright;
    public LightSensor lightleft;

    public MultiplexColorSensor muxColor;

    public ModernRoboticsI2cGyro gyro;

    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    public Hardware(HardwareMap map) {
        this.hwMap = map;
    }

    public void init() {
        init(true);
    }

    public void init(boolean gyroflag) {

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

        beaconservo = this.hwMap.servo.get("beaconservo");

        WEST = this.hwMap.servo.get("WEST");

        lightright = this.hwMap.lightSensor.get("lightright");
        lightleft = this.hwMap.lightSensor.get("lightleft");

        lightright.enableLed(true);
        lightleft.enableLed(true);
        if (gyroflag) {
            gyro = (ModernRoboticsI2cGyro) this.hwMap.gyroSensor.get("gyro");

            gyro.calibrate();
            while (gyro.isCalibrating()) {
                this.waitForTick(10);
            }
        } else {
            gyro=null;
        }

        ballcapper = this.hwMap.dcMotor.get("ballcap");


        int[] ports = {0, 1, 2, 3};

        muxColor = new MultiplexColorSensor(hwMap, "mux", "ada", ports, 48, MultiplexColorSensor.GAIN_16X);

        // Set all motors to zero power
        left.setPower(0);
        right.setPower(0);

        // Set all motors to run with encoders.
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set all servos to start positions
        beaconservo.setPosition(0.98);
        WEST.setPosition(0.07);

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


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
