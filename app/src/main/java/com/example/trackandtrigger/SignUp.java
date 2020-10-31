package com.example.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SignUp extends AppCompatActivity {
    private String username, phoneNumber, gmailID, userType,password;
    TextInputEditText USERNAME, PHONENUMBER, GMAILID,PASSWORD;
    Spinner USERTYPE;

    protected void readInfo(){

        username = USERNAME.getText().toString();
        //Toast.makeText(this,username, Toast.LENGTH_SHORT).show();

        phoneNumber = PHONENUMBER.getText().toString();
        //Toast.makeText(this,phoneNumber, Toast.LENGTH_SHORT).show();

        gmailID = GMAILID.getText().toString();
        //Toast.makeText(this,gmailID, Toast.LENGTH_SHORT).show();

        password = PASSWORD.getText().toString();
        //Toast.makeText(this,password, Toast.LENGTH_SHORT).show();

        userType = USERTYPE.getSelectedItem().toString();
        //Toast.makeText(this,userType, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Tools.setSystemBarLight(this);
        Tools.setSystemBarColor(this, R.color.white);

        USERNAME = findViewById(R.id.reg_username);
        PHONENUMBER = findViewById(R.id.reg_phone);
        GMAILID = findViewById(R.id.reg_gmailId);
        PASSWORD = findViewById(R.id.signup_pass);
        USERTYPE = (Spinner)findViewById(R.id.singup_type);

    }

    public void createAccount(View view) {
        readInfo();
        //USERNAME.setError("Invalid Username");
    }
}