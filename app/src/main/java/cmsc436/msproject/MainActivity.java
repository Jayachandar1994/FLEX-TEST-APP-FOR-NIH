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


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.selCurlTestBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle extras;
                Intent intent = new Intent(v.getContext(), CurlMenu.class);
                if((extras = getIntent().getExtras()) != null)
                    intent.putExtras(extras);
                startActivity(intent);
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
