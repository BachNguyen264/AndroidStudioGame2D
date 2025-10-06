package com.example.androidstudio2dgamedevelopment.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.androidstudio2dgamedevelopment.GameDisplay;

/**
 * Circle is an abstract class which implements a draw method from GameObject for drawing the object
 * as a circle.
 */
public abstract class Circle extends GameObject {
    protected double radius;
    protected Paint paint;

    public Circle(Context context, int color, double positionX, double positionY, double radius) {
        super(positionX, positionY);
        this.radius = radius;

        // Set colors of circle
        paint = new Paint();
        paint.setColor(color);
    }

    /**
     * isColliding checks if two circle objects are colliding, based on their positions and radii.
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean isColliding(Circle obj1, Circle obj2) {
        double distance = getDistanceBetweenObjects(obj1, obj2);
        double distanceToCollision = obj1.getRadius() + obj2.getRadius();
        if (distance < distanceToCollision)
            return true;
        else
            return false;
    }

    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        canvas.drawCircle(
                (float) gameDisplay.gameToDisplayCoordinatesX(positionX),
                (float) gameDisplay.gameToDisplayCoordinatesY(positionY),
                (float) radius,
                paint
        );
    }
    public void drawLoop(Canvas canvas, GameDisplay gameDisplay, int mapWidth, int mapHeight) {
        // Dịch chuyển theo 8 hướng
        int[] dx = {-mapWidth, 0, mapWidth};
        int[] dy = {-mapHeight, 0, mapHeight};

        for (int i = 0; i < dx.length; i++) {
            for (int j = 0; j < dy.length; j++) {
                if (dx[i] == 0 && dy[j] == 0) continue; // bỏ bản chính

                float drawX = (float) gameDisplay.gameToDisplayCoordinatesX(positionX + dx[i]);
                float drawY = (float) gameDisplay.gameToDisplayCoordinatesY(positionY + dy[j]);

                canvas.drawCircle(drawX, drawY, (float) radius, paint);
            }
        }
    }


    public double getRadius() {
        return radius;
    }
}