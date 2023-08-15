package com.example.tinder2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinder2.Account.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;

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
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;

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
        setContentView(R.layout.activity_register);
        toLoginTV = findViewById(R.id.toLoginTV_register);
        inputUsername = findViewById(R.id.inputUsername_rg);
        inputPassword1 = findViewById(R.id.inputPassword1_rg);
        inputPassword2 = findViewById(R.id.inputPassword2);
        inputEmail = findViewById(R.id.inputEmail);
        checkBox = findViewById(R.id.checkBox);
        button_register = findViewById(R.id.button_register);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(RegisterActivity.this);
        button_register.setOnClickListener(view -> checkCredentials());
        toLoginTV.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

    }
    private void checkCredentials() {
        String email = inputEmail.getText().toString();
        String password = inputPassword1.getText().toString();
        String secondPassword = inputPassword2.getText().toString();
        String username = inputUsername.getText().toString();

        if (username.isEmpty() || username.length() < 5 || !StringUtils.isAlphanumeric(username)) {
            showError(inputUsername, "Your username is not valid");
        } else if (!isEmailValid(email)) {
            showError(inputEmail, "Email is not valid");
        } else if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            showError(inputPassword1, "Your password isn't strong enough");
        } else if (!password.equals(secondPassword)) {
            showError(inputPassword2, "Your passwords don't match");
        } else if (!checkBox.isChecked()) {
            checkBox.setTextColor(Color.RED);
        } else {
            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please wait, while we check your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println("Don't forget to change not to go to MainActivity but to chat/swipe");

                    Toast.makeText(RegisterActivity.this, "Successfully registration", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, SettingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "User registration failed.", Toast.LENGTH_SHORT).show();
                }
            });

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
