package com.example.tinder2;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private FirebaseDatabaseHelper firebaseDatabaseHelper;
    private FirebaseAuth mAuth;

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
        firebaseDatabaseHelper = new FirebaseDatabaseHelper(this);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });
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

        if (username.isEmpty() || username.length() < 7 || !StringUtils.isAlphanumeric(username)) {
            showError(inputUsername, "Your username is not valid");
        } else if (!isEmailValid(email)) {
            showError(inputEmail, "Email is not valid");
        } else if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            showError(inputPassword1, "Your password isn't strong enough");
        } else if (!password.equals(secondPassword) || secondPassword.isEmpty()) {
            showError(inputPassword2, "Your passwords don't match");
        } else if (!checkBox.isChecked()) {
            checkBox.setTextColor(Color.RED);
        } else {
            // Call registration method
            firebaseDatabaseHelper = new FirebaseDatabaseHelper(this);
            firebaseDatabaseHelper.registerUser(username, password, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        //can get UI to navigate to chat or swipe activity
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        System.out.println("Don't forget to change not to go to MainActivity but to chat/swipe");
                        finish();
                    } else {
                            Toast.makeText(RegisterActivity.this, "User registration failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Toast.makeText(this, "" +
                    "Call registration method", Toast.LENGTH_SHORT).show();
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
