package com.example.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    //USERNAME.setError("Invalid Username");

    public void createAccount(View view) {
        readInfo();
        if( validateUsername()==false){
            USERNAME.setError("Username should be 6 to 30 characters long, containing only alphanumerics or _ , and should start with an alphabet.");
        }
        else Toast.makeText(this,"Sab badiya!",Toast.LENGTH_SHORT).show();

    }

    public boolean validateUsername(){
        // TODO: 31-10-2020  Check if username is available

        /*
        A username is considered valid if all the following constraints are satisfied:
        1. The username consists of 6 to 30 characters inclusive.
            If the username consists of less than 6 or greater than 30 characters, then it is an invalid username.
        2. The username can only contain alphanumeric characters and underscores (_).
            Alphanumeric characters describe the character set consisting of lowercase characters [a – z],
            uppercase characters [A – Z], and digits [0 – 9].
        3. The first character of the username must be an alphabetic character, i.e., either lowercase character
            [a – z] or uppercase character [A – Z].
         */

        String regex = "^[A-Za-z]\\w{5,29}$";
        Pattern p = Pattern.compile(regex);

        if (username == null) {
            return true;
        }

        Matcher m = p.matcher(username);
        return m.matches();
    }
}