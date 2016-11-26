package org.firstinspires.ftc.teamcode;

/**
 * Created by Kevin on 11/26/2016.
 */
public class GyroSensorData {

    private int integratedZ;
    private float x;
    private float y;
    private float z;

    public GyroSensorData(int integratedZ, float x, float y, float z) {
        this.integratedZ = integratedZ;
        this.x = x;
        this.y = y;
        this.z = z;

    }

    public int getIntegratedZ() {
        return integratedZ;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
