package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.teamcode.Globals;
import org.firstinspires.ftc.teamcode.Teleop;
import org.firstinspires.ftc.teamcode.config.Vars;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Kevin on 10/22/2016.
 */
public class ConfigChecker extends Module {
    public ConfigChecker(Teleop t) {
        super(t);
    }

    @Override
    public void init() {

        ArrayList<String> differentKeys = new ArrayList<>();

        for (Map.Entry<String, Object> entry : Vars.vars.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (!Vars.defaults.containsKey(key) || !Vars.defaults.get(key).equals(value)) {
                //value is not equal to default.
                differentKeys.add(key);
            }
        }

        for (String key : differentKeys) {
            teleop.telemetry.addData("Different Key: " + key, "Value: " + Vars.vars.get(key).toString());
        }
        teleop.telemetry.update();
    }
}
