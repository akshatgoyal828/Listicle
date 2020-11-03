package com.example.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 12345;

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

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

        //For facebook log-in
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton fb_login = (LoginButton)findViewById(R.id.fb_login_button);
        fb_login.setPermissions(Arrays.asList("email"));

        
        fb_login.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                // TODO: 03-11-2020
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(MainActivity.this, "Facebook sign in cancelled",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, UI will update with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Facebook Login Successful!", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // If sign-in fails, a message will display to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Facebook Login Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            // TODO: 01-11-2020
            Log.d(TAG, "Currently Signed in: " + currentUser.getEmail());
            Toast.makeText(MainActivity.this, "Currently Logged in: " + currentUser.getDisplayName(), Toast.LENGTH_LONG).show();
            //updateUI(currentUser);  //Send currentUser to Dasboard
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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