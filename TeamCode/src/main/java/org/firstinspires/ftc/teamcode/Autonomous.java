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

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Trapezoid Drive", group="Igutech")  // @Autonomous(...) is the other common choice
public class Autonomous extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    int step = 0;
    double tickstart = 0;
    double ticks = 0;
    double speed;



    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Hardware hardware = new Hardware(this);
        hardware.init();
        new AutonomousUtils(hardware);

        //hardware.left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //hardware.right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            switch (step){
                case 0:
                    tickstart=runtime.milliseconds();
                    step++;
                    break;
                case 1:
                    ticks = runtime.milliseconds() - tickstart;
                    speed = ticks*0.0002;
                    hardware.left.setPower(speed);
                    hardware.right.setPower(speed);
                    if(ticks >=2000){
                        step++;
                    }
                    telemetry.addData("Status: ", " Increasing Speed");
                    telemetry.addData("Speed: ", speed);
                    break;
                case 2:
                    tickstart =runtime.milliseconds();
                    step++;
                    break;
                case 3:
                    ticks =runtime.milliseconds() -tickstart;
                    speed = ticks*0.0002;
                    hardware.left.setPower(0.5-speed);
                    hardware.right.setPower(0.5-speed);
                    if(ticks >=2500){
                        step++;
                    }
                    telemetry.addData("Status: ", " Decreasing Speed");
                    telemetry.addData("Speed: ", 0.5-speed);
                    break;
                case 4:
                    hardware.left.setPower(0);
                    hardware.right.setPower(0);
                    telemetry.addData("Status: ", " Stopped");
                    telemetry.addData("Speed: ", 0);
                    break;
            }

            telemetry.addData("Step: ", step);
            telemetry.addData("ticks: ", ticks);
            telemetry.update();


        }
        hardware.left.setPower(0);
        hardware.right.setPower(0);
    }
}
