package cmsc436.msproject.curlTest.calibrate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cmsc436.msproject.R;

/**
 * This activity is the entry point to the Curl Test calibration
 */
public class CurlCalibrateMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl_calibrate_menu);

        String appTitle = getTitle() + " Calibration";
        setTitle(appTitle);

        findViewById(R.id.instructionsBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CurlCalibrateInstructions.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.calibrateBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CurlCalibrateSelect.class);
                startActivity(intent);
            }
        });
    }
}
