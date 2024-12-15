package com.BrigBryu.SpaceShooter.gameObjects.uiElements;

import com.BrigBryu.SpaceShooter.gameObjects.Ship;

public interface ShipUpgradeSystem {
    void showUpgradeOptions();
    void handleUpgrade(int choice, Ship ship);
    boolean isUpgradeAvailable(int choice);
    void choseUpgrade(int choice, Ship ship);
}
