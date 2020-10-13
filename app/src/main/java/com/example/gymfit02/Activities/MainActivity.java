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
// import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView btm_nav_view;
    private Button mLogoutBtn;
    private FirebaseAuth fAuth;
//    private FirebaseFirestore fStore;
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

        mLogoutBtn = findViewById(R.id.buttonLogout);
        dashboardFragment = new DashboardFragment();
        analyseFragment = new AnalyseFragment();
        friendsFragment = new FriendsFragment();
        profilFragment = new ProfilFragment();

        // Firebase
        fAuth = FirebaseAuth.getInstance();
        // fStore = FirebaseFirestore.getInstance();


        resendVerificationCode = findViewById(R.id.buttonResendVerificationCode);
        mVerifyMsg = findViewById(R.id.textViewAccountNotVerified);

        userID = fAuth.getCurrentUser().getUid();
        // FirebaseUser user = fAuth.getCurrentUser();

        //TODO BottomNavigationView
        btm_nav_view = findViewById(R.id.bottomNavigationView);





        /*
        // if email is not verified show button to send verification email again
        if(!user.isEmailVerified()) {
            //TODO Verifizierungsprozess ist jetzt in dem ProfilFragment - Neu Verknüpfen
          //  mVerifyMsg.setVisibility(View.VISIBLE);
          //  resendVerificationCode.setVisibility(View.VISIBLE);

            resendVerificationCode.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Send email with verification link to verify user email address
                    FirebaseUser user = fAuth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Du hast eine E-Mail zum Bestätigen erhalten.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Registration", "sendEmailVerification: failure - email not sent! " + e.getMessage());
                        }
                    });
                }
            });

        }
        */


        navigationListener();

        // Go to LoginActivity if user click to logout
        //logoutUserListener();
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
                        Toast.makeText(MainActivity.this, "Training", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.analyse:
                        setFragment(analyseFragment);
                        Toast.makeText(MainActivity.this, "Analyse", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.friends:
                        setFragment(friendsFragment);
                        Toast.makeText(MainActivity.this, "Freundesliste", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.profil:
                        setFragment(profilFragment);
                        Toast.makeText(MainActivity.this, "Profil", Toast.LENGTH_SHORT).show();
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
        fragmentTransaction.replace(R.id.mainActivityContainer, fragment);
        fragmentTransaction.commit();

    }



    /**
     * Logout-Button OnClickListener:
     * If user click on the logout button he will be logout.
     */
    private void logoutUserListener() {
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Du hast Dich erfolgreich abgemeldet!", Toast.LENGTH_SHORT).show();
                Log.d("Logout", "logoutUser: success");
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                fAuth.signOut();
            }

        });
    }
}