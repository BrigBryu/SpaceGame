package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser implements Boundable {
    protected Rectangle boundingBox;

    private float velocityX;
    private float velocityY;

    float damage;

    TextureRegion textureRegion;

    /**
     * degrees
      */
    private float rotationAngle;

    /**
     * Constructor for Laser messy as fuck
     *
     * @param xPosition      Initial X position
     * @param yPosition      Initial Y position
     * @param width          Width of the laser
     * @param height         Height of the laser
     * @param velocityX      Velocity in the X direction (units per second)
     * @param velocityY      Velocity in the Y direction (units per second)
     * @param damage         Damage per shot
     * @param textureRegion  Texture of the laser
     * @param rotationAngle  Rotation angle in degrees
     */
    public Laser(float xPosition, float yPosition, float width, float height,
                 float velocityX, float velocityY, float damage, TextureRegion textureRegion,
                 float rotationAngle) {
        this.boundingBox = new Rectangle(xPosition, yPosition, width, height);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.damage = damage;
        this.textureRegion = textureRegion;
        this.rotationAngle = rotationAngle;
    }

    public void update(float deltaTime, boolean playerLaser) {
        boundingBox.x += velocityX * deltaTime;
        if(playerLaser) {
            boundingBox.y += velocityY * deltaTime;
        } else {
            boundingBox.y -= velocityY * deltaTime;
        }
    }

    public void draw(Batch batch) {

        batch.draw(
            textureRegion,
            boundingBox.x, // x position
            boundingBox.y, // y position
            boundingBox.width / 2, // originX (relative to x)
            boundingBox.height / 2, // originY (relative to y)
            boundingBox.width, // width
            boundingBox.height, // height
            1, // scaleX
            1, // scaleY
            rotationAngle // rotation in degrees
        );
    }

    @Override
    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}
