package com.example.trackandtrigger;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class DashBoard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Tools.setSystemBarLight(this);
        Tools.setSystemBarColor(this, R.color.white);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TrackFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_track:
                            selectedFragment = new TrackFragment();
                            break;
                        case R.id.nav_todo:
                            selectedFragment = new ToDoFragment();
                            break;
                        case R.id.nav_journal:
                            selectedFragment = new JournalFragment();
                            break;
                        case R.id.nav_exit:
                            selectedFragment = new MenuFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    private void signOut() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth!=null){
            mAuth.signOut();

            //Facebook Signout
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
            if(isLoggedIn) LoginManager.getInstance().logOut();

            //Google SignOut
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this,
                    GoogleSignInOptions.DEFAULT_SIGN_IN);
            googleSignInClient.signOut();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
