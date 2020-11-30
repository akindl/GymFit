package com.example.gymfit02.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymfit02.Activities.LoginActivity;
import com.example.gymfit02.Activities.MainActivity;
import com.example.gymfit02.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {


    private static final String TAG = "profilFragment";

    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private Button mLogoutBtn;
    private Button resendVerificationCode;
    private TextView mVerifyMsg;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect to Firebase user
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        // Set Logout Button Listener
        mLogoutBtn = (Button) rootView.findViewById(R.id.buttonLogout);
        setLogoutUserListener();

        // Set Resend Verification Code Listener
        resendVerificationCode = (Button) rootView.findViewById(R.id.buttonResendVerificationCode);
        mVerifyMsg = (TextView) rootView.findViewById(R.id.textViewAccountNotVerified);


        // if email is not verified show button to send verification email again
        if(!user.isEmailVerified()) {
            mVerifyMsg.setVisibility(View.VISIBLE);
            resendVerificationCode.setVisibility(View.VISIBLE);
            setResendVerificationCodeListener();
        }

        return rootView;
    }

    /**
     * ResendVerificationCode-Button OnClickListener
     * If user is not verified the system will send a new verification code per mail
     */
    private void setResendVerificationCodeListener() {
        resendVerificationCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Send email with verification link to verify user email address
                FirebaseUser user = fAuth.getCurrentUser();
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Du hast eine E-Mail zum Bestätigen erhalten.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "sendEmailVerification: failure - email not sent! " + e.getMessage());
                    }
                });
            }
        });
    }


    /**
     * Logout-Button OnClickListener:
     * If user click on the logout button he will be logout.
     */
    private void setLogoutUserListener() {
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Du hast Dich erfolgreich abgemeldet!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "logoutUser: success");
                startActivity(new Intent(getActivity(), LoginActivity.class));
                fAuth.signOut();
            }

        });
    }
}