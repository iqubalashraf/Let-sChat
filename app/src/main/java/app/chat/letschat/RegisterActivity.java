package app.chat.letschat;

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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "RegisterActivity";
    private final String USER_NAME = "app.chat.letschat.USER_NAME";
    private final String USER_GENDER = "app.chat.letschat.USER_GENDER";
    private final int MALE=0, FEMALE=1;
    EditText user_name;
    Button button_submit;
    RadioButton radio_button_male, radio_button_female;
    RadioGroup radio_button;
    int gender=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeViews();
        initializeOnclickListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        radio_button.clearCheck();
        user_name.setText("");
        gender = 2;
    }

    private void initializeViews(){
        user_name = (EditText)findViewById(R.id.user_name);
        button_submit = (Button)findViewById(R.id.button_submit);
        radio_button_male = (RadioButton)findViewById(R.id.radio_button_male);
        radio_button_female = (RadioButton)findViewById(R.id.radio_button_female);
        radio_button = (RadioGroup)findViewById(R.id.radio_button);
    }
    private void initializeOnclickListener(){
        button_submit.setOnClickListener(this);
        radio_button_male.setOnClickListener(this);
        radio_button_female.setOnClickListener(this);
    }
    private boolean validateForm(){
        boolean result = true;
        if (user_name.getText().toString().equals("")) {
            user_name.setError(Html.fromHtml("<font color='#ff9a00'>Enter Your Name</font>"));
            result = false;
        }
        if (gender==2) {
            Toast.makeText(getApplicationContext(),"Please select gender",Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_submit:
                if (GenralUtils.checkConnection(this)) {
                    if(validateForm()){
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra(USER_NAME, user_name.getText().toString());
                        intent.putExtra(USER_GENDER, gender);
                        startActivity(intent);
//                        finish();
                    }
                }else {
                    Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
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
}
