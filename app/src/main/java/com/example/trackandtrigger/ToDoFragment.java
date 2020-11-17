package com.example.trackandtrigger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ToDoFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private NoteAdapter adapter;

    protected View mView;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo,container,false);
        this.mView = view;
        setUpRecyclerView();
        return view;
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
        else {
            Toast.makeText(getActivity(),"View Empty!",Toast.LENGTH_SHORT).show();
        }
        
    }
    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "Started!", Toast.LENGTH_SHORT).show();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getActivity(), "Closed!", Toast.LENGTH_SHORT).show();
        adapter.stopListening();
    }
}
