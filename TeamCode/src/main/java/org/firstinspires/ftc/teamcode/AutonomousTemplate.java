package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;
import org.firstinspires.ftc.teamcode.modules.WESTTimerThread;

/**
 * Created by Logan on 11/2/2016.
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Autonomous Template", group="Igutech")
@Disabled
public class AutonomousTemplate extends LinearOpMode {

    public LinearOpMode getOp() {
        return (LinearOpMode)this;
    }

    public void runOpMode() {

        /*
         * NEW SYSTEM! Read below!
         *
         * When initializing an autonomous program, you need to do what you see in the following:
         *  - Initialize hardware with a reference to a linear OpMode
         *  - Call constructor with a reference to the hardware object
         *   * This allows the AutonomousUtils class to use interrupts
         *  - DO NOT initialize or attempt to use any methods inside AutonomousUtils unless from within a LienarOpMode.
         *   * This causes a null pointer because it cannot find a LinearOpMode object
         *
         *   >> DO NOT USE ANYTHING RELATED TO AUTONOMOUSUTILS IN TELEOP OR ANY REGULAR OPMODE! <<
         */

        final Hardware hardware = new Hardware(this);
        hardware.init();
        new AutonomousUtils(hardware);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean done = false;
                while (!done) {
                    hardware.updateAutonomous(getOp());
                    AutonomousUtils.hardware = hardware;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    if (!getOp().opModeIsActive()) {
                        done = true;
                    }
                }
            }
        }).start();

        telemetry.addData("Calibration complete", "");
        telemetry.update();
        hardware.dim1.setLED(0,true); //turns on led when done with init

        waitForStart();
        hardware.preStartOperations();

        //TODO: Create your autonomous program here. Refer to the engineering notebook appendix for instructions.

    }

}
