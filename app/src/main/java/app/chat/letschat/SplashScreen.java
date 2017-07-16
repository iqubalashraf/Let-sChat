package app.chat.letschat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ashrafiqubal on 12/07/17.
 */

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalsh_screen);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                proceedUsaully();
            }
        }, getResources().getInteger(R.integer.SPLASH_SCREEN_TIMEOUT));
    }
    private void proceedUsaully(){
//        Intent intent = new Intent();
//        startActivity(intent);
        startActivity(new Intent(this,RegisterActivity.class));
        finish();
    }
}
