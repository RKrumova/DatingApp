package com.example.tinder2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.lang3.StringUtils;



public class LoginActivity extends AppCompatActivity {
    private EditText inputUsername;
    private EditText inputPassword;
    private Button button_Login;
    private TextView toRestartPassTV;
    private Button btnGoogle;
    private Button btnFacebook;
    private LinearLayout toRegisterLL;
    private FirebaseAuth mAuth;

    private ProgressDialog mLoadingBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        Button button_Login = findViewById(R.id.button_Login);
        TextView toRestartPassTV = findViewById(R.id.toRestartPassTV);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        Button btnFacebook = findViewById(R.id.btnFacebook);
        LinearLayout toRegisterLL = findViewById(R.id.linearLayoutRegister);
        mAuth  = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(LoginActivity.this);
        button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });

        toRegisterLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
    public String username = inputUsername.getText().toString();
    private void checkCredentials() {
        String password = inputPassword.getText().toString();
        /*if (username.isEmpty() || username.length()<5  || !StringUtils.isAlphanumeric(username)) {
            showError(inputUsername, "Your username is not valid");
        } else if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            showError(inputPassword, "Your password isn't strong enough");
        }
        else{*/
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please wait, while we check your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        System.out.println("Don't forget to change not to go to MainActivity but to chat/swipe");
                        mLoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Successfully registration", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Toast.makeText(this, "Call registration method", Toast.LENGTH_SHORT).show();
        //}

    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}