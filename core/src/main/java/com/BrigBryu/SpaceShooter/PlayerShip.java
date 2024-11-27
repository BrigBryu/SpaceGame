package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship{

    boolean shieldBroke = false;

    public PlayerShip(float xCenter, float yCenter,
                      float width, float height,
                      float movementSpeed, int shield, int health, int numVerticalLasers, int numDiagonalLasers,
                      float laserWidth, float laserHeight,
                      float laserSpeed, float laserTimeBetweenShots,
                      TextureRegion shipTextureRegion, TextureRegion shieldTextureRegion, TextureRegion laserTextureRegion) {
        super(xCenter, yCenter, width, height, movementSpeed, shield, health, numVerticalLasers, numDiagonalLasers, laserWidth, laserHeight, laserSpeed, laserTimeBetweenShots, 25, shipTextureRegion, shieldTextureRegion, laserTextureRegion);
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


}
