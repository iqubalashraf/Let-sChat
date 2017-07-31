package app.chat.letschat;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ashrafiqubal on 12/07/17.
 */

public class GenralUtils {
    public static final String dateFormat = "hh:mm a";
    public static final String DATE_FORMAT_ddMMyy = "ddMMyy";
    public static final String DATE_FORMAT_yyyy_dash_MM_dash_dd = "yyyy-MM-dd";
    public static final String DATE_FORMAT_dd_dash_MMM_dash_yyyy = "dd-MMM-yyyy";
    public static final String DATE_FORMAT_dd_dash_MM_dash_yyyy = "dd-MM-yyyy";
    public static final String DATE_FORMAT_dd_slash_mm_slash_yyyy = "dd/MM/yyyy";
    public static final String DATE_FORMAT_EEE_space_comma_d_space_LLL = "EEE, d LLL";
    public static final String DATE_FORMAT_d_space_LLL = "d LLL";
    public static final String DATE_FORMAT_d_space_LLL_space_yyyy = "d LLL yyyy";
    public static final String DATE_FORMAT_dd = "dd";
    public static final String DATE_FORMAT_MMMM_yyyy = "MMMM yyyy";
    public static final String DATE_FORMAT_MM_YYYY = "MM";
    public static final String DATE_FORMAT_EEEE = "EEEE";
    public static final String DATE_FORMAT_dd_space_MM_space_yyyy = "dd MM yyyy";
    public static final String DATE_FORMAT_dd_space_MMM_space_yyyy = "dd MMM yyyy";
    public static final String DATE_FORMAT_dd_dot_MMM_dot_yyyy = "dd.MMM.yyyy";
    public static final String DATE_FORMAT_yyyy_dash_MM_dash_dd_space_HH_colon_mm_colon_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_dd_dash_MMM_dash_yyyy_comma_HH_colon_mm_colon_am_pm = "dd-MMM-yyyy, HH:mm a";
    private static final String SHARED_PREFERENCES_KEY = "app.chat.letschat";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);


    public static boolean checkConnection(Context context) {
        return AppStatus.getInstance(context).isOnline();
    }

    public static String getFormattedTime(String unixTime) {
        Date date = new Date(Long.parseLong(unixTime));
        return simpleDateFormat.format(date);
    }

    public static boolean isShareDialogShown(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        return prefs.getBoolean("isShareDialogShown", false);
    }

    public static void isShareDialogShown(Context context, boolean value) {
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isShareDialogShown", value);
        editor.apply();
    }

    public static boolean isRegistered(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        return prefs.getBoolean("isRegistered", false);
    }

    public static void isRegistered(Context context, boolean value) {
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isRegistered", value);
        editor.apply();
    }

    public static String userName(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        return prefs.getString("userName", "");
    }

    public static void userName(Context context, String name) {
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userName", name);
        editor.apply();
    }

    public static int userGender(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        return prefs.getInt("userGender", 2);
    }

    public static void userGender(Context context, int gender) {
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("userGender", gender);
        editor.apply();
    }

    public static int userAge(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        return prefs.getInt("userAge", 27);
    }

    public static void userAge(Context context, int age) {
        final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("userAge", age);
        editor.apply();
    }
}
