package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Explosion {
    private Animation<TextureRegion> explosionAnimation;
    private float explosionTimer;
    private Rectangle boundingBox;

    public Explosion(TextureRegion[] frames, Rectangle boundingBox, float animationLength){
        explosionAnimation = new Animation<TextureRegion>(animationLength/frames.length, frames);
        this.boundingBox = boundingBox;
        explosionTimer = 0;
    }

    public void update(float deltaTime){
        explosionTimer += deltaTime;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(explosionAnimation.getKeyFrame(explosionTimer),
            boundingBox.x,
            boundingBox.y,
            boundingBox. width,
            boundingBox.height);
    }

    public boolean finished(){
        return explosionAnimation.isAnimationFinished(explosionTimer);
    }
}
