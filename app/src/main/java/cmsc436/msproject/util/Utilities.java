package cmsc436.msproject.util;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class contains general utility functions which can be used for any Java classes
 */
public class Utilities extends Activity {
    public static final String MS_SHARED_PREF_TAG = "MS_SHARED_PREF";
    public static final String PATIENT_ID_TAG = "PATIENT_ID";
    public static final String PATIENT_DIALOG_FRAG_TAG = "PATIENT_DIALOG_FRAG";
    public static final String ALERT_DIALOG_FRAG_TAG = "ALERT_DIALOG_FRAG";
    public static final String WARNING_DIALOG_FRAG_TAG = "WARNING_DIALOG_FRAG_TAG";
    private static final String LOG_TAG = Utilities.class.getSimpleName();

    /**
     * Capitalize the given word
     *
     * @param word The {@code String} to capitalize
     * @return The capitalized {@code String}
     */
    public static String capitalize(String word){
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    /**
     * Get the average value of an int array
     *
     * @param array An int array
     * @return Average value of the array
     */
    public static double average(int[] array){
        double sum = 0;
        for (int num: array){
            sum += num;
        }
        return sum / array.length;
    }

    /**
     * Display a SetPatientIdFragment to prompt the user to enter a patient id
     *
     * @param fragmentManager The calling activity's FragmentManager
     */
    public static void displayPatientLoginDialogFragment(FragmentManager fragmentManager){
        DialogFragment patientFragment = SetPatientIdFragment.newInstance();
        patientFragment.show(fragmentManager, PATIENT_DIALOG_FRAG_TAG);
    }

    /**
     * Display an {@code AlertFragment} to alert the user when the test is about to begin
     *
     * @param fragmentManager The calling activity's FragmentManager
     * @param title The title of the fragment
     * @param message The message in the {@code AlertFragment}
     */
    public static void displayAlertDialogFragment(FragmentManager fragmentManager, String title, String message) {
        DialogFragment alertFragment = AlertFragment.newInstance(title, message);
        alertFragment.show(fragmentManager, ALERT_DIALOG_FRAG_TAG);
    }

    /**
     * Display an {@code WaningFragment} to warn the user when something went wrong or is about to go wrong
     *
     * @param fragmentManager The calling activity's FragmentManager
     * @param title The title of the fragment
     * @param message The message of the WarningFragment
     */
    public static void displayWarningDialogFragment(FragmentManager fragmentManager, String title, String message) {
        DialogFragment alertFragment = WarningFragment.newInstance(title, message);
        alertFragment.show(fragmentManager, WARNING_DIALOG_FRAG_TAG);
    }

    /**
     * Save a String in a file using the phone's internal storage
     *
     * @param context The application context
     * @param fileName The file to save to
     * @param text The String to append to the end of the file
     */
    public static void saveToInternalStorage(Context context, String fileName, String text){
        File file = new File(context.getFilesDir(), fileName);
        try {
            if (!file.exists()){
                if (!file.createNewFile()){
                    Log.d(LOG_TAG, "Unable to create file " + file.getAbsolutePath());
                    return;
                }
            }

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();
            fw.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Read data from a file
     * This method is for debugging purposes
     *
     * @param context The application context
     * @param fileName The file to read from
     * @return {@code String} of the file data, null if the file does not exist
     */
    public static String readFromInternalStorage(Context context, String fileName){
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()){
            return null;
        }

        StringBuilder sb = new StringBuilder();
        FileInputStream inputStream;
        try {
            inputStream = context.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                sb.append(line).append("\n");
            }
            bufferedReader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        File file = new File(context.getFilesDir(), fileName);
//        if (!file.delete()){
//            Log.d(LOG_TAG, "Unable to delete file " + file.getAbsolutePath());
//        }

        return sb.toString();
    }

    /**
     * Get the maximum of two int value
     * @param val1 the first int
     * @param val2 the second int
     * @return the maximum int
     */
    public static int max(int val1, int val2){
        if (val1 > val2){
            return val1;
        }
        return val2;
    }
}
