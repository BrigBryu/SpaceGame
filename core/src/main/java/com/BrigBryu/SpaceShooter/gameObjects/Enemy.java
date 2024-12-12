package com.BrigBryu.SpaceShooter.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Enemy extends Ship {
    public int type;
    public Enemy(float xCenter, float yCenter,
                      float width, float height,
                      float movementSpeed, int shield, int health,
                      float laserWidth, float laserHeight,
                      float laserSpeed, float laserTimeBetweenShots,
                      TextureRegion shipTextureRegion, TextureRegion shieldTextureRegion, TextureRegion laserTextureRegion,
                      int type) {
        super(xCenter, yCenter, width, height, movementSpeed, shield, health, 1, 0, laserWidth, laserHeight, laserSpeed, laserTimeBetweenShots, 10, shipTextureRegion, shieldTextureRegion, laserTextureRegion);
        this.type = type;
    }

    public float getX() {
        return boundingBox.x;
    }

    public float getY() {
        return boundingBox.y;
    }

    public void setPosition(float x, float y) {
        boundingBox.x = x;
        boundingBox.y = y;
    }

    @Override
    public Laser[] fireLasers() {
        float angle = 10;

        float diagonalVelocityX = laserSpeed * MathUtils.sinDeg(angle);
        float diagonalVelocityY = laserSpeed * MathUtils.cosDeg(angle);

        int totalDiagonalLasers = numDiagonalLasers * 2; // diagonal laser has pair
        int totalLasers = totalDiagonalLasers + numVerticalLasers;

        Laser[] lasers = new Laser[totalLasers];

        float yPosition = boundingBox.y; //Fire lasers from bottom for enemy only difference between enemy and player
        float midX = boundingBox.x + boundingBox.width / 2.0f - laserWidth / 2.0f;
        float spacing = laserWidth + laserWidth/2; // TODO might be bad

        int laserIndex = 0;

        // diagonal lasers
        for (int i = 0; i < numDiagonalLasers; i++) {
            // Left diagonal
            float offsetXLeft = (numVerticalLasers / 2 + i + 1) * spacing;
            lasers[laserIndex++] = new Laser(
                midX - offsetXLeft,
                yPosition,
                laserWidth,
                laserHeight,
                -diagonalVelocityX, // velocityX (left)
                diagonalVelocityY,  // velocityY
                damagePerShot,
                laserTextureRegion,
                angle // rotationAngle in degrees
            );

            // Right diagonal
            float offsetXRight = (numVerticalLasers / 2 + i + 1) * spacing;
            lasers[laserIndex++] = new Laser(
                midX + offsetXRight,
                yPosition,
                laserWidth,
                laserHeight,
                diagonalVelocityX, // velocityX (right)
                diagonalVelocityY, // velocityY
                damagePerShot,
                laserTextureRegion,
                -angle // rotationAngle in degrees negative for right
            );
        }

        //stat vertical
        float startX;
        if (numVerticalLasers % 2 == 0) {
            // Even put space mid
            startX = midX - (numVerticalLasers / 2 - 0.5f) * spacing;
        } else {
            // Odd put guy mid
            startX = midX - (numVerticalLasers / 2) * spacing;
        }

        // vertical lasers
        for (int i = 0; i < numVerticalLasers; i++) {
            float xPosition = startX + i * spacing;
            lasers[laserIndex++] = new Laser(
                xPosition,
                yPosition,
                laserWidth,
                laserHeight,
                0, // velocityX
                laserSpeed, // velocityY
                damagePerShot,
                laserTextureRegion,
                0 //no
            );
        }

        timeSinceLastShot = 0;
        return lasers;
    }
}
