package com.example.gymfit02.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gymfit02.Adapter.WorkoutExercisePerformanceRecyclerAdapter;
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
 * Use the {@link AnalyseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalyseFragment extends Fragment {

    private static final String TAG = "analyseFragment";

    // Firestore Connection
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;

    // View
    private Query query;
    private RecyclerView workoutExercisesRecyclerView;
    private FirestoreRecyclerOptions<DatabaseWorkoutExerciseModel> options;
    private WorkoutExercisePerformanceRecyclerAdapter adapter;

    public AnalyseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnalyseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalyseFragment newInstance(String param1, String param2) {
        AnalyseFragment fragment = new AnalyseFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect to Firebase user
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        // Reference to get the userData like name and email
        //documentReferenceUsers = fStore.collection("users").document(userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_analyse, container, false);

        getActivity().setTitle("Deine Analysen");

        return rootView;
    }

    private void setupRecyclerView(View view) {

//        query = fStore.collection("Users")
//                .document(userId).collection("Exercises");
//        //.whereArrayContains("workouts", workoutId);
//        //.orderBy("exerciseName", Query.Direction.ASCENDING); //alphabetisch sortiert
//
//
//        // TODO .groupCollection("ExercisePerformances").equalTo("workoutId", workoutId);
//
//
//        // RecyclerOptions
//        options = new FirestoreRecyclerOptions.Builder<DatabaseWorkoutExerciseModel>()
//                .setLifecycleOwner(this) // this start and stop the adapter automatically
//                .setQuery(query, DatabaseWorkoutExerciseModel.class)
//                .build();
//
//        adapter = new WorkoutExercisePerformanceRecyclerAdapter(options);
//
//        workoutExercisesRecyclerView = (RecyclerView) view.findViewById(R.id.workoutExercisesRecyclerView);
//        workoutExercisesRecyclerView.setHasFixedSize(true);
//        workoutExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        workoutExercisesRecyclerView.setAdapter(adapter);
    }



}