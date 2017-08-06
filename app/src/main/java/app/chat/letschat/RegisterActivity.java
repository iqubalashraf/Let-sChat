package app.chat.letschat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by ashrafiqubal on 12/07/17.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "RegisterActivity";
    private final String USER_NAME = "app.chat.letschat.USER_NAME";
    private final String USER_GENDER = "app.chat.letschat.USER_GENDER";
    private final String USER_AGE = "app.chat.letschat.USER_AGE";
    private final int MALE = 0, FEMALE = 1;
    EditText user_name, user_age;
    Button button_submit;
    RadioButton radio_button_male, radio_button_female;
    RadioGroup radio_button;
    int gender = 2, age = 0;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_register);
//        if (!Constants.getBuildVersion())
            if (GenralUtils.isRegistered(context)) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        initializeViews();
        initializeOnclickListener();
        if (!GenralUtils.isShareDialogShown(this)) {
            displayShareDialog();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        radio_button.clearCheck();
        user_name.setText("");
        gender = 2;
    }

    private void initializeViews() {
        user_name = (EditText) findViewById(R.id.user_name);
        user_age = (EditText) findViewById(R.id.user_age);
        button_submit = (Button) findViewById(R.id.button_submit);
        radio_button_male = (RadioButton) findViewById(R.id.radio_button_male);
        radio_button_female = (RadioButton) findViewById(R.id.radio_button_female);
        radio_button = (RadioGroup) findViewById(R.id.radio_button);
    }

    private void initializeOnclickListener() {
        button_submit.setOnClickListener(this);
        radio_button_male.setOnClickListener(this);
        radio_button_female.setOnClickListener(this);
    }

    private boolean validateForm() {
        if (user_name.getText().toString().equals("")) {
            user_name.setError(Html.fromHtml("<font color='#ff9a00'>Enter name</font>"));
            return false;
        }
        if (user_age.getText().toString().equals("")) {
            user_age.setError(Html.fromHtml("<font color='#ff9a00'>Enter age</font>"));
            return false;
        } else if (Integer.parseInt(user_age.getText().toString()) < 14) {
            user_age.setError(Html.fromHtml("<font color='#ff9a00'>You must be 14 year old to use this app.</font>"));
            return false;
        } else if (Integer.parseInt(user_age.getText().toString()) > 116) {
            user_age.setError(Html.fromHtml("<font color='#ff9a00'>Invalid age.</font>"));
            return false;
        }
        if (gender == 2) {
            Toast.makeText(context, "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                if (GenralUtils.checkConnection(this)) {
                    if (validateForm()) {
                        GenralUtils.userName(context, user_name.getText().toString());
                        GenralUtils.userAge(context, Integer.parseInt(user_age.getText().toString()));
                        GenralUtils.userGender(context, gender);
                        GenralUtils.isRegistered(context, true);
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.radio_button_male:
                gender = MALE;
                break;
            case R.id.radio_button_female:
                gender = FEMALE;
                break;
        }
    }

    private void displayShareDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Message");
        dialog.setIcon(R.drawable.ic_warning);
        dialog.setCancelable(false);
        dialog.setMessage("You're first time user, you need to share app with 5 whats app contacts to continue.");
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I found this amazing app, *Let's Chat* for making new friends. Let's give a try. Download now from *Play Store* and enjoy.\n "
                        + getResources().getString(R.string.app_bitly_link));
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "Whats app not installed", Toast.LENGTH_SHORT).show();
                }
                GenralUtils.isShareDialogShown(context, true);
            }
        });
        dialog.show();
    }
}
