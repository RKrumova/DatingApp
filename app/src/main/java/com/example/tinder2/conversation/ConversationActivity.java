package com.example.tinder2.conversation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tinder2.Account.MemoryData;
import com.example.tinder2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://dating-app-bfd70-default-rtdb.firebaseio.com/");
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
    private Button sendButton;
    private EditText messageET;
    String getUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        convoAdapter = new ConversationAdapter(convoLists, ConversationActivity.this);
        // Request no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set flags for full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_conversation);
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

        getUserName = MemoryData.getData(ConversationActivity.this);
        nameTextView.setText(getName);
        Picasso.get().load(getProfilePic).into(profilePic);

        convoRecyclerView.setHasFixedSize(true);
        convoRecyclerView.setLayoutManager(new LinearLayoutManager(ConversationActivity.this));
        convoRecyclerView.setAdapter(convoAdapter);

        if(convoKey.isEmpty()) {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    convoKey = "1";
                    if (snapshot.hasChild("chat")) {
                        convoKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                    if (snapshot.hasChild("chat")) {
                        if (snapshot.child("chat").child(convoKey).hasChild("message")) {
                            convoLists.clear();
                            for (DataSnapshot messageSnapshot : snapshot.child("chat").getChildren()) {
                                if (messageSnapshot.hasChild("message") && messageSnapshot.hasChild("username")) {
                                    String messageTimestamps = messageSnapshot.getKey();
                                    String getUser = messageSnapshot.child("username").getValue(String.class);
                                    String getMessage = messageSnapshot.child("message").getValue(String.class);

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

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }


        sendButton.setOnClickListener(view -> {

            String getTxtMessage = String.valueOf(messageET.getText());
            String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
            MemoryData.saveConversationLast(currentTimestamp, convoKey, ConversationActivity.this);

            if (getName != null) {
                databaseReference.child("chat").child(convoKey).child("user_1").setValue(getName);
                databaseReference.child("chat").child(convoKey).child("user_2").setValue(getName);
                databaseReference.child("chat").child(convoKey).child("user_1").child(getName);
                databaseReference.child("chat").child(convoKey).child("user_2").child(getName);
                databaseReference.child("chat").child(convoKey).child(currentTimestamp).child("message").setValue(getTxtMessage);
            }
            messageET.setText(""); // clear edit text
        });


        backButton.setOnClickListener(view -> finish());
    }

}