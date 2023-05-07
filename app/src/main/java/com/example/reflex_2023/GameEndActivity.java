package com.example.reflex_2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GameEndActivity extends AppCompatActivity {
    Button backToProfileButton;
    TextView outcomeTextView, pointsTextView;
    UserDAO userDao;
    PointsDAO pointsDao;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);

        if (getIntent().getIntExtra("secret", 0) != 42) {
            finish();
        }

        backToProfileButton = findViewById(R.id.backToProfileButton);
        outcomeTextView = findViewById(R.id.outcomeTextView);
        pointsTextView = findViewById(R.id.pointsTextView);
        userDao = new UserDAO();
        pointsDao = new PointsDAO();

        pointsTextView.setText(Integer.toString(getIntent().getIntExtra("points", 0)));
        outcomeTextView.setText("Your points:");

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userDao.getCurrentUser().addOnCompleteListener(new OnCompleteListener<User>() {
                @Override
                public void onComplete(@NonNull Task<User> task) {
                    if (task.isSuccessful()) {
                        User currentUser = task.getResult();
                        if (currentUser != null) {
                            Point p = new Point(FirebaseAuth.getInstance().getUid(),
                                    String.valueOf(System.currentTimeMillis()),
                                    Integer.toString(getIntent().getIntExtra("points", 0)),
                                    currentUser.getUsername());

                            pointsDao.add(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(GameEndActivity.this, "Points saved!", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                        }
                    } else {
                    }
                }
            });
        }
    }

    public void backToHome(View view) {
        Intent profileIntent = new Intent(this, MainActivity.class);
        profileIntent.putExtra("secret", 42);
        finish();
        startActivity(profileIntent);
    }

    public void backToRanklist(View view) {
        Intent profileIntent = new Intent(this, RanglistActivity.class);
        profileIntent.putExtra("secret", 42);
        finish();
        startActivity(profileIntent);
    }
}
