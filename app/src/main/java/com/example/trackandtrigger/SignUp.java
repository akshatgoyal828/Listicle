package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignUp";
    //Navigate back to sign-in when sign-up successful
    Intent intent;

    public FirebaseAuth mAuth;

    private String username, phoneNumber, gmailID, userType,password;
    TextInputEditText USERNAME, PHONENUMBER, GMAILID,PASSWORD;
    Spinner USERTYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Tools.setSystemBarLight(this);
        Tools.setSystemBarColor(this, R.color.white);

        mAuth = FirebaseAuth.getInstance();
    }

    protected void readInfo(){
        //Reference for Views
        USERNAME = findViewById(R.id.reg_username);
        PHONENUMBER = findViewById(R.id.reg_phone);
        GMAILID = findViewById(R.id.reg_gmailId);
        PASSWORD = findViewById(R.id.signup_pass);
        //USERTYPE = (Spinner)findViewById(R.id.singup_type);

        //Get data from InputEditText
        username = USERNAME.getText().toString();
        phoneNumber = PHONENUMBER.getText().toString();
        gmailID = GMAILID.getText().toString();
        password = PASSWORD.getText().toString();
        //userType = USERTYPE.getSelectedItem().toString();
        //Toast.makeText(this,userType, Toast.LENGTH_SHORT).show();
    }

    public void createAccount(View view) {
        // SignUp's Signup button onClick method
        readInfo();
        //Validation
        if(!validateUsername(username)){
            USERNAME.setError("Username should be 6 to 30 characters long, containing only alphanumerics or _ , and should start with an alphabet.");
        }
        else if(!validatePassword(password)){
            PASSWORD.setError("Password should be atleast 6 characters long");
        }
        else {
            //All fields are valid, sign up the user
            // TODO: 31-10-2020 Register username into the database
            createAccountWithEmailPassword(gmailID,password);
        }

    }

    private void createAccountWithEmailPassword(String gmailID, String password) {
        /*
        Input: Validated Email and Password
        Function: sign up a user using give email and password
         */
        mAuth.createUserWithEmailAndPassword(gmailID, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // TODO: 31-10-2020
                            addOtherDetails();
                            verifyPhoneAndEmail();
                            Toast.makeText(SignUp.this, "Verify Email and Login!",
                                    Toast.LENGTH_SHORT).show();
                            // TODO: 01-11-2020
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // TODO: 31-10-2020
                            //updateUI(null);  Send this to vastav
                        }
                        // ...
                    }
                });

    }

    private void verifyPhoneAndEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification();
    }

    private void addOtherDetails() {
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean validateUsername(String username){
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

    public static boolean validatePassword(String password){
        /*
        A password is considered valid if all the following constraints are satisfied:
        1. Length greater than equal to 6
         */
        return password.length()>=6;
    }
}