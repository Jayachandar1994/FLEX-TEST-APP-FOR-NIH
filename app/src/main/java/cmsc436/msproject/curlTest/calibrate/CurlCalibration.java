package cmsc436.msproject.curlTest.calibrate;

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
import android.widget.Button;
import android.widget.TextView;

import cmsc436.msproject.R;
import cmsc436.msproject.curlTest.CurlMenu;
import cmsc436.msproject.curlTest.CurlUtil;
import cmsc436.msproject.util.Side;
import cmsc436.msproject.util.Utilities;

public class CurlCalibration extends AppCompatActivity implements SensorEventListener{
    /**
     * TextView to display calibration info
     */
    private TextView calibrationLabel;

    /**
     * TextView to display the timer
     */
    private TextView timerLabel;

    /**
     * Button to go back to the curl menu
     */
    private Button curlMenuBtn;

    /**
     * The body part to calibrate
     */
    private String bodyPart;

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
     * The position of the arm/foot, either CURLED or STRETCHED
     */
    private String position;

    /**
     * General calibration information
     */
    private String calibrateInfo;

    /**
     * Contains whether the activity has already tested the opposite arm/leg side
     */
    private boolean calibratedOppositeSide = false;

    /**
     * Contains whether the activity has already calibrate the opposite position (If currently
     * calibrating CURLED then the opposite position is STRETCHED and vice-versa)
     */
    private boolean calibratedOppositeMotion = false;

    /**
     * These variables contain the force along x, y, and z axes
     */
    private float x, y, z;

    /**
     * The total number of sensor events
     */
    private int sensorEventCount;

    /**
     * This timer counts down the number of seconds until the calibration begins. This is to give the
     * user a few seconds to ready themselves
     */
    private CountDownTimer startTimer;

    /**
     * This timer begins right after the startTimer CounDownTimer and it counts down the
     * the calibration time
     */
    private CountDownTimer calibrationTimer;

    /**
     * The number of milliseconds to wait before starting the calibration
     */
    private static final int startTime = 3000;

    /**
     * The number of milliseconds for the calibration
     */
    private static final int calibrationTime = 10000;

    /**
     * ToneGenerator to signal the user with beeps
     */
    private ToneGenerator toneGenerator;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final String LOG_TAG = CurlCalibration.class.getSimpleName();
    private static final String CALIBRATION_COMPLETED = "Completed calibration for ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl_calibration);

        // Get the intent data
        Intent intent = getIntent();
        side = intent.getIntExtra(CurlUtil.SIDE, 0);
        sideName = Side.getSideName(side);
        bodyPart = intent.getStringExtra(CurlUtil.BODY_PART).toLowerCase();
        calibratedOppositeSide = intent.getBooleanExtra(CurlUtil.TESTED_OPP_SIDE, false);
        position = CurlUtil.STRETCHED;

        // Set activity title
        calibrateInfo = "Calibrate " + Utilities.capitalize(sideName) + " " +
                Utilities.capitalize(bodyPart);
        String title = (String) getTitle();
        title = title + " " + calibrateInfo;
        setTitle(title);

        // Initialize layout Views
        calibrationLabel = (TextView) findViewById(R.id.calibrationLabel);

        timerLabel = (TextView) findViewById(R.id.timerLabel);
        timerLabel.setText(getSeconds(startTime));

        curlMenuBtn = (Button) findViewById(R.id.curlMenuBtn);
        curlMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CurlMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Initialize the sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Initialize ToneGenerator
        toneGenerator = new ToneGenerator(AudioManager.STREAM_RING, 100);

        // Initialize sensor data
        x = y = z = 0;
        sensorEventCount = 0;

