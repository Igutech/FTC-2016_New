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
    private boolean status = false;
    public int deltaPos;
    public int lastencoderpos;

    public FlyWheelMonitor(FlyWheel flyWheel) {
        this.flyWheel = flyWheel;
        period.reset();
    }

    @Override
    public void run() {



        while (true) {

            deltaPos = Math.abs(flyWheel.hardware.flywheel.getCurrentPosition()-lastPosition);

            encoderValues.add((float) deltaPos);
            lastPosition = flyWheel.hardware.flywheel.getCurrentPosition();


            if (standardDeviation(encoderValues) > stDevSP) {
                status = false;
            } else {
                status = true;
            }


            if (period.milliseconds() > resetTime) {
                ArrayList toRemove = new ArrayList();
                for (Float f: encoderValues) {
                    toRemove.add(f);
                }
                encoderValues.removeAll(toRemove);
                period.reset();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean getStatus() {
        return status;
    }

    public int getLastPosition() {
        return deltaPos;
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
