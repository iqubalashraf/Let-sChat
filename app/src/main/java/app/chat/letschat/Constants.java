package app.chat.letschat;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by ashrafiqubal on 12/07/17.
 */

public class Constants {
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 2;
    public static final int VIEW_TYPE_MY_MESSAGE = 1;
    public static final int VIEW_TYPE_OTHER_MESSAGE = 3;

    public static final String REQUEST_SUCCESS = "";

    private static Constants constants = new Constants();

    private Constants() {
    }

    public static Constants getInstance() {
        return constants;
    }

    public static boolean getBuildVersion() {
        if (BuildConfig.DEBUG) {
            return true;
        }
        return false;
    }
    public static String getDeviceUniqueId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    public static String getCountryCode(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
        return tm.getSimCountryIso().toUpperCase();
    }
}
