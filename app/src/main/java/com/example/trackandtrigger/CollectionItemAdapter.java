package com.example.trackandtrigger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CollectionItemAdapter extends FirestoreRecyclerAdapter<CollectionItem, CollectionItemAdapter.CollectionItemHolder> {

    public CollectionItemAdapter(@NonNull FirestoreRecyclerOptions<CollectionItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CollectionItemHolder holder, int position, @NonNull CollectionItem model) {
        holder.textViewTitle.setText(model.getTitle());
        //holder.textViewDescription.setText(model.getDescription());
        holder.textViewPriority.setText(String.valueOf(model.getPriority()));
    }

    @NonNull
    @Override
    public CollectionItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_sub_item,
                parent, false);
        return new CollectionItemHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class CollectionItemHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        //TextView textViewDescription;
        TextView textViewPriority;

        public CollectionItemHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title_subitem);
            //textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority_subitem);
        }
    }
}