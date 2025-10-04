package com.example.androidstudio2dgamedevelopment.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Score {
    private Context context;
    private int score;
    private Paint paint;
    private float x, y;

    public Score(Context context, float x, float y) {
        this.context = context;
        this.score = 0;
        this.x = x;
        this.y = y;

        paint = new Paint();
        paint.setColor(Color.RED); // Or any color you prefer
        paint.setTextSize(50); // Adjust size as needed
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public void draw(Canvas canvas) {
        canvas.drawText("Score: " + score, x, y, paint);
    }

    public void addScore(int points) {
        this.score += points;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        this.score = 0;
    }
}