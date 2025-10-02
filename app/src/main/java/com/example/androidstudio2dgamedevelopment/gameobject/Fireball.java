package com.example.androidstudio2dgamedevelopment.gameobject;

import android.content.Context;
import android.graphics.Color;

/**
 * Fireball is a type of spell that can be cast by the player.
 * For now, it's a subclass of Spell to differentiate it, but can be given unique behaviors later.
 */
public class Fireball extends Spell {
    public Fireball(Context context, Player player) {
        super(context, player);
        // Change color to distinguish from a normal spell
        paint.setColor(Color.RED);
    }
}