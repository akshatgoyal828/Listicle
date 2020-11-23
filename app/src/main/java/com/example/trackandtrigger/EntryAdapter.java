package com.example.trackandtrigger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class EntryAdapter extends FirestoreRecyclerAdapter<Entry, EntryAdapter.EntryHolder> {

    public EntryAdapter(@NonNull FirestoreRecyclerOptions<Entry> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EntryHolder holder, int position, @NonNull Entry model) {
        holder.textViewTitle.setText(model.getTitle());
        //holder.textViewDescription.setText(model.getDescription());
        //holder.textViewPriority.setText(String.valueOf(model.getPriority()));
    }

    @NonNull
    @Override
    public EntryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item,
                parent, false);
        return new EntryHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class EntryHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        //TextView textViewDescription;
        //TextView textViewPriority;

        public EntryHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView)itemView.findViewById(R.id.text_view_title_journal);
            //textViewDescription = itemView.findViewById(R.id.edit_text_entry);
            //textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }
}