package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship{

    public PlayerShip(float xCenter, float yCenter,
                      float width, float height,
                      float movementSpeed, int shield,
                      float laserWidth, float laserHeight,
                      float laserSpeed, float laserTimeBetweenShots,
                      TextureRegion shipTextureRegion, TextureRegion shieldTextureRegion, TextureRegion laserTextureRegion) {
        super(xCenter, yCenter, width, height, movementSpeed, shield, laserWidth, laserHeight, laserSpeed, laserTimeBetweenShots, shipTextureRegion, shieldTextureRegion, laserTextureRegion);
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] lasers = new Laser[1];
        //one laser from mid
        lasers[0] =  new Laser(xPosition + width /2, yPosition+height,
            laserWidth,laserHeight,laserSpeed,laserTextureRegion);
        timeSinceLastShot = 0;
        return lasers;
    }
}
