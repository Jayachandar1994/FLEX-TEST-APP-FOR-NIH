package cmsc436.msproject.curlTest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

//        findViewById(R.id.instructionsBtn).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = new Intent(v.getContext(), CurlInstructions.class);
//                startActivity(intent);
//            }
//        });

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
                Intent intent = new Intent(v.getContext(), StartActivity.class);
                startActivityForResult(intent,-1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("FIISH", requestCode+"");
        setResult(RESULT_OK);
        finish();
    }
}
