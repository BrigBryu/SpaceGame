package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemyShip extends Ship{
    public EnemyShip(float xCenter, float yCenter,
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
        lasers[0] =  new Laser(boundingBox.x + boundingBox.width /2 - laserWidth/2, boundingBox.y - laserHeight,
            laserWidth,laserHeight,laserSpeed,laserTextureRegion);
        timeSinceLastShot = 0;
        return lasers;
    }
}
