package com.example.tinder2.Plans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.tinder2.R;
import com.google.firebase.database.DatabaseReference;

public class PlanActivity extends AppCompatActivity {
    DatabaseReference databaseReference;

    private Button rescheduleButton = findViewById(R.id.rescheduleButton);
    private Button cancelButton = findViewById(R.id.CancelButton);
    private Button backButton = findViewById(R.id.BackButton);
    private Button messageButton = findViewById(R.id.messageButton);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        // Request no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set flags for full screen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        rescheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sends a message {Are we still going out}
                //Option 1: A pop up message with yes/no
                //Option 2: Redirect to convo
                //?

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlanActivity.this, CalendarActivity.class));
            }
        });
    }
}