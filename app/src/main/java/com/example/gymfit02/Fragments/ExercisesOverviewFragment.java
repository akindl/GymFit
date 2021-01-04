package com.example.gymfit02.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gymfit02.Models.DatabaseExerciseModel;
import com.example.gymfit02.R;
import com.example.gymfit02.Util.ExerciseViewHolder;
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

    private static final String TAG = "trainingFragment";

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

    public ExercisesOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExercisesOverviewFragment newInstance(String param1, String param2) {
        ExercisesOverviewFragment fragment = new ExercisesOverviewFragment();

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

        // Reference to get the userData
        documentReferenceUsers = fStore.collection("users").document(userId);

        // Query
        // query = fStore.collection("testData");
        query = fStore.collection("users").document(userId).collection("Database-Exercises");

        // RecyclerOptions
        options = new FirestoreRecyclerOptions.Builder<DatabaseExerciseModel>()
                .setQuery(query, DatabaseExerciseModel.class)
                .build();

    }


    // -----------------------------------------------------------------------------------------
    // YT TUTORIAL Recycler View https://www.youtube.com/watch?v=cBwaJYocb9I&t=419s


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exercises_overview, container, false);

        createRecyclerAdapter();

        exercisesRecyclerView = (RecyclerView) rootView.findViewById(R.id.exercisesRecyclerView);
        exercisesRecyclerView.setHasFixedSize(true);
        // exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        exercisesRecyclerView.setAdapter(adapter);

        return rootView;
    }

    /**
     * Initilize RecyclerViewAdapter Options to show DatabaseExercises
     */

    private void createRecyclerAdapter() {

        // RecyclerAdapter
        adapter = new FirestoreRecyclerAdapter<DatabaseExerciseModel, ExerciseViewHolder>(options) {
            @NonNull
            @Override
            public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_database_exercise_single, parent, false);
                return new ExerciseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position, @NonNull DatabaseExerciseModel model) {
                holder.getExercise_name().setText(model.getName());
                holder.getExercise_numberOfSets().setText(model.getNumberOfSets());
                holder.getExercise_volume().setText(model.getVolume());
                holder.getExercise_maxLoad().setText(model.getMaxLoad());
                holder.getExercice_notes().setText(model.getNotes());

            }
        };
    }



    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }


}