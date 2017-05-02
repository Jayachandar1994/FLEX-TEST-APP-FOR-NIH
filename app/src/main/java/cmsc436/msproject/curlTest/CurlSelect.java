package cmsc436.msproject.curlTest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cmsc436.msproject.R;
import cmsc436.msproject.util.Side;
import cmsc436.msproject.util.Utilities;

/**
 * This activity allows the user to select whether they want to test their arms or feet, and then
 * prompts the user to pick either their left or right arm/leg to test first
 */
public class CurlSelect extends AppCompatActivity {
    /**
     * This TextView prompts the user to perform an action
     */
    private TextView selectLabel;

    /**
     * The first Button in the LinearLayout
     */
    private Button btn1;

    /**
     * The second Button in the LinearLayout
     */
    private Button btn2;

    /**
     * The title of the Activity
     */
    private String title;

    /**
     * The text of the selectLabel TextView
     */
    private String selectLabelText;

    /**
     * The btn1 label
     */
    private String btn1Label;

    /**
     * The btn2 label
     */
    private String btn2Label;

    /**
     * The name of body part user selected: "Arms" or "Feet". The value is null if user has not selected arm or
     * foot yet.
     */
    private String bodyPart;

    private Resources res;

    private static final String LOG_TAG = CurlSelect.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl_select);

        // Get the starting intent
        Intent intent = getIntent();
        bodyPart = intent.getStringExtra(CurlUtil.BODY_PART);
        Log.d(LOG_TAG, "User selected " + bodyPart);

        // Set the activity layout views
        title = (String) getTitle();
        selectLabel = (TextView) findViewById(R.id.selectLabel);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);

        res = getResources();

        // Set the non-view global variables
        setGlobalVariables();

        // Set the values/listeners of the activity layout views
        setTitle(title);
        selectLabel.setText(selectLabelText);
        btn1.setText(btn1Label);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                buttonClick(view);
            }
        });
        btn2.setText(btn2Label);
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                buttonClick(view);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume()");
        Log.d(LOG_TAG, "title = " + title);
        Log.d(LOG_TAG, "selectLabelText = " + selectLabelText);
        Log.d(LOG_TAG, "btn1Label = " + btn1Label);
        Log.d(LOG_TAG, "btn2Label = " + btn2Label);
        Log.d(LOG_TAG, "bodyPart = " + bodyPart);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Reset bodyPart variable if user went back to re-select arm or foot to test
        if (btn1Label.charAt(0) == 'A') {
            bodyPart = null;
        }
    }

    /**
     * Set the title, selectLabelText, btn1Label, and btn2Label variables
     */
    private void setGlobalVariables(){
        // if the user has not selected whether to test arm or foot
        if (bodyPart == null){
            title = title + " Select";
            selectLabelText = res.getString(R.string.selectArmOrLegLabel);
            btn1Label = res.getString(R.string.armsLabel);
            btn2Label = res.getString(R.string.legsLabel);
        }
        else {
            if (bodyPart.equals(CurlUtil.ARM)) {
                title = title + " Select Arm";
                selectLabelText = res.getString(R.string.selectArmLabel);
            }
            else{
                title = title + " Select Foot";
                selectLabelText = res.getString(R.string.selectLegLabel);
            }
            btn1Label = res.getString(R.string.leftUpperCase);
            btn2Label = res.getString(R.string.rightUpperCase);
        }
    }

    /**
     * Perform action when the button is clicked
     */
    private void buttonClick(View view){
        String text = (String) ((Button) view).getText();
        if (bodyPart == null){
            bodyPart = getBodyPart(text);
            // if selected body part is already calibrated
            if (calibrated(bodyPart)){
                Intent intent = new Intent(getApplicationContext(), CurlSelect.class);
                intent.putExtra(CurlUtil.BODY_PART, bodyPart);
                startActivity(intent);
            }
            else{
                Log.d(LOG_TAG, "User has not calibrated " + bodyPart);
                String title = "Warning";
                String message = "The device has not been calibrated for the " + bodyPart.toLowerCase() + " curl test";
                Utilities.displayWarningDialogFragment(getFragmentManager(), title, message);
            }
        }
        else{
            int side = Side.getSide(text);
            Log.d(LOG_TAG, "Selected side = " + text + " -> " + side);
            Intent intent = new Intent(getApplicationContext(), CurlTest.class);
            intent.putExtra(CurlUtil.SIDE, side);
            intent.putExtra(CurlUtil.BODY_PART, bodyPart);
            startActivity(intent);
        }
    }

    /**
     * Get the final constant String value of the selected body part
     *
     * @param bodyPart The body part the user selected
     * @return CurlUtil.ARM if user selected "Arms", CurlUtil.LEG otherwise
     */
    private String getBodyPart(String bodyPart){
        if (bodyPart.equals(res.getString(R.string.armsLabel))){
            return CurlUtil.ARM;
        }
        return CurlUtil.LEG;
    }

    /**
     * Check if the device is calibrated for the given body part
     *
     * @param bodyPart Name of the body part. This is either "Arms" or "Legs"
     * @return true if device is calibrated for the given body part, false otherwise
     */
    private boolean calibrated(String bodyPart){
        Log.d(LOG_TAG, "clibrated(" + bodyPart + ")");
        String key = CurlUtil.CALIBRATED + "." + bodyPart;
        Log.d(LOG_TAG, "key = " + key);
        SharedPreferences sharedPref = getSharedPreferences(Utilities.MS_SHARED_PREF_TAG, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }
}
