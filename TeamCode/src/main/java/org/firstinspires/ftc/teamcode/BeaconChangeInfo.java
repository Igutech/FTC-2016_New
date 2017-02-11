package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;

/**
 * Created by Kevin on 2/10/2017.
 */
public class BeaconChangeInfo {
    public float initialvalue;
    public BeaconState targetColor;
    private ArrayList<Float> values;
    private boolean successful;

    public BeaconChangeInfo(float initialvalue, BeaconState targetColor) {
        this.initialvalue = initialvalue;
        this.targetColor = targetColor;
        values = new ArrayList<Float>();
        successful = false;
    }

    public void addNewValue(float value) {
        values.add(value);
    }

    public ArrayList<Float> getValues() {
        return values;
    }
    public int getTotalValues() {
        return values.size();
    }
    public void setSuccessful (boolean successful) {
        this.successful = successful;
    }
    public boolean getSuccessful () {
        return successful;
    }
}
