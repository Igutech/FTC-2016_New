package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.config.KeyNotFoundException;
import org.firstinspires.ftc.teamcode.config.Vars;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Kevin on 12/23/2016.
 */
@TeleOp(name="Edit Configuration", group="Igutech")
public class EditConfig extends OpMode {

    MenuStatus menuStatus = MenuStatus.MAIN;
    int idSelected = 0;
    int totalIDs = 0;
    ArrayList<String> keys = new ArrayList<>();

    Hardware hardware;

    @Override
    public void init() {
        hardware = new Hardware(hardwareMap);
        hardware.init(false);

        for (Map.Entry<String, Object> entry : Vars.vars.entrySet()) {
            totalIDs++;
            keys.add(entry.getKey());
        }
    }

    @Override
    public void loop() {


        //update display

        int parsing = 0;

        try {
            if (menuStatus.equals(MenuStatus.MAIN)) {
                for (String key : keys) {
                    String starting = "  ";
                    if (parsing == idSelected) {
                        starting = "> ";
                    }
                    telemetry.addLine(starting + key);
                    parsing++;
                }
            } else {

                for (String key : keys) {
                    if (parsing == idSelected) {
                        telemetry.addData(key, hardware.configuration.get(key).toString());
                    }
                    parsing++;
                }
            }
        } catch (KeyNotFoundException e) {
            e.printStackTrace();
        }


        //Parse controls
        if (gamepad1.dpad_right) {
            menuStatus = MenuStatus.EDITING;
        } else if (gamepad1.dpad_left) {
            menuStatus = MenuStatus.MAIN;
        }

        if (menuStatus.equals(MenuStatus.MAIN)) {
            if (gamepad1.dpad_up) {
                while (gamepad1.dpad_up) { //wait until released
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                idSelected--;
                if (idSelected < 0) {
                    idSelected = 0;
                }
            }

            if (gamepad1.dpad_down) {
                while (gamepad1.dpad_down) { //wait until released
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                idSelected++;
                if (idSelected > totalIDs) {
                    idSelected = totalIDs;
                }
            }
        }


        if (menuStatus.equals(MenuStatus.EDITING)) {
            
        }



    }

    private enum MenuStatus {
        MAIN,
        EDITING
    }
}
