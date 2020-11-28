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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

public class UpdateSubItemActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextQuantity;
    private String collection_id;
    private String document_id;
    Button incrementBtn, decrementBtn, resetBtn;
    //private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sub_item_update);
        Tools.setSystemBarLight(this);
        Tools.setSystemBarColor(this, R.color.white);

        Intent intent = getIntent();
        String collection_id = intent.getStringExtra("COLLECTION_ID");
        document_id = intent.getStringExtra("DOC_ID");
        this.collection_id = collection_id;


        if (getActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getActionBar().show();
        }

        setTitle("Update Item");

        editTextTitle = findViewById(R.id.edit_text_title_sub_item_update);
        editTextQuantity = findViewById(R.id.edit_text_description_sub_item_update);
        //numberPickerPriority = findViewById(R.id.number_picker_priority);
        incrementBtn = (Button)findViewById(R.id.increment);
        decrementBtn = (Button)findViewById(R.id.decrement);
        resetBtn = (Button)findViewById(R.id.reset);

        //numberPickerPriority.setMinValue(1);
        //numberPickerPriority.setMaxValue(5);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance()
                .collection(user.getUid())
                .document("Sub Categories")
                .collection(collection_id)
                .document(document_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CollectionItem collectionItem = documentSnapshot.toObject(CollectionItem.class);
                String title = collectionItem.getTitle();
                int quanity = collectionItem.getPriority();
                editTextTitle.setText(title);
                editTextQuantity.setText(String.valueOf(quanity));
            }
        });
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
                updateNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateNote() {
        String title = editTextTitle.getText().toString();
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        //int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || editTextQuantity.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and Quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // TODO: 25-11-2020 Get Collection ID
        CollectionItem collectionItem = new CollectionItem(title, quantity);
        FirebaseFirestore.getInstance()
                .collection(collection_id).document(document_id).set(collectionItem, SetOptions.merge());
        Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }
    public void increment(View view) {
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        editTextQuantity.setText(String.valueOf(++quantity));
    }

    public void decrement(View view) {
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        editTextQuantity.setText(String.valueOf(--quantity));
    }

    public void reset(View view) {
        editTextQuantity.setText("0");
    }
}