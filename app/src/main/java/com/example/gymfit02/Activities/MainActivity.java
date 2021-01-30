package com.example.gymfit02.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.gymfit02.Fragments.AnalyseFragment;
import com.example.gymfit02.Fragments.TrainingFragment;
import com.example.gymfit02.Fragments.FriendsFragment;
import com.example.gymfit02.Fragments.ProfilFragment;
import com.example.gymfit02.Fragments.ExercisesOverviewFragment;
import com.example.gymfit02.Fragments.WorkoutCreationFragment;
import com.example.gymfit02.Fragments.WorkoutsOverviewFragment;
import com.example.gymfit02.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

// import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView btm_nav_view;
    private Button mLogoutBtn;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private String userID;
    private Button resendVerificationCode;
    private TextView mVerifyMsg;
    private Fragment trainingFragment;
    private Fragment analyseFragment;
    private Fragment friendsFragment;
    private Fragment profilFragment;

    private Fragment exercisesOverviewFragment;
    private Fragment workoutsOverviewFragment;
    private Fragment workoutCreationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trainingFragment = new TrainingFragment();
        analyseFragment = new AnalyseFragment();
        friendsFragment = new FriendsFragment();
        profilFragment = new ProfilFragment();

        //TODO delete this fragments
        exercisesOverviewFragment = new ExercisesOverviewFragment();
        workoutsOverviewFragment = new WorkoutsOverviewFragment();
        workoutCreationFragment = new WorkoutCreationFragment();


        fAuth = FirebaseAuth.getInstance();

        // Enable database offline support
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);


        // BottomNavigationView
        btm_nav_view = findViewById(R.id.bottomNavigationView);

        // Setup BottomNavigationBar and set trainingFragment as first default fragment
        navigationListener();
        setFragment(trainingFragment);


        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }


    /**
     * Listener to navigate throw the bottomNavigationView.
     */
    private void navigationListener() {
        btm_nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.dashboard:
                        setFragment(trainingFragment);
                        // Toast.makeText(MainActivity.this, "Training", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.analyse:
                        setFragment(analyseFragment);
                        // Toast.makeText(MainActivity.this, "Analyse", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.friends:
                        setFragment(friendsFragment);
                        // Toast.makeText(MainActivity.this, "Freundesliste", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.profil:
                        setFragment(profilFragment);
                        // Toast.makeText(MainActivity.this, "Profil", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }

            }
        });
    }

    /**
     * Help-method to open the selected fragment in the MainActivity
     */
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
             fragment.onActivityResult(requestCode,resultCode,data);
        }

    }


}