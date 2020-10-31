package com.example.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public FirebaseAuth mAuth;

    String input,password;

    TextInputEditText INPUT,PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tools.setSystemBarLight(this);
        Tools.setSystemBarColor(this, R.color.white);

        mAuth = FirebaseAuth.getInstance();

        INPUT = findViewById(R.id.signIn_input);
        PASSWORD = findViewById(R.id.signIn_pass);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // TODO: 30-10-2020  
        //updateUI(currentUser);  Send currentUser to Vastav
    }


    public void openSignUp(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void signIN(View view) {
        readInfo();
        //Choose and Validate
        if(false){

        }
        else{
            //All fields are valid, sign in the user
            mAuth.signInWithEmailAndPassword(input, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                // TODO: 31-10-2020
                                Toast.makeText(MainActivity.this, "Login successful.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(user); send this to vastav
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Invalid Username or Password",
                                        Toast.LENGTH_SHORT).show();
                                // TODO: 31-10-2020
                                //updateUI(null); send this to vastav
                            }

                            // ...
                        }
                    });
        }
    }

    public void readInfo(){
        input = INPUT.getText().toString();
        //Toast.makeText(this,input, Toast.LENGTH_SHORT).show();
        password = PASSWORD.getText().toString();
        //Toast.makeText(this,password,Toast.LENGTH_SHORT).show();

    }
}