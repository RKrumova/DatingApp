package com.example.tinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
        setContentView(R.layout.activity_chat);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://dating-app-bfd70-default-rtdb.firebaseio.com/");
        FirebaseApp.initializeApp(this);

        username = getIntent().getStringExtra("username");

        log_outIV = findViewById(R.id.log_out_imageView);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter = new MessagesAdapter(messLists, ChatActivity.this);
        messagesRecyclerView.setAdapter(messagesAdapter);

        Button messagesButton = findViewById(R.id.messagesButton);
        Button profileButton = findViewById(R.id.profileButton);
        Button datesButton = findViewById(R.id.datesButton);
        Button swipesButton = findViewById(R.id.swipesButton);

        loadPicture();
        loadLikedUsers();

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
        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    if (chatSnapshot.hasChild("user_1") && chatSnapshot.hasChild("user_2") && chatSnapshot.hasChild("convo")) {
                        String getUserOne = chatSnapshot.child("user_1").getValue(String.class);
                        String getUserTwo = chatSnapshot.child("user_2").getValue(String.class);

                        if (getUserOne.equals(username) || getUserTwo.equals(username)) {
                            String otherUser = getUserOne.equals(username) ? getUserTwo : getUserOne;
                            String profilePic = snapshot.child(otherUser).child("profile_pic").getValue(String.class);

                            for (DataSnapshot messageSnapshot : chatSnapshot.child("messages").getChildren()) {
                                long messageKey = Long.parseLong(messageSnapshot.getKey());
                                long lastSeenMessage = Long.parseLong(MemoryData.getLastConvo(ChatActivity.this, chatSnapshot.getKey()));
                                String message = messageSnapshot.child("message").getValue(String.class);

                                if (messageKey > lastSeenMessage) {
                                    unseenMessages++;
                                }

                                lastMessage = message;
                            }

                            MessagesList messagesList = new MessagesList(otherUser, lastMessage, profilePic, unseenMessages, chatSnapshot.getKey());
                            messLists.add(messagesList);
                        }
                    }
                }

                messagesAdapter.updateData(messLists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
    private void loadLikedUsers() {
        DatabaseReference swipesReference = databaseReference.child("swipes").child("likes");
        swipesReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MessagesList> messLists = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    loadMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void loadPicture(){
        //getting picture from firebase
        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String profilePicUrl = snapshot.child("users").child(username).child("profile_pic").getValue(String.class);
                if (profilePicUrl != null) {
                    Picasso.get().load(profilePicUrl).into(userProfilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}