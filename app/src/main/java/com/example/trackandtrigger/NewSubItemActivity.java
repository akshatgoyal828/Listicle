package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewSubItemActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextQuantity;
    private String collection_id;

    //private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sub_item);
        Tools.setSystemBarLight(this);
        //Tools.setSystemBarColor(this, R.color.white);

        Intent intent = getIntent();
        //String collection_id = "No Luck";
        String collection_id = intent.getStringExtra("Collection_ID");
        this.collection_id = collection_id;
        Toast.makeText(this,"Add in Collection ID:"+collection_id,Toast.LENGTH_SHORT).show();

        if(getActionBar()!=null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getActionBar().show();
        }

        setTitle("Add Item");

        editTextTitle = findViewById(R.id.edit_text_title_sub_item);
        editTextQuantity = findViewById(R.id.edit_text_description_sub_item);
        //numberPickerPriority = findViewById(R.id.number_picker_priority);

        //numberPickerPriority.setMinValue(1);
        //numberPickerPriority.setMaxValue(5);
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
                saveItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveItem() {
        String title = editTextTitle.getText().toString();
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        //int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || editTextQuantity.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and Quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // TODO: 25-11-2020 Get Collection ID
        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection(user.getUid()+"_"+collection_id+"Item_");
        notebookRef.add(new CollectionItem(title,  quantity));
        Toast.makeText(this, "item added", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}