package com.example.tinder2;

import android.content.Intent;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinder2.Account.SettingActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SwipeActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    DatabaseReference swipesReference;
    private String newUser = "";
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
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://dating-app-bfd70-default-rtdb.firebaseio.com/");
        setContentView(R.layout.activity_swipe);
        username = getIntent().getStringExtra("username");
        simpleImageView = findViewById(R.id.swipeImageView);
        newUserTextView = findViewById(R.id.newUserTextView);
        dislikeButton = findViewById(R.id.dislikeButton);
        likeButton = findViewById(R.id.likeButton);
        askButton = findViewById(R.id.askButton);
        Button messagesButton = findViewById(R.id.messagesButton);
        Button profileButton = findViewById(R.id.profileButton);
        Button datesButton = findViewById(R.id.datesButton);
        Button swipesButton = findViewById(R.id.swipesButton);
        newUser = "check22";
        loadingUserData(newUser);
        dislikeButton.setOnClickListener(v -> {
            swipesReference = databaseReference.child("swipes").child("dislikes"); // reference to the 'swipes' branch
            setSwipe(username,  newUser);
            //then load new user
            newUser = "test123";
            loadingUserData(newUser);
        });
        likeButton.setOnClickListener(v -> {
            swipesReference = databaseReference.child("swipes").child("likes"); // reference to the 'swipes' branch
            setSwipe(username,  newUser);
            newUser = "test123";
            Toast.makeText(SwipeActivity.this, "Successfully liked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SwipeActivity.this, ChatActivity.class));
        });
        askButton.setOnClickListener(v -> {
            // Handle ask button click
        });
        messagesButton.setOnClickListener(v->{
            startActivity(new Intent(SwipeActivity.this, ChatActivity.class));
        });
        profileButton.setOnClickListener(v->{
            startActivity(new Intent(SwipeActivity.this, SettingActivity.class));
        });

    }



    private void setSwipe(String username, String newUser){
            String swipeKey = swipesReference.push().getKey(); // generate a unique key for the swipe

            if (swipeKey != null) {
                // Create a new swipe data
                SwipeEntry swipeEntry = new SwipeEntry(username, newUser);

                swipesReference.child(swipeKey).setValue(swipeEntry)
                //wipesReference.setValue(swipeEntry)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(SwipeActivity.this, "Dislike", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(SwipeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        });
            }
        }

    public void loadingUserData(String newUser) {
        Boolean dataset = false;
        databaseReference.child("users").child(newUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String gender = snapshot.child("gender").getValue(String.class);
                    String selectedSexuality = snapshot.child("sexual_orientation").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);
                    String userInformation = newUser + " " + gender + ",\t" + selectedSexuality + "\n from " + location;
                    newUserTextView.setText(userInformation);
                    loadPicture(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadPicture(DataSnapshot snapshot) {
        final String profilePicUrl = snapshot.child("profile_pic").getValue(String.class);
        if (profilePicUrl != null) {
            Picasso.get().load(profilePicUrl).into(simpleImageView);
        }
    }
}

