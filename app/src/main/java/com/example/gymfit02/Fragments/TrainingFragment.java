package com.example.gymfit02.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
    private ImageView imageViewWorkouts;
    private ImageView imageViewPlans;


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
        getActivity().setTitle("GymFit");

        imageViewWorkouts = (ImageView) rootView.findViewById(R.id.bg_workouts);
        setImageViewWorkoutsToOpenWorkoutsOverviewFragmentListener();
        imageViewPlans = (ImageView) rootView.findViewById(R.id.bg_plans);
        setImageViewPlansToOpenPlansOverviewFragmentListener();


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
    public void setImageViewWorkoutsToOpenWorkoutsOverviewFragmentListener() {
        imageViewWorkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkoutsOverviewFragment workoutsOverviewFragment = new WorkoutsOverviewFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, workoutsOverviewFragment, "")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }



    /**
     * When user click on the button "Übungen" he will direct to ExercisesOverviewFragment
     */
    public void setImageViewPlansToOpenPlansOverviewFragmentListener() {
        imageViewPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                PlansOverviewFragment plansOverviewFragment = new PlansOverviewFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        // fragment_container_view is the FragmentContainer of all fragments in MainActivity
                        .replace(R.id.fragment_container_view, plansOverviewFragment, "")
                        .addToBackStack(null)
                        .commit();

                 */
            }
        });
    }

}





































