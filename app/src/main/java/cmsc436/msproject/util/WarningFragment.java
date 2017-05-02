package cmsc436.msproject.util;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cmsc436.msproject.R;

/**
 * This fragment displays a warning to the user
 */
public class WarningFragment extends DialogFragment {
    private static final String TITLE_KEY = "TITLE";
    private static final String MESSAGE_KEY = "MESSAGE";

    public WarningFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of the WarningFragment
     *
     * @param title The title of the DialogFragment title name
     * @param message The message to display in the DialogFragment
     * @return WarningFragment object
     */
    public static WarningFragment newInstance(String title, String message) {
        WarningFragment frag = new WarningFragment();

        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(MESSAGE_KEY, message);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setCancelable(false);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_warning, container, false);

        Bundle args = getArguments();

        TextView titleLabel = (TextView) view.findViewById(R.id.title);
        titleLabel.setText(args.getString(TITLE_KEY));

        TextView messageLabel = (TextView) view.findViewById(R.id.message);
        messageLabel.setText(args.getString(MESSAGE_KEY));

        Button beginBtn = (Button) view.findViewById(R.id.okayBtn);
        beginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                dismiss();
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

}
