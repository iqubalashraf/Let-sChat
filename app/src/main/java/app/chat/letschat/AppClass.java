package app.chat.letschat;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ashrafiqubal on 16/07/17.
 */

public class AppClass extends Application {

    public static final String TAG = AppClass.class.getSimpleName();
    private static Context context;

    private static AppClass mInstance;

    public static synchronized AppClass getInstance() {

        if (mInstance == null) {
            mInstance = new AppClass();
        }
        return mInstance;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fabric.with(this, new Crashlytics());
//        logUser();
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
        mInstance = this;
        if (Constants.getBuildVersion())
            Log.d(TAG, "Analaytic tracker calls");
    }
    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier(Constants.getDeviceUniqueId(context));
//        Crashlytics.setUserEmail("Country: "+ );
        Crashlytics.setString("Country", Constants.getCountryCode(context));
        Crashlytics.setUserName(GenralUtils.userName(context));
        Crashlytics.setString("Gender", GenralUtils.userGender(context)+"");
    }

}