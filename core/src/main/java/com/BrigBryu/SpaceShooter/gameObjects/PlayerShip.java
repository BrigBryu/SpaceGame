package com.BrigBryu.SpaceShooter.gameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship {

    boolean shieldBroke = false;
    float shieldRegenDelay = 4.0f;
    float shieldRegenDelayTimmer = 0.0f;
    int maxShield = 10;

    public PlayerShip(float xCenter, float yCenter,
                      float width, float height,
                      float movementSpeed, int shield, int health, int numVerticalLasers, int numDiagonalLasers,
                      float laserWidth, float laserHeight,
                      float laserSpeed, float laserTimeBetweenShots,
                      TextureRegion shipTextureRegion, TextureRegion shieldTextureRegion, TextureRegion laserTextureRegion) {
        super(xCenter, yCenter, width, height, movementSpeed, shield, health, numVerticalLasers, numDiagonalLasers, laserWidth, laserHeight, laserSpeed, laserTimeBetweenShots, 25, shipTextureRegion, shieldTextureRegion, laserTextureRegion);
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        if(shieldBroke && shield <= 0){ //if shield is broken and no shield start regening
            if(shieldRegenDelay < shieldRegenDelayTimmer){
                shield = maxShield;
                System.out.println("shield: " + shield);
            } else {
                shieldRegenDelayTimmer += deltaTime;
            }
        }
    }

    @Override
    public boolean takeDamageAndCheckDestroyed(Laser laser){
        System.out.println("taking damage " + laser.damage);
        //take damage to health only if there is no shield
        if(shield > 0) {
            shield = (int) Math.max(0, shield - laser.damage);
            shieldRegenDelayTimmer = 0.0f; //reset shield delay if taking damge to the shield
            if(shield <= 0){
                shieldBroke = true;
                shield = 0;
            }
        } else if(health > 0) {
            //dont change shieldRegenDelay if taking damage to health
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
