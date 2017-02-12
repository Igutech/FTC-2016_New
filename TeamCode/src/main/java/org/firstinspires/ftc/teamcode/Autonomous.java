/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.modules.AutonomousUtils;

import java.util.HashMap;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Trapezoid Drive", group="Igutech")  // @Autonomous(...) is the other common choice
public class Autonomous extends LinearOpMode {

    public LinearOpMode getOp() {
        return (LinearOpMode)this;
    }
    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    int step = 0;
    double tickstart = 0;
    double ticks = 0;
    double speed;
    double lastspeed;
    int goalEncoderticks = 3212;
    int goalAccellerationPhase = 3212/3; //It's supposed to accellerate for 1/3rd of the trip
    int goalDecellerationPhase = 3212/3 *2; //This is when it's supposed to start decellerating



    //@Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

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



        waitForStart();
        hardware.preStartOperations();
        runtime.reset();

        //AutonomousUtils.pidGyro(7f,0.25f,0);
        //AutonomousUtils.gyroTurn(Motor.LEFT,TurnType.SWING,.05f,90);

        //int startGyroPos = AutonomousUtils.getGyroSensorData().getIntegratedZ();

        //telemetry.addData("gyro start pos", startGyroPos);
        //telemetry.update();

        AutonomousUtils.powerGyroTurn(90,20,0.3f,Motor.LEFT);


        /*float turnSpeed = 0.2f;
        float halfMultipler = 1f;
        int goalGyroPos = 90;
        int slowDegrees = 65;
        int isNegative;
        Boolean complete = true;

        if(goalGyroPos <0){isNegative =-1;}else{isNegative=1;}

        Motor motor = Motor.LEFT;
        slowDegrees = startGyroPos + slowDegrees;
        goalGyroPos = startGyroPos + goalGyroPos;
        while(complete){
            if(motor == Motor.LEFT){
                hardware.left.setPower(turnSpeed*isNegative*halfMultipler);
            }
            if(motor == Motor.RIGHT){
                hardware.right.setPower(turnSpeed*isNegative*halfMultipler);
            }
            if(slowDegrees <= AutonomousUtils.getGyroSensorData().getIntegratedZ()){
                halfMultipler = 0.2f;
                telemetry.addData("HALFTRIGGERED","ACTIVE");
            }
            if(goalGyroPos <= AutonomousUtils.getGyroSensorData().getIntegratedZ()){
                complete = false;
            }
            telemetry.addData("gyro pos", AutonomousUtils.getGyroSensorData().getIntegratedZ());
            telemetry.update();
        }
        hardware.left.setPower(0);
        hardware.right.setPower(0);
        */


        /*
        while (opModeIsActive()) {
            switch (step){
                case 0:
                    tickstart=runtime.milliseconds();
                    step++;
                    break;
                case 1:
                    ticks = runtime.milliseconds() - tickstart;
                    speed = ticks*0.0004;
                    hardware.left.setPower(speed);
                    hardware.right.setPower(speed);
                    if(hardware.left.getCurrentPosition() >=goalAccellerationPhase){
                        step++;
                        lastspeed = speed;
                    }
                    telemetry.addData("Status: ", " Increasing Speed");
                    telemetry.addData("Speed: ", speed);
                    break;
                case 2:
                    hardware.left.setPower(lastspeed);
                    hardware.right.setPower(lastspeed);
                    if(hardware.left.getCurrentPosition() >= goalDecellerationPhase){
                        step++;
                    }
                case 3:
                    tickstart =runtime.milliseconds();
                    step++;
                    break;
                case 4:
                    ticks =runtime.milliseconds() -tickstart;
                    speed = ticks*0.0005;
                    hardware.left.setPower(lastspeed-speed);
                    hardware.right.setPower(lastspeed-speed);
                    if(hardware.left.getCurrentPosition() >= goalEncoderticks){
                        step++;
                    }
                    if(lastspeed-speed<0.15){
                        step = 6;
                    }
                    telemetry.addData("Status: ", " Decreasing Speed");
                    telemetry.addData("Speed: ", lastspeed-speed);
                    break;
                case 5:
                    hardware.left.setPower(0);
                    hardware.right.setPower(0);
                    telemetry.addData("Status: ", " Stopped");
                    telemetry.addData("Speed: ", 0);
                    hardware.left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    hardware.right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    break;
                case 6:
                    hardware.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    hardware.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    hardware.left.setTargetPosition(goalEncoderticks);
                    hardware.right.setTargetPosition(goalEncoderticks);
                    step = 7;
                    break;
                case 7:
                    hardware.left.setPower(0.15);
                    hardware.right.setPower(0.15);
                    if(hardware.left.getCurrentPosition() <= goalEncoderticks+10 && hardware.left.getCurrentPosition() >= goalEncoderticks-10){
                        step = 5;
                    }
                    break;

            }

            telemetry.addData("Step: ", step);
            telemetry.addData("ticks: ", ticks);
            telemetry.addData("Encoder", hardware.left.getCurrentPosition());
            telemetry.update();


        }
        hardware.left.setPower(0);
        hardware.right.setPower(0);
        */

    }
}
