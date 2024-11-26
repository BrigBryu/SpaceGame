package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship{

    float damage = 25;
    boolean shieldBroke = false;

    public PlayerShip(float xCenter, float yCenter,
                      float width, float height,
                      float movementSpeed, int shield,
                      float laserWidth, float laserHeight,
                      float laserSpeed, float laserTimeBetweenShots,
                      TextureRegion shipTextureRegion, TextureRegion shieldTextureRegion, TextureRegion laserTextureRegion) {
        super(xCenter, yCenter, width, height, movementSpeed, shield, laserWidth, laserHeight, laserSpeed, laserTimeBetweenShots, 25, shipTextureRegion, shieldTextureRegion, laserTextureRegion);
    }

    @Override
    public boolean takeDamageAndCheckDestroyed(Laser laser){
        shield -= (int) laser.damage;
        if(shield <= 0) {
            if(shieldBroke) {
                shield = 0;
                return true; //destroyed
            } else {
                shieldBroke = true;
                shield = 0;
                return false;
            }

        }
        return false; //still kickin
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] lasers = new Laser[1];
        //one laser from mid
        lasers[0] =  new Laser(boundingBox.x + boundingBox.width /2 - laserWidth/2, boundingBox.y+boundingBox.height,
            laserWidth,laserHeight,laserSpeed, damagePerShot, laserTextureRegion);
        timeSinceLastShot = 0;
        return lasers;
    }
}
