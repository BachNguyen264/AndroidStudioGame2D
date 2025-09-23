package com.example.androidstudio2dgamedevelopment.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.GameDisplay;
import com.example.androidstudio2dgamedevelopment.GameLoop;
import com.example.androidstudio2dgamedevelopment.gamepanel.HealthBar;
import com.example.androidstudio2dgamedevelopment.gamepanel.Joystick;
import com.example.androidstudio2dgamedevelopment.R;
import com.example.androidstudio2dgamedevelopment.Utils;
import com.example.androidstudio2dgamedevelopment.graphics.PlayerAnimator;

/**
 * Player is the main character of the game, which the user can control with a touch joystick.
 * The player class is an extension of a Circle, which is an extension of a GameObject
 */
public class Player extends Circle {
    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    public static final int MAX_HEALTH_POINTS = 5;
    private Joystick joystick;
    private HealthBar healthBar;
    private int healthPoints = MAX_HEALTH_POINTS;
    private PlayerAnimator playerAnimator;
    private PlayerState playerState;
    private boolean facingRight = false;

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius, PlayerAnimator playerAnimator) {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
        this.healthBar = new HealthBar(context, this);
        this.playerAnimator = playerAnimator;
        this.playerState = new PlayerState(this);
    }

    public void update() {

        // Update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;

        // Update position
        positionX += velocityX;
        positionY += velocityY;

        // Update direction
        if (velocityX != 0 || velocityY != 0) {
            // Normalize velocity to get direction (unit vector of velocity)
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = velocityX/distance;
            directionY = velocityY/distance;
        }
        //facing
        if (velocityX > 0) {
            facingRight = true;
        } else if (velocityX < 0) {
            facingRight = false;
        }

        playerState.update();
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        playerAnimator.draw(canvas, gameDisplay, this);

        healthBar.draw(canvas, gameDisplay);
    }

    public int getHealthPoint() {
        return healthPoints;
    }

    public void setHealthPoint(int healthPoints) {
        // Only allow positive values
        if (healthPoints >= 0)
            this.healthPoints = healthPoints;
    }

    // Reset toàn bộ trạng thái player
    public void reset(double startX, double startY) {
        this.positionX = startX;
        this.positionY = startY;
        this.healthPoints = MAX_HEALTH_POINTS;
        this.velocityX = 0;
        this.velocityY = 0;
        this.directionX = 1; // hướng mặc định (tùy bạn chọn)
        this.directionY = 0;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }
    public boolean isFacingRight() {
        return facingRight;
    }
}
