package com.example.tinder2.conversation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tinder2.Account.MemoryData;
import com.example.tinder2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;


public class ConversationActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://dating-app-bfd70-default-rtdb.firebaseio.com/");
    String convoKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        final ImageView backButton = findViewById(R.id.backButton);
        final CircleImageView profilePic = findViewById(R.id.profilePic);
        final TextView nameTextView = findViewById(R.id.name);
        final Button sendButton = findViewById(R.id.sendMessageButton);
        EditText messageET = findViewById(R.id.messageET);
        // get data from convo adapter
        final String getName = getIntent().getStringExtra("username");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        convoKey = getIntent().getStringExtra("convo_key");

        nameTextView.setText(getName);
        Picasso.get().load(getProfilePic).into(profilePic);

        assert convoKey != null;
        if(convoKey.isEmpty()){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    convoKey = "1";
                    if (snapshot.hasChild("conversation")) {
                        convoKey = String.valueOf(snapshot.child("conversation").getChildrenCount() + 1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
        sendButton.setOnClickListener(view -> {
            final String getTxtMessage = String.valueOf(messageET.getText());
            final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
            MemoryData.saveConversationLast(currentTimestamp, convoKey, ConversationActivity.this);
            if (getName != null) {
                databaseReference.child("conversation").child(convoKey).child("user_1").child(getName);

                databaseReference.child("conversation").child(convoKey).child("user_2").child(getName);
                databaseReference.child("conversation").child(convoKey).child("conversation").child(currentTimestamp).child("convo").setValue(getTxtMessage);
            }
        });

        backButton.setOnClickListener(view -> finish());
    }
}