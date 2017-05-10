package cmsc436.msproject.curlTest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.umd.cmsc436.frontendhelper.TrialMode;
/* Node is a class that acts as an interface between the front end and the application. It gets the intents and executes them in the app */
public class Node extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_node);
        Intent intent = getIntent();
        Info.setEXTRAS(intent);
        Info.setUserId(TrialMode.getPatientId(intent));
        startActivityForResult(new Intent(this, CurlMenu.class),-1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        end();
    }

    @Override
    public void onBackPressed() {
        end();
        super.onBackPressed();
    }

    private void end(){
        Intent intent = new Intent();
        intent.putExtra(TrialMode.KEY_SCORE, Info.getFinalScore());
        setResult(RESULT_OK, intent);
        finish();
    }
}
