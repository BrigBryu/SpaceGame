package com.BrigBryu.SpaceShooter.gameObjects.grayModles;

import com.BrigBryu.SpaceShooter.gameObjects.Enemy;
import com.BrigBryu.SpaceShooter.helper.TextureManager;

public class GrayEnemy2 extends Enemy {
    public GrayEnemy2(float xCenter, float yCenter){
        super(xCenter, yCenter,
            6, 4,                    // width, height
            20, 0, 20,              // movementSpeed, shield, health
            3, 3,                   // laserWidth, laserHeight
            45, 2f,                 // laserSpeed, laserTimeBetweenShots
            TextureManager.getTexture("shipGray4"),
            null,
            TextureManager.getTexture("bulletGray2"),
            2);                     // type=2 for stronger enemy
    }
}
