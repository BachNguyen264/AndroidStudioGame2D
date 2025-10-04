package com.example.androidstudio2dgamedevelopment.gamepanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameTimer {
    private long startTime;
    private long elapsedTimeSeconds;
    private Paint paint;
    private float x, y;
    private boolean running = true;  // mặc định chạy ngay khi khởi tạo

    public GameTimer(float x, float y) {
        this.x = x;
        this.y = y;
        this.startTime = System.currentTimeMillis();

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public void update() {
        if (running) {
            elapsedTimeSeconds = (System.currentTimeMillis() - startTime) / 1000;
        }
    }

    public void draw(Canvas canvas) {
        long minutes = elapsedTimeSeconds / 60;
        long seconds = elapsedTimeSeconds % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);

        canvas.drawText(timeString, x, y, paint);
    }

    public void reset() {
        this.startTime = System.currentTimeMillis();
        this.elapsedTimeSeconds = 0;
        this.running = true;
    }

    public void pause() {
        this.running = false;
    }

    public void resume() {
        this.startTime = System.currentTimeMillis() - elapsedTimeSeconds * 1000;
        this.running = true;
    }

    public long getElapsedTimeSeconds() {
        return elapsedTimeSeconds;
    }
}
