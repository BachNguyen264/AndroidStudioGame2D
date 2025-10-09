package com.example.androidstudio2dgamedevelopment.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.GameDisplay;
import com.example.androidstudio2dgamedevelopment.GameLoop;
import com.example.androidstudio2dgamedevelopment.R;
import com.example.androidstudio2dgamedevelopment.graphics.SimpleAnimator;

/**
 * Enemy is a character which always moves in the direction of the player.
 * The Enemy class is an extension of a Circle, which is an extension of a GameObject
 */
public class Enemy extends Circle {
    public static final int MAX_HEALTH_POINTS = 8;
    private int healthPoints = MAX_HEALTH_POINTS;
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND*0.6;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double SPAWNS_PER_MINUTE = 20;
    private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE/60.0;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    private Player player;
    private SimpleAnimator animator;   // 🎯 thêm animator
    private boolean isFrozen  = false;
    private long freezeEndTimeMs  = 0;
    private double dynamicSpeed = MAX_SPEED;
    private int mapWidth;
    private int mapHeight;


    public Enemy(Context context, Player player, double positionX, double positionY, double radius, SimpleAnimator animator) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;
        this.animator = animator;
    }

    /**
     * Enemy is an overload constructor used for spawning enemies in random locations
     * @param context
     * @param player
     */
    public Enemy(Context context, Player player, SimpleAnimator animator) {
        super(
            context,
            ContextCompat.getColor(context, R.color.enemy),
   Math.random()*1000,
   Math.random()*1000,
     30
        );
        this.player = player;
        this.animator = animator;
    }

    public Enemy(Context context, Player player, SimpleAnimator animator, double speed, int health) {
        super(context, ContextCompat.getColor(context, R.color.enemy),
                Math.random() * 1000,
                Math.random() * 1000,
                30);
        this.player = player;
        this.animator = animator;
        this.healthPoints = health;
        this.dynamicSpeed = speed / GameLoop.MAX_UPS;
    }


    /**
     * readyToSpawn checks if a new enemy should spawn, according to the decided number of spawns
     * per minute (see SPAWNS_PER_MINUTE at top)
     * @return
     */
    public static boolean readyToSpawn(double spawnMultiplier) {
        if (updatesUntilNextSpawn <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN / spawnMultiplier;
            return true;
        } else {
            updatesUntilNextSpawn--;
            return false;
        }
    }
    public void setMapSize(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public static double getBaseSpeed() {
        return SPEED_PIXELS_PER_SECOND;
    }

    @Override
    public void update() {
        if (isFrozen) {
            if (System.currentTimeMillis() > freezeEndTimeMs) {
                unfreeze();
            }
            return;
        }

        // === Wrap-aware distance tính toán ===
        double dx = player.getPositionX() - positionX;
        double dy = player.getPositionY() - positionY;

        // Nếu có map kích thước (đã được set từ Game)
        if (mapWidth > 0 && mapHeight > 0) {
            // Chọn hướng ngắn nhất giữa khoảng cách trực tiếp và khoảng cách qua biên
            if (Math.abs(dx) > mapWidth / 2) {
                dx -= Math.signum(dx) * mapWidth;
            }
            if (Math.abs(dy) > mapHeight / 2) {
                dy -= Math.signum(dy) * mapHeight;
            }
        }

        // Tính độ dài khoảng cách
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Cập nhật vận tốc
        if (distance > 0) {
            velocityX = (dx / distance) * dynamicSpeed;
            velocityY = (dy / distance) * dynamicSpeed;
        } else {
            velocityX = 0;
            velocityY = 0;
        }

        // Di chuyển
        positionX += velocityX;
        positionY += velocityY;

        // === Wrap lại vị trí nếu vượt biên ===
        if (mapWidth > 0 && mapHeight > 0) {
            if (positionX < 0) positionX += mapWidth;
            if (positionX > mapWidth) positionX -= mapWidth;
            if (positionY < 0) positionY += mapHeight;
            if (positionY > mapHeight) positionY -= mapHeight;
        }
    }


    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        if (animator != null) {
            animator.draw(canvas, gameDisplay, this);
        } else {
            super.draw(canvas, gameDisplay); // fallback nếu chưa có sprite
        }
        if (isFrozen) {
            Paint freezeOverlay = new Paint();
            freezeOverlay.setColor(Color.argb(100, 0, 200, 255)); // xanh nhạt, mờ
            canvas.drawCircle(
                    (float) gameDisplay.gameToDisplayCoordinatesX(positionX),
                    (float) gameDisplay.gameToDisplayCoordinatesY(positionY),
                    (float) radius,
                    freezeOverlay
            );
        }
    }
    @Override
    public void drawLoop(Canvas canvas, GameDisplay gameDisplay, int mapWidth, int mapHeight) {
        int[] dx = {-mapWidth, 0, mapWidth};
        int[] dy = {-mapHeight, 0, mapHeight};

        for (int i = 0; i < dx.length; i++) {
            for (int j = 0; j < dy.length; j++) {
                if (dx[i] == 0 && dy[j] == 0) continue;

                double loopX = positionX + dx[i];
                double loopY = positionY + dy[j];

                if (animator != null) {
                    animator.drawAt(canvas, gameDisplay, loopX, loopY);
                } else {
                    // fallback - vẽ hình tròn cơ bản (trường hợp không có sprite)
                    float drawX = (float) gameDisplay.gameToDisplayCoordinatesX(loopX);
                    float drawY = (float) gameDisplay.gameToDisplayCoordinatesY(loopY);
                    canvas.drawCircle(drawX, drawY, (float) radius, paint);
                }
            }
        }
    }

    public void freeze(long durationMs) {
        isFrozen = true;
        freezeEndTimeMs = System.currentTimeMillis() + durationMs;
    }

    public void unfreeze() {
        isFrozen = false;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public int getHealthPoint() {
        return healthPoints;
    }

    public void setHealthPoint(int healthPoints) {
        // Only allow positive values
        if (healthPoints >= 0)
            this.healthPoints = healthPoints;
    }

    // Call this method when player collides with an enemy
    public void takeDamage(int damageAmount) {
        int healthAfterDamage = getHealthPoint() - damageAmount;
        if(healthAfterDamage < 0){
            setHealthPoint(0);
        }
        else{
            setHealthPoint(getHealthPoint() - damageAmount);
        }
    }
    public boolean isDead(){
        return getHealthPoint() == 0;
    }
}

