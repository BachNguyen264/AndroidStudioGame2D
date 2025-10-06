package com.example.androidstudio2dgamedevelopment.map;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.androidstudio2dgamedevelopment.graphics.Sprite;
import com.example.androidstudio2dgamedevelopment.graphics.SpriteSheet;

class LavaTile extends Tile implements TileBehavior {
    private final Sprite sprite;

    public LavaTile(SpriteSheet spriteSheet, Rect mapLocationRect) {
        super(mapLocationRect);
        sprite = spriteSheet.getLavaSprite();
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas, mapLocationRect.left, mapLocationRect.top);
    }

    @Override
    public int getDamagePerSecond() {
        return 1; // trừ 1 máu mỗi giây
    }
}
