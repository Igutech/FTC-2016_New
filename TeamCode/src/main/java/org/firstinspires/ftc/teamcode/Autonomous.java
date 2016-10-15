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

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Auto (3ft)", group="Igutech")  // @Autonomous(...) is the other common choice
public class Autonomous extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    private HashMap<String, Boolean> decisions;
    // DcMotor leftMotor = null;
    // DcMotor rightMotor = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Hardware.left = hardwareMap.dcMotor.get("left");
        Hardware.right = hardwareMap.dcMotor.get("right");
        Hardware.right.setDirection(DcMotorSimple.Direction.REVERSE);

        Hardware.dim1 = hardwareMap.deviceInterfaceModule.get("dim1");
        Hardware.dim1.setDigitalChannelMode(Hardware.LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);
        Hardware.dim2 = hardwareMap.deviceInterfaceModule.get("dim2");
        Hardware.colorSensor1 = hardwareMap.colorSensor.get("sensor_color1");
        Hardware.colorSensor2 = hardwareMap.colorSensor.get("sensor_color2");
        Hardware.dim1.setDigitalChannelState(Hardware.LED_CHANNEL, false); //turn off LED
        // Wait for the game to start (driver presses PLAY)

        decisions = new HashMap<String, Boolean>();

        boolean confirmed = false;
        decisions.put("Color", true);
        while (!confirmed) {
            if (gamepad1.b) {
                decisions.put("Color", true); //true if red
            }
            if (gamepad1.x) {
                decisions.put("Color", false); //false if blue
            }

            if (gamepad1.start) {
                confirmed = true;
            }

            if (decisions.get("Color")) {
                telemetry.addData("Color", "RED");
            } else {
                telemetry.addData("Color", "BLUE");
            }

            if (confirmed) {
                telemetry.addData("LOCKED IN", "TO BEGIN, PRESS START ON PHONE");
            }
            telemetry.update();
        }


        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)

        boolean done = false;

        while (opModeIsActive()) {
            if (!done) {
                AutonomousUtils.resetEncoders();
                AutonomousUtils.driveEncoderTicks(460*3, 1);

                done = true;

                // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
                // leftMotor.setPower(-gamepad1.left_stick_y);
                // rightMotor.setPower(-gamepad1.right_stick_y);
            }
        }
    }
}
