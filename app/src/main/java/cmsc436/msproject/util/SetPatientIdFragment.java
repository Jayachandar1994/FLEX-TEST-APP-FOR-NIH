package cmsc436.msproject.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import cmsc436.msproject.R;

/**
 * This DialogFragment prompts the user to enter their Patient ID when they first use the app
 */
public class SetPatientIdFragment extends DialogFragment {
    /**
     * The EditText in which the user inputs the patient id
     */
    private EditText inputTextField;

    /**
     * The SharedPreferences of the app
     */
    private SharedPreferences sharedPreferences;

    private static final String LOG_TAG = SetPatientIdFragment.class.getSimpleName();

    public SetPatientIdFragment() {
        // Required empty public constructor
    }

    /**
     * Get a new instance of the SetPatientIdFragment
     *
     * @return a new instance of the SetPatientIdFragment
     */
    public static SetPatientIdFragment newInstance() {
        return new SetPatientIdFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setCancelable(false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_patient_id, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.MS_SHARED_PREF_TAG, Context.MODE_PRIVATE);

        inputTextField = (EditText) view.findViewById(R.id.patientIdInput);
        String patientId = sharedPreferences.getString(Utilities.PATIENT_ID_TAG, null);
        if (patientId != null){
            inputTextField.setText(patientId);
        }

        Button loginBtn = (Button) view.findViewById(R.id.setPatientIdBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String patientId = inputTextField.getText().toString();
                Log.d(LOG_TAG, "patientId = " + patientId);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Utilities.PATIENT_ID_TAG, patientId);
                editor.apply();
                dismiss();
            }
        });

        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String patientId = sharedPreferences.getString(Utilities.PATIENT_ID_TAG, null);
                if (patientId != null){
                    dismiss();
                }
                else {
                    getActivity().finish();
                    dismiss();
                }
            }
        });

        return view;
    }

}
