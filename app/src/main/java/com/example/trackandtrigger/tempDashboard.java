package com.example.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

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

}