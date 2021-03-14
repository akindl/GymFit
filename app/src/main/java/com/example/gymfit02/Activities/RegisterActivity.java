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

import com.example.gymfit02.Models.DatabaseExerciseModel;
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

import java.util.ArrayList;
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

                                    // Save demo exercises to user data
                                    for(DatabaseExerciseModel model : loadDemoExercises())
                                        saveExerciseToDatabase(model);

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



    private ArrayList<DatabaseExerciseModel> loadDemoExercises() {
        ArrayList<DatabaseExerciseModel>list = new ArrayList<>();


        DatabaseExerciseModel databaseExerciseModel = new DatabaseExerciseModel();
        databaseExerciseModel.setExerciseName("Bankdrücken");
        databaseExerciseModel.setDeviceName("Flachbank");
        databaseExerciseModel.setNotes("");
        databaseExerciseModel.setCreatorId("admin");
        databaseExerciseModel.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel);

        DatabaseExerciseModel databaseExerciseModel2 = new DatabaseExerciseModel();
        databaseExerciseModel2.setExerciseName("Bankdrücken");
        databaseExerciseModel2.setDeviceName("Schrägbank");
        databaseExerciseModel2.setNotes("");
        databaseExerciseModel2.setCreatorId("admin");
        databaseExerciseModel2.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel2);

        DatabaseExerciseModel databaseExerciseModel3 = new DatabaseExerciseModel();
        databaseExerciseModel3.setExerciseName("Bankdrücken");
        databaseExerciseModel3.setDeviceName("Multipresse");
        databaseExerciseModel3.setNotes("");
        databaseExerciseModel3.setCreatorId("admin");
        databaseExerciseModel3.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel3);

        DatabaseExerciseModel databaseExerciseModel4 = new DatabaseExerciseModel();
        databaseExerciseModel4.setExerciseName("Kniebeuge");
        databaseExerciseModel4.setDeviceName("Langhantel");
        databaseExerciseModel4.setNotes("");
        databaseExerciseModel4.setCreatorId("admin");
        databaseExerciseModel4.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel4);

        DatabaseExerciseModel databaseExerciseModel5 = new DatabaseExerciseModel();
        databaseExerciseModel5.setExerciseName("Ausfallschritt");
        databaseExerciseModel5.setDeviceName("Langhantel");
        databaseExerciseModel5.setNotes("");
        databaseExerciseModel5.setCreatorId("admin");
        databaseExerciseModel5.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel5);

        DatabaseExerciseModel databaseExerciseModel6 = new DatabaseExerciseModel();
        databaseExerciseModel6.setExerciseName("Schulterdrücken");
        databaseExerciseModel6.setDeviceName("Kurzhantel");
        databaseExerciseModel6.setNotes("");
        databaseExerciseModel6.setCreatorId("admin");
        databaseExerciseModel6.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel6);

        DatabaseExerciseModel databaseExerciseModel7 = new DatabaseExerciseModel();
        databaseExerciseModel7.setExerciseName("Seitheben");
        databaseExerciseModel7.setDeviceName("Kurzhantel");
        databaseExerciseModel7.setNotes("");
        databaseExerciseModel7.setCreatorId("admin");
        databaseExerciseModel7.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel7);

        DatabaseExerciseModel databaseExerciseModel8 = new DatabaseExerciseModel();
        databaseExerciseModel8.setExerciseName("Butterfly");
        databaseExerciseModel8.setDeviceName("Maschine");
        databaseExerciseModel8.setNotes("");
        databaseExerciseModel8.setCreatorId("admin");
        databaseExerciseModel8.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel8);

        DatabaseExerciseModel databaseExerciseModel9 = new DatabaseExerciseModel();
        databaseExerciseModel9.setExerciseName("Reverse Butterfly");
        databaseExerciseModel9.setDeviceName("Maschine");
        databaseExerciseModel9.setNotes("");
        databaseExerciseModel9.setCreatorId("admin");
        databaseExerciseModel9.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel9);

        DatabaseExerciseModel databaseExerciseModel10 = new DatabaseExerciseModel();
        databaseExerciseModel10.setExerciseName("Rudern");
        databaseExerciseModel10.setDeviceName("Seilzug");
        databaseExerciseModel10.setNotes("");
        databaseExerciseModel10.setCreatorId("admin");
        databaseExerciseModel10.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel10);

        DatabaseExerciseModel databaseExerciseModel11 = new DatabaseExerciseModel();
        databaseExerciseModel11.setExerciseName("Latzug");
        databaseExerciseModel11.setDeviceName("Seilzug");
        databaseExerciseModel11.setNotes("");
        databaseExerciseModel11.setCreatorId("admin");
        databaseExerciseModel11.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel11);

        DatabaseExerciseModel databaseExerciseModel12 = new DatabaseExerciseModel();
        databaseExerciseModel12.setExerciseName("Klimmzug");
        databaseExerciseModel12.setDeviceName("Stange");
        databaseExerciseModel12.setNotes("");
        databaseExerciseModel12.setCreatorId("admin");
        databaseExerciseModel12.setWorkouts(new ArrayList<String>());
        list.add(databaseExerciseModel12);


        return list;

    }

    private void saveExerciseToDatabase(DatabaseExerciseModel model) {
        fStore.collection("Users").document(userID)
                .collection("Exercises").add(model);
    }
}




































