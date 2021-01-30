package com.example.gymfit02.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gymfit02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

//TODO Button muss neue UserExercise in Firebase anlegen
//TODO RadioButtons: https://www.youtube.com/watch?v=fwSJ1OkK304
//TODO CreateNewExercise: https://www.youtube.com/watch?v=1YMK2SatG8o&list=PLrnPJCHvNZuAXdWxOzsN5rgG2M4uJ8bH1&index=4



public class CreateExerciseFragment extends Fragment {

    private static final String TAG = "createExerciseFragment";

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;
    private DocumentReference documentReferenceUsers;
    private StorageReference storageReference;


    private Button createExerciseBtn;
    private RadioGroup radioGroup;
    private RadioButton radioButton_weight;
    private RadioButton radioButton_time;

    private EditText createExercise_title;
    private TextView textView_settingTitle;



    public CreateExerciseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupFirestoreConnection();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_exercise, container, false);

        getActivity().setTitle("Neue Übung erstellen");


        createExerciseBtn = (Button) rootView.findViewById(R.id.createExerciseConfirm);
        setCreateNewExerciseListener();
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup_selectSetting);
        radioButton_weight = (RadioButton) rootView.findViewById(R.id.radioButton_selectWeight);
        radioButton_time = (RadioButton) rootView.findViewById(R.id.radioButton_selectTime);

        createExercise_title = (EditText) rootView.findViewById(R.id.createExercise_title);
        textView_settingTitle = (TextView) rootView.findViewById(R.id.textView_settingTitle);


        return rootView;
    }

    /**
     * Help-method to connect to Firestore
     */
    private void setupFirestoreConnection() {
        // Connect to Firebase user
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        // Reference to get the userData
        documentReferenceUsers = fStore.collection("users").document(userId);

    }

    /**
     *
     */
    private void setCreateNewExerciseListener() {
        createExerciseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Button geklickt", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
