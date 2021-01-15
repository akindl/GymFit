package com.example.gymfit02.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gymfit02.Adapter.WorkoutRecyclerAdapter;
import com.example.gymfit02.Models.DatabaseWorkoutModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutsOverviewFragment extends Fragment {

    private static final String TAG = "workoutsOverviewFragment";

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;

    private DocumentReference documentReferenceUsers;
    private StorageReference storageReference;

    private RecyclerView workoutsRecyclerView;
    private Query query;
    private FirestoreRecyclerOptions<DatabaseWorkoutModel> options;
    private WorkoutRecyclerAdapter adapter;

    private Button createWorkoutButton;

    public WorkoutsOverviewFragment() {
        // Required empty public constructor
    }


    public static WorkoutsOverviewFragment newInstance(String param1, String param2) {
        WorkoutsOverviewFragment fragment = new WorkoutsOverviewFragment();

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
        View rootView = inflater.inflate(R.layout.fragment_workouts_overview, container, false);

        setupRecyclerView(rootView);

        createWorkoutButton = (Button) rootView.findViewById(R.id.addExerciseButton);
        setOpenCreateWorkoutFragmentListener();

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
                .document(userId).collection("Workouts")
                .orderBy("name", Query.Direction.ASCENDING); //alphabetisch sortiert



        // RecyclerOptions
        options = new FirestoreRecyclerOptions.Builder<DatabaseWorkoutModel>()
                .setLifecycleOwner(this) // this start and stop the adapter automatically
                .setQuery(query, DatabaseWorkoutModel.class)
                .build();

        adapter = new WorkoutRecyclerAdapter(options);

        workoutsRecyclerView = (RecyclerView) view.findViewById(R.id.workoutsRecyclerView);
        workoutsRecyclerView.setHasFixedSize(true);
        workoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        workoutsRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new WorkoutRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String workoutId = documentSnapshot.getId();
                Bundle bundle = new Bundle();
                bundle.putString("workoutId", workoutId);

                WorkoutExercisesOverviewFragment workoutExercisesOverviewFragment = new WorkoutExercisesOverviewFragment();
                workoutExercisesOverviewFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, workoutExercisesOverviewFragment, "openWorkoutExercisesOverviewFragment")
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onItemLongClick(DocumentSnapshot documentSnapshot, int position) {
            }
        });




    }


    /**
     * When user click on the button "Workout erstellen" he will direct to CreateWorkoutFragment
     */
    public void setOpenCreateWorkoutFragmentListener() {
        createWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO CreateExerciseFragment austauschen zu CreateWorkoutFragment (vorher anlegen)
                CreateExerciseFragment createExerciseFragment = new CreateExerciseFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, createExerciseFragment, "openCreateWorkoutFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });


    }


}