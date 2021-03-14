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

import com.example.gymfit02.Models.DatabaseExerciseModel;
import com.example.gymfit02.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

//TODO Button muss neue UserExercise in Firebase anlegen
//TODO RadioButtons: https://www.youtube.com/watch?v=fwSJ1OkK304
//TODO CreateNewExercise: https://www.youtube.com/watch?v=1YMK2SatG8o&list=PLrnPJCHvNZuAXdWxOzsN5rgG2M4uJ8bH1&index=4



public class CreateExerciseFragment extends Fragment {

    private static final String TAG = "createExerciseFragment";

    // Firestore Connection
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;
    private DocumentReference documentReferenceUsers;
    private StorageReference storageReference;

    // Bundle Information
    private String exerciseId;
    private String exerciseName;
    private String deviceName;
    private String notes;


    // View
    private Button createExerciseBtn;
    private TextView exerciseNameTitleTextField;
    private TextView deviceNameTitleTextField;
    private TextView notesTitleTextField;
    private EditText exerciseNameEditText;
    private EditText deviceNameEditText;
    private EditText notesEditText;





    public CreateExerciseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            this.exerciseId = bundle.getString("exerciseId");
            this.exerciseName = bundle.getString("exerciseName");
            this.deviceName = bundle.getString("deviceName");
            this.notes = bundle.getString("notes");
        }

        setupFirestoreConnection();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_exercise, container, false);

        getActivity().setTitle("Übung erstellen und bearbeiten");

        createExerciseBtn = (Button) rootView.findViewById(R.id.createExercise_btn_confirm);

        // radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup_selectSetting);
        // radioButton_weight = (RadioButton) rootView.findViewById(R.id.radioButton_selectWeight);
        // radioButton_time = (RadioButton) rootView.findViewById(R.id.radioButton_selectTime);

        exerciseNameTitleTextField = (TextView) rootView.findViewById(R.id.createExercise_exerciseName_title);
        deviceNameTitleTextField = (TextView) rootView.findViewById(R.id.createExercise_deviceName_title);
        notesTitleTextField = (TextView) rootView.findViewById(R.id.createExercise_notes_title);
        exerciseNameEditText = (EditText) rootView.findViewById(R.id.createExercise_exerciseName);
        deviceNameEditText = (EditText) rootView.findViewById(R.id.createExercise_deviceName);
        notesEditText = (EditText) rootView.findViewById(R.id.createExercise_notes);

        // Set Bundle infos
        if(!exerciseId.isEmpty()) {
            exerciseNameEditText.setText(exerciseName);
            deviceNameEditText.setText(deviceName);
            notesEditText.setText(notes);
        }

        setCreateNewExerciseListener();

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


    private void setCreateNewExerciseListener() {
        createExerciseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(exerciseNameEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Bitte gib der Übung einen Namen.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(deviceNameEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Bitte gib einen Gerätenamen ein.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String exercise_name = exerciseNameEditText.getText().toString();
                String device_name = deviceNameEditText.getText().toString();
                String exercise_notes = notesEditText.getText().toString();

                if(!exerciseId.isEmpty()) {

                    if(!exercise_name.equals(exerciseName)) {
                        updateDatabaseExerciseField("exerciseName", exercise_name);
                        exerciseName = exercise_name;
                    }

                    if(!device_name.equals(deviceName)) {
                        updateDatabaseExerciseField("deviceName", device_name);
                        deviceName = device_name;
                    }

                    if(!exercise_notes.equals(notes)) {
                        updateDatabaseExerciseField("notes", exercise_notes);
                        notes = exercise_notes;
                    }

                // Create new exercise
                } else {

                    DatabaseExerciseModel exerciseModel = new DatabaseExerciseModel();

                    List<String> workouts = new ArrayList<>();

                    exerciseModel.setCreatorId(userId);
                    exerciseModel.setExerciseName(exercise_name);
                    exerciseModel.setDeviceName(device_name);
                    exerciseModel.setNotes(exercise_notes);
                    exerciseModel.setWorkouts(workouts);

                    fStore.collection("Users").document(userId).collection("Exercises")
                            .add(exerciseModel);

                }

                ExerciseFragment exerciseFragment = new ExerciseFragment();

                Toast.makeText(getContext(), "Speichern erfolgreich!",
                        Toast.LENGTH_LONG).show();

                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, exerciseFragment, "openCreateWorkoutFragment")
                        .addToBackStack(null)
                        .commit();



            }

        });
    }

    private void updateDatabaseExerciseField(String key, String value) {
        fStore.collection("Users").document(userId)
                .collection("Exercises").document(exerciseId)
                .update(key, value);

    }
}


