        // Initialize timers
        startTimer = new CountDownTimer(startTime, 100) {
            @Override
            public void onTick(long l) {
                String seconds = getSeconds(l);
                timerLabel.setText(seconds);
            }

            @Override
            public void onFinish() {
                String text = "Calibrating " + Utilities.capitalize(sideName) + " " +
                        Utilities.capitalize(bodyPart);
                calibrationLabel.setText(text);
                calibrationTimer.start();
                beginTestBeep();
                registerAccelerometer();
            }
        };
        calibrationTimer = new CountDownTimer(calibrationTime, 100) {
            @Override
            public void onTick(long l) {
                String seconds = getSeconds(l);
                timerLabel.setText(seconds);
            }

            @Override
            public void onFinish() {
                unregisterAccelerometer();
                endTestBeep();
                storeToSharedPreferences();

                // if the user has not calibrated the opposite CURLED or STRETCHED position
                if (!calibratedOppositeMotion){
                    calibratedOppositeMotion = true;
                    resetAccelVariables();
                    position = CurlUtil.getOppositePosition(position);
                    String fragmentMessage = "Calibrate the " + position.toLowerCase() + " position of your " +
                            sideName + " " + bodyPart;
                    Utilities.displayAlertDialogFragment(getFragmentManager(), calibrateInfo, fragmentMessage);
                }
                else{
                    checkSharedPreferences();

                    // if the user has not calibrated the LEFT or RIGHT side
                    if (!calibratedOppositeSide){
                        // Calibrate the opposite side
                        Intent intent = new Intent(getApplicationContext(), CurlCalibration.class);
                        intent.putExtra(CurlUtil.SIDE, Side.getOppositeSide(side));
                        intent.putExtra(CurlUtil.BODY_PART, bodyPart.toUpperCase());
                        intent.putExtra(CurlUtil.TESTED_OPP_SIDE, true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else{
                        // Stop calibration
                        String text = CALIBRATION_COMPLETED + " both " + bodyPart + "s";
                        calibrationLabel.setText(text);
                        timerLabel.setVisibility(View.INVISIBLE);
                        curlMenuBtn.setVisibility(View.VISIBLE);

                        SharedPreferences sharedPreferences = getSharedPreferences(Utilities.MS_SHARED_PREF_TAG, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String key = CurlUtil.getSharedPrefKey(bodyPart);
                        Log.d(LOG_TAG, "key = " + key);
                        editor.putBoolean(key,true);
                        editor.apply();
                    }
                }
            }
        };

        // Display AlertDialogFragment to alert the user the calibration is beginning
        String fragmentMessage = "Calibrate the " + position.toLowerCase() + " position of your " +
                sideName + " " + bodyPart;
        Utilities.displayAlertDialogFragment(getFragmentManager(), calibrateInfo, fragmentMessage);
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
        if (startTimer != null){
            startTimer.cancel();
        }
        if (calibrationTimer != null){
            calibrationTimer.cancel();
        }
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
     * Reset the accelerometer variables
     */
    private void resetAccelVariables(){
        x = y = z = 0;
        sensorEventCount = 0;
    }

    /**
     * Store the accelerometer data to the device's SharedPreferences
     */
    private void storeToSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(Utilities.MS_SHARED_PREF_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        float xAve = x / sensorEventCount;
        float yAve = y / sensorEventCount;
        float zAve = z / sensorEventCount;
        String key = CurlUtil.getSharedPrefKey(sideName, bodyPart, position);

        Log.d(LOG_TAG, "key = " + key);
//        Log.d(LOG_TAG, "sensorEventCount = " + sensorEventCount + ", xAve = " + xAve +
//                ", yAve = " + yAve + ", zAve = " + zAve);

        editor.putFloat(key + "X",xAve);
        editor.putFloat(key + "Y",yAve);
        editor.putFloat(key + "Z",zAve);
        editor.apply();
    }

    /**
     * Check if the accelerometer data is being stored in the SharedPreference correctly
     */
    private void checkSharedPreferences(){
        String keyStretched = CurlUtil.getSharedPrefKey(sideName, bodyPart, CurlUtil.STRETCHED);
        String keyStretchedX = keyStretched + CurlUtil.X;
        String keyStretchedY = keyStretched + CurlUtil.Y;
        String keyStretchedZ = keyStretched + CurlUtil.Z;

        String keyCurled = CurlUtil.getSharedPrefKey(sideName, bodyPart, CurlUtil.CURLED);
        String keyCurledX = keyCurled + CurlUtil.X;
        String keyCurledY = keyCurled + CurlUtil.Y;
        String keyCurledZ = keyCurled + CurlUtil.Z;

        SharedPreferences sharedPreferences = getSharedPreferences(Utilities.MS_SHARED_PREF_TAG, Context.MODE_PRIVATE);
        Log.d(LOG_TAG, keyStretchedX + " = " + sharedPreferences.getFloat(keyStretchedX, 0));
        Log.d(LOG_TAG, keyStretchedY + " = " + sharedPreferences.getFloat(keyStretchedY, 0));
        Log.d(LOG_TAG, keyStretchedZ + " = " + sharedPreferences.getFloat(keyStretchedZ, 0));
        Log.d(LOG_TAG, keyCurledX + " = " + sharedPreferences.getFloat(keyCurledX, 0));
        Log.d(LOG_TAG, keyCurledY + " = " + sharedPreferences.getFloat(keyCurledY, 0));
        Log.d(LOG_TAG, keyCurledZ + " = " + sharedPreferences.getFloat(keyCurledZ, 0));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            sensorEventCount++;
            x += sensorEvent.values[0];
            y += sensorEvent.values[1];
            z += sensorEvent.values[2];

//            Log.d(LOG_TAG, "x = " + sensorEvent.values[0] + ", y = " + sensorEvent.values[1] + ", z = " + sensorEvent.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
