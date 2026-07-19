package com.example.snakegame;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnSettings, btnInstructions, btnExit;
    TextView highScoreText;
    ImageView snakeMascot;

    SharedPreferences preferences;
    int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applyTheme();

        // Initialize Views
        btnStart = findViewById(R.id.btnStart);
        btnSettings = findViewById(R.id.btnSettings);
        btnInstructions = findViewById(R.id.btnInstructions);
        btnExit = findViewById(R.id.btnExit);
        highScoreText = findViewById(R.id.highScore);
        snakeMascot = findViewById(R.id.snakeMascot);

        // Load High Score
        preferences = getSharedPreferences("SnakeGame", MODE_PRIVATE);
        highScore = preferences.getInt("HIGH_SCORE", 0);
        highScoreText.setText("High Score: " + highScore);

        // Apply Animation to Snake
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        snakeMascot.startAnimation(bounce);

        // Start Game Button
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        // Settings Button

        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        // Instructions Button
        btnInstructions.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, InstructionsActivity.class));
        });

        // Exit Button
        btnExit.setOnClickListener(v -> {
            showExitDialog();
        });
    }


    //  Exit Dialog
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Game");
        builder.setMessage("Are you sure you want to exit?");

        builder.setPositiveButton("Yes", (dialog, which) -> finish());
        builder.setNegativeButton("No", null);

        builder.show();
    }

    //  Reset High Score
    private void resetHighScore() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("HIGH_SCORE", 0);
        editor.apply();

        highScore = 0;
        highScoreText.setText("High Score: 0");
    }
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