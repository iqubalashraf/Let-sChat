package app.chat.letschat;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ashrafiqubal on 12/07/17.
 */

public class GenralUtils {
    public static final String dateFormat = "hh:mm a";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

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
    public static final String DATE_FORMAT_dd_dash_MMM_dash_yyyy_comma_HH_colon_mm_colon_am_pm="dd-MMM-yyyy, HH:mm a";




    public static boolean checkConnection(Context context) {
        return AppStatus.getInstance(context).isOnline();
    }

    public static String getFormattedTime(String unixTime){
        Date date = new Date(Long.parseLong(unixTime));
        return simpleDateFormat.format(date);
    }
}
