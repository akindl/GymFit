package com.example.gymfit02.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymfit02.Adapter.ExerciseRecyclerAdapter;
import com.example.gymfit02.Adapter.PlanRecyclerAdapter;
import com.example.gymfit02.Models.DatabaseExerciseModel;
import com.example.gymfit02.Models.DatabasePlanModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PlansOverviewFragment extends Fragment {

    private static final String TAG = "plansOverviewFragment";

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;
    private DocumentReference documentReferenceUsers;

    // Elements
    private RecyclerView plansOverviewRecyclerView;
    private Button createPlanButton;
    private Query query;
    private FirestoreRecyclerOptions<DatabasePlanModel> options;
    private FirestoreRecyclerAdapter adapter;

    public PlansOverviewFragment() {
        // Required empty public constructor
    }


    public static PlansOverviewFragment newInstance(String param1, String param2) {
        PlansOverviewFragment fragment = new PlansOverviewFragment();

        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupFirestoreConnection();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plans_overview, container, false);

        getActivity().setTitle("Deine Trainingspläne");

        setupRecyclerView(rootView);

        createPlanButton = (Button) rootView.findViewById(R.id.createPlanButton);
        setCreatePlanButtonToOpenCreatePlanFragmentListener();


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
                .document(userId).collection("Plans")
                .orderBy("name", Query.Direction.ASCENDING); //alphabetisch sortiert

        // RecyclerOptions
        options = new FirestoreRecyclerOptions.Builder<DatabasePlanModel>()
                .setLifecycleOwner(this) // this start and stop the adapter automatically
                .setQuery(query, DatabasePlanModel.class)
                .build();

        adapter = new PlanRecyclerAdapter(options);

        plansOverviewRecyclerView = (RecyclerView) view.findViewById(R.id.plansOverviewRecyclerView);
        plansOverviewRecyclerView.setHasFixedSize(true);
        plansOverviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        plansOverviewRecyclerView.setAdapter(adapter);

    }


    /**
     * When user click on the button "Plan erstellen" he will direct to CreatePlanFragment
     */
    public void setCreatePlanButtonToOpenCreatePlanFragmentListener() {
        createPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePlanFragment createPlanFragment = new CreatePlanFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, createPlanFragment, "openCreatePlansFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
