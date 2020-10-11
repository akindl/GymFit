package com.example.gymfit02.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

/**
 * Activity to login with an existing account or go to the RegisterActivity to create a new account
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mUserEmail, mUserPassword;
    private Button mLoginBtn;
    private TextView mCreateBtn, mforgotPasswordLink;
    private ProgressBar progressBar;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserEmail = findViewById(R.id.editTextLoginPersonEmail);
        mUserPassword = findViewById(R.id.editTextLoginPersonPassword);
        mLoginBtn = findViewById(R.id.buttonLogin);
        mCreateBtn = findViewById(R.id.textViewNoAccountToRegister);
        progressBar = findViewById(R.id.progressBarLogin);
        mforgotPasswordLink = findViewById(R.id.textViewForgotPassword);
        fAuth = FirebaseAuth.getInstance();


        // Check if the account data of the users match to an existing account
        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = mUserEmail.getText().toString().trim();
                String password = mUserPassword.getText().toString().trim();

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

                // Authenticate the user
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Du hast Dich erfolgreich angemeldet!", Toast.LENGTH_SHORT).show();
                            Log.d("Login", "loginUserWithEmail: success");
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                        Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Login", "loginUserWithEmail: failure");
                        progressBar.setVisibility(View.GONE);
                    }
                    }
                });
            }
        });


        // Go to RegisterActivity if user click that he has no account yet
        mCreateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });


        // If user forgot the password he can reset it
        mforgotPasswordLink.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText editTextResetPasswordEMail = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Passwort zurücksetzen");
                passwordResetDialog.setMessage("Bitte gib deine E-Mail an, um dein Passwort zurückzusetzen.");
                passwordResetDialog.setView(editTextResetPasswordEMail);

                passwordResetDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // extract the email and send reset link
                        String userEmail = editTextResetPasswordEMail.getText().toString();
                        fAuth.sendPasswordResetEmail(userEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Erfolgreich! Du hast einen Link zum Zurücksetzen per E-Mail erhalten.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Fehler! Link zum Zurücksetzen konnte nicht gesendet werden.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // close the dialog
                        return;
                    }
                });

                // Show Dialog
                passwordResetDialog.show();
            }
        }
        ));
    }
}






















