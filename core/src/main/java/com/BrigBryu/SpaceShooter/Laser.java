package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser implements Boundable{
    protected Rectangle boundingBox;


    float movementSpeed; //in world units per second

    //graphics
    TextureRegion textureRegion;

    /**
     * x y = bottom left
     */
    public Laser(float xPosition, float yPosition, float width, float height, float movementSpeed, TextureRegion textureRegion) {
        this.boundingBox = new Rectangle(xPosition ,yPosition,width,height);
        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
    }

    public void draw(Batch batch){
        batch.draw(textureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    @Override
    public Rectangle getBoundingBox(){
        return boundingBox;
    }
}
