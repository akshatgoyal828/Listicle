package com.example.trackandtrigger;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ThrowOnExtraProperties;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DashBoard extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference notebookRef = db.collection("User");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference docRef = notebookRef.document(user.getUid());

    private final int REQUEST_USER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Tools.setSystemBarLight(this);
        Tools.setSystemBarColor(this, R.color.white);

        checkNewUser();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TrackFragment()).commit();
        }
    }
    private void checkNewUser() {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        return; // Dashboard already customized because document already exists;
                    }
                    else{
                        Intent intent = new Intent(DashBoard.this, UserType.class);
                        startActivityForResult(intent,REQUEST_USER);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_USER){
            String userType = data.getStringExtra("UserType");
            //Notify customized
            Map<String, String> obj = new HashMap<>();
            obj.put("Customized Already", "true");
            db.collection("User").document(user.getUid())
                    .set(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DashBoard.this, "Customized!", Toast.LENGTH_SHORT).show();
                    customizeDashboard(userType);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DashBoard.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void customizeDashboard(String userType) {
        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection(user.getUid()+"_Collection");

        int WP = 0, JS = 1, HM = 2, BH = 3, OTH = 4;
        String[][] categories = {
                {"Groceries","Bills","Medicine","Stationary"},
                {"Magazines","Bills","Medicine","Books"},
                {"Groceries","Medicine","Bills"},
                {"Stationary","Books","Groceries","Medicines"},
                {"Groceries"}
        };
        int i = WP;
        switch (userType){
            case "Working Professionals":
                i = WP; break;
            case "Job Seekers":
                i = JS; break;
            case "Home Makers":
                i = HM; break;
            case "Bachelors":
                i = BH; break;
            default:
                i = OTH;
        }

        for(int j=0;j<categories[i].length;j++){
            String title = categories[i][j];
            notebookRef.add(new Collect(title));
        }
        Toast.makeText(this,userType + " customized!",Toast.LENGTH_SHORT).show();
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

    public void loadGmap(View view) {
        String url = "https://www.google.com/maps";

        Intent intent = new Intent(this, WebViewer.class);
        intent.putExtra("SITE",url);
        startActivity(intent);
    }

    public void loadAmazon(View view) {
        String url = "https://www.amazon.in/";

        Intent intent = new Intent(this, WebViewer.class);
        intent.putExtra("SITE",url);
        startActivity(intent);
    }

    public void loadFlipkart(View view) {
        String url = "https://www.flipkart.com/";

        Intent intent = new Intent(this, WebViewer.class);
        intent.putExtra("SITE",url);
        startActivity(intent);
    }

    public void loadBigbasket(View view) {
        String url = "https://www.bigbasket.com/";

        Intent intent = new Intent(this, WebViewer.class);
        intent.putExtra("SITE",url);
        startActivity(intent);
    }

    public void loadOneMg(View view) {
        String url = "https://www.1mg.com/";

        Intent intent = new Intent(this, WebViewer.class);
        intent.putExtra("SITE",url);
        startActivity(intent);
    }

    public void loadMyntra(View view) {
        String url = "https://www.myntra.com/";

        Intent intent = new Intent(this, WebViewer.class);
        intent.putExtra("SITE",url);
        startActivity(intent);
    }

    public void signOut(View view) {
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
