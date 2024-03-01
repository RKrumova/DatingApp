package com.example.tinder2;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.tinder2.Auth.LoginActivity.username;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinder2.Account.MemoryData;
import com.example.tinder2.Account.SettingActivity;
import com.example.tinder2.Auth.LoginActivity;
import com.example.tinder2.Plans.CardActivity;
import com.google.firebase.auth.FirebaseAuth;
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
    private String user2 = "";

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
        //username = getIntent().getStringExtra("username");
        simpleImageView = findViewById(R.id.swipeImageView);
        newUserTextView = findViewById(R.id.newUserTextView);
        dislikeButton = findViewById(R.id.dislikeButton);
        likeButton = findViewById(R.id.likeButton);
        askButton = findViewById(R.id.askButton);
        Button messagesButton = findViewById(R.id.messagesButton);
        Button profileButton = findViewById(R.id.profileButton);
        Button logOutButton = findViewById(R.id.logOutButton);
        Button swipesButton = findViewById(R.id.swipesButton);
        user2 = "check22";
        loadingUserData(user2);
        Intent intent = getIntent();
        intent.putExtra("username",username);


        // -------- SWIPE BUTTONS -------------------
        dislikeButton.setOnClickListener(v -> {
            swipesReference = databaseReference.child("swipes").child("dislikes"); // reference to the 'swipes' branch
            //setSwipe(username, user2);
            //then load new user
            user2 = "test123";
            loadingUserData(user2);
        });
        likeButton.setOnClickListener(v -> {
            swipesReference = databaseReference.child("swipes").child("likes"); // reference to the 'swipes' branch
            setSwipe(username, user2, "");
            user2 = "test123";
            loadingUserData(user2);
            Toast.makeText(SwipeActivity.this, "Successfully liked", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(SwipeActivity.this, ChatActivity.class));
        });
        askButton.setOnClickListener(v -> {
            swipesReference = databaseReference.child("swipes").child("likes");
            askOut(v,username, user2);
            //setSwipe(username, user2, "Do you want to go out?");
            //user2 = "azor1d";
            //loadingUserData(user2);
            //Toast.makeText(SwipeActivity.this, "Liked and asked out uD83D\\uDC97", Toast.LENGTH_SHORT).show();
        });
        // ----------BOTTOM NAV PANEL
        messagesButton.setOnClickListener(v->{
            startActivity(new Intent(SwipeActivity.this, ChatActivity.class));
        });
        profileButton.setOnClickListener(v->{
            startActivity(new Intent(SwipeActivity.this, SettingActivity.class));
        });
        logOutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intentLG = new Intent(SwipeActivity.this, LoginActivity.class);
            intentLG.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intentLG.removeExtra(username);
            startActivity(intentLG);
        });
    }

    private void askOut(View view, String username, String user2) {
        CardActivity   cardActivity = new CardActivity();
        cardActivity.somethingHappening(view, username, user2);
    }

    private void createEmtyConvo(String user1, String user2,String swipeKey, String swipeVal) {

        swipesReference.child("likes").child(swipeKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean convoExist = false;
                for (DataSnapshot swipeSnapshot : snapshot.getChildren()) {
                    if (swipeSnapshot.hasChild("user_1") && swipeSnapshot.hasChild("user_2")) {
                        String getUserOne = swipeSnapshot.child("user_1").getValue(String.class);
                        String getUserTwo = swipeSnapshot.child("user_2").getValue(String.class);
                        //checks if already exist convo
                        if ((getUserOne.equals(user1) && getUserTwo.equals(user2)) || (getUserOne.equals(user2) && getUserTwo.equals(user1))) {
                            convoExist = true;
                            break;
                        }
                    }
                }
                /** if the convo doesnt exist, create new one*/

                if(!convoExist){
                    String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                    DatabaseReference chatReference = databaseReference.child("chat").push(); //chat-convoKey
                    String convoKey = chatReference.getKey();
                    //set users
                    chatReference.child("user_1").setValue(user1);
                    chatReference.child("user_2").setValue(user2);
                    DatabaseReference messReference = chatReference.child("messages").child(currentTimestamp);
                            //branch chat - id - user1, user2, message {ids - current time
                    if(!swipeVal.isEmpty()){
                        // chat - convoKey - messages - messageKey
                        messReference.child("user").setValue(username);
                        messReference.child("text").setValue(swipeVal);
                        MemoryData.saveConversationLast(currentTimestamp, convoKey, SwipeActivity.this);
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getDetails());
            }
        });
    }

    private void setSwipe(String user_1, String user_2, String swipeVal){
            String swipeKey = swipesReference.push().getKey(); // generate a unique key for the swipe
            if (swipeKey != null) {
                // Create a new swipe data
                SwipeEntry swipeEntry = new SwipeEntry(user_1, user_2);
                swipesReference.child(swipeKey).setValue(swipeEntry)
                        .addOnSuccessListener(aVoid -> {
                            createEmtyConvo(user_1, user_2, swipeKey, swipeVal);

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

