package com.BrigBryu.SpaceShooter.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
}
