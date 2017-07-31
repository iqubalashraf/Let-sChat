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
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "ETni5mdYRVBjlmJzju9r7R8tR";
    private static final String TWITTER_SECRET = "tHOzGLTMjjMREgeCSV968FLf9BVXSmuVp8F0zy5ZdeVIsnxjcF";
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
        Fabric.with(this, new Crashlytics());
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
//        if(!Constants.getBuildVersion()){
//            Fabric.with(this, new Crashlytics(), new TwitterCore(new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET)), new TweetComposer());
//            logUser();
//        }
        context = getApplicationContext();
        mInstance = this;
        if (Constants.getBuildVersion())
            Log.d(TAG, "Analaytic tracker calls");
    }
//    private void logUser() {
//        // TODO: Use the current user's information
//        // You can call any combination of these three methods
//        Crashlytics.setUserIdentifier(GeneralUtil.GetAccessToken(this));
////        Crashlytics.setUserEmail("user@fabric.io");
//        Crashlytics.setUserName(GeneralUtil.GetUserName(this));
//    }

}