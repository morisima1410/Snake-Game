package com.example.snakegame;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class GameOverActivity extends AppCompatActivity {

    TextView txtFinalScore, txtHighScore;
    Button btnPlayAgain, btnHome;
    ImageView trophy;


    int score, highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        applyTheme();

        //  Views
        txtFinalScore = findViewById(R.id.txtFinalScore);
        txtHighScore = findViewById(R.id.txtHighScoreOver);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnHome = findViewById(R.id.btnHome);
        trophy = findViewById(R.id.trophy);


        score = getIntent().getIntExtra("score", 0);

        SharedPreferences preferences = getSharedPreferences("SnakeGame", MODE_PRIVATE);
        highScore = preferences.getInt("HIGH_SCORE", 0);

        //  Animated Score Counter
        animateScore(txtFinalScore, score);

        txtHighScore.setText("High Score : " + highScore);

        //  Trophy show if high score


        //  Play Again
        btnPlayAgain.setOnClickListener(v -> {
            startActivity(new Intent(this, GameActivity.class));
            finish();
        });

        //  Home
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    //  Score Animation
    private void animateScore(TextView textView, int finalScore) {
        ValueAnimator animator = ValueAnimator.ofInt(0, finalScore);
        animator.setDuration(1000);

        animator.addUpdateListener(animation -> {
            textView.setText("Score : " + animation.getAnimatedValue());
        });

        animator.start();
    }

    //  Confetti Animation


    //  Theme
    private void applyTheme() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark", true);

        if (isDark) {
            findViewById(android.R.id.content)
                    .setBackgroundResource(R.drawable.bg_gradient);
        } else {
            findViewById(android.R.id.content)
                    .setBackgroundColor(Color.parseColor("#E0F7FA"));
        }
    }
}