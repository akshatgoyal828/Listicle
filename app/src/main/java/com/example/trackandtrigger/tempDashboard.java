package com.example.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class tempDashboard extends AppCompatActivity {

    FirebaseUser user;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_dashboard);

        text = findViewById(R.id.textView_tempDashboard);
        displayUserInfo();
    }

    private void displayUserInfo() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        String msg = "Hello, "+user.getDisplayName() +"!";
        text.setText(msg);
    }

    public void SignOut(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        //Facebook Signout
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn) LoginManager.getInstance().logOut();

        //Google SignOut
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_SIGN_IN);
        googleSignInClient.signOut();
        
        //Notify SignOut
        Toast.makeText(this, "Bye bro!", Toast.LENGTH_SHORT).show();
        updateUI();
    }

    private void updateUI() {
        Intent intent = new Intent(this, DashBoard.class);
        Toast.makeText(this,"Intent Made!",Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        // TODO: 17-11-2020 delete this function later 
        super.onStart();
        updateUI();
    }
}