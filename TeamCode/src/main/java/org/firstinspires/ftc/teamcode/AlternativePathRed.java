package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;
import org.firstinspires.ftc.teamcode.modules.WESTTimerThread;

/**
 * Created by Tilman G on 2/12/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AlternativePathRed", group="Igutech")
public class AlternativePathRed extends LinearOpMode {

    public boolean competition = false; //TODO: CHANGE THIS AT COMPETITIONS!

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
        hardware.flywheel.setPower(-Globals.flywheelWheelSpeed+0.04f);
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
        AutonomousUtils.powerGyroTurn(47,30,0.3f,Motor.RIGHT);
        //drive to beacon
        AutonomousUtils.pidGyro(3.3f,0.25f,47);
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
                AutonomousUtils.tankDriving(0.07f);
            }
            if(hardware.left.getCurrentPosition() <-150){
                checkvar = false;
            }
        }
        AutonomousUtils.stopDriving();
        ColorDetectionThread colorDetectionThread = new ColorDetectionThread(1.5f, 0f, 0f, 0f, BeaconState.RED);
        Thread t = new Thread(colorDetectionThread);
        t.start();
        delayTime(500);
        BeaconState state = colorDetectionThread.getState();
        telemetry.addData("State", state);
        telemetry.update();
        float distance = 0f;
        if (state == BeaconState.BLUERED) {
            distance = .35f;
            //push first button
            AutonomousUtils.resetEncoders();
            AutonomousUtils.driveEncoderFeet(distance, .15f, false);
            hardware.beaconleft.setPosition(Globals.beaconLeftEnabled);
            delayTime(700);
            hardware.beaconleft.setPosition(Globals.beaconLeftDisabled);
        } else if (state == BeaconState.REDBLUE) {
            //push second button
            hardware.beaconleft.setPosition(Globals.beaconLeftEnabled);
            delayTime(700);
            hardware.beaconleft.setPosition(Globals.beaconLeftDisabled);
        }
        if (competition) {
            AutonomousUtils.pidGyro(3f - distance, 0.15f, 0);
        } else {
            AutonomousUtils.pidGyro(2.5f - distance, 0.15f, 0);
        }
        AutonomousUtils.resetEncoders();
        Thread lineThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean checkvar = true;
                while (hardware.opModeIsActive() && checkvar) {
                    telemetry.addData("Light", AutonomousUtils.getLightSensorData(0).getData());
                    telemetry.update();
                    if(AutonomousUtils.getLightSensorData(0).getData() > Globals.lightThreshold){
                        checkvar = false;
                    }
                    if(hardware.left.getCurrentPosition() < -1250){
                        checkvar = false;
                    }
                }
                AutonomousUtils.interruptGyro();
            }
        });
        lineThread.start();
        AutonomousUtils.pidGyro(0.07f, 3);
        AutonomousUtils.stopDriving();

        AutonomousUtils.driveEncoderFeet(0.2f, 0.07f, false);
        AutonomousUtils.powerGyroTurn(0 - AutonomousUtils.getGyroSensorData().getIntegratedZ(), 0, 0.10f, Motor.LEFT);
        telemetry.addData("Gyro", AutonomousUtils.getGyroSensorData().getIntegratedZ());
        telemetry.update();
        colorDetectionThread = new ColorDetectionThread(1.5f, 0f, 0f, 0f, BeaconState.RED);
        t = new Thread(colorDetectionThread);
        t.start();
        delayTime(500);
        state = colorDetectionThread.getState();
        telemetry.addData("State", state);
        telemetry.update();
        distance = 0f;
        if (state == BeaconState.BLUERED) {
            distance = .25f;
            //push first button
            AutonomousUtils.resetEncoders();
            AutonomousUtils.driveEncoderFeet(distance, .15f, false);
            hardware.beaconleft.setPosition(Globals.beaconLeftEnabled);
            delayTime(700);
            hardware.beaconleft.setPosition(Globals.beaconLeftDisabled);
        } else if (state == BeaconState.REDBLUE) {
            //push second button
            hardware.beaconleft.setPosition(Globals.beaconLeftEnabled);
            delayTime(700);
            hardware.beaconleft.setPosition(Globals.beaconLeftDisabled);
        }
        AutonomousUtils.resetEncoders();
        if (competition) {
            AutonomousUtils.driveEncoderFeetBackwards(2f+distance, .25f, false);
        } else {
            AutonomousUtils.driveEncoderFeetBackwards(1f+distance, .25f, false);
        }
        AutonomousUtils.powerGyroTurn(-115, -80, 0.55f, Motor.LEFT);
        AutonomousUtils.resetEncoders();
        AutonomousUtils.pidGyro(4f, 0.25f, -115);


    }

    public void delayTime(int msec){
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
