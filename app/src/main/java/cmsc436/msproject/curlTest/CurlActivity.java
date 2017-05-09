package cmsc436.msproject.curlTest;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cmsc436.msproject.R;

public class CurlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl);

        new CountDownTimer(4000, 1000) {

            TextView timer = (TextView)findViewById(R.id.timer);
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished/1000 == 1){
                    timer.setText("Test begins in " + millisUntilFinished / 1000 + " second.");
                } else {
                    timer.setText("Test begins in " + millisUntilFinished / 1000 + " seconds.");
                }
            }

            public void onFinish() {
                // VIBRATE
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500); // Vibrate for 500 milliseconds

                // Make sound
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();

                beginTest();
            }

        }.start();
    }

    public void beginTest(){
        Integer limb = getIntent().getIntExtra("limb", 0);

        final SensorManager sensorService = (SensorManager) getApplicationContext().getSystemService(getApplicationContext().SENSOR_SERVICE);
        Sensor sensor = sensorService.getDefaultSensor(Sensor.TYPE_GRAVITY);
        final CurlListener listener = new CurlListener(limb);
        sensorService.registerListener(listener, sensorService.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_FASTEST);

        new CountDownTimer(11000, 1000) {

            TextView timer = (TextView)findViewById(R.id.timer);
            public void onTick(long millisUntilFinished) {
                timer.setText("Test Has Started!\nSeconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                // VIBRATE
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500); // Vibrate for 500 milliseconds

                // Make sound
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();

                String score = listener.getScoreString();

                Intent intent = new Intent();
                intent.putExtra("score", score);
                setResult(RESULT_OK, intent);
                sensorService.unregisterListener(listener);
                finish();
            }

        }.start();
    }
}

