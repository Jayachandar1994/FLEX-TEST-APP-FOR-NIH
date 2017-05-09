package cmsc436.msproject.curlTest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cmsc436.msproject.R;
import cmsc436.msproject.curlTest.calibrate.CurlCalibrateMenu;
import edu.umd.cmsc436.frontendhelper.TrialMode;
import edu.umd.cmsc436.sheets.Sheets;


public class StartActivity extends AppCompatActivity {
    static final int START_COUNT_RIGHT_ARM = 1, START_COUNT_LEFT_ARM =2,
            START_COUNT_RIGHT_LEG = 3, START_COUNT_LEFT_LEG =4;
    TextView rightArmTextView, leftArmTextView, rightLegTextView, leftLegTextView;

    final String myTag = "DocsUpload";

    SheetManager sheetManager;
    private void initTest(){
        Sheets.TestType t = TrialMode.getAppendage(Info.EXTRAS);
        switch(t){
            case RH_CURL:
                findViewById(R.id.right_arm).setVisibility(View.VISIBLE);
                findViewById(R.id.right_arm_text_view).setVisibility(View.VISIBLE);
            case LH_CURL:
                findViewById(R.id.left_arm).setVisibility(View.VISIBLE);
                findViewById(R.id.left_arm_text_view).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initTest();
        sheetManager = new SheetManager(this);
        rightArmTextView = (TextView) findViewById(R.id.right_arm_text_view);
        leftArmTextView = (TextView) findViewById(R.id.left_arm_text_view);
        rightLegTextView = (TextView) findViewById(R.id.right_leg_text_view);
        leftLegTextView = (TextView) findViewById(R.id.left_leg_text_view);
    }

    public void startCurlRight(View view){
        if (CurlListener.rightArmMax == 0 && CurlListener.rightArmMin == 0){
            Intent intent = new Intent(this, CurlCalibrateMenu.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, CurlActivity.class);
            intent.putExtra("limb", START_COUNT_RIGHT_ARM);
            startActivityForResult(intent, START_COUNT_RIGHT_ARM);
            view.setVisibility(View.INVISIBLE);
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
            view.setVisibility(View.INVISIBLE);

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
            view.setVisibility(View.INVISIBLE);

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
            view.setVisibility(View.INVISIBLE);

        }
    }



    public void postData(float leftArm, float rightArm) {

        String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSfEkWAzWBf2V6JRqYnv2K8VpF9gbkb127bDAFwKUrBNzCqEjg/formResponse";
        HttpRequest mReq = new HttpRequest();
        String col1 = Float.toString(leftArm);
        String col2 = Float.toString(rightArm);

        String data = null;
        try {
            data = "entry.1451805253=" + URLEncoder.encode(col1,"UTF-8") + "&" +
                    "entry.8104665=" + URLEncoder.encode(col2,"UTF-8") ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String response = mReq.sendPost(fullUrl, data);
        Log.i(myTag, response);
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
//        final float finalLeftScore = leftScore;
//        final float finalRightScore = rightScore;
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                postData(finalLeftScore, finalRightScore);
//
//            }
//        });
//        t.start();
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

