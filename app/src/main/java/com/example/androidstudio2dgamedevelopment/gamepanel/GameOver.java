package com.example.androidstudio2dgamedevelopment.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
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
    private Paint paint;
    public GameOver(Context context) {
        this.context = context;
        paint = new Paint();

        // Tạo 2 button (toạ độ tuỳ chỉnh cho hợp màn hình)
        restartButton = new Rect(600, 400, 1000, 550); // x1,y1,x2,y2
        menuButton = new Rect(600, 600, 1000, 750);
    }

    public void draw(Canvas canvas) {
        // Vẽ chữ Game Over
        String text = "Game Over";
        paint.setColor(ContextCompat.getColor(context, R.color.gameOver));
        paint.setTextSize(150);
        canvas.drawText(text, 800, 200, paint);

        // Vẽ button Restart
        paint.setColor(ContextCompat.getColor(context, R.color.gameOver));
        canvas.drawRect(restartButton, paint);
        paint.setColor(ContextCompat.getColor(context, R.color.white));
        paint.setTextSize(60);
        canvas.drawText("Restart", restartButton.left + 50, restartButton.centerY() + 20, paint);

        // Vẽ button Menu
        paint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        canvas.drawRect(menuButton, paint);
        paint.setColor(ContextCompat.getColor(context, R.color.white));
        canvas.drawText("Menu", menuButton.left + 80, menuButton.centerY() + 20, paint);
    }

    public boolean isRestartPressed(float x, float y) {
        return restartButton.contains((int)x, (int)y);
    }

    public boolean isMenuPressed(float x, float y) {
        return menuButton.contains((int)x, (int)y);
    }
}
