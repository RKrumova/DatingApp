package com.example.tinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

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

    String username = getIntent().getStringExtra("username");
    ImageView log_outIV;
    RecyclerView messagesRecyclerView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://dating-app-bfd70-default-rtdb.firebaseio.com/");
    //    https://dating-app-bfd70-default-rtdb.firebaseio.com/
    final private List<MessagesList> messLists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FirebaseApp.initializeApp(this);
        log_outIV = findViewById(R.id.log_out_imageView);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProgressDialog progressDialog =  new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
       //getting picture from firebase
        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String profilePicUrl = snapshot.child("users").child("username").child("profile_pic").getValue(String.class);
                Picasso.get().load(profilePicUrl).into(userProfilePic);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messLists.clear();
                for(DataSnapshot dataSnapshot : snapshot.child("users").getChildren()){
                    String getUsername = dataSnapshot.getKey();
                    if(!getUsername.equals(username)){ //check if current user is different from user from database
                        String getName = dataSnapshot.child("username").getValue(String.class);
                        String getProfilePic = String.valueOf(dataSnapshot.child("profile_pic"));
                        //user - last message - pic - unseen
                        MessagesList messagesList = new MessagesList(getName, "", getProfilePic, 0); //class with the extracted data (username, profile picture URL) and a default value of 0 for unseen messages
                        messLists.add(messagesList);
                    }
                }
                messagesRecyclerView.setAdapter(new MessagesAdapter(messLists, ChatActivity.this));

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