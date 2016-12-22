package org.firstinspires.ftc.teamcode.config;

import android.content.res.AssetManager;

import java.util.Map;

/**
 * Created by Kevin on 12/22/2016.
 */
public class Vars {
    public static float westEnabled = 0.30f;
    public static float westDisabled = 0.65f;
    public static float flywheelWheelSpeed = .54f;
    public static float beaconLeftEnabled = .98f;
    public static float beaconLeftDisabled = .2f;
    public static float beaconRightEnabled = .02f;
    public static float beaconRightDisabled = .78f;
    public static float lightThreshold = .3f;

    public static String configPath = "/IgutechConfig";

    public static Map<String, Object> vars;

    public static AssetManager globalAssets;
}
