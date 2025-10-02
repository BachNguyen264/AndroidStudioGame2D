// In a new file: Shield.java
package com.example.androidstudio2dgamedevelopment.gameobject;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.example.androidstudio2dgamedevelopment.R;

public class Shield extends Item {

    public static final long SHIELD_DURATION_MS = 5000; // 5 seconds

    public Shield(Context context, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.shieldColor), // Define this color
                positionX, positionY, radius, ItemType.SHIELD);
    }

    @Override
    public void applyEffect(Player player) {
        if (!isCollected()) {
            player.activateShield(SHIELD_DURATION_MS);
            setCollected(true);
        }
    }

    @Override
    public void update() {

    }
}