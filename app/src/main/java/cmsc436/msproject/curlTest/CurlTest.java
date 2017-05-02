package cmsc436.msproject.curlTest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import cmsc436.msproject.R;
import cmsc436.msproject.curlTest.calibrate.CurlCalibration;
import cmsc436.msproject.util.Side;
import cmsc436.msproject.util.Utilities;

public class CurlTest extends AppCompatActivity implements SensorEventListener {
    /**
     * TextView to display curl test info
     */
    private TextView curlTestLabel;

    /**
     * TextView to display the number of curls
     */
    private TextView curlsLabel;

    /**
     * TextView to display the number of curls completed
     */
    private TextView completedCurlsLabel;

    /**
     * TextView to display the start timer
     */
    private TextView timerLabel;

    /**
     * The side the activity is currently calibrating. 0 if calibrating left side, 1 if calibrating
     * right side
     */
    private int side;

    /**
     * The spelled out name of the side the activity is calibrating
     */
    private String sideName;

    /**
     * The body part to calibrate
     */
    private String bodyPart;

    /**
     * Contains whether the activity has already tested the opposite arm/leg side
     */
    private boolean testedOppositeSide;

    /**
     * This timer counts down the number of seconds until the calibration begins. This is to give the
     * user a few seconds to ready themselves
     */
    private CountDownTimer startTimer;

    /**
     * True if user completed a full curl, false otherwise
     */
    private boolean completedCurl = false;

    /**
     * Number of curls the user has done for each hand
     */
    private int[] numOfCurls = {-1, -1};

    /**
     * Number of complete curls the user has done for each hand
     */
    private int[] numOfCompletedCurls = new int[2];

    /**
     * Total amount of time it took for all the completed curls for each hand
     */
    private long[] totalCompletedCurlTime = new long[2];

    /**
     * The time when the user starts a curl from a stretched out position
     */
    private long startCurlTime;

    /**
     * The time when the user ends a curl back in a stretched out position
     */
    private long endCurlTime;

    /**
     * Contains the x, y, z values when the arm/leg is in a stretched position
     */
    private float[] stretchedValues = new float[3];

    /**
     * Contains the x, y, z values when the arm/leg is in a curled position
     */
    private float[] curledValues = new float[3];

    /**
     * Contains the x, y, z point values of the previous sensor event
     */
    private float[] prevPoint = new float[3];

    /**
     * Number of milliseconds to wait before starting the test
     */
    private static final int COUNT_DOWN_TIME = 3000;

    /**
     * The maximum number of curls to test for
     */
    private static final int MAX_NUM_OF_CURLS = 10;

    /**
     * The allowed standard deviation for the arm/leg's stretched x, y, z points
     */
    private static final float STRETCHED_STD = 1.5f;

    /**
     * The allowed standard deviation for the arm/leg's curled x, y, z points
     */
    private static final float CURLED_STD = 1.5f;

    /**
     * ToneGenerator to signal the user with beeps
     */
    private ToneGenerator toneGenerator;

