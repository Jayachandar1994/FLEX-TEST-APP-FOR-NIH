package cmsc436.msproject.curlTest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.ArrayList;

public class CurlListener implements SensorEventListener {

/*calculating the curls. The main logic behin counting the curl is written in this class */
    float score;
    //private float x, y, z;
    private int sensorEventCount;
    boolean flag = true;
    public static float leftArmMin, leftArmMax, leftLegMin, leftLegMax, // public static so it could be accessed in the CurlCalibration class
            rightArmMin, rightArmMax, rightLegMin, rightLegMax;
    public float min, max, stddev;
    public ArrayList<Float> arr = new ArrayList<>();


    public CurlListener(Integer limb){
        switch (limb){
            case 1:
                stddev = 2;
                min = rightArmMin;
                max = rightArmMax;
                break;
            case 2:
                stddev = 2;
                min = leftArmMin;
                max = leftArmMax;
                break;
            case 3:
                stddev = (float) .25;
                min = rightLegMin;
                max = rightLegMax;
                break;
            case 4:
                stddev = (float) .25;
                min = leftLegMin;
                max = leftLegMax;
                break;
        }
        score = 0;
    }
/* using gravity sensor to find curls */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        if(sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY){
            sensorEventCount++;
            updateEvent(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    /* During the curl test all the values are stored in array list and progression of numbers is observed
     if the numbers are either in an increasing or decreasing progression and closer to the calibarated min and max
      the score is updated*/

    public String getScoreString(){

//        Log.i("OUTPUT: ", "outside for loop"); this is good

        for(int i = 0; i < arr.size(); i++) {

//          Log.i("OUTPUT: ", "inside for loop"); this is good
            if( arr.get(i) > (max -stddev) && !flag) { //and if flag == false
                Log.i("OUTPUT: ", Float.toString(score));
                score += 0.5;
                flag = true;
            }
            else if(arr.get(i) < (min +stddev) && flag) {
                Log.i("extend OUTPUT: ", Float.toString(score));
                score += 0.5;
                flag = false;
            }
        }

        Float scoreString = score;
        return scoreString.toString();
    }

    public int updateEvent(SensorEvent event){
        arr.add(event.values[1]);
        return 1;
    }
}

