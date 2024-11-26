package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Ship implements Boundable{
    //ship stuff
    float movementSpeed; //world units per second
    int shield;
    //position
    protected Rectangle boundingBox;

    //graphics
    protected TextureRegion shipTextureRegion, shieldTextureRegion, laserTextureRegion;

    //laser info
    protected float laserWidth, laserHeight;
    protected float laserSpeed, laserTimeBetweenShots;
    protected float timeSinceLastShot = 0;
    protected float damagePerShot;


    public Ship(float xCenter, float yCenter,
                float width, float height,
                float movementSpeed, int shield,
                float laserWidth, float laserHeight,
                float laserSpeed, float laserTimeBetweenShots, float damagePerShot,
                TextureRegion shipTextureRegion, TextureRegion shieldTextureRegion, TextureRegion laserTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.boundingBox = new Rectangle(xCenter - width/2,yCenter - height/2,width,height);
        this.shipTextureRegion = shipTextureRegion;
        this.shieldTextureRegion = shieldTextureRegion;
        this.laserTextureRegion = laserTextureRegion;
        this.laserHeight = laserHeight;
        this.laserSpeed = laserSpeed;
        this.laserWidth = laserWidth;
        this.laserTimeBetweenShots = laserTimeBetweenShots;
        this.damagePerShot = damagePerShot;
    }

    public void update(float deltaTime){
        timeSinceLastShot += deltaTime;
    }

    public boolean canFireLaser(){
        //might need to be timeSinceLastShot - laserTimeBetweenShots >= 0
        return timeSinceLastShot >= laserTimeBetweenShots;
    }

    @Override
    public Rectangle getBoundingBox(){
        return boundingBox;
    }

    public boolean intersects(Rectangle other){
        return getBoundingBox().overlaps(other);
    }

    public boolean takeDamageAndCheckDestroyed(Laser laser){
        shield -= (int) laser.damage;
        if(shield <= 0) {
            shield = 0;
            return true; //destroyed
        }
        return false; //still kickin
    }

    public void translate(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
    }

    public void draw(Batch batch){
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y,boundingBox.width,boundingBox.height);
        //if there is still shield draw shield on top of the ship
        if (shield > 0 && shieldTextureRegion != null) {
            if (boundingBox.width > boundingBox.height) {
                batch.draw(shieldTextureRegion, boundingBox.x - boundingBox.width / 2 / 2, boundingBox.y - boundingBox.width / 2 / 2, boundingBox.width + boundingBox.width / 2, boundingBox.width + boundingBox.width / 2);
            } else {
                batch.draw(shieldTextureRegion, boundingBox.x - boundingBox.height / 2 / 2, boundingBox.y - boundingBox.height / 2 / 2, boundingBox.height + boundingBox.height / 2, boundingBox.height + boundingBox.height / 2);
            }
        }
    }

    public abstract Laser[] fireLasers();
}
