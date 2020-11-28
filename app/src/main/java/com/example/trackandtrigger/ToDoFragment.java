package com.example.trackandtrigger;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

public class ToDoFragment extends Fragment{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private CollectionReference notebookRef = FirebaseFirestore.getInstance()
            .collection(user.getUid()).document("Notebook").collection("Notes");
    private NoteAdapter adapter;

    protected View mView;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo,container,false);
        this.mView = view;
        Tools.setSystemBarLight(getActivity());
        Tools.setSystemBarColor(getActivity(), R.color.white);

        setUpRecyclerView();

        if(mView!=null){
            FloatingActionButton fab = (FloatingActionButton)mView.findViewById(R.id.button_add_task);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchNewNoteActivity();
                }
            });
        }
        else{
            Toast.makeText(getActivity(),"View NULL",Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void launchNewNoteActivity() {
        Toast.makeText(getActivity(),"Add new note!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), NewNoteActivity.class));
    }

    private void setUpRecyclerView() {
        Query query = notebookRef.orderBy("priority", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        adapter = new NoteAdapter(options);
        if(mView!=null){
            recyclerView = (RecyclerView)mView.findViewById(R.id.recycler_view_toDo);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        //Toast.makeText(getActivity(), "Closed!", Toast.LENGTH_SHORT).show();
        adapter.stopListening();
    }
}
