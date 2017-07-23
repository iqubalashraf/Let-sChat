package app.chat.letschat;

/**
 * Created by ashrafiqubal on 12/07/17.
 */

public class Constants {
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 2;
    public static final int VIEW_TYPE_MY_MESSAGE = 1;
    public static final int VIEW_TYPE_OTHER_MESSAGE = 3;

    public static final String REQUEST_SUCCESS = "";

    private static Constants constants = new Constants();
    private  Constants(){}
    public static Constants getInstance(){
        return  constants;
    }
    public static boolean getBuildVersion(){
        if (BuildConfig.DEBUG) {
            return true;
        }
        return false;
    }
}
