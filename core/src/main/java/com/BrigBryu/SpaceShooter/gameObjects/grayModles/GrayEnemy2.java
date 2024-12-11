package com.BrigBryu.SpaceShooter.gameObjects.grayModles;

import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.TextureManager;

public class GrayEnemy2 extends Ship {
    //levels of enemy
    //1 shipGray1 shoots 1 bulletGray1
    //2 shipGray4 shoots 1
    public GrayEnemy2(float xCenter, float yCenter){
        super(xCenter,yCenter,6, 4, 20, 0, 20, 2, 0,
            3, 3, 45, 2f, 15,
            TextureManager.getTexture("shipGray4"),
            null,
            TextureManager.getTexture("bulletGray2"));
    }

//    @Override
//    public Laser[] fireLasers() {
//        Laser[] lasers = new Laser[1];
//        //one laser from mid
//        lasers[0] =  new Laser(boundingBox.x + boundingBox.width /2 - laserWidth/2, boundingBox.y - laserHeight,
//            laserWidth,laserHeight,
//            laserSpeed,damagePerShot, laserTextureRegion);
//        timeSinceLastShot = 0;
//        return lasers;
//    }
}
