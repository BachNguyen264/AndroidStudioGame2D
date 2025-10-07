package com.example.androidstudio2dgamedevelopment.gameobject;

import android.content.Context;
import android.graphics.Color;

import com.example.androidstudio2dgamedevelopment.Game;

import java.util.List;

public class Missile extends Spell {
    private static final double TURN_SPEED = 0.15; // tốc độ xoay hướng
    private static final int DAMAGE = 8;
    private Enemy target;
    private Game game;

    public Missile(Context context, Player player, Game game) {
        super(context, player);
        paint.setColor(Color.BLACK);
        this.game = game;
        this.target = findClosestEnemy(); // chọn enemy gần nhất ngay khi bắn
        damage = DAMAGE;
    }

    @Override
    public void update() {
        // Nếu target chết hoặc biến mất → tìm lại
        if (target == null || target.isDead() || game.getEnemyList().isEmpty()) {
            target = findClosestEnemy();
            if (target == null) {
                // không có enemy nào → bay thẳng
                positionX += velocityX;
                positionY += velocityY;
                return;
            }
        }

        // Tính vector hướng tới enemy
        double dx = target.getPositionX() - positionX;
        double dy = target.getPositionY() - positionY;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist > 0) {
            // Chuẩn hóa hướng
            dx /= dist;
            dy /= dist;

            // Tính vận tốc hiện tại (từ lớp Spell)
            double currentSpeed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);

            // Dần dần điều chỉnh hướng bay (tracking mượt hơn)
            velocityX = velocityX + TURN_SPEED * (dx * MAX_SPEED - velocityX);
            velocityY = velocityY + TURN_SPEED * (dy * MAX_SPEED - velocityY);

            // Chuẩn hóa lại để không tăng tốc vô hạn
            double len = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            velocityX = (velocityX / len) * currentSpeed;
            velocityY = (velocityY / len) * currentSpeed;

            // Cập nhật vị trí
            positionX += velocityX;
            positionY += velocityY;
        }
    }

    private Enemy findClosestEnemy() {
        if (game.getEnemyList().isEmpty()) return null;
        Enemy closest = null;
        double minDist = Double.MAX_VALUE;
        for (Enemy e : game.getEnemyList()) {
            double dx = e.getPositionX() - positionX;
            double dy = e.getPositionY() - positionY;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < minDist) {
                minDist = dist;
                closest = e;
            }
        }
        return closest;
    }

    @Override
    public void onHit(Enemy enemy, List<Enemy> allEnemies) {
        enemy.takeDamage(damage);
    }
}
