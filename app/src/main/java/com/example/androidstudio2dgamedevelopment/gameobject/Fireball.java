package com.example.androidstudio2dgamedevelopment.gameobject;

import android.content.Context;
import android.graphics.Color;

import com.example.androidstudio2dgamedevelopment.Game;

import java.util.List;

/**
 * Fireball is a type of spell that can be cast by the player.
 * For now, it's a subclass of Spell to differentiate it, but can be given unique behaviors later.
 */
public class Fireball extends Spell {
    private static final double EXPLOSION_RADIUS = 200;
    private static final int EXPLOSION_DAMAGE = 8;

    public Fireball(Context context, Player player) {
        super(context, player);
        paint.setColor(Color.RED);
    }

    @Override
    public void onHit(Enemy enemy, List<Enemy> allEnemies) {
        // Gây damage trực tiếp cho enemy va chạm
        enemy.takeDamage(EXPLOSION_DAMAGE);

        // Gây AOE xung quanh
        for (Enemy e : allEnemies) {
            double dx = e.getPositionX() - this.positionX;
            double dy = e.getPositionY() - this.positionY;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist <= EXPLOSION_RADIUS && e != enemy) {
                e.takeDamage(EXPLOSION_DAMAGE / 2); // kẻ gần bị ít hơn hoặc vẫn giữ nguyên damage
            }
        }
    }
}
