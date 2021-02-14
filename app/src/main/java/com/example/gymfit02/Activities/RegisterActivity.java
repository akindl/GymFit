package com.example.gymfit02.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymfit02.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
// import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Activity to register a new account or to return to login to an existing account
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText mUserName, mUserEmail, mUserPassword;
    private Button mRegisterBtn;
    private TextView mLoginBtn;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;
    //TODO Outsourcen in eigene Klasse
    private FirebaseFirestore fStore;
    private String userID;

    private static final String TAG = "Registration";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserName       = findViewById(R.id.editTextPersonName);
        mUserEmail      = findViewById(R.id.editTextRegisterPersonEmail);
        mUserPassword   = findViewById(R.id.editTextRegisterPersonPassword);
        mRegisterBtn    = findViewById(R.id.buttonCreateNewAccount);
        mLoginBtn       = findViewById(R.id.textViewAlreadyRegisteredToLogin);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBarRegister);




        // Sign in existing users and open MainActivity
        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        // Sign up new users with a click on the register button
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String email = mUserEmail.getText().toString().trim();
                String password = mUserPassword.getText().toString().trim();
                String name = mUserName.getText().toString().trim();

                // Check if email is not empty
                if(TextUtils.isEmpty(email)) {
                    mUserEmail.setError("Eine E-Mail Adresse ist erforderlich.");
                    return;
                }

                // Check if password is not empty
                if(TextUtils.isEmpty(password)) {
                    mUserPassword.setError("Ein Passwort ist erforderlich.");
                    return;
                }

                // Check if password is >6 chars
                if(password.length() < 6) {
                    mUserPassword.setError("Passwort muss mind. 6 Zeichen lang sein.");
                    return;
                }

                // Little loading progress bar to show the user that someting is processing
                progressBar.setVisibility(View.VISIBLE);

                // Register new User to Firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            // ----------------- START: User Email Verification -----------------

                            // Send email with verification link to verify user email address
                            FirebaseUser user = fAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Du hast eine E-Mail zum Bestätigen erhalten.", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "sendEmailVerification: failure - email not sent! " + e.getMessage());
                                }
                            });

                            // ----------------- END: User Email Verification -------------------



                            // Toast.makeText(RegisterActivity.this, "Du hast Dich erfolgreich registriert!", Toast.LENGTH_SHORT).show();
                            // Log.d(TAG, "createUserWithEmail: success");



                            // ----------------- START: Save user data in Firestore -----------------

                            // Get the new created UserID of the user
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String, Object> fUser = new HashMap<>();

                            fUser.put("name", mUserName.getText().toString().trim());
                            fUser.put("email", mUserEmail.getText().toString().trim());
                            documentReference.set(fUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user profile is created on firestore for " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: user profil is NOT created on firestore " + e.toString());
                                }
                            });

                            // ----------------- END: Save user data in Firestore -------------------



                            // Go to MainActivity after successfull registration
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "createUserWithEmail: failure");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }));

            }
        });


        // Go to LoginActivity if user click that he has already an account
        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}




































