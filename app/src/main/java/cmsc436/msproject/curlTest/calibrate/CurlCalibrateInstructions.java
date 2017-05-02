package cmsc436.msproject.curlTest.calibrate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cmsc436.msproject.R;

public class CurlCalibrateInstructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl_calibrate_instructions);

        String appTitle = getTitle() + " Calibration Instructions";
        setTitle(appTitle);
    }
}
