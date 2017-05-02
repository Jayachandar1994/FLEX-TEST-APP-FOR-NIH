package cmsc436.msproject.util;

/**
 * This class contains variable and methods keep track of whether the user is testing their left
 * or right hand, arm, or leg
 */
public class Side {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    private static final String[] SIDE_NAMES = {"left", "right"};

    /**
     * Get the value of the selected side.
     *
     * @param side {@code String} of the selected side
     * @return LEFT if side is "left" , RIGHT if side is "right", -1 otherwise
     */
    public static int getSide(String side){
        side = side.toLowerCase();
        switch (side){
            case "left":
                return LEFT;
            case "right":
                return RIGHT;
            default:
                return -1;
        }
    }

    /**
     * Get the String name of the selected side
     *
     * @param side {@code LEFT} or {@code RIGHT} side
     * @return "left" if side is {@code LEFT}, "right" if side is {@code RIGHT}, null otherwise
     */
    public static String getSideName(int side){
        if (side == 0 || side == 1) {
            return SIDE_NAMES[side];
        }
        return null;
    }

    /**
     * Get the opposite side
     *
     * @param side {@code LEFT} or {@code RIGHT} side
     * @return {@code LEFT} if side is {@code RIGHT}, {@code RIGHT} if side is {@code LEFT}
     */
    public static int getOppositeSide(int side){
        if (side == LEFT){
            return RIGHT;
        }
        return LEFT;
    }
}
