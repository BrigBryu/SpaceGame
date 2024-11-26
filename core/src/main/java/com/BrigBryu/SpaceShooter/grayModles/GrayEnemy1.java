package com.BrigBryu.SpaceShooter.grayModles;

import com.BrigBryu.SpaceShooter.Laser;
import com.BrigBryu.SpaceShooter.Ship;
import com.BrigBryu.SpaceShooter.helper.TextureManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GrayEnemy1 extends Ship {
    //levels of enemy
    //1 shipGray1 shoots 1 bulletGray1
    //2 shipGray4 shoots 1
    public GrayEnemy1(float xCenter, float yCenter){
        super(xCenter,yCenter,6, 4, 20, 0,
            3, 3, 45, 2f, 10,
            TextureManager.getTexture("shipGray1"),
            null,
            TextureManager.getTexture("bulletGray1"));
    }

    @Override
    public Laser[] fireLasers() {
        Laser[] lasers = new Laser[1];
        //one laser from mid
        lasers[0] =  new Laser(boundingBox.x + boundingBox.width /2 - laserWidth/2, boundingBox.y - laserHeight,
            laserWidth,laserHeight,
            laserSpeed,damagePerShot, laserTextureRegion);
        timeSinceLastShot = 0;
        return lasers;
    }
}
