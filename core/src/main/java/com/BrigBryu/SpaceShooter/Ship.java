package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public abstract class Ship implements Boundable{
    //ship stuff
    float movementSpeed; //world units per second
    /**
     * most enemies will not have shield but all things that are rendered need to have health
     */
    int shield, health;
    //position
    protected Rectangle boundingBox;

    //graphics
    protected TextureRegion shipTextureRegion, shieldTextureRegion, laserTextureRegion;

    //laser info
    protected float laserWidth, laserHeight;
    protected float laserSpeed, laserTimeBetweenShots;
    protected float timeSinceLastShot = 0;
    protected float damagePerShot;
    /**
     * each diagonal laser has a pair so 1 numDiagonalLasers creates two lasaers
     */
    int numDiagonalLasers;
    int numVerticalLasers;


    public Ship(float xCenter, float yCenter,
                float width, float height,
                float movementSpeed, int shield, int health, int numVerticalLasers, int numDiagonalLasers,
                float laserWidth, float laserHeight,
                float laserSpeed, float laserTimeBetweenShots, float damagePerShot,
                TextureRegion shipTextureRegion, TextureRegion shieldTextureRegion, TextureRegion laserTextureRegion) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.health = health;
        this.numDiagonalLasers = numDiagonalLasers;
        this.numVerticalLasers = numVerticalLasers;
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
        if (shield > 0) {
            shield = (int) Math.max(0, shield - laser.damage);
        } else if (health > 0) {
            health = (int) Math.max(0, health - laser.damage);
        }

        if(health <= 0) {
            health = 0;
            return true; //destroyed
        }

        return false; //still alive
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

    public Laser[] fireLasers() {
        float angle = 10;

        float diagonalVelocityX = laserSpeed * MathUtils.sinDeg(angle);
        float diagonalVelocityY = laserSpeed * MathUtils.cosDeg(angle);

        int totalDiagonalLasers = numDiagonalLasers * 2; // diagonal laser has pair
        int totalLasers = totalDiagonalLasers + numVerticalLasers;

        Laser[] lasers = new Laser[totalLasers];

        float yPosition = boundingBox.y + boundingBox.height;
        float midX = boundingBox.x + boundingBox.width / 2.0f - laserWidth / 2.0f;
        float spacing = laserWidth + laserWidth/2; // TODO might be bad

        int laserIndex = 0;

        // diagonal lasers
        for (int i = 0; i < numDiagonalLasers; i++) {
            // Left diagonal
            float offsetXLeft = (numVerticalLasers / 2 + i + 1) * spacing;
            lasers[laserIndex++] = new Laser(
                midX - offsetXLeft,
                yPosition,
                laserWidth,
                laserHeight,
                -diagonalVelocityX, // velocityX (left)
                diagonalVelocityY,  // velocityY
                damagePerShot,
                laserTextureRegion,
                angle // rotationAngle in degrees
            );

            // Right diagonal
            float offsetXRight = (numVerticalLasers / 2 + i + 1) * spacing;
            lasers[laserIndex++] = new Laser(
                midX + offsetXRight,
                yPosition,
                laserWidth,
                laserHeight,
                diagonalVelocityX, // velocityX (right)
                diagonalVelocityY, // velocityY
                damagePerShot,
                laserTextureRegion,
                -angle // rotationAngle in degrees negative for right
            );
        }

        //stat vertical
        float startX;
        if (numVerticalLasers % 2 == 0) {
            // Even put space mid
            startX = midX - (numVerticalLasers / 2 - 0.5f) * spacing;
        } else {
            // Odd put guy mid
            startX = midX - (numVerticalLasers / 2) * spacing;
        }

        // vertical lasers
        for (int i = 0; i < numVerticalLasers; i++) {
            float xPosition = startX + i * spacing;
            lasers[laserIndex++] = new Laser(
                xPosition,
                yPosition,
                laserWidth,
                laserHeight,
                0, // velocityX
                laserSpeed, // velocityY
                damagePerShot,
                laserTextureRegion,
                0 //no
            );
        }

        timeSinceLastShot = 0;
        return lasers;
    }


    /**
     For all ship types
     upgradeDamage(int scalar)
     upgradeNumberOfShots()
        adds a laser when calling fireLasers
        1: 1 shot in middle
        2: 2 shots in middle
        2+n: 2 shots at angle on edge n shots in middle centered on middle shot or middle of two depending even or odd
     upgradeHealth(int scalar)
     upgradeShield(int scalar) will scale if there is shield otherwise need to call add shield
     addShield(int amount)
     */
    public void upgradeDamage(double scalar) {
        damagePerShot = (float) (damagePerShot * scalar);
    }

    public void upgradeNumberOfShotsDiagonal() {
        numDiagonalLasers += 1;
    }

    public void upgradeNumberOfShotsVertical() {
        numVerticalLasers += 1;
    }

    public void upgradeHealth(double scalar){
        health = (int) (health * scalar);
    }

    public void upgradeShield(double scalar){
        shield = (int) (shield * scalar);
    }

    public void addShield(int amount) {
        shield += amount;
    }
}
