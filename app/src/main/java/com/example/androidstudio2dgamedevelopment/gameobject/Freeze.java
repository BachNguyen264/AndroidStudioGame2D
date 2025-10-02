// In a new file: Freeze.java
package com.example.androidstudio2dgamedevelopment.gameobject;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.R;

public class Freeze extends Item {

    public static final long FREEZE_DURATION_MS = 3000; // 3 seconds

    public Freeze(Context context, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.freezeColor), // Define this color
                positionX, positionY, radius, ItemType.FREEZE);
    }

    @Override
    public void applyEffect(Player player) {
        // The effect is applied to enemies, so we'll need a way to access them.
        // This will likely be managed in the Game.java class.
        // For now, we'll just mark it as collected.
        if (!isCollected()) {
            // Logic to freeze enemies will be handled in Game.java or a similar manager class
            setCollected(true);
        }
    }

    @Override
    public void update() {
    }

}