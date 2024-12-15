package com.BrigBryu.SpaceShooter.gameObjects.uiElements;

import com.BrigBryu.SpaceShooter.gameObjects.Ship;

public class InputUpgradeSystem implements ShipUpgradeSystem {
    private static final double UPGRADE_SCALAR = 1.5;
    private static final int SHIELD_UPGRADE_AMOUNT = 10;

    private UpgradeUI ui;

    public InputUpgradeSystem(UpgradeUI ui) {
        this.ui = ui;
    }

    @Override
    public void showUpgradeOptions() {
        ui.showOptions();
    }

    @Override
    public void handleUpgrade(int choice, Ship ship) {
        switch (choice) {
            case 1:
                ship.upgradeDamage(UPGRADE_SCALAR);
                ui.showMessage("Damage upgraded!");
                break;
            case 2:
                ship.upgradeNumberOfShotsDiagonal();
                ui.showMessage("Added diagonal shot!");
                break;
            case 3:
                ship.upgradeNumberOfShotsVertical();
                ui.showMessage("Added vertical shot!");
                break;
            case 4:
                ship.upgradeHealth(UPGRADE_SCALAR);
                ui.showMessage("Health upgraded to: " + ship.getHealth());
                break;
            case 5:
                ship.upgradeShield(UPGRADE_SCALAR);
                ui.showMessage("Shield upgraded to: " + ship.getShield());
                break;
            case 6:
                ship.addShield(SHIELD_UPGRADE_AMOUNT);
                ui.showMessage("Shield increased to: " + ship.getShield());
                break;
            default:
                ui.showMessage("Invalid upgrade choice!");
        }
    }

    @Override
    public boolean isUpgradeAvailable(int choice) {
        return choice >= 1 && choice <= 6;
    }

    @Override
    public void choseUpgrade(int choice, Ship ship) {
    }
}