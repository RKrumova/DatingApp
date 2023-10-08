package com.example.tinder2;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.tinder2.Account.SettingActivity;
import com.example.tinder2.Auth.LoginActivity;
import com.example.tinder2.messages.MessagesAdapter;
import com.example.tinder2.messages.MessagesList;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    final private List<MessagesList> messLists = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //    https://dating-app-bfd70-default-rtdb.firebaseio.com/
    private boolean dataSet;
    private String username;
    private String convoKey;
    private int unseenMessages;
    private String lastMessage;
    private String messageKey;
    private String user2, profile2, personSending;
    private MessagesAdapter messagesAdapter;
    private RecyclerView messagesRecyclerView;


    //Log.d("", "");
    //Log.e(TAG, "I went through onCreate ");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ChatActivity", "onCreate started");
        super.onCreate(savedInstanceState);
        // Request no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set flags for full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_chat);
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Intent intent = getIntent();

        //username = intent.getStringExtra("username");
        username = "azor1d";

        CircleImageView profilePic = findViewById(R.id.userProfilePic);
        Button messagesButton = findViewById(R.id.messagesButton);
        Button profileButton = findViewById(R.id.profileButton);
        Button logOutButton = findViewById(R.id.logOutButton);
        Button swipesButton = findViewById(R.id.swipesButton);

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter = new MessagesAdapter(messLists, ChatActivity.this);
        messagesRecyclerView.setAdapter(messagesAdapter);
        messagesRecyclerView.setVisibility(View.VISIBLE);
        stuff(username);
        loadPicture(profilePic, username);
        swipesButton.setOnClickListener(view -> {
            startActivity(new Intent(ChatActivity.this, SwipeActivity.class));
        });
        profileButton.setOnClickListener(view -> {
            startActivity(new Intent(ChatActivity.this, SettingActivity.class));
        });
        logOutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intentLG = new Intent(ChatActivity.this, LoginActivity.class);
            intentLG.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intentLG.removeExtra(username);
            startActivity(intentLG);
        });
    }

    private void stuff(String username) {
        //messLists.clear();
        unseenMessages = 0;
        lastMessage = "";
        /**         GETTING DATA FOR SECOND USER **/

        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    if (chatSnapshot.hasChild("user_1") && chatSnapshot.hasChild("user_2") && chatSnapshot.hasChild("messages")) {
                        convoKey = snapshot.getKey();
                        String actialConvoKey = chatSnapshot.getKey();
                        Log.e(convoKey, convoKey);

                        String getUserOne = chatSnapshot.child("user_1").getValue(String.class);
                        String getUserTwo = chatSnapshot.child("user_2").getValue(String.class);
                        Log.d("\\ works till here Get users ", getUserOne + " \n" + getUserTwo);

                        assert getUserOne != null;
                        assert getUserTwo != null;
                        if (getUserOne.equals(username) || getUserTwo.equals(username)) {
                            String user2 = getUserOne.equals(username) ? getUserTwo : getUserOne;
                            Log.d("asdsadsad", user2);
                            profile2 = databaseReference.child("users").child(user2).child("profile_pic").toString();
                            if (profile2.isEmpty()) {
                                profile2 = "https://images.rawpixel.com/image_png_social_square/cHJpdmF0ZS9sci9pbWFnZJMvd2Vic2l0ZS8yMDIzLTAxL3JtNjA5LXNvbGlkaWNvbi13LTAwMi1wLnBuZw.png";
                            }

                            messageKey = chatSnapshot.child("messages").getChildren().toString();
                            Log.e("Message key: ", messageKey);
                            if (chatSnapshot.child("messages").hasChildren()) {
                                loadInsideChat();
                            }
                            // saving to list
                            MessagesList messagesList = new MessagesList(user2, lastMessage, 1, profile2, actialConvoKey);
                            messLists.add(messagesList);
                            messagesAdapter.updateData(messLists);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.e(TAG, error.getDetails());
            }
        });
    }

    private void loadInsideChat() {
        DatabaseReference messageReference = databaseReference.child("chat").child(convoKey).child("messages");
        messageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot messageSnapshot) {
                Log.d("\nhi:", "hi" + messageSnapshot.getChildren());
                for (DataSnapshot individialMessages : messageSnapshot.getChildren()){
                String messageText = individialMessages.child("text").getValue(String.class);
                String messageUser = messageSnapshot.child("user").getValue(String.class);
                Log.d("message content: ", messageText);
                Log.d("user: ", messageUser);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getDetails());
            }
        });
    }


    private void loadPicture(CircleImageView profilePic, String user) {


        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profilePicUrl = snapshot.child(user).child("profile_pic").getValue(String.class);
                    if (profilePicUrl != null) {
                        Picasso.get().load(profilePicUrl).into(profilePic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }

}
