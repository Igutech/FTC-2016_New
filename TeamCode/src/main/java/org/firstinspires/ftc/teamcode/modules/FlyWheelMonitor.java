package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 11/6/2016.
 */
public class FlyWheelMonitor implements Runnable {

    private FlyWheel flyWheel;
    private ArrayList<Float> encoderValues = new ArrayList<Float>();
    private int lastPosition = 0;
    private float stDevSP = 200;
    private int resetTime = 1000;
    private ElapsedTime period = new ElapsedTime();

    public FlyWheelMonitor(FlyWheel flyWheel) {
        this.flyWheel = flyWheel;
        period.reset();
    }

    @Override
    public void run() {
        while (true) {
            encoderValues.add((float)Math.abs(flyWheel.hardware.flywheel.getCurrentPosition()-lastPosition));
            lastPosition = flyWheel.hardware.flywheel.getCurrentPosition();
            if (period.milliseconds() > resetTime) {
                encoderValues = new ArrayList<Float>();
                period.reset();
            }
        }
    }

    public boolean getStatus() {
        if (standardDeviation(encoderValues) < stDevSP) {
            return true;
        }
        return false;
    }

    public float sum (List<Float> a){
        if (a.size() > 0) {
            int sum = 0;

            for (Float i : a) {
                sum += i;
            }
            return sum;
        }
        return 0;
    }
    public float mean (List<Float> a){
        float sum = sum(a);
        float mean = 0f;
        mean = sum / (a.size() * 1.0f);
        return mean;
    }


    private float standardDeviation (List<Float> a){
        float sum = 0f;
        double mean = mean(a);

        for (Float i : a)
            sum += Math.pow((i - mean), 2);
        return (float) (Math.sqrt(sum / (a.size() - 1f))); // sample
    }
}
