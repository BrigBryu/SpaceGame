package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Ship {
    //ship stuff
    float movementSpeed; //world units per second
    int shield;
    //position
    /**
     * bottom left
     */
    float xPosition, yPosition;//bottom left of ship
    float width, height;
    //graphics
    TextureRegion shipTextureRegion, shieldTextureRegion, laserTextureRegion;

    //laser info
    float laserWidth, laserHeight;
    float laserSpeed, laserTimeBetweenShots;
    float timeSinceLastShot = 0;

    public Ship(float xCenter, float yCenter,
                float width, float height,
                float movementSpeed, int shield,
                float laserWidth, float laserHeight,
                float laserSpeed, float laserTimeBetweenShots,
                TextureRegion shipTextureRegion, TextureRegion shieldTextureRegion, TextureRegion laserTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.xPosition = xCenter - width/2;
        this.yPosition = yCenter - height/2;
        this.width = width;
        this.height = height;
        this.shipTextureRegion = shipTextureRegion;
        this.shieldTextureRegion = shieldTextureRegion;
        this.laserTextureRegion = laserTextureRegion;
        this.laserHeight = laserHeight;
        this.laserSpeed = laserSpeed;
        this.laserWidth = laserWidth;
        this.laserTimeBetweenShots = laserTimeBetweenShots;
    }

    public void update(float deltaTime){
        timeSinceLastShot += deltaTime;
    }

    public boolean canFireLaser(){
        //might need to be timeSinceLastShot - laserTimeBetweenShots >= 0
        return timeSinceLastShot >= laserTimeBetweenShots;
    }

    public void draw(Batch batch){
        batch.draw(shipTextureRegion, xPosition, yPosition,width,height);
        //if there is still shield draw shield ontop of the ship
        if(shield > 0){
            batch.draw(shieldTextureRegion, xPosition,yPosition,width,height);
        }
    }

    public abstract Laser[] fireLasers();
}
