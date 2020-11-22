package com.example.trackandtrigger;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
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

public class NewEntryActivity extends AppCompatActivity {
    private TextView entryTitle;
    private EditText editTextEntry;
    private String day;
    private int priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        Toast.makeText(this, "New Entry Launch!",Toast.LENGTH_SHORT).show();
        if(getActionBar()!=null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getActionBar().show();
        }
        else Toast.makeText(this,"Empty Action Bar",Toast.LENGTH_SHORT).show();

        setTitle("New Entry");

        //Get Current Date
        Date dt = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String formattedDate = df.format(dt);

        entryTitle = findViewById(R.id.new_entry_title);
        editTextEntry = findViewById(R.id.edit_text_entry);

        day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(dt.getTime());
        //int date = Integer.parseInt(new SimpleDateFormat("dd", Locale.ENGLISH).format(dt.getTime()));
        //int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.ENGLISH).format(dt.getTime()));
        //int year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.ENGLISH).format(dt.getTime()));

        String title = day + ", "+formattedDate;
        entryTitle.setText(title);
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
                saveEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveEntry() {
        String title = entryTitle.getText().toString();
        String entry = editTextEntry.getText().toString();
        int priority = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        if (entry.trim().isEmpty()) {
            Toast.makeText(this, "Please write some entry", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection("Journal_"+user.getUid().toString());
        notebookRef.add(new Entry(title, entry, priority));
        Toast.makeText(this, "Entry added!", Toast.LENGTH_SHORT).show();
        finish();
    }
}