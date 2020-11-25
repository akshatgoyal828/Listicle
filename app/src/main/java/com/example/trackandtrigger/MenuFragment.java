package com.example.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class MenuFragment extends Fragment {
    private View mView;
    Button menu_logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,container,false);
        this.mView = view;

        if(mView!=null){
            String collectionID = getArguments().getString("CollectionID");
            Toast.makeText(getContext(), collectionID, Toast.LENGTH_SHORT).show();
            menu_logout = mView.findViewById(R.id.menu_logout);
            if(menu_logout!=null){
                menu_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
        return mView;
    }
}
