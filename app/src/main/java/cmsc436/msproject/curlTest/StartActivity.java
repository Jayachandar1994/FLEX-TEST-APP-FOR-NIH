package cmsc436.msproject.curlTest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cmsc436.msproject.R;
import cmsc436.msproject.curlTest.calibrate.CurlCalibrateMenu;
import edu.umd.cmsc436.frontendhelper.TrialMode;
import edu.umd.cmsc436.sheets.Sheets;

/* This is the main class responsible to executing the curl test. It is responsible for the particular tests
 * that the patient has to take and in the end displaying scores to the patient */

public class StartActivity extends AppCompatActivity {
    static final int START_COUNT_RIGHT_ARM = 1, START_COUNT_LEFT_ARM =2,
            START_COUNT_RIGHT_LEG = 3, START_COUNT_LEFT_LEG =4;
    TextView rightArmTextView, leftArmTextView, rightLegTextView, leftLegTextView;

    Button left_arm, right_arm, left_leg, right_leg;



    SheetManager sheetManager;
    private void initTest(){
        Sheets.TestType t = TrialMode.getAppendage(Info.EXTRAS);
        Log.i("OUTPUT: ", t.toString());

        if( t.toString() == "RH_CURL") {
            left_arm.setVisibility(View.INVISIBLE);
            leftArmTextView.setVisibility(View.INVISIBLE);
            left_leg.setVisibility(View.INVISIBLE);
            leftLegTextView.setVisibility(View.INVISIBLE);
            right_leg.setVisibility(View.INVISIBLE);
            rightLegTextView.setVisibility(View.INVISIBLE);
        } else  if( t.toString() == "LH_CURL") {
            right_arm.setVisibility(View.INVISIBLE);
            rightArmTextView.setVisibility(View.INVISIBLE);
            left_leg.setVisibility(View.INVISIBLE);
            leftLegTextView.setVisibility(View.INVISIBLE);
            right_leg.setVisibility(View.INVISIBLE);
            rightLegTextView.setVisibility(View.INVISIBLE);
        } else if( t.toString() == "RF_CURL") {
            right_arm.setVisibility(View.INVISIBLE);
            rightArmTextView.setVisibility(View.INVISIBLE);
            left_leg.setVisibility(View.INVISIBLE);
            leftLegTextView.setVisibility(View.INVISIBLE);
            left_arm.setVisibility(View.INVISIBLE);
            leftArmTextView.setVisibility(View.INVISIBLE);
        } else if( t.toString() == "LF_CURL") {
            right_arm.setVisibility(View.INVISIBLE);
            rightArmTextView.setVisibility(View.INVISIBLE);
            left_arm.setVisibility(View.INVISIBLE);
            leftArmTextView.setVisibility(View.INVISIBLE);
            right_leg.setVisibility(View.INVISIBLE);
            rightLegTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sheetManager = new SheetManager(this);

        rightArmTextView = (TextView) findViewById(R.id.right_arm_text_view);
        leftArmTextView = (TextView) findViewById(R.id.left_arm_text_view);
        rightLegTextView = (TextView) findViewById(R.id.right_leg_text_view);
        leftLegTextView = (TextView) findViewById(R.id.left_leg_text_view);

        left_arm = (Button) findViewById(R.id.left_arm);
        right_arm = (Button) findViewById(R.id.right_arm);
        left_leg = (Button) findViewById(R.id.left_leg);
        right_leg = (Button) findViewById(R.id.right_leg);

        initTest();

    }

    public void startCurlRight(View view){

        if (CurlListener.rightArmMax == 0 && CurlListener.rightArmMin == 0){
            Intent intent = new Intent(this, CurlCalibrateMenu.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, CurlActivity.class);
            intent.putExtra("limb", START_COUNT_RIGHT_ARM);
            startActivityForResult(intent, START_COUNT_RIGHT_ARM);
            view.setVisibility(View.VISIBLE);
        }

    }

    public void startCurlLeft(View view){


        if (CurlListener.leftArmMax == 0 && CurlListener.leftArmMin == 0){
            Intent intent = new Intent(this, CurlCalibrateMenu.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, CurlActivity.class);
            intent.putExtra("limb", START_COUNT_LEFT_ARM);
            startActivityForResult(intent, START_COUNT_LEFT_ARM);
            view.setVisibility(View.VISIBLE);

        }
    }

    public void startLiftRight(View view){

        if (CurlListener.rightLegMax == 0 && CurlListener.rightLegMin == 0){
            Intent intent = new Intent(this, CurlCalibrateMenu.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, CurlActivity.class);
            intent.putExtra("limb", START_COUNT_RIGHT_LEG);
            startActivityForResult(intent, START_COUNT_RIGHT_LEG);
            view.setVisibility(View.VISIBLE);

        }
    }

    public void startLiftLeft(View view){

        if (CurlListener.leftLegMax == 0 && CurlListener.leftLegMin == 0){
            Intent intent = new Intent(this, CurlCalibrateMenu.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, CurlActivity.class);
            intent.putExtra("limb", START_COUNT_LEFT_LEG);
            startActivityForResult(intent, START_COUNT_LEFT_LEG);
            view.setVisibility(View.VISIBLE);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == START_COUNT_LEFT_ARM||requestCode == START_COUNT_RIGHT_ARM||requestCode == START_COUNT_LEFT_LEG||requestCode == START_COUNT_RIGHT_LEG) {
            super.onActivityResult(requestCode, resultCode, data);
            float leftScore = 0;
            float rightScore = 0;
            float ten = (float) 10.0;
            Float rightTemp;
            Float leftTemp;
            if (resultCode == RESULT_OK && data != null) {
                String text = data.getStringExtra("score");
                if (requestCode == START_COUNT_RIGHT_ARM) {
                    rightTemp = Float.parseFloat(text);
                    rightScore = rightTemp / ten;
                    rightArmTextView.setText("Right Arm Score: Reps = " + text + " & curls per second = " + rightScore);
                    sheetManager.sendData(rightScore,new float[]{rightScore,rightTemp},TrialMode.getAppendage(Info.getEXTRAS()));
                    Info.setFinalScore(rightScore);
                } else if (requestCode == START_COUNT_LEFT_ARM) {
                    leftTemp = Float.parseFloat(text);
                    leftScore = leftTemp / ten;
                    leftArmTextView.setText("Left Arm Score: Reps = " + text + " & curls per second = " + leftScore);
                    sheetManager.sendData(leftScore,new float[]{leftScore,leftTemp},TrialMode.getAppendage(Info.getEXTRAS()));
                    Info.setFinalScore(leftScore);
                } else if (requestCode == START_COUNT_RIGHT_LEG) {
                    rightTemp = Float.parseFloat(text);
                    rightScore = rightTemp / ten;
                    rightLegTextView.setText("Right Leg Score: Reps = " + text + " & curls per second = " + rightScore);
                    sheetManager.sendData(rightScore,new float[]{rightScore,rightTemp},TrialMode.getAppendage(Info.getEXTRAS()));
                    Info.setFinalScore(rightScore);
                } else if (requestCode == START_COUNT_LEFT_LEG) {
                    leftTemp = Float.parseFloat(text);
                    leftScore = leftTemp / ten;
                    leftLegTextView.setText("Left Leg Score: Reps = " + text + " & curls per second = " + leftScore);
                    sheetManager.sendData(rightScore,new float[]{leftScore,leftTemp},TrialMode.getAppendage(Info.getEXTRAS()));
                    Info.setFinalScore(leftScore);
                }

                (findViewById(R.id.back_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_OK);
                        finish();
//                        StartActivity.this.onBackPressed();
                    }
                });
                (findViewById(R.id.back_button)).setVisibility(View.VISIBLE);
            }

        }
        else{
            sheetManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        sheetManager.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }
}

