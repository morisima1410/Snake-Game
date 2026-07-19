package com.example.snakegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    Paint paint = new Paint();
    int cols = 20, rows = 20, cellSize;

    ArrayList<int[]> snake = new ArrayList<>();
    int[] food = new int[2];

    String direction = "RIGHT";
    String theme = "NEON";

    Random random = new Random();
    GameActivity gameActivity;

    boolean isBonusFood = false; // ⭐ BONUS FOOD FLAG

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        resetGame();
    }

    public void setGameActivity(GameActivity activity) {
        this.gameActivity = activity;
    }

    public void setTheme(String themeName) {
        this.theme = themeName;
        invalidate();
    }

    public void resetGame() {
        snake.clear();
        snake.add(new int[]{5, 5});
        snake.add(new int[]{4, 5});
        direction = "RIGHT";
        spawnFood();
        invalidate();
    }

    private void spawnFood() {
        food[0] = random.nextInt(cols);
        food[1] = random.nextInt(rows);

        // ⭐ 20% chance bonus food
        isBonusFood = random.nextInt(100) < 20;
    }

    public void setDirection(String dir) {
        if (direction.equals("UP") && dir.equals("DOWN")) return;
        if (direction.equals("DOWN") && dir.equals("UP")) return;
        if (direction.equals("LEFT") && dir.equals("RIGHT")) return;
        if (direction.equals("RIGHT") && dir.equals("LEFT")) return;

        direction = dir;
    }

    public void moveSnake() {

        int[] head = snake.get(0);
        int x = head[0];
        int y = head[1];

        switch (direction) {
            case "UP": y--; break;
            case "DOWN": y++; break;
            case "LEFT": x--; break;
            case "RIGHT": x++; break;
        }

        // WALL COLLISION
        if (x < 0 || x >= cols || y < 0 || y >= rows) {
            gameOver();
            return;
        }

        // SELF COLLISION
        for (int i = 1; i < snake.size(); i++) {
            int[] s = snake.get(i);
            if (s[0] == x && s[1] == y) {
                gameOver();
                return;
            }
        }

        snake.add(0, new int[]{x, y});

        // FOOD EATEN
        if (x == food[0] && y == food[1]) {

            if (gameActivity != null) {
                if (isBonusFood) {
                    gameActivity.score += 50; // ⭐ BONUS POINTS
                } else {
                    gameActivity.score += 10; // normal points
                }

                gameActivity.onFoodEaten();
            }

            spawnFood();

        } else {
            snake.remove(snake.size() - 1);
        }

        invalidate();
    }

    private void gameOver() {

        SharedPreferences settings = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean isSoundOn = settings.getBoolean("sound", true);

        if (gameActivity != null && gameActivity.bgMusic != null) {
            gameActivity.bgMusic.stop();
            gameActivity.bgMusic.release();
            gameActivity.bgMusic = null;
        }

        if (isSoundOn) {
            MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.game_over);
            mp.start();
            mp.setOnCompletionListener(MediaPlayer::release);
        }

        if (gameActivity != null) {
            gameActivity.runOnUiThread(() -> {
                Intent intent = new Intent(getContext(), GameOverActivity.class);
                intent.putExtra("score", gameActivity.score);
                getContext().startActivity(intent);
                gameActivity.finish();
            });
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        cellSize = getWidth() / cols;

        int bgColor, snakeColor, foodColor;

        if (theme.equals("DARK")) {
            bgColor = Color.parseColor("#1E1E1E");
            snakeColor = Color.parseColor("#4CAF50");
            foodColor = Color.parseColor("#FF9800");

        } else if (theme.equals("RETRO")) {
            bgColor = Color.BLACK;
            snakeColor = Color.parseColor("#00FF00");
            foodColor = Color.YELLOW;

        } else {
            bgColor = Color.BLACK;
            snakeColor = Color.parseColor("#39FF14");
            foodColor = Color.RED;
        }

        canvas.drawColor(bgColor);

        // BORDER
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(foodColor);
        paint.setStrokeWidth(6);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        paint.setStyle(Paint.Style.FILL);

        // FOOD (NORMAL / BONUS)
        if (isBonusFood) {
            paint.setColor(Color.MAGENTA); // ⭐ BONUS FOOD COLOR
        } else {
            paint.setColor(foodColor);
        }

        canvas.drawRect(
                food[0] * cellSize,
                food[1] * cellSize,
                (food[0] + 1) * cellSize,
                (food[1] + 1) * cellSize,
                paint
        );

        // SNAKE
        paint.setColor(snakeColor);

        for (int[] s : snake) {
            canvas.drawRect(
                    s[0] * cellSize,
                    s[1] * cellSize,
                    (s[0] + 1) * cellSize,
                    (s[1] + 1) * cellSize,
                    paint
            );
        }
    }
}