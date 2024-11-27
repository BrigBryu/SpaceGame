package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class EnemyShip extends Ship{
    Vector2 directionVector;
    float timeSinceLastDirectionChange = 0;
    float directionChangeFrequency = 0.75f;

    public EnemyShip(float xCenter, float yCenter,
                      float width, float height,
                      float movementSpeed, int shield, int health,
                      float laserWidth, float laserHeight,
                      float laserSpeed, float laserTimeBetweenShots,
                      TextureRegion shipTextureRegion, TextureRegion shieldTextureRegion, TextureRegion laserTextureRegion) {
        super(xCenter, yCenter, width, height, movementSpeed, shield, health, 1, 0, laserWidth, laserHeight, laserSpeed, laserTimeBetweenShots, 10, shipTextureRegion, shieldTextureRegion, laserTextureRegion);
        directionVector = new Vector2(0,-1);
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        timeSinceLastDirectionChange += deltaTime;
        if(timeSinceLastDirectionChange > directionChangeFrequency) {
            randomizeDirectionVector();
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }
    }
    private void randomizeDirectionVector(){
        //strategist up is 0 right is positive
        double barring = SpaceShooterGame.random.nextDouble() * 6.283185; //2 pi
        directionVector.x = (float) Math.sin(barring);
        directionVector.y = (float) Math.cos(barring);
    }

    public Vector2 getDirectionVector(){
        return directionVector;
    }
}
