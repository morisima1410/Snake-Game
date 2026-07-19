package com.example.snakegame;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    TextView txtScore, txtLevel, txtHighScore;
    ImageButton btnPause;
    Button btnUp, btnDown, btnLeft, btnRight, btnRestart;

    RelativeLayout pauseLayout;
    Button btnResume, btnRestartPause, btnExitPause;

    GameView gameView;

    int score = 0, level = 1, highScore = 0;
    boolean isPaused = false;

    SharedPreferences preferences;
    SharedPreferences settings;

    Handler handler = new Handler();
    int delay = 300;

    public MediaPlayer bgMusic;

    boolean isMusicOn = true;
    boolean isSoundOn = true;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isPaused) {
                gameView.moveSnake();
            }
            handler.postDelayed(this, delay);
        }
    };

    float startX, startY, endX, endY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        txtScore = findViewById(R.id.txtScore);
        txtLevel = findViewById(R.id.txtLevel);
        txtHighScore = findViewById(R.id.txtHighScore);
        btnPause = findViewById(R.id.btnPause);

        btnUp = findViewById(R.id.btnUp);
        btnDown = findViewById(R.id.btnDown);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnRestart = findViewById(R.id.btnRestart);

        pauseLayout = findViewById(R.id.pauseLayout);
        btnResume = findViewById(R.id.btnResume);
        btnRestartPause = findViewById(R.id.btnRestartPause);
        btnExitPause = findViewById(R.id.btnExitPause);

        gameView = findViewById(R.id.gameView);
        gameView.setGameActivity(this);

        preferences = getSharedPreferences("SnakeGame", MODE_PRIVATE);
        settings = getSharedPreferences("Settings", MODE_PRIVATE);

        highScore = preferences.getInt("HIGH_SCORE", 0);

        //  APPLY SETTINGS
        applySettings();

        updateUI();
        handler.post(runnable);

        //  START MUSIC IF ENABLED
        if (isMusicOn) {
            bgMusic = MediaPlayer.create(this, R.raw.bg_music);
            bgMusic.setLooping(true);
            bgMusic.start();
        }

        //  Pause
        btnPause.setOnClickListener(v -> {
            isPaused = true;
            pauseLayout.setVisibility(View.VISIBLE);

            if (bgMusic != null && bgMusic.isPlaying()) {
                bgMusic.pause();
            }
        });

        // Resume
        btnResume.setOnClickListener(v -> {
            isPaused = false;
            pauseLayout.setVisibility(View.GONE);

            if (bgMusic != null && isMusicOn) {
                bgMusic.start();
            }
        });

        // Restart (Pause)
        btnRestartPause.setOnClickListener(v -> {
            restartGame();
            pauseLayout.setVisibility(View.GONE);
        });

        //  Exit
        btnExitPause.setOnClickListener(v -> finish());

        //  Controls
        btnUp.setOnClickListener(v -> gameView.setDirection("UP"));
        btnDown.setOnClickListener(v -> gameView.setDirection("DOWN"));
        btnLeft.setOnClickListener(v -> gameView.setDirection("LEFT"));
        btnRight.setOnClickListener(v -> gameView.setDirection("RIGHT"));

        btnRestart.setOnClickListener(v -> restartGame());
    }

    //  APPLY SETTINGS
    private void applySettings() {

        isMusicOn = settings.getBoolean("music", true);
        isSoundOn = settings.getBoolean("sound", true);

        String difficulty = settings.getString("difficulty", "MEDIUM");

        if (difficulty.equals("EASY")) delay = 400;
        else if (difficulty.equals("HARD")) delay = 150;
        else delay = 250;
    }

    // Restart Game
    private void restartGame() {
        score = 0;
        level = 1;
        gameView.resetGame();
        updateUI();
        isPaused = false;
    }

    //  FOOD EATEN
    public void onFoodEaten() {
        score += 10;

        if (score % 50 == 0) level++;

        //  HIGH SCORE
        if (score > highScore) {
            highScore = score;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("HIGH_SCORE", highScore);
            editor.apply();

            // 🎉 WIN SOUND (only if sound ON)
            if (isSoundOn) {
                MediaPlayer mp = MediaPlayer.create(this, R.raw.win);
                mp.start();
                mp.setOnCompletionListener(mediaPlayer -> mediaPlayer.release());
            }
        }

        updateUI();
    }

    public void updateUI() {
        txtScore.setText("Score: " + score);
        txtLevel.setText("Level: " + level);
        txtHighScore.setText("High: " + highScore);
    }

    //  Swipe Controls
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();

                float dx = endX - startX;
                float dy = endY - startY;

                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0) gameView.setDirection("RIGHT");
                    else gameView.setDirection("LEFT");
                } else {
                    if (dy > 0) gameView.setDirection("DOWN");
                    else gameView.setDirection("UP");
                }
                break;
        }

        return true;
    }

    //  Lifecycle
    @Override
    protected void onPause() {
        super.onPause();
        if (bgMusic != null && bgMusic.isPlaying()) {
            bgMusic.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bgMusic != null && isMusicOn && !isPaused) {
            bgMusic.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(runnable);

        if (bgMusic != null) {
            bgMusic.release();
            bgMusic = null;
        }
    }
}