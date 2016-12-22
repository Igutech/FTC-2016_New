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


    public Object get(String key) {
        if (Vars.vars != null) {
            return Vars.vars.get(key);
        }
        return null;
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

    public boolean setDefaults() {
        HashMap<String, Object> defaults = new HashMap<String, Object>();
        defaults.put("WestEnabled", 0.35f);
        defaults.put("WestDisabled", 0.65f);
        try {
            yaml.dump(defaults, new FileWriter(configFile));
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
