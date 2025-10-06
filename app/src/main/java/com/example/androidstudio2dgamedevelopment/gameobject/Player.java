package com.example.androidstudio2dgamedevelopment.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

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
    // Thêm biến tạm để lưu kích thước map
    private int mapWidth;
    private int mapHeight;

    // Shield properties
    private boolean isShieldActive = false;
    private long shieldEndTimeMs = 0;
    private Paint shieldPaint; // For drawing shield indicator

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius, PlayerAnimator playerAnimator) {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
        this.healthBar = new HealthBar(context, this);
        this.playerAnimator = playerAnimator;
        this.playerState = new PlayerState(this);

        // Initialize shield paint
        shieldPaint = new Paint();
        shieldPaint.setColor(ContextCompat.getColor(context, R.color.shieldActiveColor)); // Define this color
        shieldPaint.setStyle(Paint.Style.STROKE);
        shieldPaint.setStrokeWidth(10); // Adjust as needed
    }

    public void update() {

        // Update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;

        // Update position
        positionX += velocityX;
        positionY += velocityY;

        // Wrap quanh biên map
        loopPosition();

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

        // Shield countdown
        if (isShieldActive && System.currentTimeMillis() > shieldEndTimeMs) {
            deactivateShield();
        }

        playerState.update();
    }

    // Hàm mới để set giá trị map
    public void setMapSize(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }
    public void loopPosition() {
        if (mapWidth > 0 && mapHeight > 0) {
            if (positionX < 0) positionX += mapWidth;
            if (positionX > mapWidth) positionX -= mapWidth;
            if (positionY < 0) positionY += mapHeight;
            if (positionY > mapHeight) positionY -= mapHeight;
        }
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        playerAnimator.draw(canvas, gameDisplay, this);

        healthBar.draw(canvas, gameDisplay);
        // Draw shield indicator if active
        if (isShieldActive) {
            // Calculate screen coordinates for the player
            float screenX = (float) gameDisplay.gameToDisplayCoordinatesX(getPositionX());
            float screenY = (float) gameDisplay.gameToDisplayCoordinatesY(getPositionY());
            // Adjust radius for display if necessary, or use a fixed size for the shield indicator
            float displayRadius = (float) (radius * 1.2); // Make shield slightly larger than player

            canvas.drawCircle(screenX, screenY, displayRadius, shieldPaint);
        }
    }

    public int getHealthPoint() {
        return healthPoints;
    }

    public void setHealthPoint(int healthPoints) {
        if (isShieldActive) { // If shield is active, player doesn't lose health
            return;
        }
        // Only allow positive values
        if (healthPoints >= 0)
            this.healthPoints = healthPoints;
    }

    // Call this method when player collides with an enemy
    public void takeDamage(int damageAmount) {
        if (!isShieldActive) {
            setHealthPoint(getHealthPoint() - damageAmount);
        }
        // If you want shield to absorb one hit and then deactivate:
        /*
        if (isShieldActive) {
            deactivateShield();
        } else {
            setHealthPoint(getHealthPoint() - damageAmount);
        }
        */
    }

    public void activateShield(long durationMs) {
        isShieldActive = true;
        shieldEndTimeMs = System.currentTimeMillis() + durationMs;
    }

    private void deactivateShield() {
        isShieldActive = false;
    }

    public boolean isShieldActive() {
        return isShieldActive;
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
