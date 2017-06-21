package com.amtee.friendsfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amtee.friendsfinder.pojo.ResponseCode_Pojo;
import com.amtee.friendsfinder.retofitwork.Rest_Interface;
import com.amtee.friendsfinder.retofitwork.Service_Generator;
import com.google.gson.JsonObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class Resistration_Activity extends AppCompatActivity implements View.OnClickListener {

    Button submit_btn;
    EditText name, number, mail, password;
    RadioGroup radiogroup;
    RadioButton male, female;

    ///
    String username, usernumber, usermail, userpassword;
    String sex = "";//default 1 for male
    Button submitbtn;
    Rest_Interface resistration_verification;
    Call<ResponseCode_Pojo> submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resistration_);

        submit_btn = (Button) findViewById(R.id.submit_btn);
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);
        mail = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.password);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        resistration_verification = Service_Generator.createService(Rest_Interface.class, Rest_Interface.BASE_URL);
        submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.submit_btn:
                if (userGender() == null)
                {
                    Toast.makeText(getApplicationContext(), "Please select your gender", Toast.LENGTH_SHORT).show();
                } else {
                    username = name.getText().toString();
                    usernumber = number.getText().toString();
                    usermail = mail.getText().toString();
                    userpassword = password.getText().toString();

                    final JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("deviceId", App_Constant.get_IMEI_Number(Resistration_Activity.this));
                    jsonObject.addProperty("userName", username);
                    jsonObject.addProperty("phoneNo", usernumber);
                    jsonObject.addProperty("sex", userGender());
                    jsonObject.addProperty("email",usermail);
                    jsonObject.addProperty("password", userpassword);

                    submit = resistration_verification.getSignUpStatus(jsonObject);
                    submit.enqueue(new Callback<ResponseCode_Pojo>() {
                        @Override
                        public void onResponse(Response<ResponseCode_Pojo> response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                ResponseCode_Pojo response_code_pojo = response.body();
                                if (response_code_pojo.getResponseCode().equals("1")) {
                                    Intent intent = new Intent(Resistration_Activity.this, Main_Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                if (response_code_pojo.getResponseCode().equals("")) {
                                    //try after some time
                                }
                            } else {
                                //failure

                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });

                }
                break;
        }
    }


    /////
    ///this for radio button
    ////

    private String userGender() {
        if (radiogroup.getCheckedRadioButtonId() == R.id.male) {
            return "male";
        } else if (radiogroup.getCheckedRadioButtonId() == R.id.female) {
            return "female";
        } else {
            return null;
        }
    }



}
