package com.example.tinder2;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tinder2.Account.MemoryData;
import com.example.tinder2.Account.SettingActivity;
import com.example.tinder2.messages.MessagesAdapter;
import com.example.tinder2.messages.MessagesList;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //    https://dating-app-bfd70-default-rtdb.firebaseio.com/
    private boolean dataSet;
    private String username;
    private String convoKey;
    final private List<MessagesList> messLists = new ArrayList<>();
    private int unseenMessages;
    private String lastMessage;
    private MessagesAdapter messagesAdapter;
    private RecyclerView messagesRecyclerView;
    private ImageView log_outIV;
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

        username = getIntent().getStringExtra("username");
        log_outIV = findViewById(R.id.log_out_imageView);
        Button messagesButton = findViewById(R.id.messagesButton);
        Button profileButton = findViewById(R.id.profileButton);
        Button datesButton = findViewById(R.id.datesButton);
        Button swipesButton = findViewById(R.id.swipesButton);

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);

        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter = new MessagesAdapter(messLists, ChatActivity.this);
        messagesRecyclerView.setAdapter(messagesAdapter);
        messagesRecyclerView.setVisibility(View.VISIBLE);
        //loadPicture(username);
        //loadLikedUsers();

        //loadPicture();
        Log.d("On create Chat Activity", "HI, On create Chat Activity");
        loadMessages();

        swipesButton.setOnClickListener(view -> {
            startActivity(new Intent(ChatActivity.this, SwipeActivity.class));
        });
        profileButton.setOnClickListener(view -> {
            Intent intent = new Intent(ChatActivity.this, SettingActivity.class);
            startActivity(intent);
        });
        log_outIV.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ChatActivity.this, LoginActivity.class));
        });
    }

    private void loadMessages() {
        messLists.clear();
        unseenMessages = 0;
        lastMessage = "";
        convoKey = "";

        DatabaseReference chatReference = databaseReference.child("chat");

        Log.d("ChatActivity", "About to fetch chats from Firebase");
        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    Log.d("\nChatActivity", "Processing chat ID: " + chatSnapshot.getKey());
                    if (chatSnapshot.hasChild("user_1") && chatSnapshot.hasChild("user_2") && chatSnapshot.hasChild("messages")) {
                        String getUserOne = chatSnapshot.child("user_1").getValue(String.class);
                        String getUserTwo = chatSnapshot.child("user_2").getValue(String.class);
                        if (getUserOne.equals(username) || getUserTwo.equals(username)) {
                            String user2 = getUserOne.equals(username) ? getUserTwo : getUserOne;
                            String profilePic = "https://cdn-icons-png.flaticon.com/512/1791/1791342.png";
                            //String profilePic = databaseReference.child(user2).child("profile_pic").getValue(String.class);
                            DatabaseReference messageReference = chatSnapshot.child("messages").getRef();
                            messageReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot messageSnapshot) {
                                    long lastSeenMessage = Long.parseLong(MemoryData.getLastConvo(ChatActivity.this, chatSnapshot.getKey()));
                                    String message = messageSnapshot.child("text").getValue(String.class);

                                    long messageKey = Long.parseLong(messageSnapshot.getKey());
                                    if (messageKey > lastSeenMessage) {
                                        unseenMessages++;
                                    }

                                    lastMessage = message;

                                    MessagesList messagesList = new MessagesList(user2, lastMessage, profilePic, unseenMessages, chatSnapshot.getKey());
                                    messLists.add(messagesList);
                                    messagesAdapter.updateData(messLists);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Log.e("FirebaseError", "Error fetching chat data: ", error.toException());
            }
        });
    }


    private void loadPicture(){

        CircleImageView profilePic = findViewById(R.id.userProfilePic);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String profilePicUrl = snapshot.child("users").child(username).child("profile_pic").getValue(String.class);
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
