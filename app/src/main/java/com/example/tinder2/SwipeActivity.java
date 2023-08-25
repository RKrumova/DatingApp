package com.example.tinder2;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinder2.Account.FacebookAuthHelper;
import com.example.tinder2.Account.GoogleAuthHelper;
import com.example.tinder2.Account.SettingActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class SwipeActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    private ImageView profile_imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    String username;
    private ImageView simpleImageView;
    private TextView newUserTextView;
    private Button logOutButton;
    private Button dislikeButton;
    private Button likeButton;
    private Button askButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set flags for full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://dating-app-bfd70-default-rtdb.firebaseio.com/");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users");
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_swipe);
        username = getIntent().getStringExtra("username");
        simpleImageView = findViewById(R.id.swipeImageView);
        newUserTextView = findViewById(R.id.newUserTextView);
        dislikeButton = findViewById(R.id.dislikeButton);
        likeButton = findViewById(R.id.likeButton);
        askButton = findViewById(R.id.askButton);
        loadingUserData();
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle dislike button click
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle like button click
            }
        });

        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle ask button click
            }
        });
    }
    public void loadingUserData (){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                loadPicture(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadPicture (DataSnapshot snapshot){
        final String profilePicUrl = snapshot.child("users").child("username").child("profile_pic").getValue(String.class);
        if (profilePicUrl != null) {
            Picasso.get().load(profilePicUrl).into(simpleImageView);
        }
    }
}

