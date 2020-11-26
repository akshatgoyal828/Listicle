package com.example.trackandtrigger;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class CollectionItemAdapter extends FirestoreRecyclerAdapter<CollectionItem, CollectionItemAdapter.CollectionItemHolder> {

    private OnItemClickListener mListener;
    private String msg;

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

    public String shareItem(int position) {
        DocumentReference docRef = getSnapshots().getSnapshot(position).getReference();
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CollectionItem item = documentSnapshot.toObject(CollectionItem.class);
                String title = item.getTitle();
                String quantity = String.valueOf(item.getPriority());

                String txt = "Title: "+title+
                        "\n"+ "Quantity: "+quantity;
                msg = txt;
            }
        });
        return msg;
    }
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public DocumentReference getDocumentID(int position) {
        DocumentReference docRef = getSnapshots().getSnapshot(position).getReference();
        return docRef;
    }

    class CollectionItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextView textViewTitle;
        //TextView textViewDescription;
        TextView textViewPriority;

        public CollectionItemHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title_subitem);
            //textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority_subitem);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem Share = menu.add(Menu.NONE, 1, 1, "Share");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            Share.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShareClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShareClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}