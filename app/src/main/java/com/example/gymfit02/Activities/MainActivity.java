package com.example.gymfit02.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymfit02.Fragments.AnalyseFragment;
import com.example.gymfit02.Fragments.DashboardFragment;
import com.example.gymfit02.Fragments.FriendsFragment;
import com.example.gymfit02.Fragments.ProfilFragment;
import com.example.gymfit02.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private Fragment dashboardFragment;
    private Fragment analyseFragment;
    private Fragment friendsFragment;
    private Fragment profilFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dashboardFragment = new DashboardFragment();
        analyseFragment = new AnalyseFragment();
        friendsFragment = new FriendsFragment();
        profilFragment = new ProfilFragment();

        fAuth = FirebaseAuth.getInstance();
        // Enable database offline support
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);


        // BottomNavigationView
        btm_nav_view = findViewById(R.id.bottomNavigationView);

        navigationListener();



        userID = fAuth.getCurrentUser().getUid();
        // FirebaseUser user = fAuth.getCurrentUser();

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
                        setFragment(dashboardFragment);
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


}