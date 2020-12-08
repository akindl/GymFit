package com.example.gymfit02.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymfit02.Activities.LoginActivity;
import com.example.gymfit02.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {


    private static final String TAG = "profilFragment";
    public static final int REQUEST_CODE_IMAGE = 1000;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;

    private ImageView profileImageView;
    private TextView userNameView;
    private TextView userEmailView;

    private Button mLogoutBtn;
    private Button resendVerificationCode;
    private TextView mVerifyMsg;

    private DocumentReference documentReferenceUsers;
    private StorageReference storageReference;
    private StorageReference profileImageRef;


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
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        // Reference to get the userData like name and email
        documentReferenceUsers = fStore.collection("users").document(userId);
        // Reference to Upload the profileImage
        storageReference = FirebaseStorage.getInstance().getReference();
        //Reference to the uploaded profileImage of the current user
        profileImageRef = storageReference.child("users/" + userId + "profileImage.jpg");
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


        // Get profil information
        userNameView = (TextView) rootView.findViewById(R.id.userName);
        userEmailView = (TextView) rootView.findViewById(R.id.userEmail);
        setDocumentReferenceUsersListener();
        profileImageView = (ImageView) rootView.findViewById(R.id.profilImage);
        setChangeProfileImageListener();
        getProfileImageFromStorage();


        return rootView;

    }



    // ---------------------- PROFILE IMAGE METHODS ------------------------------------------------

    /**
     * Profile Image Listener
     * User can change the profil image with images from his gallery
     */
    private void setChangeProfileImageListener() {
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open the gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, REQUEST_CODE_IMAGE);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_IMAGE) {
            Uri imageUri = data.getData();
            // Set the profil Image to the selected Image from gallery
            // HINT: Disable that the image is shown after it was selected because we want to
            // download the uploaded image to get it every time the user login
            // profilImage.setImageURI(imageUri);
            // Toast.makeText(getActivity(), "Bild ausgetauscht.", Toast.LENGTH_SHORT).show();
            uploadImageToFirebase(imageUri);
        }
    }

    /**
     * Help-method to upload image file to Firebase Storage
     */
    private void uploadImageToFirebase(Uri imageUri) {

        final StorageReference fileRef = storageReference.child("users/" + userId + "profileImage.jpg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "ImageUpload: success");
                        Toast.makeText(getActivity(), "Bild hochgeladen.", Toast.LENGTH_SHORT).show();
                        // Get the URL of the image
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Set the image to the imageView of the profilFragment
                                Picasso.get().load(uri).into(profileImageView);
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG, "ImageUpload: failed");
                    }
                });
    }


    /**
     * Getter Method
     * Download the profilImage and set it into the profilImageView
     */
    private void getProfileImageFromStorage() {
        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        });
    }



    // ---------------------- PROFILE INFORMATION METHODS ------------------------------------------

    /**
     * Profile information Listener
     * Update the shown userName and userEmail
     */
    private void setDocumentReferenceUsersListener() {
        documentReferenceUsers.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    userNameView.setText(snapshot.getString("name"));
                    userEmailView.setText(snapshot.getString("email"));

                    Log.d(TAG, "Current data: " + snapshot.getData());

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }




    // ---------------------- PROFILE VERIFICATION AND LOGOUT METHODS ------------------------------

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