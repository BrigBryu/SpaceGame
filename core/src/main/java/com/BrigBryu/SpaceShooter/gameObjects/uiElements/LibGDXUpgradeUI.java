package com.BrigBryu.SpaceShooter.gameObjects.uiElements;

public class LibGDXUpgradeUI implements UpgradeUI {
    @Override
    public void showOptions() {
        System.out.println("=== UPGRADE OPTIONS ===");
        System.out.println("Press a number key (1-6) to choose:");
        System.out.println("1. Upgrade Damage");
        System.out.println("2. Add Diagonal Shot");
        System.out.println("3. Add Vertical Shot");
        System.out.println("4. Upgrade Health");
        System.out.println("5. Upgrade Shield");
        System.out.println("6. Add Shield");
        System.out.println("=======================");
    }

    @Override
    public void showMessage(String msg) {
        System.out.println(msg);
    }
}