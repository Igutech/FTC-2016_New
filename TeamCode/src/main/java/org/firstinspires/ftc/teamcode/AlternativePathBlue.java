package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;
import org.firstinspires.ftc.teamcode.modules.WESTTimerThread;

/**
 * Created by Tilman G on 2/12/2017.
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AlternativePathBlue", group="Igutech")
public class AlternativePathBlue extends LinearOpMode {

    public boolean competition = true;

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
        hardware.flywheel.setPower(-Globals.flywheelWheelSpeed+0.05f);
        hardware.brushes.setPower(1f); //enable brushes
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
        AutonomousUtils.powerGyroTurn(-48,-25,0.3f,Motor.LEFT);
        //drive to beacon
        AutonomousUtils.pidGyro(4.4f,0.35f,-60);
        //turn parallel to wall
        AutonomousUtils.powerGyroTurn(0-AutonomousUtils.getGyroSensorData().getIntegratedZ(),35,0.18f,Motor.RIGHT);
        //Delay a short amount of time so the robot comes to rest
        AutonomousUtils.stopDriving();
        delayTime(250);
        //It's assumed that we drove over the line so we'll back up first a tiny bit
        AutonomousUtils.resetEncoders();
        //back up slowly
        AutonomousUtils.tankDriving(-.05f);
        boolean checkvar = true;
        //check to see if we cross the line
        while(opModeIsActive() && hardware.left.getCurrentPosition() < 400 && checkvar)
        {
            if(AutonomousUtils.getLightSensorData(1).getData() > Globals.lightThreshold){
                checkvar = false;
            }
            if(hardware.left.getCurrentPosition() > 300){
                AutonomousUtils.tankDriving(0.07f);
            }
            if(hardware.left.getCurrentPosition() <-150){
                checkvar = false;
            }
        }
        AutonomousUtils.stopDriving();
        ColorDetectionThread colorDetectionThread = new ColorDetectionThread(1.5f, 0f, 0f, 0f, BeaconState.BLUE);
        Thread t = new Thread(colorDetectionThread);
        t.start();
        delayTime(500);
        BeaconState state = colorDetectionThread.getState();
        telemetry.addData("State", state);
        telemetry.update();
        float distance = 0f;
        if (state == BeaconState.REDBLUE) {
            distance = .35f;
            //push first button
            AutonomousUtils.resetEncoders();
            AutonomousUtils.driveEncoderFeet(distance, .15f, false);
            hardware.beaconright.setPosition(Globals.beaconRightEnabled);
            delayTime(700);
            hardware.beaconright.setPosition(Globals.beaconRightDisabled);
        } else if (state == BeaconState.BLUERED) {
            //push second button
            hardware.beaconright.setPosition(Globals.beaconRightEnabled);
            delayTime(700);
            hardware.beaconright.setPosition(Globals.beaconRightDisabled);
        }
        if (competition) {
            AutonomousUtils.pidGyro(3f - distance, 0.25f, 3);
        } else {
            AutonomousUtils.pidGyro(2.5f - distance, 0.25f, 3);
        }
        AutonomousUtils.resetEncoders();
        Thread lineThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean checkvar = true;
                while (hardware.opModeIsActive() && checkvar) {
                    telemetry.addData("Light", AutonomousUtils.getLightSensorData(1).getData());
                    telemetry.update();
                    if(AutonomousUtils.getLightSensorData(1).getData() > Globals.lightThreshold){
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

        AutonomousUtils.powerGyroTurn(0 - AutonomousUtils.getGyroSensorData().getIntegratedZ(), 0, 0.10f, Motor.LEFT);
        telemetry.addData("Gyro", AutonomousUtils.getGyroSensorData().getIntegratedZ());
        telemetry.update();
        colorDetectionThread = new ColorDetectionThread(1.5f, 0f, 0f, 0f, BeaconState.BLUE);
        t = new Thread(colorDetectionThread);
        t.start();
        delayTime(500);
        state = colorDetectionThread.getState();
        telemetry.addData("State", state);
        telemetry.update();
        distance = 0f;
        if (state == BeaconState.REDBLUE) {
            distance = .35f;
            //push first button
            AutonomousUtils.resetEncoders();
            AutonomousUtils.driveEncoderFeet(distance, .15f, false);
            hardware.beaconright.setPosition(Globals.beaconRightEnabled);
            delayTime(700);
            hardware.beaconright.setPosition(Globals.beaconRightDisabled);
        } else if (state == BeaconState.BLUERED) {
            //push second button
            hardware.beaconright.setPosition(Globals.beaconRightEnabled);
            delayTime(700);
            hardware.beaconright.setPosition(Globals.beaconRightDisabled);
        }

        AutonomousUtils.resetEncoders();
        AutonomousUtils.resetEncoders();
        if (competition) {
            AutonomousUtils.driveEncoderFeetBackwards(1f+distance, .25f, false);
        } else {
            AutonomousUtils.driveEncoderFeetBackwards(distance, .25f, false);
        }
        AutonomousUtils.powerGyroTurn(125, 80, 0.55f, Motor.RIGHT);
        AutonomousUtils.pidGyro(4f, 0.25f, 125);



    }

    public void delayTime(int msec){
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
