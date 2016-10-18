package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.Device;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorAdafruitRGB;

/**
 * This AARDVARK HARDWARE program is designed to be used for the aardvark robot. Anytime you want to
 * use the aardvark robot, just call and then initialize this program.
 *
 * Using commands such as
 * robot.left.setpower(); or whatever
 */
public class AARDVARKhardware
{
    /* Public OpMode members. */
    public DcMotor  left   = null;
    public DcMotor  right  = null;
    public DeviceInterfaceModule dim1 = null;
    public DeviceInterfaceModule dim2 = null;
    public ColorSensor color1 = null;
    public ColorSensor color2 = null;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public AARDVARKhardware(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        left   = hwMap.dcMotor.get("left");
        right  = hwMap.dcMotor.get("right");
        left.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        right.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        //Define and initialize Sensors and DIM modules
        dim1 = hwMap.deviceInterfaceModule.get("dim1");
        dim2 = hwMap.deviceInterfaceModule.get("dim2");
        color1 = hwMap.colorSensor.get("sensor_color1"); //Currently the back color sensor
        color2 = hwMap.colorSensor.get("sensor_color2");

        // Set all motors to zero power
        left.setPower(0);
        right.setPower(0);

        // Set all motors to run with encoders.
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public int getBrightestColor(ColorSensor color)
    {
        //This function gets the most brightest color from the color sensor specified
        if(color.red()>color.green()&&color.red()>color.blue()){return 1;} //returns one on red
        else if(color.green()>color.red()&&color.green()>color.blue()){return 2;} //returns two on green
        else if(color.blue()>color.green()&&color.blue()>color.red()){return 3;} //returns three on blue
        else{return 0;}
    }
    public double StandardDeviation(ColorSensor color, int c){
        //This function gets the standard deviation compared to the other colors from the color sensor and the color chosen
        double colorsum = Math.abs(color.green())+Math.abs(color.red())+Math.abs(color.blue())+Math.abs(color.alpha());
        double coloraverage = colorsum/4;
        if(c==1){return Math.round(((Math.abs(color.red()-coloraverage)+coloraverage)/coloraverage)*100);}
        else if(c==2){return Math.round(((Math.abs(color.green()-coloraverage)+coloraverage)/coloraverage)*100);}
        else if(c==3){return Math.round(((Math.abs(color.blue()-coloraverage)+coloraverage)/coloraverage)*100);}
        else{return 1;}
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */
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

