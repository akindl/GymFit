package com.example.gymfit02.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymfit02.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Button mLogoutBtn;
    private FirebaseAuth fAuth;
//    private FirebaseFirestore fStore;
    private String userID;
    private Button resendVerificationCode;
    private TextView mVerifyMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogoutBtn = findViewById(R.id.buttonLogout);

        // Firebase
        fAuth = FirebaseAuth.getInstance();
        // fStore = FirebaseFirestore.getInstance();

        //TODO YT Video Part 4: Verify User
        resendVerificationCode = findViewById(R.id.buttonResendVerificationCode);
        mVerifyMsg = findViewById(R.id.textViewAccountNotVerified);

        userID = fAuth.getCurrentUser().getUid();
        FirebaseUser user = fAuth.getCurrentUser();


        // if email is not verified show button to send verification email again
        if(!user.isEmailVerified()) {
            mVerifyMsg.setVisibility(View.VISIBLE);
            resendVerificationCode.setVisibility(View.VISIBLE);

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



        // Go to LoginActivity if user click to logout
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