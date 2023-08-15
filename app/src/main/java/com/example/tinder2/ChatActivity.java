package com.example.tinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.tinder2.Account.MemoryData;
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
    private final String username = getIntent().getStringExtra("username");
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
        setContentView(R.layout.activity_chat);
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://dating-app-bfd70-default-rtdb.firebaseio.com/");
        FirebaseApp.initializeApp(this);
        log_outIV = findViewById(R.id.log_out_imageView);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter = new MessagesAdapter(messLists, ChatActivity.this);
        messagesRecyclerView.setAdapter(messagesAdapter);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        //getting picture from firebase
        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String profilePicUrl = snapshot.child("users").child("username").child("profile_pic").getValue(String.class);
                if (profilePicUrl != null) {
                    Picasso.get().load(profilePicUrl).into(userProfilePic);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
        // to complex
        /**In the loop inside databaseReference.addValueEventListener(),
         * the logic is a bit complex.  Consider breaking it down into
         * smaller methods for better readability and maintainability.
         * The code for loading and handling messages could potentially be separated into its own method, as it contains a lot of nested logic.
         * MessLists could be renamed to MessageList for consistency.
         * */
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messLists.clear();
                unseenMessages = 0;
                lastMessage = "";
                convoKey = "";
                dataSet = false;
                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {
                    String getUsername = dataSnapshot.getKey();
                    if (!Objects.requireNonNull(getUsername).equals(username)) { //check if current user is different from user from database
                        String getName = dataSnapshot.child("username").getValue(String.class);
                        String getProfilePic = String.valueOf(dataSnapshot.child("profile_pic"));

                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int getChatCount = (int) snapshot.getChildrenCount();
                                if (getChatCount > 0) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                                        final String getKey = dataSnapshot1.getKey();
                                        convoKey = getKey;
                                        if (dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("convo")) {
                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);
                                            if (Objects.requireNonNull(getUserOne).equals(getUsername) && Objects.requireNonNull(getUserTwo).equals(username)
                                                    || getUserOne.equals(username) && Objects.requireNonNull(getUserTwo).equals(getUsername)) {
                                                for (DataSnapshot convoDataSnapshot : dataSnapshot1.child("messages").getChildren()) {
                                                    final long getMessageKey = Long.parseLong(Objects.requireNonNull(convoDataSnapshot.getKey()));
                                                    final long getLastSeenMessage = Long.parseLong(MemoryData.getLastConvo(ChatActivity.this, getKey));
                                                    lastMessage = convoDataSnapshot.child("message").getValue(String.class);
                                                    if (getMessageKey > getLastSeenMessage) {
                                                        unseenMessages++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if(!dataSet){
                                    dataSet = true;
                                    MessagesList messagesList = new MessagesList(getName, lastMessage, getProfilePic, unseenMessages, convoKey); //class with the extracted data (username, profile picture URL) and a default value of 0 for unseen messages
                                    messLists.add(messagesList);
                                    messagesAdapter.updateData(messLists);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        log_outIV.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ChatActivity.this, LoginActivity.class));
            /*moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);*/
        });
    }
}