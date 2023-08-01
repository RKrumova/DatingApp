package com.example.tinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    TextView toLoginTV;
    EditText inputUsername;
    EditText inputPassword1;
    EditText inputPassword2;
    EditText inputEmail;
    CheckBox checkBox;
    Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toLoginTV = findViewById(R.id.toLoginTV_register);
        inputUsername = findViewById(R.id.inputUsername_rg);
        inputPassword1 = findViewById(R.id.inputPassword1_rg);
        inputPassword2 = findViewById(R.id.inputPassword2);
        inputEmail = findViewById(R.id.inputEmail);
        checkBox = findViewById(R.id.checkBox);
        button_register = findViewById(R.id.button_register);


        button_register.setOnClickListener((view -> checkCredentials()));
        toLoginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void checkCredentials() {
        String username = inputUsername.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword1.getText().toString();
        String secondPassword = inputPassword2.getText().toString();
        if (username.isEmpty() || username.length()<7 || !StringUtils.isAlphanumeric(username)) {
            showError(inputUsername, "Your username is not valid");
        } else if (!isEmailValid(email)) {
            showError(inputEmail, "Email is not valid");
        } else if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            showError(inputPassword1, "Your password isn't strong enough");
        } else if (!password.equals(secondPassword) || secondPassword.isEmpty()) {
            showError(inputPassword2, "Your passwords don't match");
        } else if(!checkBox.isChecked()){
            checkBox.setTextColor(Color.RED);
        }
        else{
            Toast.makeText(this, "Call registration method", Toast.LENGTH_SHORT).show();
        }

    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    public static boolean isEmailValid(String emailAddress) {
        return Pattern.compile("^(.+)@(\\S+)$")
                .matcher(emailAddress)
                .matches();
    }

}