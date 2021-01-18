package com.example.gymfit02.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gymfit02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class WorkoutSingleExercisePerformanceFragment extends Fragment {

    //TODO
    // 1. Basic Informationen wie Namen, Notizen anzeigen lassen
    // 2. Tabelle erstellen und Inhalte anzeigen lassen
    // 3. Sätze hinzufügen, löschen und abhaken können
    // 4. Übung abhaken können mit sichtbaren Effekt

    // Firestore Connection
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;

    // Bundle information
    private String workoutId;
    private String executionDate;
    private String deviceName;
    private String exerciseName;
    private String notes;

    // View
    private Button addSetButton;
    private Button checkExercisePerformanceButton;
    private TextView exerciseNameView;
    private TextView deviceNameView;
    private TextView noteView;
    private LinearLayout layoutList;

    List<String> rpeSpinnerList = new ArrayList<>();


    public WorkoutSingleExercisePerformanceFragment() {
    }

    public WorkoutSingleExercisePerformanceFragment(int contentLayoutId) {
        super(contentLayoutId);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the workoutId from the selected Workout
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            this.workoutId = bundle.getString("workoutId");
            this.executionDate = bundle.getString("executionDate");
            this.deviceName = bundle.getString("deviceName");
            this.exerciseName = bundle.getString("exerciseName");
            this.notes = bundle.getString("notes");
            // Toast.makeText(getContext(), executionDate, Toast.LENGTH_SHORT).show();
        }

        rpeSpinnerList.add("Einfach");
        rpeSpinnerList.add("Mittel");
        rpeSpinnerList.add("Schwer");
        rpeSpinnerList.add("Muskelversagen");

        setupFirestoreConnection();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_single_exercise_performance, container, false);

        addSetButton = (Button) rootView.findViewById(R.id.button__workoutExercisePerformance_addSet);
        checkExercisePerformanceButton = (Button) rootView.findViewById(R.id.button_workoutExercisePerformance_exercisePerformanceCheck);
        exerciseNameView = (TextView) rootView.findViewById(R.id.textView_workoutExercisePerformance_exerciseName);
        deviceNameView = (TextView) rootView.findViewById(R.id.textView_workoutExercisePerformance_deviceName);
        noteView = (TextView) rootView.findViewById(R.id.textView_workoutExercisePerformance_notes);
        // layoutList = (LinearLayout) rootView.findViewById(R.id.linearLayout_workoutExercisePerformance_setLayoutList);


        exerciseNameView.setText(exerciseName);
        deviceNameView.setText(deviceName);
        noteView.setText(notes);



        setAddExerciseSetRowListener();


        return rootView;
    }

    /**
     * Add a new row to the setList.
     */
    private void setAddExerciseSetRowListener() {

        addSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View singleSetView = getLayoutInflater().inflate(R.layout.item_exercise_set_row_single, null,false);

                TextView setNumber = (TextView) singleSetView.findViewById(R.id.textView_workoutExercisePerformance_setNumber);
                EditText reps = (EditText) singleSetView.findViewById(R.id.editText_workoutExercisePerformance_reps);
                EditText load = (EditText) singleSetView.findViewById(R.id.editText_workoutExercisePerformance_load);
                Spinner rpe = (Spinner) singleSetView.findViewById(R.id.spinner_workoutExercisePerformance_rpe);
                ImageView remove = (ImageView) singleSetView.findViewById(R.id.imageView_workoutExercisePerformance_remove);

                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, rpeSpinnerList);
                rpe.setAdapter(arrayAdapter);

                layoutList.addView(singleSetView);

                remove.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        setRemoveExerciseSetRow(singleSetView);
                    }
                });
            }
        });

    }


    /**
     * Remove a single row of the setList
     */
    private void setRemoveExerciseSetRow(View view) {
        layoutList.removeView(view);
    }

    /**
     * Help-method to connect to Firestore
     */
    private void setupFirestoreConnection() {
        // Connect to Firebase user
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        userId = fAuth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
    }


}






















