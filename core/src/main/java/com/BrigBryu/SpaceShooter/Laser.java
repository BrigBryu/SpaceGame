package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser implements Boundable{
    protected Rectangle boundingBox;


    float movementSpeed; //in world units per second
    float damage;

    //graphics
    TextureRegion textureRegion;

    /**
     * x y = bottom left
     */
    public Laser(float xPosition, float yPosition, float width, float height, float movementSpeed, float damage, TextureRegion textureRegion) {
        this.boundingBox = new Rectangle(xPosition ,yPosition,width,height);
        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
        this.damage = damage;
    }

    public void draw(Batch batch){
        batch.draw(textureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    @Override
    public Rectangle getBoundingBox(){
        return boundingBox;
    }
}
