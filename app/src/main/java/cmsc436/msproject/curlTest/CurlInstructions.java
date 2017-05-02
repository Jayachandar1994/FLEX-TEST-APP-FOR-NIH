package cmsc436.msproject.curlTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cmsc436.msproject.R;

public class CurlInstructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl_instructions);

        String appTitle = getTitle() + " Instructions";
        setTitle(appTitle);
    }
}
