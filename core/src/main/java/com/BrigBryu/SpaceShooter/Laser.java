package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Laser {
    float xCenter, yPosition; //bottom center
    float width, height;

    float movementSpeed; //in world units per second

    //graphics
    TextureRegion textureRegion;

    /**
     * x y = bottom left
     */
    public Laser(float xPosition, float yPosition, float width, float height, float movementSpeed, TextureRegion textureRegion) {
        this.xCenter = xPosition - width/2;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
    }

    public void draw(Batch batch){
        batch.draw(textureRegion, xCenter, yPosition, width, height);
    }
}
