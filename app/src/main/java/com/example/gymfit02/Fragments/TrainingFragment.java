package com.example.gymfit02.Fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gymfit02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainingFragment extends Fragment {

    private static final String TAG = "trainingFragment";

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;
    private DocumentReference documentReferenceUsers;
    private StorageReference storageReference;

    // Elements
    private ImageView imageViewPlans;
    private ImageView imageViewExercises;


    public TrainingFragment() {
        // Required empty public constructor
    }


    public static TrainingFragment newInstance(String param1, String param2) {
        TrainingFragment fragment = new TrainingFragment();

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
        documentReferenceUsers = fStore.collection("users").document(userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_training, container, false);

        imageViewPlans = (ImageView) rootView.findViewById(R.id.bg_plans);
        setImageViewPlansToOpenCreatePlansFragmentListener();
        imageViewExercises = (ImageView) rootView.findViewById(R.id.bg_exercises);
        setImageViewPlansToOpenExercisesOverviewFragmentListener();


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
     * When user click on the button "Trainingspläne" he will direct to CreatePlansFragment
     */
    public void setImageViewPlansToOpenCreatePlansFragmentListener() {
        imageViewPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePlansFragment createPlansFragment = new CreatePlansFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, createPlansFragment, "openCreatePlansFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }



    /**
     * When user click on the button "Übungen" he will direct to ExercisesOverviewFragment
     */
    public void setImageViewPlansToOpenExercisesOverviewFragmentListener() {
        imageViewExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExercisesOverviewFragment exercisesOverviewFragment = new ExercisesOverviewFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, exercisesOverviewFragment, "openExercisesOverviewFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}





































