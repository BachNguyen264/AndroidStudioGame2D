package com.example.androidstudio2dgamedevelopment.graphics;

import android.graphics.Canvas;

import com.example.androidstudio2dgamedevelopment.GameDisplay;
import com.example.androidstudio2dgamedevelopment.gameobject.GameObject;

public class SimpleAnimator {
    private Sprite[] spriteArray;
    private int currentFrameIndex = 0;
    private int updatesBeforeNextFrame;
    private int maxUpdatesBeforeNextFrame;

    public SimpleAnimator(Sprite[] spriteArray, int maxUpdatesBeforeNextFrame) {
        this.spriteArray = spriteArray;
        this.maxUpdatesBeforeNextFrame = maxUpdatesBeforeNextFrame;
        this.updatesBeforeNextFrame = maxUpdatesBeforeNextFrame;
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay, GameObject gameObject) {
        // Trừ bộ đếm update để chuyển frame
        updatesBeforeNextFrame--;
        if (updatesBeforeNextFrame <= 0) {
            updatesBeforeNextFrame = maxUpdatesBeforeNextFrame;
            toggleFrame();
        }

        // Lấy frame hiện tại và vẽ
        Sprite sprite = spriteArray[currentFrameIndex];
        sprite.draw(
                canvas,
                (int) gameDisplay.gameToDisplayCoordinatesX(gameObject.getPositionX()) - sprite.getWidth()/2,
                (int) gameDisplay.gameToDisplayCoordinatesY(gameObject.getPositionY()) - sprite.getHeight()/2
        );
    }

    public void drawAt(Canvas canvas, GameDisplay gameDisplay, double posX, double posY) {
        Sprite sprite = spriteArray[currentFrameIndex];
        sprite.draw(
                canvas,
                (int) gameDisplay.gameToDisplayCoordinatesX(posX) - sprite.getWidth() / 2,
                (int) gameDisplay.gameToDisplayCoordinatesY(posY) - sprite.getHeight() / 2
        );
    }


    private void toggleFrame() {
        currentFrameIndex++;
        if (currentFrameIndex >= spriteArray.length) {
            currentFrameIndex = 0; // quay vòng
        }
    }
}
