package org.firstinspires.ftc.teamcode.config;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * Created by Kevin on 12/22/2016.
 */
public class ConfigHandler {

    private String fileName = "config.yml";
    private File fileDirectory = new File(Environment.getExternalStorageDirectory().toString() + Vars.configPath);
    private File configFile = new File(fileDirectory, fileName);
    private Yaml yaml;

    public ConfigHandler() {
        yaml = new Yaml();
        init();
    }

    private void init() {

        Vars.defaults = new HashMap<String, Object>();
        Vars.defaults.put("flywheelWheelSpeed", .54f);
        Vars.defaults.put("WestEnabled", 0.35f);
        Vars.defaults.put("WestDisabled", 0.65f);
        Vars.defaults.put("beaconLeftEnabled", 0.98f);
        Vars.defaults.put("beaconLeftDisabled", .2f);
        Vars.defaults.put("beaconRightEnabled", .02f);
        Vars.defaults.put("beaconRightDisabled", .78f);
        Vars.defaults.put("lightThreshold", .3f);


        if (configDirectoryExists()) {
            if (configExists()) {
                InputStream config;
                try {
                    config = new FileInputStream(configFile);
                    Vars.vars = (Map<String, Object>) yaml.load(config);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public Object get(String key) throws KeyNotFoundException {
        if (Vars.vars != null) {
            return Vars.vars.get(key);
        }
        throw new KeyNotFoundException();
    }

    public Double getDouble(String key) throws KeyNotFoundException {
        if (Vars.vars != null) {
            return (Double) Vars.vars.get(key);
        }
        throw new KeyNotFoundException();
    }

    public float getFloat(String key) throws KeyNotFoundException {
        if (Vars.vars != null) {
            return ((Double)Vars.vars.get(key)).floatValue();
        }
        throw new KeyNotFoundException();
    }

    public String getString(String key) throws KeyNotFoundException {
        if (Vars.vars != null) {
            return (String)Vars.vars.get(key);
        }
        throw new KeyNotFoundException();
    }

    public boolean set(String key, Object object) {
        if (Vars.vars != null) {
            if (Vars.vars.containsKey(key)) {
                Vars.vars.remove(key);
            }
            Vars.vars.put(key, object);

            try {
                yaml.dump(Vars.vars, new FileWriter(configFile));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }
        return false;
    }

    private boolean setDefaults() {
        try {
            yaml.dump(Vars.defaults, new FileWriter(configFile));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean configDirectoryExists() {
        if (fileDirectory.exists()) {
            return true;
        } else {
            if (fileDirectory.mkdirs()) {
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean configExists() {
        if (configFile.exists()) {
            return true;
        } else {
            try {
                if (configFile.createNewFile()) {
                    setDefaults();
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }


}
