package com.example.gymfit02.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gymfit02.Adapter.AnalyseExerciseRecyclerAdapter;
import com.example.gymfit02.Adapter.WorkoutCreationRecyclerAdapter;
import com.example.gymfit02.Adapter.WorkoutExercisePerformanceRecyclerAdapter;
import com.example.gymfit02.Models.DatabaseExerciseModel;
import com.example.gymfit02.Models.DatabaseWorkoutExerciseModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


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

        setupRecyclerView(rootView);

        return rootView;
    }

    private void setupRecyclerView(View view) {

        query = fStore.collection("Users")
                .document(userId).collection("Exercises")
                .orderBy("exerciseName", Query.Direction.ASCENDING); //alphabetisch sortiert


        // RecyclerOptions
        FirestoreRecyclerOptions<DatabaseExerciseModel> options = new FirestoreRecyclerOptions.Builder<DatabaseExerciseModel>()
                .setLifecycleOwner(this) // this start and stop the adapter automatically
                .setQuery(query, DatabaseExerciseModel.class)
                .build();

        AnalyseExerciseRecyclerAdapter adapter = new AnalyseExerciseRecyclerAdapter(options);

        RecyclerView analyseExerciseRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_analyse_allExercisesList);
        analyseExerciseRecyclerView.setHasFixedSize(true);
        analyseExerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        analyseExerciseRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AnalyseExerciseRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String exerciseId = documentSnapshot.getId();
                String exerciseName = (String) documentSnapshot.get("exerciseName");
                String deviceName = (String) documentSnapshot.get("deviceName");
                Bundle bundle = new Bundle();
                bundle.putString("exerciseId", exerciseId);
                bundle.putString("exerciseName", exerciseName);
                bundle.putString("deviceName", deviceName);

                AnalyseExerciseFragment workoutExercisesOverviewFragment = new AnalyseExerciseFragment();
                workoutExercisesOverviewFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, workoutExercisesOverviewFragment, "openAnalyseExerciseFragment")
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onItemLongClick(DocumentSnapshot documentSnapshot, int position) {

            }
        });
    }



}