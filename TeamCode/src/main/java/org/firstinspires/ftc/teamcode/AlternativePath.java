package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;
import org.firstinspires.ftc.teamcode.modules.WESTTimerThread;

/**
 * Created by Tilman G on 2/12/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AlternativePath", group="Igutech")
public class AlternativePath extends LinearOpMode {

    public LinearOpMode getOp() {
        return (LinearOpMode)this;
    }

    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Calibrating");
        telemetry.update();

        WESTTimerThread westTimer;

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

        hardware.preStartOperations();
        hardware.release.setPosition(Globals.releaseDisabled);
        westTimer = new WESTTimerThread(hardware);
        Thread t2 = new Thread(westTimer);
        t2.start();
        telemetry.addData("Status"," Ready to run");
        telemetry.update();
        waitForStart();

        //turn on flywheel
        hardware.flywheel.setPower(-Globals.flywheelWheelSpeed);
        //drive to be in range of vortex
        //TODO: Depending on battery the shots fly to far
        AutonomousUtils.pidGyro(1f, .25f, 0);

        //wait till flywheel is spun up
        delayTime(1000);
        //shooting balls
        westTimer.trigger();
        delayTime(1250);
        westTimer.trigger();
        delayTime(350);
        hardware.flywheel.setPower(0);
        //turn towards beacon
        AutonomousUtils.powerGyroTurn(50,30,0.3f,Motor.RIGHT);
        //drive to beacon
        AutonomousUtils.pidGyro(3,0.25f,50);
        //turn parallel to wall
        AutonomousUtils.powerGyroTurn(0-AutonomousUtils.getGyroSensorData().getIntegratedZ(),-25,0.15f,Motor.LEFT);
        //Delay a short amount of time so the robot comes to rest
        AutonomousUtils.stopDriving();
        delayTime(250);
        //It's assumed that we drove over the line so we'll back up first a tiny bit
        AutonomousUtils.resetEncoders();
        //back up slowly
        AutonomousUtils.tankDriving(-.05f);
        boolean checkvar = true;
        //check to see if we cross the line
        while(opModeIsActive() && hardware.left.getCurrentPosition() < 300 && checkvar)
        {
            if(AutonomousUtils.getLightSensorData(0).getData() > Globals.lightThreshold){
                checkvar = false;
            }
            if(hardware.left.getCurrentPosition() > 250){
                AutonomousUtils.tankDriving(0.15f);
            }
            if(hardware.left.getCurrentPosition() <-150){
                checkvar = false;
            }
        }
        AutonomousUtils.stopDriving();
        //TODO: PUT BEACON BUTTON PUSH CODE HERE
        AutonomousUtils.pidGyro(2.5f,0.15f, 0);
        AutonomousUtils.stopDriving();
        delayTime(340);
        AutonomousUtils.resetEncoders();
        checkvar = true;
        while(opModeIsActive() && checkvar)
        {
            AutonomousUtils.tankDriving(0.15f);
            if(AutonomousUtils.getLightSensorData(0).getData() > Globals.lightThreshold){
                checkvar = false;
            }
            if(hardware.left.getCurrentPosition() < -1250){
                checkvar = false;
            }
        }
        AutonomousUtils.stopDriving();
        AutonomousUtils.powerGyroTurn(0-AutonomousUtils.getGyroSensorData().getIntegratedZ(),0,0.10f,Motor.LEFT);
        telemetry.addData("Gyro", AutonomousUtils.getGyroSensorData().getIntegratedZ());
        telemetry.update();
        delayTime(1000);


    }

    public void delayTime(int msec){
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
