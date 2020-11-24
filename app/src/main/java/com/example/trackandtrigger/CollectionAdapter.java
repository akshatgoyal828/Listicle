package com.example.trackandtrigger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CollectionAdapter extends FirestoreRecyclerAdapter<Collect, CollectionAdapter.CollectionHolder> {

    public CollectionAdapter(@NonNull FirestoreRecyclerOptions<Collect> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CollectionHolder holder, int position, @NonNull Collect model) {
        holder.textViewTitle.setText(model.getTitle());
        //holder.textViewDescription.setText(model.getDescription());
        //holder.textViewPriority.setText(String.valueOf(model.getPriority()));
    }

    @NonNull
    @Override
    public CollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item,
                parent, false);
        return new CollectionHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class CollectionHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        //TextView textViewDescription;
        //TextView textViewPriority;

        public CollectionHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title_collection);
            //textViewDescription = itemView.findViewById(R.id.text_view_description);
            //textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }
}