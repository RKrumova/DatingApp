package com.example.tinder2.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tinder2.R;
import com.example.tinder2.SwipeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText inputemail;
    private EditText inputPassword;
    private Button button_Login;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;
    public String username;
    private String email;
    private String password;
    private GoogleAuthHelper googleAuthHelper;
    private FacebookAuthHelper facebookAuthHelper;


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

        setContentView(R.layout.activity_login);
        inputemail = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        button_Login = findViewById(R.id.button_Login);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        Button btnFacebook = findViewById(R.id.btnFacebook);
        LinearLayout toRegisterLL = findViewById(R.id.linearLayoutRegister);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(LoginActivity.this);
        button_Login.setOnClickListener(view -> checkCredentials());
        btnGoogle.setOnClickListener(view -> performGoogleSignIn());
        btnFacebook.setOnClickListener(view -> performFacebookSignIn());
        toRegisterLL.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void checkCredentials() {
        password = inputPassword.getText().toString();
        email = inputemail.getText().toString();
        if (email.isEmpty()) {
            showError(inputemail, "Your email is missing");
        } else if (password.isEmpty()) {
            showError(inputPassword, "Your password is missing");
        } else {
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please wait, while we check your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                username= extractUsernameFromEmail(email);
                if (task.isSuccessful()) {
                    mLoadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, SwipeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    mLoadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                }
            });
            Toast.makeText(this, "Call login method", Toast.LENGTH_SHORT).show();

        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    private void performGoogleSignIn() {
        //nothing for now
    }

    private void performFacebookSignIn() {
        //nothing for now
    }

    private String extractUsernameFromEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length > 0) {
            // Remove special symbols from the username
            String username = parts[0].replaceAll("[^a-zA-Z0-9]", "");
            return username;
        }
        return "";
    }

}

