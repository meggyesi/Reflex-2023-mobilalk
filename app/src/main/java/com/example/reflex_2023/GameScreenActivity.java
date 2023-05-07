package com.example.reflex_2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class GameScreenActivity extends AppCompatActivity {

    private static final int KEY = 42;
    int questionNum, layer;
    TextView questionNumber, time, question;
    EditText answer;
    List<String> correctAnswer;
    CountDownTimer ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        if (getIntent().getIntExtra("secret", 0) != 42) {
            finish();
        }

        Log.d("time", String.valueOf(getIntent().getIntExtra("time", 0)));

        question = findViewById(R.id.question);
        time = findViewById(R.id.time);
        questionNumber = findViewById(R.id.questionNumber);
        answer = findViewById(R.id.answer);
        questionNum = 1;
        layer = 1;

        ct = new CountDownTimer(getIntent().getIntExtra("time", 0) * 1000L, 1000) {

            public void onTick(long millisUntilFinished) {
                time.setText(String.format("%d", millisUntilFinished / 1000));
            }

            public void onFinish() {
                time.setText(R.string.done);
            }

        }.start();

        questionNumber.setText(Integer.toString(getIntent().getIntExtra("questionNum", 0)));

        switch (getIntent().getIntExtra("questionNum", 0)) {
            case 1:
                question.setText(R.string.q1);
                correctAnswer = Arrays.asList(getString(R.string.a1).split(", "));
                break;
            case 2:
                question.setText(R.string.q2);
                correctAnswer = Arrays.asList(getString(R.string.a2).split(", "));
                break;
            case 3:
                question.setText(R.string.q3);
                correctAnswer = Arrays.asList(getString(R.string.a3).split(", "));
                break;
            case 4:
                Intent endIntent = new Intent(this, GameEndActivity.class);
                endIntent.putExtra("secret", KEY);
                endIntent.putExtra("points", getIntent().getIntExtra("time", 0));
                finish();
                startActivity(endIntent);
        }

        answer.setOnKeyListener((view, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                Log.d("keyboard", "enter_key_called");
                Log.d("time", time.getText().toString());

                finish();
                getIntent().putExtra("questionNum", getIntent().getIntExtra("questionNum", 0) + 1);
                getIntent().putExtra("time", Integer.parseInt(time.getText().toString()));
                startActivity(getIntent());
            }

            return false;
        });
    }
}