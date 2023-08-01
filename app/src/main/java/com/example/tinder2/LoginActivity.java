package com.example.tinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;



public class LoginActivity extends AppCompatActivity {
    private EditText inputUsername;
    private EditText inputPassword;
    private Button button_Login;
    private TextView toRestartPassTV;
    private Button btnGoogle;
    private Button btnFacebook;
    private LinearLayout toRegisterLL;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView loginTV = findViewById(R.id.loginTV);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        Button button_Login = findViewById(R.id.button_Login);
        TextView toRestartPassTV = findViewById(R.id.toRestartPassTV);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        Button btnFacebook = findViewById(R.id.btnFacebook);
        LinearLayout toRegisterLL = findViewById(R.id.linearLayoutRegister);

        checkCredentials();
        toRegisterLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
    private void checkCredentials() {
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();
        if (username.isEmpty() || username.length()<7  || !StringUtils.isAlphanumeric(username)) {
            showError(inputUsername, "Your username is not valid");
        } else if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            showError(inputPassword, "Your password isn't strong enough");
        }
        else{
            Toast.makeText(this, "Call registration method", Toast.LENGTH_SHORT).show();
        }

    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}