// In a new file: HealthBoost.java
package com.example.androidstudio2dgamedevelopment.gameobject;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.example.androidstudio2dgamedevelopment.R;

public class HealthBoost extends Item {

    public static final int HEALTH_TO_RESTORE = 1;

    public HealthBoost(Context context, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.healthBoostColor), // Define this color in colors.xml
                positionX, positionY, radius, ItemType.HEALTH_BOOST);
    }

    @Override
    public void applyEffect(Player player) {
        if (!isCollected()) {
            int currentHealth = player.getHealthPoint();
            if (currentHealth < Player.MAX_HEALTH_POINTS) {
                player.setHealthPoint(currentHealth + HEALTH_TO_RESTORE);
            }
            setCollected(true); // Mark as collected
        }
    }

    @Override
    public void update() {
    }
}