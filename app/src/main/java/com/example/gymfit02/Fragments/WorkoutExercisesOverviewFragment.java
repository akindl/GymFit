package com.example.gymfit02.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymfit02.Adapter.WorkoutExercisesRecyclerAdapter;
import com.example.gymfit02.Models.DatabaseWorkoutExerciseModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutExercisesOverviewFragment extends Fragment {

    private static final String TAG = "workoutExercisesOverviewFragment";

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;
    private DocumentReference documentReferenceUsers;
    private StorageReference storageReference;

    private RecyclerView workoutExercisesRecyclerView;
    private Query query;
    private FirestoreRecyclerOptions<DatabaseWorkoutExerciseModel> options;
    private WorkoutExercisesRecyclerAdapter adapter;

    private Button createExerciseButton;

    private String workoutId;

    public WorkoutExercisesOverviewFragment() {
        // Required empty public constructor
    }


    public static WorkoutExercisesOverviewFragment newInstance(String param1, String param2) {
        WorkoutExercisesOverviewFragment fragment = new WorkoutExercisesOverviewFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the workoutId from the selected Workout
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            this.workoutId = bundle.getString("workoutId");
        }

        setupFirestoreConnection();
    }


    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_exercises_overview, container, false);

        setupRecyclerView(rootView);
        Log.d(TAG, "UserId = " + userId);
        Log.d(TAG, "WorkoutId =  " + workoutId);
        Log.d(TAG, "Exercises Collection ID =  " + fStore.collection("users")
                .document(userId).collection("Exercises").getPath());

        createExerciseButton = (Button) rootView.findViewById(R.id.addWorkoutExerciseButton);
        setOpenCreateExerciseFragmentListener();

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
     * Help-method to setup the RecyclerView
     * @param view
     */
    private void setupRecyclerView(View view) {

        // Query

        query = fStore.collection("Users")
                .document(userId).collection("Exercises")
                .whereArrayContains("workouts", workoutId);
                //.orderBy("exerciseName", Query.Direction.ASCENDING); //alphabetisch sortiert


        // RecyclerOptions
        options = new FirestoreRecyclerOptions.Builder<DatabaseWorkoutExerciseModel>()
                .setLifecycleOwner(this) // this start and stop the adapter automatically
                .setQuery(query, DatabaseWorkoutExerciseModel.class)
                .build();

        adapter = new WorkoutExercisesRecyclerAdapter(options);

        workoutExercisesRecyclerView = (RecyclerView) view.findViewById(R.id.workoutExercisesRecyclerView);
        workoutExercisesRecyclerView.setHasFixedSize(true);
        workoutExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        workoutExercisesRecyclerView.setAdapter(adapter);

    }


    /**
     * When user click on the button "Übung erstellen" he will direct to CreateExerciseFragment
     */
    public void setOpenCreateExerciseFragmentListener() {
        createExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateExerciseFragment createExerciseFragment = new CreateExerciseFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, createExerciseFragment, "openCreateExerciseFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });


    }


}