package com.example.gymfit02.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gymfit02.Adapter.WorkoutRecyclerAdapter;
import com.example.gymfit02.Models.DatabaseExercisePerformanceSetModel;
import com.example.gymfit02.Models.DatabaseWorkoutModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;

import static com.google.firebase.firestore.FieldValue.serverTimestamp;

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
        getActivity().setTitle("Deine Workouts");

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

    }

    /**
     * Help-method to setup the RecyclerView
     * @param view
     */
    private void setupRecyclerView(View view) {

        // Query
        query = fStore.collection("Users")
                .document(userId).collection("Workouts")
                .orderBy("executionDate", Query.Direction.DESCENDING); // Newest on top



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
                Timestamp executionDate = (Timestamp) documentSnapshot.get("executionDate");
                String workoutName = (String) documentSnapshot.get("name");
                Bundle bundle = new Bundle();
                bundle.putString("workoutId", workoutId);
                bundle.putString("workoutName", workoutName);
                bundle.putString("timestampSeconds", String.valueOf(executionDate.getSeconds()));
                bundle.putString("timestampNanoseconds", String.valueOf(executionDate.getNanoseconds()));

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


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                // delete item from firestore
                adapter.deleteItem(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(workoutsRecyclerView);


    }


    /**
     * When user click on the button "Workout erstellen" he will direct to CreateWorkoutFragment
     */
    public void setOpenCreateWorkoutFragmentListener() {
        createWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Timestamp timestamp = new Timestamp(new Date());

                DatabaseWorkoutModel workoutModel = new DatabaseWorkoutModel();
                workoutModel.setName("Workout"); // Set default workout name
                workoutModel.setExecutionDate(timestamp);

                // HashMap<String, Object> workout = new HashMap<>();
                // workout.put("executionDate", serverTimestamp());

                fStore.collection("Users").document(userId).collection("Workouts")
                        .add(workoutModel)// .add(workout)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                String workoutId = task.getResult().getId();

                                Bundle bundle = new Bundle();
                                bundle.putString("workoutId", workoutId);
                                bundle.putString("workoutName", "");
                                bundle.putString("timestampSeconds", String.valueOf(timestamp.getSeconds()));
                                bundle.putString("timestampNanoseconds", String.valueOf(timestamp.getNanoseconds()));


                                WorkoutCreationFragment workoutCreationFragment = new WorkoutCreationFragment();
                                workoutCreationFragment.setArguments(bundle);

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                                        .replace(R.id.fragment_container_view, workoutCreationFragment, "openCreateWorkoutFragment")
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });


            }
        });


    }


}