package cmsc436.msproject.curlTest;

/**
 * This class contains general variables and methods for the Curl Test
 */
public class CurlUtil {
    public static final String SIDE = "LEFT_OR_RIGHT";
    public static final String BODY_PART = "BODY_PART";
    public static final String CALIBRATED = "CALIBRATED";

    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";
    public static final String ARM = "ARM";
    public static final String LEG = "LEG";
    public static final String CURLED = "CURLED";
    public static final String STRETCHED = "STRETCHED";

    public static final String X = "X";
    public static final String Y = "Y";
    public static final String Z = "Z";

    public static final String TESTED_OPP_SIDE = "TESTED_OPP_SIDE";

    public static final String NUM_OF_CURLS = "NUM_OF_CURLS";
    public static final String NUM_OF_COMPLETED_CURLS = "NUM_OF_COMPLETED_CURLS";
    public static final String TOTAL_COMPLETED_CURL_TIME = "TOTAL_COMPLETED_CURL_TIME";

    public static final String CURL_RANGE = "CURL_RANGE";
    public static final String STRETCH_RANGE = "STRETCH_RANGE";

    /**
     * Get the shared preference key of the given parameters
     *
     * @param side "LEFT" or "RIGHT"
     * @param bodyPart "ARM" or "LEG"
     * @param position "CURLED" or "STRETCHED"
     * @return a key in the format of "side.bodyPart.position.", null if the given parameters do not
     * match their correct values
     */
    public static String getSharedPrefKey(String side, String bodyPart, String position){
        String sideUpper = side.toUpperCase();
        if (!sideUpper.equals(LEFT) && !sideUpper.equals(RIGHT)){
            return null;
        }

        String bodyPartUpper = bodyPart.toUpperCase();
        if (!bodyPartUpper.equals(ARM) && !bodyPartUpper.equals(LEG)){
            return null;
        }

        String positionUpper = position.toUpperCase();
        if (!positionUpper.equals(STRETCHED) && !positionUpper.equals(CURLED)){
            return null;
        }

        return sideUpper + "." + bodyPartUpper + "." + positionUpper + ".";
    }

    /**
     * Get the shared preference key to check if a body part has already been calibrated
     *
     * @param bodyPart "ARM" or "LEG"
     * @return the shared preference key in the format "CALIBRATED.bodyPart"
     */
    public static String getSharedPrefKey(String bodyPart){
        String bodyPartUpper = bodyPart.toUpperCase();
        return CALIBRATED + "." + bodyPartUpper;
    }

    /**
     * Get the opposite position of the passed parameter
     *
     * @param position Either "CURLED" or "STRETCHED"
     * @return "CURLED" if position is "STRETCHED" and vice-versa
     */
    public static String getOppositePosition(String position){
        if (position.toUpperCase().equals(CURLED)){
            return STRETCHED;
        }
        return CURLED;
    }
}

