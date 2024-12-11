package com.BrigBryu.SpaceShooter.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship {

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
        System.out.println("taking damage " + laser.damage);
        //take damage to health only if there is no shield
        if(shield > 0) {
            shield = (int) Math.max(0, shield - laser.damage);
        } else if(health > 0) {
            if(health - laser.damage == 0){
                health = 0;
                return true;
            }
            health = (int) Math.max(0, health - laser.damage);
        } else {
            return true;
        }
        return false; //still kickin
    }


}