    private static final String LOG_TAG = CurlCalibration.class.getSimpleName();
    private static final String CURLS_LABEL_TEXT = "Curls: ";
    private static final String COMPL_CURLS_LABEL_TEXT = "Completed Curls: ";

    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl_test);

        // Get the intent data
        Intent intent = getIntent();
        side = intent.getIntExtra(CurlUtil.SIDE, 0);
        sideName = Side.getSideName(side);
        bodyPart = intent.getStringExtra(CurlUtil.BODY_PART).toLowerCase();
        testedOppositeSide = intent.getBooleanExtra(CurlUtil.TESTED_OPP_SIDE, false);
        if (testedOppositeSide){
            numOfCurls = intent.getIntArrayExtra(CurlUtil.NUM_OF_CURLS);
            numOfCompletedCurls = intent.getIntArrayExtra(CurlUtil.NUM_OF_COMPLETED_CURLS);
            totalCompletedCurlTime = intent.getLongArrayExtra(CurlUtil.TOTAL_COMPLETED_CURL_TIME);
        }
        Log.d(LOG_TAG, "numOfCurls = " + Arrays.toString(numOfCurls));
        Log.d(LOG_TAG, "numOfCompletedCurls = " + Arrays.toString(numOfCompletedCurls));
        Log.d(LOG_TAG, "totalCompletedCurlTime = " + Arrays.toString(totalCompletedCurlTime));

        // Set activity title
        String title = (String) getTitle();
        title = title + " " + Utilities.capitalize(sideName) + " " + Utilities.capitalize(bodyPart);
        setTitle(title);

        // Initialize activity layout views
        curlTestLabel = (TextView) findViewById(R.id.curlTestLabel);
        String curlTestLabelText = Utilities.capitalize(sideName) + " " + bodyPart + " test";
        curlTestLabel.setText(curlTestLabelText);

        curlsLabel = (TextView) findViewById(R.id.curlsLabel);
        String curlsLabelText = CURLS_LABEL_TEXT + 0;
        curlsLabel.setText(curlsLabelText);

        completedCurlsLabel = (TextView) findViewById(R.id.completedCurlsLabel);
        String completedCurlsLabelText = COMPL_CURLS_LABEL_TEXT + 0;
        completedCurlsLabel.setText(completedCurlsLabelText);

        timerLabel = (TextView) findViewById(R.id.timerLabel);

        // Initialize the sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initialize the toneGenerator
        toneGenerator = new ToneGenerator(AudioManager.STREAM_RING, 100);

        // Initialize the start timer
        startTimer = new CountDownTimer(COUNT_DOWN_TIME, 100) {
            @Override
            public void onTick(long l) {
                String seconds = getSeconds(l);
                timerLabel.setText(seconds);
            }

            @Override
            public void onFinish() {
                timerLabel.setVisibility(View.INVISIBLE);
                beginTestBeep();
                registerAccelerometer();
            }
        };

        // Initialize the curled and stretched x, y, z positions
        initArrayValues(curledValues, sideName, bodyPart, CurlUtil.CURLED);
        initArrayValues(stretchedValues, sideName, bodyPart, CurlUtil.STRETCHED);
        Log.d(LOG_TAG, "curledValues = " + Arrays.toString(curledValues));
        Log.d(LOG_TAG, "stretchedValues = " + Arrays.toString(stretchedValues));

        // Display AlertDialogFragment to alert the user the calibration is beginning
        String fragmentMessage = "Complete 10 curls with your " + sideName + " " + bodyPart;
        Utilities.displayAlertDialogFragment(getFragmentManager(), "Curl Test", fragmentMessage);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(LOG_TAG, "onWindowFocusChanged().hasFocus = " + hasFocus);
        if (hasFocus) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            startTimer.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        startTimer.cancel();
        unregisterAccelerometer();
        toneGenerator.release();
    }

    /**
     * Convert miliseconds to seconds
     *
     * @param milisec The number of miliseconds to convert
     * @return The number of seconds as a String
     */
    private String getSeconds(Number milisec){
        return Integer.toString((int) Math.ceil(milisec.doubleValue() /1000));
    }

    /**
     * Output short beep to alert the user the test has begun
     */
    private void beginTestBeep(){
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_1, 500);
    }

    /**
     * Output longer beep to alert the user the test has ended
     */
    private void endTestBeep(){
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 1000);
    }

    /**
     * Register the accelerometer sensor in the SensorManager
     */
    private void registerAccelerometer(){
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Unregister the accelerometer sensor from the SensorManager
     */
    private void unregisterAccelerometer(){
        sensorManager.unregisterListener(this);
    }

    /**
     * Initialize the array values from shared preferences
     *
     * @param array The array to initialize
     * @param side The "left" or "right" side the user is testing
     * @param bodyPart The body part ("arm" or "leg") the user is testing
     * @param position The arm/leg position
     */
    private void initArrayValues(float[] array, String side, String bodyPart, String position){
        Log.d(LOG_TAG, "initArrayValues()");
        String key = CurlUtil.getSharedPrefKey(side, bodyPart, position);
        Log.d(LOG_TAG, "key = " + key);

        SharedPreferences sharedPreferences = getSharedPreferences(Utilities.MS_SHARED_PREF_TAG, Context.MODE_PRIVATE);
        array[0] = sharedPreferences.getFloat(key + CurlUtil.X, 0);
        array[1] = sharedPreferences.getFloat(key + CurlUtil.Y, 0);
        array[2] = sharedPreferences.getFloat(key + CurlUtil.Z, 0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
//            Log.d(LOG_TAG, "x = " + x + ", y = " + y + ", z = " + z);

            if (isStretched(x, y, z, STRETCHED_STD) && !isStretched(prevPoint[0], prevPoint[1], prevPoint[2], STRETCHED_STD)){
                Log.d(LOG_TAG, bodyPart + " is stretched");
                Log.d(LOG_TAG, "x = " + x + ", y = " + y + ", z = " + z);
                numOfCurls[side]++;
                String curls = "Curls: " + numOfCurls[side];
                curlsLabel.setText(curls);
                if (completedCurl){
                    endCurlTime = System.currentTimeMillis();
                    long curlDuration = endCurlTime - startCurlTime;
                    totalCompletedCurlTime[side] += curlDuration;
                    numOfCompletedCurls[side]++;
                    String completedCurls = "Completed curls: " + numOfCompletedCurls[side];
                    completedCurlsLabel.setText(completedCurls);
                }
                completedCurl = false;
                Log.d(LOG_TAG, "numOfCurls = " + numOfCurls[side]);
                Log.d(LOG_TAG, "numOfCompletedCurls = " + numOfCompletedCurls[side]);

                startCurlTime = System.currentTimeMillis();

                if (numOfCurls[side] == MAX_NUM_OF_CURLS){
                    unregisterAccelerometer();
                    endTestBeep();
                    completeCurlTest();
                }
            }
            else if (isArmCurled(x, y, z, CURLED_STD)){
                Log.d(LOG_TAG, "Arm is curled");
                Log.d(LOG_TAG, "x = " + x + ", y = " + y + ", z = " + z);
                completedCurl = true;
            }

            prevPoint[0] = x;
            prevPoint[1] = y;
            prevPoint[2] = z;
        }
    }

    /**
     * True if the user's arm/leg is stretched out, false otherwise
     */
    private boolean isStretched(float x, float y, float z, float std){
        return inRange(x, stretchedValues[0] - std, stretchedValues[0] + std) &&
                inRange(y, stretchedValues[1] - std, stretchedValues[1] + std) &&
                inRange(z, stretchedValues[2] - std, stretchedValues[2] + std);
    }

    /**
     * True if the user's arm/leg is curled, false otherwise
     */
    private boolean isArmCurled(float x, float y, float z, float std){
        return inRange(x, curledValues[0] - std, curledValues[0] + std) &&
                inRange(y, curledValues[1] - std, curledValues[1] + std) &&
                inRange(z, curledValues[2] - std, curledValues[2] + std);
    }

    /**
     * This function checks if a value is within the given range
     *
     * @param val Value to check
     * @param min Minimum point of the range
     * @param max Maximum point of the range
     * @return True if val is between low and high, false otherwise
     */
    private boolean inRange(float val, float min, float max){
        //Log.d(LOG_TAG, "inRange: " + min + " <= " + val + " <= " + max + " -> " + ((min <= val) && (val <= max)));
        return ((min <= val) && (val <= max));
    }

    /**
     *  Complete the curl test
     */
    private void completeCurlTest(){
        // if the user has not tested their opposite body part side
        if (!testedOppositeSide){
            Intent intent = new Intent(this, CurlTest.class);
            intent.putExtra(CurlUtil.SIDE, Side.getOppositeSide(side));
            intent.putExtra(CurlUtil.BODY_PART, bodyPart.toUpperCase());
            intent.putExtra(CurlUtil.TESTED_OPP_SIDE, true);
            intent.putExtra(CurlUtil.NUM_OF_CURLS, numOfCurls);
            intent.putExtra(CurlUtil.NUM_OF_COMPLETED_CURLS, numOfCompletedCurls);
            intent.putExtra(CurlUtil.TOTAL_COMPLETED_CURL_TIME, totalCompletedCurlTime);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, CurlResults.class);
            intent.putExtra(CurlUtil.BODY_PART, bodyPart.toUpperCase());
            intent.putExtra(CurlUtil.NUM_OF_COMPLETED_CURLS, numOfCompletedCurls);
            intent.putExtra(CurlUtil.TOTAL_COMPLETED_CURL_TIME, totalCompletedCurlTime);
            startActivity(intent);
            finish();
        }
    }
}
