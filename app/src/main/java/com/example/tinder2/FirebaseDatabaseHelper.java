package com.example.tinder2;

import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.auth.oauth2.GoogleCredentials;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FirebaseDatabaseHelper {

    private FirebaseAuth mAuth;

    public FirebaseDatabaseHelper(Context context) {
        initFirebase(context);
    }
    // Initialize FileInputStream with the path to serviceAccountKey.json
    private void initFirebase(Context context) throws IOException {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error loading serviceAccountKey.json.", Toast.LENGTH_SHORT).show();
        }
        // Build FirebaseOptions using the FileInputStream
        FirebaseOptions options = null;
        GoogleCredentials credentials = null;
        credentials = GoogleCredentials.fromStream(serviceAccount);

        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error loading Firebase options.", Toast.LENGTH_SHORT).show();
        }
        // Get an instance of FirebaseAuth
        if (options != null) {
            FirebaseApp.initializeApp(context, options);
        }

        mAuth = FirebaseAuth.getInstance();
    }
    static final Supplier<GoogleCredentials> APPLICATION_DEFAULT_CREDENTIALS =
            new Supplier<GoogleCredentials>() {
                @Override
                public GoogleCredentials get() {
                    try {
                        return ApplicationDefaultCredentialsProvider.getApplicationDefault()
                                .createScoped(FIREBASE_SCOPES);
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                }
            };
    /**
     * This method is used to sign in a user with a custom token. It takes the custom token as a
     * parameter and an OnCompleteListener to handle the result of the sign-in operation.
    */
    public void signInWithCustomToken(String mCustomToken, OnCompleteListener<AuthResult> onCompleteListener) {
        mAuth.signInWithCustomToken(mCustomToken)
                .addOnCompleteListener(onCompleteListener);
    }
    /** USER REGISTER
     * */
    public void registerUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }

    // Add more methods for handling other authentication-related operations like
    // user registration, user login, etc.
    // For example:
    // public void registerUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) { ... }
    // public void loginUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) { ... }
    // and so on...
}
