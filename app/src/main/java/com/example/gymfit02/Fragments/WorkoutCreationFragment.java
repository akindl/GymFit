package com.example.gymfit02.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymfit02.Adapter.WorkoutCreationRecyclerAdapter;
import com.example.gymfit02.Adapter.WorkoutExercisePerformanceRecyclerAdapter;
import com.example.gymfit02.Models.DatabaseExerciseModel;
import com.example.gymfit02.Models.DatabaseExercisePerformanceModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutCreationFragment extends Fragment {


    private static final String TAG = "workoutCreationFragment";

    // Firestore Connection
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;

    // Bundle Information
    private String workoutId;
    private int timestampSeconds;
    private int timestampNanoseconds;
    private String workoutName;

    // View
    private Button confirmWorkoutButton;
    private EditText workoutNameEditText;


    public WorkoutCreationFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the workoutId from the selected Workout
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            this.workoutId = bundle.getString("workoutId");
            this.timestampSeconds = Integer.parseInt(bundle.getString("timestampSeconds"));
            this.timestampNanoseconds = Integer.parseInt(bundle.getString("timestampNanoseconds"));
            this.workoutName = bundle.getString("workoutName");
        }

        setupFirestoreConnection();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_creation, container, false);

        getActivity().setTitle("Neues Workout erstellen");

        workoutNameEditText = (EditText) rootView.findViewById(R.id.editText_workoutCreation_workoutName);
        confirmWorkoutButton = (Button) rootView.findViewById(R.id.button_workoutCreation_confirmWorkout);

        if(!workoutName.isEmpty())
            workoutNameEditText.setText(workoutName);

        setupRecyclerView(rootView);
        setOpenWorkoutExercisesOverviewFragmentListener(rootView);

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
     *
     * @param view
     */
    private void setupRecyclerView(final View view) {

        // Query
        Query query = fStore.collection("Users").document(userId)
                .collection("Exercises");


        // RecyclerOptions
        FirestoreRecyclerOptions<DatabaseExerciseModel> options = new FirestoreRecyclerOptions.Builder<DatabaseExerciseModel>()
                .setLifecycleOwner(this) // this start and stop the adapter automatically
                .setQuery(query, DatabaseExerciseModel.class)
                .build();

        WorkoutCreationRecyclerAdapter adapter = new WorkoutCreationRecyclerAdapter(options);

        RecyclerView workoutCreationRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_workoutCreation_allExercisesList);
        workoutCreationRecyclerView.setHasFixedSize(true);
        workoutCreationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        workoutCreationRecyclerView.setAdapter(adapter);

        ArrayList<Integer> clicked = new ArrayList<>();

        adapter.setOnItemClickListener(new WorkoutCreationRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                // TODO Create ExercisePerformance-Document for clicked exercise
                //  Put timestamp as performanceDate and workoutId as workoutId
                //  Set imageView check-icon visible

                DatabaseExerciseModel exerciseModel = documentSnapshot.toObject(DatabaseExerciseModel.class);
                DocumentReference singleExerciseReference = documentSnapshot.getReference();

                String exerciseId = singleExerciseReference.getId();
                String exerciseName = exerciseModel.getExerciseName();
                String deviceName = exerciseModel.getDeviceName();
                List<String> workouts =  exerciseModel.getWorkouts();


                if(!workouts.isEmpty() && workouts.contains(workoutId)) {
                    singleExerciseReference.update("workouts", FieldValue.arrayRemove(workoutId));

                    Query query = fStore.collection("Users").document(userId)
                            .collection("Exercises").document(exerciseId)
                            .collection("ExercisePerformances")
                            .whereEqualTo("workoutId", workoutId)
                            .limit(1);
                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            queryDocumentSnapshots.getDocuments().get(0).getReference().delete();
                        }
                    });


                } else {
                    singleExerciseReference.update("workouts", FieldValue.arrayUnion(workoutId));

                    DatabaseExercisePerformanceModel data = new DatabaseExercisePerformanceModel(
                            0, "", new Timestamp(timestampSeconds, timestampNanoseconds),
                            0, workoutId, 0, exerciseName, deviceName);

                    fStore.collection("Users").document(userId)
                            .collection("Exercises").document(exerciseId)
                            .collection("ExercisePerformances")
                            .add(data);

                }

                // Todo Set checkIcon ImageView
                // ImageView iconCheck = (ImageView) view.findViewById(R.id.imageView_workout_creation_check);

                /*
                int visibility = iconCheck.getVisibility();
                if(visibility == View.VISIBLE) {
                    iconCheck.setVisibility(View.GONE);
                } else {
                    iconCheck.setVisibility(View.VISIBLE);
                }

                 */

            }

            @Override
            public void onItemLongClick(DocumentSnapshot documentSnapshot, int position) {
            }
        });

    }


    public void setOpenWorkoutExercisesOverviewFragmentListener(View view) {
        confirmWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(workoutNameEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Bitte benenne dein Workout.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String workoutName = workoutNameEditText.getText().toString();
                fStore.collection("Users").document(userId)
                        .collection("Workouts").document(workoutId)
                        .update("name", workoutName);

                Bundle bundle = new Bundle();
                bundle.putString("workoutId", workoutId);
                bundle.putString("workoutName", workoutName);
                bundle.putString("timestampSeconds", String.valueOf(timestampSeconds));
                bundle.putString("timestampNanoseconds", String.valueOf(timestampNanoseconds));

                WorkoutExercisesOverviewFragment workoutExercisesOverviewFragment = new WorkoutExercisesOverviewFragment();
                workoutExercisesOverviewFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, workoutExercisesOverviewFragment, "openWorkoutExercisesOverviewFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });


    }
}