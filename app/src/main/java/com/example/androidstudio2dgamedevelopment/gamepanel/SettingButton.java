package com.example.androidstudio2dgamedevelopment.gamepanel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.androidstudio2dgamedevelopment.R;


public class SettingButton {
    private final Bitmap icon;
    private final int x;
    private final int y;

    public SettingButton(Context context, int screenWidth, int screenHeight) {
        // Load icon gốc
        Bitmap original = BitmapFactory.decodeResource(context.getResources(), R.drawable.settings);

        // Tính toán kích thước mới (ví dụ 5% chiều rộng màn hình)
        int newWidth = screenWidth / 20;
        int newHeight = (int) ((float) original.getHeight() / original.getWidth() * newWidth);

        // Scale ảnh
        icon = Bitmap.createScaledBitmap(original, newWidth, newHeight, true);

        // Góc phải trên, cách lề 5% chiều rộng và 5% chiều cao
        x = screenWidth - icon.getWidth() - (int)(0.05f * screenWidth);
        y = (int)(0.05f * screenHeight);
    }


    public void draw(Canvas canvas) {
        canvas.drawBitmap(icon, x, y, null);
    }

    public boolean isPressed(float touchX, float touchY) {
        return touchX >= x && touchX <= (x + icon.getWidth()) &&
                touchY >= y && touchY <= (y + icon.getHeight());
    }
}


