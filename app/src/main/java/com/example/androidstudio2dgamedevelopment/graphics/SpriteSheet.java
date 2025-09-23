package com.example.androidstudio2dgamedevelopment.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.androidstudio2dgamedevelopment.R;

public class SpriteSheet {
    private static final int SPRITE_WIDTH_PIXELS = 64;
    private static final int SPRITE_HEIGHT_PIXELS = 64;
    private Bitmap bitmap;

    public SpriteSheet(Context context) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite, bitmapOptions);
    }

    public Sprite[] getPlayerSpriteArray() {
        Sprite[] spriteArray = new Sprite[5];
        spriteArray[0] = getSpriteByIndex(0,0);
        spriteArray[1] = getSpriteByIndex(0,1);
        spriteArray[2] = getSpriteByIndex(0,2);
        spriteArray[3] = getSpriteByIndex(0,3);
        spriteArray[4] = getSpriteByIndex(0,4);
        return spriteArray;
    }

    public Sprite[] getEnemySpriteArray() {
        Sprite[] spriteArray = new Sprite[5];
        spriteArray[0] = getSpriteByIndex(2,0);
        spriteArray[1] = getSpriteByIndex(2,1);
        spriteArray[2] = getSpriteByIndex(2,2);
        spriteArray[3] = getSpriteByIndex(2,3);
        spriteArray[4] = getSpriteByIndex(2,4);
        return spriteArray;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Sprite getWaterSprite() {
        return getSpriteByIndex(1, 0);
    }

    public Sprite getLavaSprite() {
        return getSpriteByIndex(1, 1);
    }

    public Sprite getGroundSprite() {
        return getSpriteByIndex(1, 2);
    }

    public Sprite getGrassSprite() {
        return getSpriteByIndex(1, 3);
    }

    public Sprite getTreeSprite() {
        return getSpriteByIndex(1, 4);
    }


    private Sprite getSpriteByIndex(int idxRow, int idxCol) {
        return new Sprite(this, new Rect(
                idxCol*SPRITE_WIDTH_PIXELS,
                idxRow*SPRITE_HEIGHT_PIXELS,
                (idxCol + 1)*SPRITE_WIDTH_PIXELS,
                (idxRow + 1)*SPRITE_HEIGHT_PIXELS
        ));
    }
}
