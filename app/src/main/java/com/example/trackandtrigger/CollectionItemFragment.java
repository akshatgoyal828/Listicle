package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_item,container,false);
        this.mView = view;

        collection_id = getArguments().getString("Collection_ID");
        Toast.makeText(getContext(),collection_id,Toast.LENGTH_SHORT).show();
        notebookRef = db.collection( user.getUid()+"_"+collection_id+"Item_");

        setUpRecyclerView();

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
            }

            @Override
            public void onShareClick(int position) {    
                Toast.makeText(getActivity(), "Share Click! "+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {
                Toast.makeText(getActivity(), "Delete Click! "+position, Toast.LENGTH_SHORT).show();
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
