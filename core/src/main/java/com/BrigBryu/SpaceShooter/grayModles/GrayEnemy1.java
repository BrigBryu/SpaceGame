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
        super(xCenter,yCenter,6, 4, 20, 0, 10, 1, 0,
            3, 3, 45, 2f, 10,
            TextureManager.getTexture("shipGray1"),
            null,
            TextureManager.getTexture("bulletGray1"));
    }
}
