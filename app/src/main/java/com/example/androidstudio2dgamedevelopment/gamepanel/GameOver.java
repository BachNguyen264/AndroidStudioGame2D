package com.example.androidstudio2dgamedevelopment.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * GameOver is a panel which draws the text Game Over to the screen.
 */
public class GameOver {

    private Context context;
    private Rect restartButton, menuButton, highScoreButton;
    private int screenWidth, screenHeight;
    private Paint buttonPaint, textPaint, titlePaint, infoPaint;

    private int finalScore;
    private long finalTimeSeconds;

    public GameOver(Context context, int screenWidth, int screenHeight, int finalScore, long finalTimeSeconds) {
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.finalScore = finalScore;
        this.finalTimeSeconds = finalTimeSeconds;

        // Paint cho button
        buttonPaint = new Paint();
        buttonPaint.setColor(Color.DKGRAY);

        // Paint cho chữ trong button
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Paint cho tiêu đề "Game Over"
        titlePaint = new Paint();
        titlePaint.setColor(Color.RED);
        titlePaint.setTextSize(150);
        titlePaint.setTextAlign(Paint.Align.CENTER);

        // Paint cho thông tin Score, Time
        infoPaint = new Paint();
        infoPaint.setColor(Color.YELLOW);
        infoPaint.setTextSize(70);
        infoPaint.setTextAlign(Paint.Align.CENTER);

        // Kích thước button theo màn hình
        int buttonWidth = screenWidth / 3;
        int buttonHeight = screenHeight / 10;
        int centerX = screenWidth / 2;

        // Restart button
        restartButton = new Rect(
                centerX - buttonWidth/2,
                screenHeight/2,
                centerX + buttonWidth/2,
                screenHeight/2 + buttonHeight
        );

        // Menu button
        menuButton = new Rect(
                centerX - buttonWidth/2,
                screenHeight/2 + buttonHeight + 50,
                centerX + buttonWidth/2,
                screenHeight/2 + 2*buttonHeight + 50
        );

        // High Score button
        highScoreButton = new Rect(
                centerX - buttonWidth / 2,
                screenHeight / 2 + 2 * buttonHeight + 100,
                centerX + buttonWidth / 2,
                screenHeight / 2 + 3 * buttonHeight + 100
        );
    }

    public void draw(Canvas canvas) {
        // Vẽ tiêu đề "Game Over"
        canvas.drawText("Game Over", screenWidth / 2, screenHeight / 4, titlePaint);

        // Vẽ thông tin Score và Time
        String scoreText = "Score: " + finalScore;
        String timeText = "Time: " + formatTime(finalTimeSeconds);

        canvas.drawText(scoreText, screenWidth / 2, screenHeight / 3, infoPaint);
        canvas.drawText(timeText, screenWidth / 2, screenHeight / 3 + 80, infoPaint);

        // Vẽ button Restart
        canvas.drawRect(restartButton, buttonPaint);
        drawCenteredText(canvas, restartButton, "Restart", textPaint);

        // Vẽ button Menu
        canvas.drawRect(menuButton, buttonPaint);
        drawCenteredText(canvas, menuButton, "Menu", textPaint);

        // Vẽ button High Score
        canvas.drawRect(highScoreButton, buttonPaint);
        drawCenteredText(canvas, highScoreButton, "High Score", textPaint);
    }

    // Format time từ giây sang mm:ss
    private String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Hàm hỗ trợ vẽ text căn giữa trong rect
    private void drawCenteredText(Canvas canvas, Rect rect, String text, Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float centerY = rect.centerY() - (fm.ascent + fm.descent) / 2;
        canvas.drawText(text, rect.centerX(), centerY, paint);
    }

    public boolean isRestartPressed(float x, float y) {
        return restartButton.contains((int)x, (int)y);
    }

    public boolean isMenuPressed(float x, float y) {
        return menuButton.contains((int)x, (int)y);
    }

    public boolean isHighScorePressed(float x, float y) {
        return highScoreButton.contains((int) x, (int) y);
    }

    public void setFinalScore(int s) {
        this.finalScore = s;
    }
    public void setFinalTime(long tSeconds) {
        this.finalTimeSeconds = tSeconds;
    }
}

