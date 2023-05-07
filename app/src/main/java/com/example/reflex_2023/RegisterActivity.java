package com.example.reflex_2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final int KEY = 42;

    private FirebaseAuth mAuth;

    EditText editTextUsername;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPasswordAgain;
    UserDAO userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        if (getIntent().getIntExtra("secret", 0) != 42) {
            finish();
        }

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordAgain = findViewById(R.id.editTextPasswordAgain);
        userDao = new UserDAO();
    }

    public void onRegisterButtonPushed(View view) {
        String userName = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordAgain = editTextPasswordAgain.getText().toString();

        if (userName.isEmpty()) {
            editTextUsername.setError("This field can't be empty!");
            editTextUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("This field can't be empty!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email address is not valid!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("This field can't be empty!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("The password has to be at least 6 character!");
            editTextPassword.requestFocus();
            return;
        }

        if (passwordAgain.isEmpty()) {
            editTextPasswordAgain.setError("This field can't be empty!");
            editTextPasswordAgain.requestFocus();
            return;
        }

        if (passwordAgain.length() < 6) {
            editTextPasswordAgain.setError("The password has to be at least 6 character!");
            editTextPasswordAgain.requestFocus();
            return;
        }

        if (!passwordAgain.equals(password)) {
            editTextPasswordAgain.setError("The passwords do not match!");
            editTextPasswordAgain.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = new User(userName, email, password);
                userDao.add(user, FirebaseAuth.getInstance().getUid()).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed to register user :(", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(RegisterActivity.this, "Failed to register user :(", Toast.LENGTH_LONG).show();
            }
        });

        redirectToLogin();
    }

    public void onCancel(View view) {
        finish();
    }

    public void redirectToPlayActivity () {
        Intent playIntent = new Intent(this, GameScreenActivity.class);

        playIntent.putExtra("secret", KEY);

        finish();

        startActivity(playIntent);
    }

    public void redirectToLogin () {
        Intent playIntent = new Intent(this, LoginActivity.class);

        playIntent.putExtra("secret", KEY);

        finish();

        startActivity(playIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent mainIntent = new Intent(this, MainActivity.class);

            finish();

            startActivity(mainIntent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}