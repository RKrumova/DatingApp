package com.example.tinder2;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText inputUsername;
    private EditText inputPassword;
    private Button button_Login;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        button_Login = findViewById(R.id.button_Login);
        TextView toRestartPassTV = findViewById(R.id.toRestartPassTV);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        Button btnFacebook = findViewById(R.id.btnFacebook);
        LinearLayout toRegisterLL = findViewById(R.id.linearLayoutRegister);
        mAuth  = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(LoginActivity.this);
        button_Login.setOnClickListener(view -> checkCredentials());

        toRegisterLL.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
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
            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    System.out.println("Don't forget to change not to go to MainActivity but to chat/swipe");
                    mLoadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Successfully registration", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(username, "username");
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
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