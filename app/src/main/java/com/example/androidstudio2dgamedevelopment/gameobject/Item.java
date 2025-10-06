// In a new file: Item.java (in the same package as Player.java)
package com.example.androidstudio2dgamedevelopment.gameobject;

import android.content.Context;
import android.graphics.Canvas;

import com.example.androidstudio2dgamedevelopment.GameDisplay;

public abstract class Item extends Circle {

    public enum ItemType {
        HEALTH_BOOST,
        SHIELD,
        FREEZE;
        private static final ItemType[] VALUES = ItemType.values();
        private static final java.util.Random RNG = new java.util.Random();
        public static ItemType getRandomType() {
            return VALUES[RNG.nextInt(VALUES.length)];
        }
    }

    protected ItemType itemType;
    protected boolean isCollected = false;

    public Item(Context context, int color, double positionX, double positionY, double radius, ItemType itemType) {
        super(context, color, positionX, positionY, radius);
        this.itemType = itemType;
    }

    public abstract void applyEffect(Player player); // To be implemented by subclasses

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        if (!isCollected) { // Only draw if not collected
            super.draw(canvas, gameDisplay);
        }
    }
    @Override
    public void drawLoop(Canvas canvas, GameDisplay gameDisplay, int mapWidth, int mapHeight) {
        if (isCollected) return;
        super.drawLoop(canvas, gameDisplay, mapWidth, mapHeight);
    }

    // You might want to add an update method if items have animations or movement
    // public void update() { }
}