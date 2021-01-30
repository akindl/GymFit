package com.example.gymfit02.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gymfit02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class CreatePlanFragment extends Fragment {

    private static final String TAG = "createPlanFragment";

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;
    private DocumentReference documentReferenceUsers;
    private StorageReference storageReference;

    public CreatePlanFragment() {
        // Required empty public constructor
    }

    public static CreatePlanFragment newInstance(String param1, String param2) {
        CreatePlanFragment fragment = new CreatePlanFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_plan, container, false);

        getActivity().setTitle("Neuen Plan erstellen");

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


}
