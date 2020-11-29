package com.example.trackandtrigger;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewCollectionActivity extends AppCompatActivity {
    private TextView title;
    RadioGroup radioGroup;
    RadioButton radioButton;
    private int image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collection);
        Tools.setSystemBarLight(this);
        //Tools.setSystemBarColor(this, R.color.white);

        //Toast.makeText(this, "New Collection Launch!",Toast.LENGTH_SHORT).show();
        if(getActionBar()!=null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getActionBar().show();
        }

        setTitle("New Collection");

        title = findViewById(R.id.edit_text_title_collection);
        radioGroup = findViewById(R.id.radioGroupImages);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveCollection();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveCollection() {
        String title = this.title.getText().toString();

        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please write collection name", Toast.LENGTH_SHORT).show();
            return;
        }

        //Save Image
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        image_id = Integer.parseInt(radioButton.getText().toString());


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection(user.getUid()).document("Collection").collection("Collections");
        notebookRef.add(new Collect(title,image_id));
        //Toast.makeText(this, "Collection added!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void checkButton(View view) {
    }
}