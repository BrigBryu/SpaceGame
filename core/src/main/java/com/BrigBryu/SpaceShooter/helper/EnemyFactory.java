package com.BrigBryu.SpaceShooter.helper;

import com.BrigBryu.SpaceShooter.Ship;
import com.BrigBryu.SpaceShooter.grayModles.GrayEnemy1;
import com.BrigBryu.SpaceShooter.grayModles.GrayEnemy2;

public class EnemyFactory {
    public static Ship createEnemy(String type, float x, float y) {
        switch (type) {
            case "g1":
                return new GrayEnemy1(x, y);
            case "g2":
                return new GrayEnemy2(x, y);
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }
    }
}
