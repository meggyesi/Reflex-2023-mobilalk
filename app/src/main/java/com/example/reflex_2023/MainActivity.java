package com.example.reflex_2023;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final int KEY = 42;
    FirebaseAuth mAuth;
    TextView logInfoTextView;
    LinearLayout linearLayout;
    Button registerButton;
    Button loginButton;
    Button logoutButton;
    Button playButton;
    UserDAO userDao;
    FirebaseUser user;
    Animation zoomout, zoomin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mAuth = FirebaseAuth.getInstance();
        logInfoTextView = findViewById(R.id.logInfoTextView);
        linearLayout = findViewById(R.id.linearLayout);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
        logoutButton = findViewById(R.id.logoutButton);
        playButton = findViewById(R.id.playButton);

        logoutButton.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);

        userDao = new UserDAO();

        user = mAuth.getCurrentUser();

        if (user != null) {
            logInfoTextView.setText(user.getEmail());
            registerButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.VISIBLE);
        }

        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);

        zoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playButton.startAnimation(zoomout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        zoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playButton.startAnimation(zoomin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        playButton.startAnimation(zoomin);

    }


    public void onLoginButtonPushed(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);

        loginIntent.putExtra("secret", KEY);
        startActivity(loginIntent);
    }

    public void onRegisterButtonPushed(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerIntent.putExtra("secret", KEY);
        startActivity(registerIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
            System.exit(0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onLogoutButtonPushed(View view) {
        mAuth.signOut();

        finish();
        startActivity(getIntent());
    }

    public void onPlayButtonPushed(View view) {
        Intent gameScreenIntent = new Intent(this, GameScreenActivity.class);
        gameScreenIntent.putExtra("secret", KEY);
        gameScreenIntent.putExtra("questionNum", 1);
        gameScreenIntent.putExtra("time", 120);
        finish();
        startActivity(gameScreenIntent);
    }

    @Override
    protected void onPause() {
        playButton.clearAnimation();
        super.onPause();
    }

    @Override
    protected void onResume() {
        playButton.startAnimation(zoomin);
        super.onResume();
    }
}


