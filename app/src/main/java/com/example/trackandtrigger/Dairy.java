package com.example.trackandtrigger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.jackandphantom.circularimageview.RoundedImage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dairy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dairy extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Dairy() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dairy.
     */
    // TODO: Rename and change types and number of parameters
    public static Dairy newInstance(String param1, String param2) {
        Dairy fragment = new Dairy();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView userName,currentDate,addJournalBtn;
    private EditText title,thoughts;
    private RoundedImage journalImage;
    private ImageView addJournalCamera;
    private ProgressBar progressBar;

    private String currentUserId;
    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private StorageReference mStorageRef;

    private String collectionPath;
    private CollectionReference collectionReference= db.collection(collectionPath:"UserJournals");//give name to new collection




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            firebaseAuth= FirebaseAuth.getInstance();
            //addition
            progressBar= progressBar.findViewById(R.id.upload_journal_progressbar);
            userName= userName.findViewById(R.id. post_journal_username);
            title= title.findViewById(R.id. add_journal_title);
            thoughts= thoughts.findViewById(R.id. add_journal_thought);
            currentDate= currentDate.findViewById(R.id.date);
            addJournalCamera= addJournalCamera.findViewById(R.id.upload_image_with_camera);
            addJournalBtn= addJournalBtn.findViewById(R.id.add_journalBtn);
            journalImage= journalImage.findViewById(R.id.journal_image);
            //get Username and id from bundle

            Bundle bundle;//CHECK THIS NOT ASSIGNED

            if(bundle!=null)
            {
                String username = bundle.getString(key:"")
            }
            //code for adding images

            addJournalBtn.setOnClickListener(this);
            addJournalCamera.setOnClickListener(this);
        }
    }

    private Object getIntent() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dairy, container, false);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.add_journalBtn:

                break;

            case R.id.upload_image_with_camera:

                break;


        }

    }
}