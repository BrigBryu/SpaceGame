package com.BrigBryu.SpaceShooter.gameObjects.grayModles;

import com.BrigBryu.SpaceShooter.gameObjects.Enemy;
import com.BrigBryu.SpaceShooter.helper.TextureManager;

public class GrayEnemy1 extends Enemy {
    public GrayEnemy1(float xCenter, float yCenter){
        super(xCenter, yCenter,
            6, 4,                    // width, height
            20, 0, 10,              // movementSpeed, shield, health
            3, 3,                   // laserWidth, laserHeight
            45, 2f,                 // laserSpeed, laserTimeBetweenShots
            TextureManager.getTexture("shipGray1"),
            null,
            TextureManager.getTexture("bulletGray1"),
            1);                     // type=1 for weakest enemy
    }
}
