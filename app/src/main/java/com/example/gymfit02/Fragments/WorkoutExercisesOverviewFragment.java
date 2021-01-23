package com.example.gymfit02.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymfit02.Adapter.WorkoutExercisePerformanceRecyclerAdapter;
import com.example.gymfit02.Models.DatabaseExercisePerformanceModel;
import com.example.gymfit02.Models.DatabaseWorkoutExerciseModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutExercisesOverviewFragment extends Fragment {

    private static final String TAG = "exercisesOverviewFragment";

    // Firestore Connection
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;

    // View
    private RecyclerView workoutExercisesRecyclerView;
    private Query query;
    private FirestoreRecyclerOptions<DatabaseWorkoutExerciseModel> options;
    private WorkoutExercisePerformanceRecyclerAdapter adapter;

    private Button createExerciseButton;

    // Bundle Information
    private String workoutId;
    private String executionDate;

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
            this.executionDate = bundle.getString("executionDate");
            // Toast.makeText(getContext(), executionDate, Toast.LENGTH_SHORT).show();
        }

        setupFirestoreConnection();
    }


    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_exercises_overview, container, false);

        setupRecyclerView(rootView);

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
        userId = fAuth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();


    }

    /**
     * Help-method to setup the RecyclerView
     * @param view
     */
    private void setupRecyclerView(View view) {

        // Query
        // TODO Query returns right ExercisesPerformances but the names of the exercises are all the same??
        // query = fStore.collectionGroup("ExercisePerformances").whereEqualTo("workoutId", workoutId);



        query = fStore.collection("Users")
                .document(userId).collection("Exercises")
                .whereArrayContains("workouts", workoutId);
                //.orderBy("exerciseName", Query.Direction.ASCENDING); //alphabetisch sortiert


        // TODO .groupCollection("ExercisePerformances").equalTo("workoutId", workoutId); 



        // RecyclerOptions
        options = new FirestoreRecyclerOptions.Builder<DatabaseWorkoutExerciseModel>()
                .setLifecycleOwner(this) // this start and stop the adapter automatically
                .setQuery(query, DatabaseWorkoutExerciseModel.class)
                .build();

        adapter = new WorkoutExercisePerformanceRecyclerAdapter(options);

        workoutExercisesRecyclerView = (RecyclerView) view.findViewById(R.id.workoutExercisesRecyclerView);
        workoutExercisesRecyclerView.setHasFixedSize(true);
        workoutExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        workoutExercisesRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new WorkoutExercisePerformanceRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                final String deviceName = (String) documentSnapshot.get("deviceName");
                final String exerciseName = (String) documentSnapshot.get("exerciseName");
                final String notes = (String) documentSnapshot.get("notes");
                final String exerciseId = (String) documentSnapshot.getId();

                // Get the ExercisePerformance Document or create a new one

                final String[] performanceId = new String[1];

                fStore.collection("Users")
                        .document(userId).collection("Exercises")
                        .document(exerciseId).collection("ExercisePerformances")
                        .whereEqualTo("workoutId", workoutId)
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document: task.getResult()) {
                                        performanceId[0] = document.getId();
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        if(performanceId.length == 0) {
                                            CollectionReference exercisePerformanceRef = fStore.collection("Users")
                                                    .document(userId).collection("Exercises")
                                                    .document(exerciseId).collection("ExercisePerformances");
                                            FieldValue performanceDate = FieldValue.serverTimestamp();
                                            exercisePerformanceRef.add(new DatabaseExercisePerformanceModel(0, "",
                                                    performanceDate, 0, workoutId, 0));



                                        }

                                        Bundle bundle = new Bundle();
                                        bundle.putString("workoutId", workoutId);
                                        bundle.putString("executionDate", executionDate);
                                        bundle.putString("deviceName", deviceName);
                                        bundle.putString("exerciseName", exerciseName);
                                        bundle.putString("exerciseId", exerciseId);
                                        bundle.putString("notes", notes);
                                        bundle.putString("performanceId", performanceId[0]);


                                        WorkoutSingleExercisePerformanceFragment workoutSingleExercisePerformanceFragment = new WorkoutSingleExercisePerformanceFragment();
                                        workoutSingleExercisePerformanceFragment.setArguments(bundle);
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                                                .replace(R.id.fragment_container_view, workoutSingleExercisePerformanceFragment, "openWorkoutSingleExercisePerformanceFragment")
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });


            }

            @Override
            public void onItemLongClick(DocumentSnapshot documentSnapshot, int position) {
            }
        });
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