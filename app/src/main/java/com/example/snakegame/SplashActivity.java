package com.example.snakegame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    TextView name, loadingText;

    Handler handler = new Handler();
    int dotCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        applyTheme();

        //  Views
        logo = findViewById(R.id.logo);
        name = findViewById(R.id.gameName);
        loadingText = findViewById(R.id.loadingText);

        //  Fade Animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);
        name.startAnimation(fadeIn);

        //  Loading Text Animation (Loading..., Loading..)
        startLoadingAnimation();

        //  Move to MainActivity after 3 sec
        handler.postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 3000);
    }

    //  Loading dots animation
    private void startLoadingAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String dots = "";
                for (int i = 0; i < dotCount; i++) {
                    dots += ".";
                }

                loadingText.setText("Loading" + dots);

                dotCount++;
                if (dotCount > 3) dotCount = 0;

                handler.postDelayed(this, 400);
            }
        }, 400);
    }

    //  Theme Apply
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