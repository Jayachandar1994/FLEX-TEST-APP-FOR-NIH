package cmsc436.msproject.curlTest;

/**
 * Created by darpanshah on 5/8/17.
 */

import android.content.Intent;

import edu.umd.cmsc436.sheets.Sheets;

/**
 * Static class that stores all of the info
 */

public class Info {

    final static String MAIN_SPREADSHEET_ID = "1YvI3CjS4ZlZQDYi5PaiA7WGGcoCsZfLoSFM0IdvdbDU";
    final static String PRIVATE_SPREADSHEET_ID = "1_Trq5Ipmfowomyvj7S2FrZ9fQLNqgQhROQzSiu0GmVQ";
    public static Intent EXTRAS = null;
    static float FINAL_SCORE = -1;


    private static Sheets.TestType TEST_TYPE = null;
    static String USER_ID = "null";
    final static String APP_NAME = "FLEX";

    public static int getActionCode(Sheets.Action action){
        switch (action){
            case REQUEST_PERMISSIONS : return 1000;
            case REQUEST_ACCOUNT_NAME: return 1001;
            case REQUEST_PLAY_SERVICES: return 1002;
            case REQUEST_AUTHORIZATION: return 1003;
            case REQUEST_CONNECTION_RESOLUTION: return  1004;
        }
        return 0;
    }



    public static Sheets.TestType getTestType(){
        if(TEST_TYPE == null) return Sheets.TestType.RH_CURL;
        return TEST_TYPE;
    }

    public static void setTestType(Sheets.TestType t){
        TEST_TYPE = t;
    }


    public static void setUserId(String id){
        USER_ID = id;
    }

//    public static Sheets.TestType getTestType(int i){
//        switch (i){
//            case 1: return Sheets.TestType.RH_CURL;
//            case 2: return Sheets.TestType.LH_CURL;
////            case 1: return Sheets.TestType.RH_CURL;
////            case 1: return Sheets.TestType.RH_CURL;
//        }
//    }


    public static void setEXTRAS(Intent EXTRAS) {
        Info.EXTRAS = EXTRAS;
    }

    public static Intent getEXTRAS() {
        return EXTRAS;
    }

    public static float getFinalScore() {
        return FINAL_SCORE;
    }

    public static void setFinalScore(float finalScore) {
        FINAL_SCORE = finalScore;
    }
}