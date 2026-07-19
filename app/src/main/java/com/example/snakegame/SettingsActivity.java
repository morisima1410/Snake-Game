package com.example.snakegame;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Switch switchSound, switchMusic, switchDark;
    RadioGroup difficultyGroup;
    Button btnBack;

    SharedPreferences preferences;

    View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        applyTheme();

        rootLayout = findViewById(R.id.main);

        switchSound = findViewById(R.id.switchSound);
        switchMusic = findViewById(R.id.switchMusic);
        switchDark = findViewById(R.id.switchDark);
        difficultyGroup = findViewById(R.id.difficultyGroup);
        btnBack = findViewById(R.id.btnBack);

        preferences = getSharedPreferences("Settings", MODE_PRIVATE);

        loadSettings();
        applyTheme(); //  APPLY THEME ON START

        //  Sound
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("sound", isChecked).apply();
        });

        //  Music
        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("music", isChecked).apply();
        });

        //  Dark Mode (LIVE CHANGE)
        switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("dark", isChecked).apply();
            applyTheme(); //  instantly change UI
        });

        //  Difficulty
        difficultyGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String level = "MEDIUM";

            if (checkedId == R.id.radioEasy) level = "EASY";
            else if (checkedId == R.id.radioHard) level = "HARD";

            preferences.edit().putString("difficulty", level).apply();
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadSettings() {

        switchSound.setChecked(preferences.getBoolean("sound", true));
        switchMusic.setChecked(preferences.getBoolean("music", true));
        switchDark.setChecked(preferences.getBoolean("dark", true));

        String difficulty = preferences.getString("difficulty", "MEDIUM");

        if (difficulty.equals("EASY")) {
            ((RadioButton)findViewById(R.id.radioEasy)).setChecked(true);
        } else if (difficulty.equals("HARD")) {
            ((RadioButton)findViewById(R.id.radioHard)).setChecked(true);
        } else {
            ((RadioButton)findViewById(R.id.radioMedium)).setChecked(true);
        }
    }

    //  APPLY THEME
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