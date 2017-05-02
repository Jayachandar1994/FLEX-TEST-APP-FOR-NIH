package cmsc436.msproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import cmsc436.msproject.curlTest.CurlMenu;
import cmsc436.msproject.util.Utilities;

/**
 * The main activity which starts the app and lets the user pick which test they want to take
 *
 * @author Mohit Bisht
 */
public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.selCurlTestBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CurlMenu.class);
                startActivity(intent.putExtras(getIntent().getExtras()));
            }
        });

        findViewById(R.id.editPatientIdBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Utilities.displayPatientLoginDialogFragment(getFragmentManager());
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(Utilities.MS_SHARED_PREF_TAG, Context.MODE_PRIVATE);
        Log.d(LOG_TAG, Utilities.PATIENT_ID_TAG + " = " + sharedPreferences.getString(Utilities.PATIENT_ID_TAG, null));
        // if user has not entered patient id
        if (sharedPreferences.getString(Utilities.PATIENT_ID_TAG, null) == null){
            Utilities.displayPatientLoginDialogFragment(getFragmentManager());
        }
    }
}
