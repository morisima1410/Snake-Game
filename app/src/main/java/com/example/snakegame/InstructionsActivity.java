package com.example.snakegame;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InstructionsActivity extends AppCompatActivity {

    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        applyTheme();

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
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