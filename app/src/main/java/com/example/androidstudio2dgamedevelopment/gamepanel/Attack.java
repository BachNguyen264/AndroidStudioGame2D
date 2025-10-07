package com.example.androidstudio2dgamedevelopment.gamepanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Attack is a UI element that displays buttons for the player to cast spells/attacks.
 */
public class Attack {
    private final int mainButtonRadius = 100;
    private final int skillButtonRadius = 60;
    private final Paint mainPaint;
    private final Paint skillPaint;
    private final Paint skillIconPaint; // For drawing icons/text on skill buttons
    private int mainButtonPointerId = -1;
    private int skillButton1PointerId = -1;
    private int skillButton2PointerId = -1;
    private boolean isMainButtonPressed = false;
    private boolean isSkillButton1Pressed = false;
    private boolean isSkillButton2Pressed = false;
    // Center coordinates of the main attack button
    private final int mainButtonCenterX;
    private final int mainButtonCenterY;

    // Center coordinates for the two smaller skill buttons
    private final int skillButton1CenterX;
    private final int skillButton1CenterY;
    private final int skillButton2CenterX;
    private final int skillButton2CenterY;
    // Fireball skill cooldown
    private static final long FIREBALL_COOLDOWN_MS = 5000; // 5s
    private long fireballCooldownRemaining = 0;

    // Missile skill cooldown
    private static final long MISSILE_COOLDOWN_MS = 8000; // 8s
    private long missileCooldownRemaining = 0;

    // Paint cho overlay cooldown
    private final Paint cooldownOverlayPaint;
    private final Paint cooldownTextPaint;



    public Attack(int centerX, int centerY) {
        this.mainButtonCenterX = centerX;
        this.mainButtonCenterY = centerY;

        // Position skill buttons around the main button
        this.skillButton1CenterX = centerX + 180;
        this.skillButton1CenterY = centerY - 50;
        this.skillButton2CenterX = centerX + 50;
        this.skillButton2CenterY = centerY - 180;

        mainPaint = new Paint();
        mainPaint.setColor(Color.WHITE);
        mainPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        skillPaint = new Paint();
        skillPaint.setColor(Color.LTGRAY);
        skillPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        skillIconPaint = new Paint();
        skillIconPaint.setColor(Color.BLACK);
        skillIconPaint.setTextSize(40);
        skillIconPaint.setTextAlign(Paint.Align.CENTER);
        cooldownOverlayPaint = new Paint();
        cooldownOverlayPaint.setColor(Color.argb(150, 0, 0, 0)); // lớp phủ đen mờ

        cooldownTextPaint = new Paint();
        cooldownTextPaint.setColor(Color.WHITE);
        cooldownTextPaint.setTextSize(40);
        cooldownTextPaint.setTextAlign(Paint.Align.CENTER);
        cooldownTextPaint.setFakeBoldText(true);
    }

    public void draw(Canvas canvas) {
        // Draw main attack button
        canvas.drawCircle(mainButtonCenterX, mainButtonCenterY, mainButtonRadius, mainPaint);

        // Draw skill button 1
        canvas.drawCircle(skillButton1CenterX, skillButton1CenterY, skillButtonRadius, skillPaint);
        canvas.drawText("F", skillButton1CenterX, skillButton1CenterY + 15, skillIconPaint); // "F" for Fireball

        // Draw skill button 2
        canvas.drawCircle(skillButton2CenterX, skillButton2CenterY, skillButtonRadius, skillPaint);
        canvas.drawText("M", skillButton2CenterX, skillButton2CenterY + 15, skillIconPaint); // "M" for Missile

        // === Fireball cooldown ===
        if (fireballCooldownRemaining > 0) {
            float progress = (float) fireballCooldownRemaining / FIREBALL_COOLDOWN_MS;
            // Vẽ overlay mờ che lên nút
            canvas.drawCircle(skillButton1CenterX, skillButton1CenterY, skillButtonRadius, cooldownOverlayPaint);

            // Hiển thị giây còn lại
            String timeLeft = String.valueOf((int) Math.ceil(fireballCooldownRemaining / 1000.0));
            canvas.drawText(timeLeft, skillButton1CenterX, skillButton1CenterY + 15, cooldownTextPaint);
        }

        // === Missile cooldown ===
        if (missileCooldownRemaining > 0) {
            float progress = (float) missileCooldownRemaining / MISSILE_COOLDOWN_MS;
            canvas.drawCircle(skillButton2CenterX, skillButton2CenterY, skillButtonRadius, cooldownOverlayPaint);

            String timeLeft = String.valueOf((int) Math.ceil(missileCooldownRemaining / 1000.0));
            canvas.drawText(timeLeft, skillButton2CenterX, skillButton2CenterY + 15, cooldownTextPaint);
        }
    }

    public boolean isMainButtonPressed(double touchX, double touchY) {
        double distance = Math.sqrt(
                Math.pow(mainButtonCenterX - touchX, 2) +
                        Math.pow(mainButtonCenterY - touchY, 2)
        );
        return distance < mainButtonRadius;
    }

    public boolean isSkillButton1Pressed(double touchX, double touchY) {
        double distance = Math.sqrt(
                Math.pow(skillButton1CenterX - touchX, 2) +
                        Math.pow(skillButton1CenterY - touchY, 2)
        );
        return distance < skillButtonRadius;
    }

    public boolean isSkillButton2Pressed(double touchX, double touchY) {
        double distance = Math.sqrt(
                Math.pow(skillButton2CenterX - touchX, 2) +
                        Math.pow(skillButton2CenterY - touchY, 2)
        );
        return distance < skillButtonRadius;
    }

    // Getters and Setters for button state and pointer IDs
    public boolean getIsMainButtonPressed() {
        return isMainButtonPressed;
    }
    public void setIsMainButtonPressed(boolean isPressed, int pointerId) {
        this.isMainButtonPressed = isPressed;
        this.mainButtonPointerId = isPressed ? pointerId : -1;
    }
    public int getMainButtonPointerId() { return mainButtonPointerId; }


    public boolean getIsSkillButton1Pressed() {
        return isSkillButton1Pressed;
    }
    public void setIsSkillButton1Pressed(boolean isPressed, int pointerId) {
        this.isSkillButton1Pressed = isPressed;
        this.skillButton1PointerId = isPressed ? pointerId : -1;
    }
    public int getSkillButton1PointerId() { return skillButton1PointerId; }

    public boolean getIsSkillButton2Pressed() { return isSkillButton2Pressed; }
    public void setIsSkillButton2Pressed(boolean isPressed, int pointerId) {
        this.isSkillButton2Pressed = isPressed;
        this.skillButton2PointerId = isPressed ? pointerId : -1;
    }
    public int getSkillButton2PointerId() { return skillButton2PointerId; }
    public boolean canCastFireball() {
        return fireballCooldownRemaining <= 0;
    }
    public boolean canCastMissile() {
        return missileCooldownRemaining <= 0;
    }

    public void triggerFireballCooldown() {
        fireballCooldownRemaining = FIREBALL_COOLDOWN_MS;
    }

    public void triggerMissileCooldown() {
        missileCooldownRemaining = MISSILE_COOLDOWN_MS;
    }
    public void updateCooldown(long deltaTimeMs) {
        if (fireballCooldownRemaining > 0)
            fireballCooldownRemaining -= deltaTimeMs;
        if (missileCooldownRemaining > 0)
            missileCooldownRemaining -= deltaTimeMs;
    }

}