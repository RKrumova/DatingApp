package com.example.tinder2.conversation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.tinder2.Account.MemoryData;
import com.example.tinder2.Auth.LoginActivity;
import com.example.tinder2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ConversationActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private final List<ConversationList> convoLists = new ArrayList<>();
    private ConversationAdapter convoAdapter;
    private RecyclerView convoRecyclerView;
    private boolean loadingFirstTme = true;
    // get data from convo adapter
    private String getName;
    private String getProfilePic;
    private String convoKey;

    private ImageView backButton;
    private CircleImageView profilePic;
    private TextView nameTextView;
    private CircleImageView sendButton;
    private EditText messageET;
    String getUserName;
    String getUser2;

    SharedPreferences sharedPref;
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
        setContentView(R.layout.activity_conversation);
        sharedPref = getSharedPreferences("myTinder2Prefs", Context.MODE_PRIVATE);
        getUserName = sharedPref.getString("loggedUser", "");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        backButton = findViewById(R.id.backButton);
        profilePic = findViewById(R.id.profilePic);
        nameTextView = findViewById(R.id.name);
        sendButton = findViewById(R.id.sendMessageButton);
        messageET = findViewById(R.id.messageET);
        convoRecyclerView = findViewById(R.id.convoRecyclerView);

        // get data from convo adapter
        getName = getIntent().getStringExtra("username");
        getProfilePic = getIntent().getStringExtra("profile_pic");
        convoKey = getIntent().getStringExtra("convo_key");
        getUser2 = getIntent().getStringExtra("user2");
        nameTextView.setText(getName);
        Picasso.get().load(getProfilePic).into(profilePic);


        convoAdapter = new ConversationAdapter(convoLists, ConversationActivity.this, getUserName);
        convoRecyclerView.setHasFixedSize(true);
        convoRecyclerView.setLayoutManager(new LinearLayoutManager(ConversationActivity.this));
        convoRecyclerView.setAdapter(convoAdapter);
        convoRecyclerView.setVisibility(View.VISIBLE);
        if(!convoKey.isEmpty()){
            populateConvoList();
        }
        sendButton.setOnClickListener(view -> {
            sendMessege();
            populateConvoList();
        });


        backButton.setOnClickListener(view -> finish());
    }

    private void sendMessege()
    {
        String getTxtMessage = String.valueOf(messageET.getText());
        String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
        MemoryData.saveConversationLast(currentTimestamp, convoKey, ConversationActivity.this);

        if (getName != null && !getTxtMessage.isEmpty()) {
            DatabaseReference chatReference = databaseReference.child("chat").child(convoKey).child("messages");
            Log.e("Database reference in send", chatReference.toString());

            chatReference.child(currentTimestamp);
            chatReference.child(currentTimestamp).child("user");
            chatReference.child(currentTimestamp).child("text");
            chatReference.child(currentTimestamp).child("user").setValue(getUserName);
            chatReference.child(currentTimestamp).child("text").setValue(getTxtMessage);
            messageET.setText(""); // clear edit text
        }
    }
    private void populateConvoList() {

        /** populating the convoLists with conversation data from Firebase Realtime Database and
         * ensuring that new messages are added to the list and displayed in the RecyclerView.
         * It calculates a unique convoKey if it's initially empty and keeps track of the last
         * received timestamp to prevent reloading the same messages.
         * */
        //chat-convo-message
        final long[] totalNumber_messages = {0};
        DatabaseReference chatReference = databaseReference.child("chat").child(convoKey);
        chatReference.child("messages").addValueEventListener(new ValueEventListener() {
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    totalNumber_messages[0] = snapshot.getChildrenCount();
                    Log.d("Total childs", String.valueOf(totalNumber_messages[0]));
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        String messageTimestamps = messageSnapshot.getKey();
                        Log.e("Messages :", messageTimestamps);
                        if (messageSnapshot.hasChild("text") && messageSnapshot.hasChild("user")) {
                            String getUser = messageSnapshot.child("user").getValue(String.class);
                            String getMessage = messageSnapshot.child("text").getValue(String.class);
                            Timestamp timestamp = new Timestamp(Long.parseLong(Objects.requireNonNull(messageTimestamps)));
                            Date date = new Date(timestamp.getTime());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                            ConversationList convoList = new ConversationList(getUser, getMessage, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                            convoLists.add(convoList);
                            if (loadingFirstTme || Long.parseLong(messageTimestamps) > Long.parseLong(MemoryData.getLastConvo(ConversationActivity.this, convoKey))) {
                                loadingFirstTme = false;
                                MemoryData.saveConversationLast(messageTimestamps, convoKey, ConversationActivity.this);
                                convoAdapter.updateConvoList(convoLists);
                                convoRecyclerView.scrollToPosition(convoLists.size() - 1);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}