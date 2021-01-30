package com.example.gymfit02.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymfit02.Adapter.WorkoutExercisePerformanceSetRecyclerAdapter;
import com.example.gymfit02.Models.DatabaseExercisePerformanceModel;
import com.example.gymfit02.Models.DatabaseExercisePerformanceSetModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class WorkoutSingleExercisePerformanceFragment extends Fragment {

    //TODO
    // 1. Basic Informationen wie Namen, Notizen anzeigen lassen
    // 2. Tabelle erstellen und Inhalte anzeigen lassen
    // 3. Sätze hinzufügen, löschen und abhaken können
    // 4. Übung abhaken können mit sichtbaren Effekt

    private static final String TAG = "exercisePerformance";

    // Firestore Connection
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;

    // Bundle information
    private String workoutId;
    private String deviceName;
    private String exerciseName;
    private String notes;
    private String exerciseId;
    private String performanceId;

    // Views to show information
    private Button checkExercisePerformanceButton;
    private TextView exerciseNameView;
    private TextView deviceNameView;
    private TextView noteView;

    // Views to add new set
    private Button addSetButton;
    private Spinner rpeSpinner;
    // private NumberPicker setNumberNumberPicker;
    private EditText repsEditText;
    private EditText loadEditText;
    private List<String> rpeSpinnerList = new ArrayList<>();

    // RecyclerView
    private RecyclerView exercisePerformanceSetsRecyclerView;
    private Query query;
    private FirestoreRecyclerOptions<DatabaseExercisePerformanceSetModel> options;
    private WorkoutExercisePerformanceSetRecyclerAdapter adapter;
    private int setCounter = 0;

    // Firestore dataReferences
    private DocumentReference exercisePerformanceReference;
    private Long totalVolume;
    private Long setCount;
    private ArrayList<Integer> oneRepMaxValues;


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
            this.deviceName = bundle.getString("deviceName");
            this.exerciseName = bundle.getString("exerciseName");
            this.notes = bundle.getString("notes");
            this.exerciseId = bundle.getString("exerciseId");
            this.performanceId = bundle.getString("performanceId");

        }

        rpeSpinnerList.add("Einfach");
        rpeSpinnerList.add("Mittel");
        rpeSpinnerList.add("Schwer");
        rpeSpinnerList.add("Muskelversagen");

        setupFirestoreConnection();


        // Get reference to the exercisePerformance to update oneRepMax and totalVolume
        exercisePerformanceReference = fStore.collection("Users")
                .document(userId).collection("Exercises")
                .document(exerciseId).collection("ExercisePerformances")
                .document(performanceId);

        setupSetCounter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_single_exercise_performance, container, false);
        // tell host activity that the fragment wants to add some menu options
        setHasOptionsMenu(true);

        getActivity().setTitle("Übung: " + exerciseName);


        // Views to show basic information
        // checkExercisePerformanceButton = (Button) rootView.findViewById(R.id.button_workoutExercisePerformance_exercisePerformanceCheck);
        exerciseNameView = (TextView) rootView.findViewById(R.id.textView_workoutExercisePerformance_exerciseName);
        deviceNameView = (TextView) rootView.findViewById(R.id.textView_workoutExercisePerformance_deviceName);
        noteView = (TextView) rootView.findViewById(R.id.textView_workoutExercisePerformance_notes);

        exerciseNameView.setText(exerciseName);
        deviceNameView.setText(deviceName);
        noteView.setText(notes);


        // Views to add new set
        addSetButton = (Button) rootView.findViewById(R.id.button__workoutExercisePerformance_addSet);
        setNewSetPerformanceListener();

        rpeSpinner = (Spinner) rootView.findViewById(R.id.spinner_workoutExercisePerformance_rpe);
        // setNumberNumberPicker = (NumberPicker) rootView.findViewById(R.id.numberPicker_workoutExercisePerformance_setNumber);
        // setNumberNumberPicker.setMinValue(1);
        // setNumberNumberPicker.setMaxValue(20);

        repsEditText = (EditText) rootView.findViewById(R.id.editText_workoutExercisePerformance_reps);
        loadEditText = (EditText) rootView.findViewById(R.id.editText_workoutExercisePerformance_load);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, rpeSpinnerList);
        rpeSpinner.setAdapter(arrayAdapter);

        setupRecyclerView(rootView);


        return rootView;
    }

    /**
     * The setCounter generates iterative setNumber values
     * The method initialize the setCounter
     */
    private void setupSetCounter() {

        fStore.collection("Users")
                .document(userId).collection("Exercises")
                .document(exerciseId).collection("ExercisePerformances")
                .document(performanceId).collection("PerformanceSets")
                .orderBy("setNumber", Query.Direction.DESCENDING).limit(1)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Log.d(TAG, queryDocumentSnapshots.getDocuments().get(0).getReference().toString());
                if (!queryDocumentSnapshots.isEmpty()) {
                    DatabaseExercisePerformanceSetModel singleSetWithHighestSetNumber = queryDocumentSnapshots.toObjects(DatabaseExercisePerformanceSetModel.class).get(0);
                    setCounter = singleSetWithHighestSetNumber.getSetNumber();
                } else {
                    setCounter = 0;
                }

            }
        });

    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.new_exercise_performance_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_exercise_performance:
                saveExercisePerformance();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveExercisePerformance() {

        fStore.collection("Users")
            .document(userId).collection("Exercises")
            .document(exerciseId).collection("ExercisePerformances")
            .document(performanceId).collection("PerformanceSets")
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        checkExerciseSetToTrue(document);
                    }

                }
            }
        });

    }

    /**
     * Iterate through the list and update all "check" entrys to true
     */
    private void checkExerciseSetToTrue(DocumentSnapshot documentSnapshot) {

        final DatabaseExercisePerformanceSetModel singleSet = documentSnapshot.toObject(DatabaseExercisePerformanceSetModel.class);
        final DocumentReference singleSetReference = documentSnapshot.getReference();
        boolean check = singleSet.getCheck();

        if(check == false) {
            documentSnapshot.getReference().update("check", true);

                exercisePerformanceReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // update totalVolume in exercisePerformance document
                        totalVolume = (Long) documentSnapshot.get("totalVolume");
                        int weight = singleSet.getLoad();
                        int reps = singleSet.getReps();
                        totalVolume += weight * reps;
                        exercisePerformanceReference.update("totalVolume", totalVolume);

                        // TODO setCount wird im exercisePerformance document nicht erhöht??
                        //  die richtige Zahl wird geladen, dann aber mit der ersten überschrieben...
                        // update setNumber in exercisePerformance document
                        setCount = (Long) documentSnapshot.get("setCount");
                        setCount += 1;
                        exercisePerformanceReference.update("setCount", setCount);

                        // update oneRepMax in performanceSet document
                        int oneRepMax = calcOneRepMax(weight, reps);
                        singleSetReference.update("oneRepMax", oneRepMax);

                        // update oneRepMax in exercisePerformance document
                        fStore.collection("Users")
                                .document(userId).collection("Exercises")
                                .document(exerciseId).collection("ExercisePerformances")
                                .document(performanceId).collection("PerformanceSets")
                                .orderBy("oneRepMax", Query.Direction.DESCENDING).limit(1)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                // Log.d(TAG, queryDocumentSnapshots.getDocuments().get(0).getReference().toString());
                                DatabaseExercisePerformanceSetModel singleSetWithOneRepMax = queryDocumentSnapshots.toObjects(DatabaseExercisePerformanceSetModel.class).get(0);
                                exercisePerformanceReference.update("oneRepMax", singleSetWithOneRepMax.getOneRepMax());
                            }
                        });
                    }
                });

                Log.d(TAG, "DocumentSnapshot successfully updated!");


        }

    }


    private void setNewSetPerformanceListener() {
        addSetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(repsEditText.getText().toString().isEmpty() || loadEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Bitte Werte eintragen.", Toast.LENGTH_SHORT).show();
                    return;
                }

                setCounter += 1;
                int reps = Integer.parseInt(repsEditText.getText().toString());
                int load = Integer.parseInt(loadEditText.getText().toString());
                String rpe = rpeSpinner.getSelectedItem().toString();

                CollectionReference performanceSetsRef = fStore.collection("Users")
                        .document(userId).collection("Exercises")
                        .document(exerciseId).collection("ExercisePerformances")
                        .document(performanceId).collection("PerformanceSets");


                performanceSetsRef.add(new DatabaseExercisePerformanceSetModel(setCounter, Integer.valueOf(reps),
                        Integer.valueOf(load), rpe, 0, false));
                load = 0;
                reps = 0;
            }
        });
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
        // Get all performanceSets of the specific performanceId
        query = fStore.collection("Users")
                .document(userId).collection("Exercises")
                .document(exerciseId).collection("ExercisePerformances")
                .document(performanceId).collection("PerformanceSets")
                .orderBy("setNumber", Query.Direction.ASCENDING); // numerical sorted


        // RecyclerOptions
        options = new FirestoreRecyclerOptions.Builder<DatabaseExercisePerformanceSetModel>()
                .setLifecycleOwner(this) // this start and stop the adapter automatically
                .setQuery(query, DatabaseExercisePerformanceSetModel.class)
                .build();

        adapter = new WorkoutExercisePerformanceSetRecyclerAdapter(options);

        exercisePerformanceSetsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_workoutExercisePerformance_setList);
        exercisePerformanceSetsRecyclerView.setHasFixedSize(true);
        exercisePerformanceSetsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        exercisePerformanceSetsRecyclerView.setAdapter(adapter);

        // Update totalVolume if single set is checked
        adapter.setOnItemClickListener(new WorkoutExercisePerformanceSetRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {
                final DatabaseExercisePerformanceSetModel singleSet = documentSnapshot.toObject(DatabaseExercisePerformanceSetModel.class);
                final DocumentReference singleSetReference = documentSnapshot.getReference();
                boolean check = singleSet.getCheck();
                if(check) {
                    documentSnapshot.getReference().update("check", false)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                exercisePerformanceReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        // update totalVolume in exercisePerformance document
                                        totalVolume = (Long) documentSnapshot.get("totalVolume");
                                        int weight = singleSet.getLoad();
                                        int reps = singleSet.getReps();
                                        totalVolume -= weight * reps;
                                        exercisePerformanceReference.update("totalVolume", totalVolume);

                                        // update setCount in exercisePerformance document
                                        setCount = (Long) documentSnapshot.get("setCount");
                                        setCount -= 1;
                                        exercisePerformanceReference.update("setCount", setCount);

                                        // update oneRepMax in performanceSet document
                                        singleSetReference.update("oneRepMax", 0);

                                        // update oneRepMax in exercisePerformance document
                                        fStore.collection("Users")
                                                .document(userId).collection("Exercises")
                                                .document(exerciseId).collection("ExercisePerformances")
                                                .document(performanceId).collection("PerformanceSets")
                                                .orderBy("oneRepMax", Query.Direction.DESCENDING).limit(1)
                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                // Log.d(TAG, queryDocumentSnapshots.getDocuments().get(0).getReference().toString());
                                                DatabaseExercisePerformanceSetModel singleSetWithOneRepMax = queryDocumentSnapshots.toObjects(DatabaseExercisePerformanceSetModel.class).get(0);
                                                exercisePerformanceReference.update("oneRepMax", singleSetWithOneRepMax.getOneRepMax());
                                            }
                                        });
                                    }
                                });
                                Log.d(TAG, "DocumentSnapshot successfully updated!");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                                Log.d(TAG, documentSnapshot.getReference().getParent().getParent().getId());
                            }
                        });
                } else {
                    documentSnapshot.getReference().update("check", true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                exercisePerformanceReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        // update totalVolume in exercisePerformance document
                                        totalVolume = (Long) documentSnapshot.get("totalVolume");
                                        int weight = singleSet.getLoad();
                                        int reps = singleSet.getReps();
                                        totalVolume += weight * reps;
                                        exercisePerformanceReference.update("totalVolume", totalVolume);

                                        // update setNumber in exercisePerformance document
                                        setCount = (Long) documentSnapshot.get("setCount");
                                        setCount += 1;
                                        exercisePerformanceReference.update("setCount", setCount);

                                        // update oneRepMax in performanceSet document
                                        int oneRepMax = calcOneRepMax(weight, reps);
                                        singleSetReference.update("oneRepMax", oneRepMax);

                                        // update oneRepMax in exercisePerformance document
                                        fStore.collection("Users")
                                                .document(userId).collection("Exercises")
                                                .document(exerciseId).collection("ExercisePerformances")
                                                .document(performanceId).collection("PerformanceSets")
                                                .orderBy("oneRepMax", Query.Direction.DESCENDING).limit(1)
                                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                // Log.d(TAG, queryDocumentSnapshots.getDocuments().get(0).getReference().toString());
                                                DatabaseExercisePerformanceSetModel singleSetWithOneRepMax = queryDocumentSnapshots.toObjects(DatabaseExercisePerformanceSetModel.class).get(0);
                                                exercisePerformanceReference.update("oneRepMax", singleSetWithOneRepMax.getOneRepMax());
                                            }
                                        });
                                    }
                                });

                                Log.d(TAG, "DocumentSnapshot successfully updated!");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                                Log.d(TAG, documentSnapshot.getReference().getParent().getParent().getId());
                            }
                        });
                }


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


                final DocumentSnapshot snapshot =  adapter.getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
                final DatabaseExercisePerformanceSetModel singleSet = snapshot.toObject(DatabaseExercisePerformanceSetModel.class);
                final DocumentReference singleSetReference = snapshot.getReference();
                boolean check = singleSet.getCheck();
                if(check) {
                    snapshot.getReference().update("check", false)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    exercisePerformanceReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            // update totalVolume in exercisePerformance document
                                            totalVolume = (Long) documentSnapshot.get("totalVolume");
                                            int weight = singleSet.getLoad();
                                            int reps = singleSet.getReps();
                                            totalVolume -= weight * reps;
                                            exercisePerformanceReference.update("totalVolume", totalVolume);

                                            // update setCount in exercisePerformance document
                                            setCount = (Long) documentSnapshot.get("setCount");
                                            setCount -= 1;
                                            exercisePerformanceReference.update("setCount", setCount);

                                            // update oneRepMax in performanceSet document
                                            singleSetReference.update("oneRepMax", 0);

                                            // update oneRepMax in exercisePerformance document
                                            fStore.collection("Users")
                                                    .document(userId).collection("Exercises")
                                                    .document(exerciseId).collection("ExercisePerformances")
                                                    .document(performanceId).collection("PerformanceSets")
                                                    .orderBy("oneRepMax", Query.Direction.DESCENDING).limit(1)
                                                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    // Log.d(TAG, queryDocumentSnapshots.getDocuments().get(0).getReference().toString());
                                                    if (!queryDocumentSnapshots.isEmpty()) {
                                                        DatabaseExercisePerformanceSetModel singleSetWithOneRepMax = queryDocumentSnapshots.toObjects(DatabaseExercisePerformanceSetModel.class).get(0);
                                                        exercisePerformanceReference.update("oneRepMax", singleSetWithOneRepMax.getOneRepMax());
                                                    } else {
                                                        exercisePerformanceReference.update("oneRepMax", 0);
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    // delete item from firestore
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                    Log.d(TAG, snapshot.getReference().getParent().getParent().getId());
                                }
                            });
                }
                // delete item from firestore
                adapter.deleteItem(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(exercisePerformanceSetsRecyclerView);

    }

    private int calcOneRepMax(int weight, int reps) {

        double oneRepMax = 0;
        oneRepMax = (double) weight * 36 / (37 - reps);
        return (int) oneRepMax;
    }

}






















