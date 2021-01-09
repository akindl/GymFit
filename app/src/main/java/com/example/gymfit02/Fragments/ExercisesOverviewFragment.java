package com.example.gymfit02.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gymfit02.Adapter.ExerciseRecyclerAdapter;
import com.example.gymfit02.Models.DatabaseExerciseModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
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
public class ExercisesOverviewFragment extends Fragment {

    private static final String TAG = "exerciseOverviewFragment";

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;

    private DocumentReference documentReferenceUsers;
    private StorageReference storageReference;

    private RecyclerView exercisesRecyclerView;
    private Query query;
    private FirestoreRecyclerOptions<DatabaseExerciseModel> options;
    private FirestoreRecyclerAdapter adapter;

    private Button createExerciseButton;

    public ExercisesOverviewFragment() {
        // Required empty public constructor
    }


    public static ExercisesOverviewFragment newInstance(String param1, String param2) {
        ExercisesOverviewFragment fragment = new ExercisesOverviewFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupFirestoreConnection();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercises_overview, container, false);

        setupRecyclerView(rootView);

        createExerciseButton = (Button) rootView.findViewById(R.id.createExerciseButton);
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
        query = fStore.collection("users")
                .document(userId).collection("Database-Exercises")
                .orderBy("name", Query.Direction.ASCENDING); //alphabetisch sortiert

        // RecyclerOptions
        options = new FirestoreRecyclerOptions.Builder<DatabaseExerciseModel>()
                .setLifecycleOwner(this) // this start and stop the adapter automatically
                .setQuery(query, DatabaseExerciseModel.class)
                .build();

        adapter = new ExerciseRecyclerAdapter(options);

        exercisesRecyclerView = (RecyclerView) view.findViewById(R.id.exercisesRecyclerView);
        exercisesRecyclerView.setHasFixedSize(true);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        exercisesRecyclerView.setAdapter(adapter);

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