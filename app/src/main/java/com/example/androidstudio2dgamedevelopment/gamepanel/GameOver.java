package com.example.androidstudio2dgamedevelopment.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.R;


/**
 * GameOver is a panel which draws the text Game Over to the screen.
 */
public class GameOver {

    private Context context;
    private Rect restartButton;
    private Rect menuButton;
    private int screenWidth, screenHeight;
    private Paint buttonPaint, textPaint, titlePaint;

    public GameOver(Context context, int screenWidth, int screenHeight) {
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

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

        // Kích thước button theo màn hình
        int buttonWidth = screenWidth / 4;
        int buttonHeight = screenHeight / 8;
        int centerX = screenWidth / 2;

        // Restart button (căn giữa, nằm giữa màn hình)
        restartButton = new Rect(
                centerX - buttonWidth/2,
                screenHeight/2,
                centerX + buttonWidth/2,
                screenHeight/2 + buttonHeight
        );

        // Menu button (căn giữa, nằm dưới restart 1 khoảng)
        menuButton = new Rect(
                centerX - buttonWidth/2,
                screenHeight/2 + buttonHeight + 50,
                centerX + buttonWidth/2,
                screenHeight/2 + 2*buttonHeight + 50
        );
    }

    public void draw(Canvas canvas) {
        // Vẽ tiêu đề "Game Over"
        canvas.drawText("Game Over", screenWidth / 2, screenHeight / 3, titlePaint);

        // Vẽ button Restart
        canvas.drawRect(restartButton, buttonPaint);
        drawCenteredText(canvas, restartButton, "Restart", textPaint);

        // Vẽ button Menu
        canvas.drawRect(menuButton, buttonPaint);
        drawCenteredText(canvas, menuButton, "Menu", textPaint);
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
}
