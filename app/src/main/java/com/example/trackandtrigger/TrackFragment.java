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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TrackFragment extends Fragment{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private CollectionReference notebookRef = db.collection( user.getUid()+"_Collection");
    private CollectionAdapter adapter;

    protected View mView;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track,container,false);
        this.mView = view;


        setUpRecyclerView();
        Tools.setSystemBarLight(getActivity());
        Tools.setSystemBarColor(getActivity(), R.color.white);

        if(mView!=null){
            FloatingActionButton fab = (FloatingActionButton)mView.findViewById(R.id.button_add_collection);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchNewCollectionActivity();
                }
            });

            FloatingActionButton google_map = (FloatingActionButton)mView.findViewById(R.id.button_collection_map);
            google_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), WebViewer.class));
                }
            });


        }
        else{
            Toast.makeText(getActivity(),"FAB null",Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void launchNewCollectionActivity() {
        Toast.makeText(getActivity(),"Add new collection!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), NewCollectionActivity.class));
    }

    private void setUpRecyclerView() {
        Query query = notebookRef.orderBy("title");
        FirestoreRecyclerOptions<Collect> options = new FirestoreRecyclerOptions.Builder<Collect>()
                .setQuery(query, Collect.class)
                .build();
        adapter = new CollectionAdapter(options);
        if(mView!=null){
            recyclerView = (RecyclerView)mView.findViewById(R.id.recycler_view_track);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
            recyclerView.setAdapter(adapter);
        }
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
        //OnClick
        adapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Collect collection = documentSnapshot.toObject(Collect.class);
                String id = documentSnapshot.getId();
                //documentSnapshot.getReference();
                //Toast.makeText(getContext(),"ID: "+id +" Position: "+position ,Toast.LENGTH_SHORT).show();
                // TODO: 25-11-2020 Send Data to next activity
                openCollectionItemFragment(id);
                //startActivity();
            }
        });
    }

    private void openCollectionItemFragment(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("Collection_ID",id);
        // set Fragmentclass Arguments
        CollectionItemFragment fragobj = new CollectionItemFragment();
        fragobj.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragobj).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "Tracker", Toast.LENGTH_SHORT).show();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        //Toast.makeText(getActivity(), "Closed!", Toast.LENGTH_SHORT).show();
        adapter.stopListening();
    }
}
