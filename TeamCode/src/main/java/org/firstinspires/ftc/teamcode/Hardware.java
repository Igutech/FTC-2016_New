package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorMRGyro;
import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;
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
    public Servo beaconleft;
    public Servo beaconright;
    public Servo WEST;
    public Servo lock; //this servo locks the movement of the ball lift system
    public Servo release; //this servo releases the holder for the ball.

    public DcMotor flywheel;
    public DcMotor ballcapper;
    public DcMotor ballcaphold;

    public LightSensor lightright;
    public LightSensor lightleft;

    public MultiplexColorSensor muxColor;

    public ModernRoboticsI2cGyro gyro;
    public ModernRoboticsI2cGyro gyro2;

    public DcMotor lights;

    LinearOpMode opMode = null;
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    public Hardware(HardwareMap map) {
        this.hwMap = map;
    }

    public Hardware(LinearOpMode opMode) {
        this.hwMap = opMode.hardwareMap;
        this.opMode = opMode;
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
        right.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors //need to flip for FALCON

        flywheel = this.hwMap.dcMotor.get("flywheel");

        flywheel.setDirection(DcMotorSimple.Direction.FORWARD);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); //This should allow the flywheel to coast to stop

        flywheel.setPower(0);

        //Define and initialize Sensors and DIM modules
        dim1 = this.hwMap.deviceInterfaceModule.get("dim1");

        beaconleft = this.hwMap.servo.get("beaconleft");
        beaconright = this.hwMap.servo.get("beaconright");

        WEST = this.hwMap.servo.get("WEST");

        lock = this.hwMap.servo.get("lock");

        release = this.hwMap.servo.get("release");
        release.setPosition(Globals.releaseDisabled);

        lightright = this.hwMap.lightSensor.get("lightright");
        lightleft = this.hwMap.lightSensor.get("lightleft");

        lights = null;

        lightright.enableLed(true);
        lightleft.enableLed(true);


        // Set all servos to start positions
        beaconleft.setPosition(Globals.beaconLeftDisabled);
        beaconright.setPosition(Globals.beaconRightDisabled);
        WEST.setPosition(Globals.westDisabled);


        if (gyroflag) {
            gyro = (ModernRoboticsI2cGyro) this.hwMap.gyroSensor.get("gyro");
            gyro.setI2cAddress(I2cAddr.create7bit(0x11));

            gyro2 = (ModernRoboticsI2cGyro) this.hwMap.gyroSensor.get("gyro2");
            gyro2.setI2cAddress(I2cAddr.create7bit(0x12));

            gyro.calibrate();
            gyro2.calibrate();

            while (gyro.isCalibrating() || gyro2.isCalibrating()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            gyro=null;
        }

        ballcapper = this.hwMap.dcMotor.get("ballcap");
        ballcapper.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ballcapper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);  //this thing gotta float for good reasons -Tilman

        ballcaphold = this.hwMap.dcMotor.get("ballcaphold");
        ballcaphold.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        ballcaphold.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ballcaphold.setTargetPosition(Globals.ballcapholdDisabled);
        ballcaphold.setPower(0);


        int[] ports = {0, 1, 2, 3};

        muxColor = new MultiplexColorSensor(hwMap, "mux", "ada", ports, 48, MultiplexColorSensor.GAIN_16X);

        // Set all motors to zero power
        left.setPower(0);
        right.setPower(0);

        // Set all motors to run with encoders.
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

    /**
     * Initializes components: Call this directly after the waitForStart function in autonomous.
     */
    public void preStartOperations() {
        GyroSensorData data1 = AutonomousUtils.getGyroSensorData(1);
        GyroSensorData data2 = AutonomousUtils.getGyroSensorData(2);

        int offset1 = Math.abs(data1.getIntegratedZ());
        int offset2 = Math.abs(data2.getIntegratedZ());

        AutonomousUtils.GyroTarget target = offset2<offset1 ? AutonomousUtils.GyroTarget.SENSOR2: AutonomousUtils.GyroTarget.SENSOR1;
        AutonomousUtils.setGyroTarget(target);

        gyro.resetZAxisIntegrator();
        gyro2.resetZAxisIntegrator();
    }

    @Deprecated
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

    public boolean opModeIsActive() {
        return opMode.opModeIsActive();
    }

    public void updateAutonomous(LinearOpMode opmode) {
        this.opMode = opmode;
    }
}
