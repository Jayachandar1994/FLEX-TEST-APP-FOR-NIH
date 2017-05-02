package cmsc436.msproject.curlTest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cmsc436.msproject.R;
import cmsc436.msproject.curlTest.calibrate.CurlCalibrateMenu;


/**
 * This activity is the entry point to the Curl Test and displays the Curl Test menu
 */
public class CurlMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl_menu);

        findViewById(R.id.instructionsBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CurlInstructions.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.calibrateBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CurlCalibrateMenu.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.takeTestBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CurlSelect.class);
                startActivity(intent);
            }
        });
    }
}
