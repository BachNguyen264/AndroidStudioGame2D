package com.example.androidstudio2dgamedevelopment.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.example.androidstudio2dgamedevelopment.GameDisplay;
import com.example.androidstudio2dgamedevelopment.gameobject.Player;

public class PlayerAnimator {
    private Sprite[] playerSpriteArray;
    private int idxNotMovingFrame = 0;
    private int idxMovingFrame = 1;
    private int updatesBeforeNextMoveFrame;
    private static final int MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME = 5;

    public PlayerAnimator(Sprite[] playerSpriteArray) {
        this.playerSpriteArray = playerSpriteArray;
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay, Player player) {
        switch (player.getPlayerState().getState()) {
            case NOT_MOVING:
                drawFrame(canvas, gameDisplay, player, playerSpriteArray[idxNotMovingFrame]);
                break;
            case STARED_MOVING:
                updatesBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME;
                drawFrame(canvas, gameDisplay, player, playerSpriteArray[idxMovingFrame]);
                break;
            case IS_MOVING:
                updatesBeforeNextMoveFrame--;
                if(updatesBeforeNextMoveFrame == 0) {
                    updatesBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME;
                    toggleIdxMovingFrame();
                }
                drawFrame(canvas, gameDisplay, player, playerSpriteArray[idxMovingFrame]);
                break;
            default:
                break;
        }
    }

    private void toggleIdxMovingFrame() {
        idxMovingFrame++;
        if (idxMovingFrame >= 5) { // tổng cộng 4 frame
            idxMovingFrame = 1;    // quay lại frame 1 (frame 0 để đứng yên)
        }
    }

    public void drawFrame(Canvas canvas, GameDisplay gameDisplay, Player player, Sprite sprite) {
        int drawX = (int) gameDisplay.gameToDisplayCoordinatesX(player.getPositionX()) - sprite.getWidth()/2;
        int drawY = (int) gameDisplay.gameToDisplayCoordinatesY(player.getPositionY()) - sprite.getHeight()/2;

        Bitmap originalBitmap = sprite.getBitmap();
        Rect src = sprite.getRect();

        if (player.isFacingRight()) {
            // Lật ngang bằng Matrix
            Matrix matrix = new Matrix();
            matrix.preScale(-1, 1);
            // Dịch chuyển để đúng vị trí
            matrix.postTranslate(drawX + sprite.getWidth(), drawY);

            canvas.drawBitmap(originalBitmap, src, new Rect(0, 0, sprite.getWidth(), sprite.getHeight()), null);
            canvas.drawBitmap(Bitmap.createBitmap(originalBitmap, src.left, src.top, sprite.getWidth(), sprite.getHeight(), matrix, false),
                    drawX, drawY, null);
        } else {
            // Vẽ bình thường
            sprite.draw(canvas, drawX, drawY);
        }
    }
}