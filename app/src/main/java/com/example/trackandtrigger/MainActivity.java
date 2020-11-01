package com.example.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;

    String input, password;

    TextInputEditText INPUT, PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tools.setSystemBarLight(this);
        Tools.setSystemBarColor(this, R.color.white);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance(); // Check onStart for already signed-in
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            // TODO: 01-11-2020
            //updateUI(currentUser);  //Send currentUser to Dasboard
        }
    }

    public void openSignUp(View view) {
        //MainActivity new user signup button onClick method
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void signIN(View view) {
        //MainActivity SigIn with input and password button onClick method
        readInfo();
        //Choose and Validate
        if (false) {
            // TODO: 01-11-2020
        } else {
            //All fields are valid, sign in the user
            FirebaseUser user = mAuth.getCurrentUser();
            signInWithEmail(input,password);
        }
    }

    private void signInWithEmail(String email, String password) {
        /*
        Input: Validated Email and Password
        Function: sign up a user using give email and password
         */
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                // TODO: 31-10-2020
                                Toast.makeText(MainActivity.this, "Login successful.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(user); //Send user to the Dashboard
                            }
                            else {
                                askToVerify();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Invalid Username or Password",
                                    Toast.LENGTH_SHORT).show();
                            // TODO: 31-10-2020
                            //updateUI(null);
                        }

                        // ...
                        //New change
                    }
                });
    }

    private void askToVerify() {
        mAuth.getCurrentUser().sendEmailVerification();
        String toast = "Verify Email and Login. Email Sent!";
        Toast.makeText(this ,toast,Toast.LENGTH_LONG).show();
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(this, tempDashboard.class);
        startActivity(intent);
    }

    public void readInfo() {
        //Reference to TextInputEditText
        INPUT = findViewById(R.id.signIn_input);
        PASSWORD = findViewById(R.id.signIn_pass);

        //Get information from InputEditText
        input = INPUT.getText().toString();
        password = PASSWORD.getText().toString();
        //Toast.makeText(this,password,Toast.LENGTH_SHORT).show();
    }
}