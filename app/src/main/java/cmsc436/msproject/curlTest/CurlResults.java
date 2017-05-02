package cmsc436.msproject.curlTest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import cmsc436.msproject.MainActivity;
import cmsc436.msproject.R;
import cmsc436.msproject.util.Side;
import cmsc436.msproject.util.Utilities;
import edu.umd.cmsc436.frontendhelper.TrialMode;
import edu.umd.cmsc436.sheets.Sheets;


public class CurlResults extends AppCompatActivity implements Sheets.Host{
    /**
     * Contains whether user is testing "ARM" or "LEG"
     */
    private String bodyPart;


    private static final int LIB_ACCOUNT_NAME_REQUEST_CODE = 1001;
    private static final int LIB_AUTHORIZATION_REQUEST_CODE = 1002;
    private static final int LIB_PERMISSION_REQUEST_CODE = 1003;
    private static final int LIB_PLAY_SERVICES_REQUEST_CODE = 1004;

    private Sheets sheet;


    /**
     * Number of complete curls the user has done for each hand. Index 0 holds the data for the left hand,
     * index 1 holds the dat for the right hand.
     */
    private int[] numOfCompletedCurls = new int[2];

    /**
     * Total amount of time it took for all the completed curls for each hand. Index 0 holds the data for the left hand,
     * index 1 holds the dat for the right hand.
     */
    private long[] totalCompletedCurlTime = new long[2];

    /**
     * Number of completed curls for the left side
     */
    private int leftCompletedCurls;

    /**
     * Number of completed curls for the right side
     */
    private int rightCompletedCurls;

    /**
     * Average time it took to complete one curl for the left side
     */
    private double leftAveCurlTime;

    /**
     * Average time it took to complete one curl for the right side
     */
    private double rightAveCurlTime;

    private final String COMPLETED_CURLS = "Completed Curls: ";

    private final String AVE_CURL_TIME = "Ave Curl Time (s): ";

    private static final DecimalFormat df = new DecimalFormat("#.#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl_results);

        // Get the intent
        Intent intent = getIntent();
        bodyPart = intent.getStringExtra(CurlUtil.BODY_PART).toLowerCase();
        numOfCompletedCurls = intent.getIntArrayExtra(CurlUtil.NUM_OF_COMPLETED_CURLS);
        totalCompletedCurlTime = intent.getLongArrayExtra(CurlUtil.TOTAL_COMPLETED_CURL_TIME);

        setResultVariables();

        // set activity title
        String title = (String) getTitle();
        title = title + " Results";
        setTitle(title);

        String leftSideLabelText = "Left " + bodyPart + " data:";
        ((TextView) findViewById(R.id.leftSideLabel)).setText(leftSideLabelText);

        String leftNumOfCurlsText = COMPLETED_CURLS + leftCompletedCurls;
        ((TextView) findViewById(R.id.leftNumOfCurls)).setText(leftNumOfCurlsText);

        String leftAveCurlTimeText = AVE_CURL_TIME + df.format(leftAveCurlTime);
        ((TextView) findViewById(R.id.leftAveCurlTime)).setText(leftAveCurlTimeText);

        String rightLabelText = "Right " + bodyPart + " data:";
        ((TextView) findViewById(R.id.rightSideLabel)).setText(rightLabelText);

        String rightNumOfCurlsText = COMPLETED_CURLS + rightCompletedCurls;
        ((TextView) findViewById(R.id.rightNumOfCurls)).setText(rightNumOfCurlsText);

        String rightAveCurlTimeText = AVE_CURL_TIME + df.format(rightAveCurlTime);
        ((TextView) findViewById(R.id.rightAveCurlTime)).setText(rightAveCurlTimeText);

        SharedPreferences sharedPreferences = getSharedPreferences(Utilities.MS_SHARED_PREF_TAG, Context.MODE_PRIVATE);
        //This links the sheet to the central sheets with the code in the third parameter
        //If you want to use our own sheets just creat another sheets object and do this exact process twice but with another code
        sheet = new Sheets(this,this, getString(R.string.app_name), "1YvI3CjS4ZlZQDYi5PaiA7WGGcoCsZfLoSFM0IdvdbDU","1GBFw0ffLR6WghtX6STbyxbHYYDIkAGKQc7Oi6aABsMw");

        String patient_id = TrialMode.getPatientId(intent);

        if(sharedPreferences != null) {
            Log.d("INSIDE", "Worked");
            sheet.writeData(Sheets.TestType.LH_CURL, patient_id + "", (float) leftAveCurlTime);
            sheet.writeData(Sheets.TestType.RH_CURL, patient_id + "", (float) rightAveCurlTime);
            Log.d("SHARE_TAG_public", "Worked");
            float[] leftTrialData = new float[]{(float)leftAveCurlTime};
            float[] rightTrialData = new float[]{(float)rightAveCurlTime};
            sheet.writeTrials(Sheets.TestType.LH_CURL, patient_id + "", leftTrialData);
            sheet.writeTrials(Sheets.TestType.LH_CURL, patient_id + "", rightTrialData);
            Log.d("SHARE_TAG_private", "Worked");
        }


        ((Button) findViewById(R.id.mainMenuBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    /**
     * Set the leftCompletedCurls, rightCompletedCurls, leftAveCurlTime, and rightAveCurlTime variables
     */
    private void setResultVariables(){
        leftCompletedCurls = numOfCompletedCurls[Side.LEFT];
        if (leftCompletedCurls != 0){
            leftAveCurlTime = totalCompletedCurlTime[Side.LEFT] / leftCompletedCurls / 1000;
        }

        rightCompletedCurls = numOfCompletedCurls[Side.RIGHT];
        if (rightCompletedCurls != 0){
            rightAveCurlTime = totalCompletedCurlTime[Side.RIGHT] / rightCompletedCurls / 1000;
        }
    }


    @Override
    public int getRequestCode(Sheets.Action action) {
        switch (action) {
            case REQUEST_ACCOUNT_NAME:
                return LIB_ACCOUNT_NAME_REQUEST_CODE;
            case REQUEST_AUTHORIZATION:
                return LIB_AUTHORIZATION_REQUEST_CODE;
            case REQUEST_PERMISSIONS:
                return LIB_PERMISSION_REQUEST_CODE;
            case REQUEST_PLAY_SERVICES:
                return LIB_PLAY_SERVICES_REQUEST_CODE;
            default:
                return -1; // boo java doesn't know we exhausted the enum
        }
    }



    public Activity getActivity() {
        return this;
    }


    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        sheet.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sheet.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void notifyFinished(Exception e) {
        if (e != null) {
            throw new RuntimeException(e); // just to see the exception easily in logcat
        }

        Log.i(getClass().getSimpleName(), "Done");
    }
}
