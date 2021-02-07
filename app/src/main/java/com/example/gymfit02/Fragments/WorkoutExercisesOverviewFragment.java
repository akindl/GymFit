package com.example.gymfit02.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymfit02.Adapter.WorkoutExercisePerformanceRecyclerAdapter;
import com.example.gymfit02.Models.DatabaseExercisePerformanceModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


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
    private FirestoreRecyclerOptions<DatabaseExercisePerformanceModel> options;
    private WorkoutExercisePerformanceRecyclerAdapter adapter;

    private Button createExerciseButton;

    // Bundle Information
    private String workoutId;
    private String workoutName;
    private String timestampSeconds;
    private String timestampNanoseconds;

    public WorkoutExercisesOverviewFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the workoutId from the selected Workout
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            this.workoutId = bundle.getString("workoutId");
            this.timestampSeconds = String.valueOf(bundle.getString("timestampSeconds"));
            this.timestampNanoseconds = String.valueOf(bundle.getString("timestampNanoseconds"));
            this.workoutName = bundle.getString("workoutName");
            // Toast.makeText(getContext(), executionDate, Toast.LENGTH_SHORT).show();
        }

        setupFirestoreConnection();
    }


    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_exercises_overview, container, false);
        getActivity().setTitle("Workout: " + workoutName);

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
        query = fStore.collectionGroup("ExercisePerformances").whereEqualTo("workoutId", workoutId);

        // RecyclerOptions
        options = new FirestoreRecyclerOptions.Builder<DatabaseExercisePerformanceModel>()
                .setLifecycleOwner(this) // this start and stop the adapter automatically
                .setQuery(query, DatabaseExercisePerformanceModel.class)
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
                final String exerciseId = documentSnapshot.getReference().getParent()
                        .getParent().getId();

                // Get the ExercisePerformance Document or create a new one
                Bundle bundle = new Bundle();
                bundle.putString("workoutId", workoutId);
                bundle.putString("deviceName", deviceName);
                bundle.putString("exerciseName", exerciseName);
                bundle.putString("exerciseId", exerciseId);
                bundle.putString("notes", notes);
                bundle.putString("performanceId", documentSnapshot.getId());

                WorkoutSingleExercisePerformanceFragment workoutSingleExercisePerformanceFragment = new WorkoutSingleExercisePerformanceFragment();
                workoutSingleExercisePerformanceFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, workoutSingleExercisePerformanceFragment, "openWorkoutSingleExercisePerformanceFragment")
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onItemLongClick(DocumentSnapshot documentSnapshot, int position) {
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


                // TODO Delete function
                //  Before deleting element, the workoutId has to be removed in Array from the
                //  parent document in collection "Exercises"

                // delete item from firestore
                adapter.deleteItem(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(workoutExercisesRecyclerView);
    }


    /**
     * When user click on the button "Übung erstellen" he will direct to CreateExerciseFragment
     */
    public void setOpenCreateExerciseFragmentListener() {
        createExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("workoutId", workoutId);
                bundle.putString("timestampSeconds", timestampSeconds);
                bundle.putString("timestampNanoseconds", timestampNanoseconds);
                bundle.putString("workoutName", workoutName);

                WorkoutCreationFragment workoutCreationFragment = new WorkoutCreationFragment();
                workoutCreationFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, workoutCreationFragment, "openCreateExerciseFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });


    }


}