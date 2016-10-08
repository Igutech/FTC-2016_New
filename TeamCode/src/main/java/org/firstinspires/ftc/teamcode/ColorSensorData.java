package org.firstinspires.ftc.teamcode;

/**
 * Created by Kevin on 10/8/2016.
 */
public class ColorSensorData {

    private int red;
    private int green;
    private int blue;
    private int alpha;

    public ColorSensorData(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
    public int getAlpha() {
        return alpha;
    }
}
