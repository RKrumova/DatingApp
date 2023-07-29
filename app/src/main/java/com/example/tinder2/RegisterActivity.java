package com.example.tinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView loginTV = findViewById(R.id.loginTV);
        TextView toRegisterTV = findViewById(R.id.toRegisterTV);
        EditText inputUsername = findViewById(R.id.inputUsername);
        EditText inputPassword1 = findViewById(R.id.inputPassword1);
        EditText inputPassword2 = findViewById(R.id.inputPassword2);
        EditText inputEmail = findViewById(R.id.inputEmail);
        CheckBox checkBox = findViewById(R.id.checkBox);
        Button button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}