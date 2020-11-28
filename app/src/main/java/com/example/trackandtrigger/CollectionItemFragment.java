package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

public class CollectionItemFragment extends Fragment{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    // TODO: 25-11-2020 Change this reference
    //private CollectionReference notebookRef = db.collection( "Notebook_"+user.getUid().toString());
    private CollectionReference notebookRef;
    private CollectionItemAdapter adapter;

    private String collection_id;

    protected View mView;
    private EditText editText;
    RecyclerView recyclerView;
    private FirestoreRecyclerOptions<CollectionItem> options;
    private CollectionItemAdapter adapter2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_item,container,false);
        this.mView = view;
        Tools.setSystemBarLight(getActivity());
        Tools.setSystemBarColor(getActivity(), R.color.white);

        collection_id = getArguments().getString("Collection_ID");
        Toast.makeText(getContext(),collection_id,Toast.LENGTH_SHORT).show();
        notebookRef = notebookRef = FirebaseFirestore.getInstance()
                .collection(user.getUid())
                .document("Sub Categories")
                .collection(collection_id);

        setUpRecyclerView();

        editText = (EditText)mView.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //search(s.toString().toUpperCase().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().trim().isEmpty()){
                    search(s.toString().toUpperCase());
                }
                else{
                    reset();
                }
            }
        });

        if(mView!=null){
            FloatingActionButton fab = (FloatingActionButton)mView.findViewById(R.id.button_add_sub_item);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchNewSubItemActivity();
                }
            });
        }
        else{
            Toast.makeText(getActivity(),"View NULL",Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void search(String s) {
        Query query = notebookRef.orderBy("title")
                .startAt(s)
                .endAt(s+"uf8ff");
                //.startAt(s)
                //.endAt(s+"\uf8ff");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    options = new FirestoreRecyclerOptions.Builder<CollectionItem>()
                            .setQuery(query, CollectionItem.class)
                            .build();
                }else{
                    Toast.makeText(getContext(), "Can't find better matches!", Toast.LENGTH_SHORT).show();
                }
                adapter.stopListening();
                adapter2 = new CollectionItemAdapter(options);
                adapter2.startListening();
                recyclerView.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
            }
        });
    }

    private void reset() {
        Query query = notebookRef.orderBy("title");
        //.startAt(s)
        //.endAt(s+"\uf8ff");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    options = new FirestoreRecyclerOptions.Builder<CollectionItem>()
                            .setQuery(query, CollectionItem.class)
                            .build();
                }else{
                    Toast.makeText(getContext(), "Empty Options!", Toast.LENGTH_SHORT).show();
                }
                CollectionItemAdapter adapter1 = new CollectionItemAdapter(options);
                adapter1.startListening();
                recyclerView.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
            }
        });
    }


    private void launchNewSubItemActivity() {
        Intent intent = new Intent(getActivity(),NewSubItemActivity.class);
        intent.putExtra("Collection_ID",collection_id);
        startActivity(intent);
    }

    private void setUpRecyclerView() {
        Query query = notebookRef.orderBy("title");
        FirestoreRecyclerOptions<CollectionItem> options = new FirestoreRecyclerOptions.Builder<CollectionItem>()
                .setQuery(query, CollectionItem.class)
                .build();
        adapter = new CollectionItemAdapter(options);
        if(mView!=null){
            recyclerView = (RecyclerView)mView.findViewById(R.id.recycler_view_collection_item);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }
        //Context Menu

        adapter.setOnItemClickListener(new CollectionItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), "Normal Click! "+position, Toast.LENGTH_SHORT).show();
                //adapter.shareItem(viewHolder.getAdapterPosition());
                DocumentReference docRef = adapter.getDocumentID(position);
                String documentID = docRef.getId();

                Intent intent = new Intent(getContext(), UpdateSubItemActivity.class);
                intent.putExtra("DOC_ID",documentID);
                intent.putExtra("COLLECTION_ID",user.getUid()+"_"+collection_id+"Item_");
                startActivity(intent);
            }

            @Override
            public void onShareClick(int position) {
                Toast.makeText(getActivity(), "Share Click! "+position, Toast.LENGTH_SHORT).show();
                String txt = adapter.shareItem(position);
                String mimeType = "text/plain";

                ShareCompat.IntentBuilder
                        .from(getActivity())
                        .setType(mimeType)
                        .setChooserTitle("Share this item:")
                        .setText(txt)
                        .startChooser();
            }


            @Override
            public void onDeleteClick(int position) {
                Toast.makeText(getActivity(), "Delete Click! "+position, Toast.LENGTH_SHORT).show();
                adapter.deleteItem(position);
            }
        });


        //Swipe deletes
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "Collection ID in Fragment: "+collection_id, Toast.LENGTH_SHORT).show();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        //Toast.makeText(getActivity(), "Closed!", Toast.LENGTH_SHORT).show();
        adapter.stopListening();
    }
}
