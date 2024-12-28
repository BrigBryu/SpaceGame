package com.BrigBryu.SpaceShooter.screens;

import com.BrigBryu.SpaceShooter.gameObjects.PlayerShip;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpgradeSelectionScreen implements Screen {

    private Stage stage;
    private Skin skin;

    // We now receive the PlayerShip from outside instead of creating our own
    private PlayerShip playerShip;

    // Possible style names for your upgrade buttons, as defined in upgradeButtonsUISkin.json
    private final String[] upgradeStyles = {
        "damageUpgrade",
        "diagonalUpgrade",
        "verticalUpgrade",
        "fireRateUpgrade",
        "laserSpeedUpgrade"
    };

    /**
     * CONSTRUCTOR accepts a PlayerShip instance. This is the key change.
     */
    public UpgradeSelectionScreen(PlayerShip playerShip) {
        this.playerShip = playerShip;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load skin from assets (the JSON file references your PNGs)
        skin = new Skin(Gdx.files.internal("upgradeButtonsUISkin.json"));

        // Randomly pick two upgrade styles
        List<String> styleList = new ArrayList<>();
        Collections.addAll(styleList, upgradeStyles);
        Collections.shuffle(styleList);
        String styleLeft = styleList.get(0);
        String styleRight = styleList.get(1);

        // Create two ImageButtons using those styles
        ImageButton leftButton = new ImageButton(skin, styleLeft);
        ImageButton rightButton = new ImageButton(skin, styleRight);

        // Position them on screen
        leftButton.setPosition(100, 300);
        rightButton.setPosition(300, 300);

        // Left button click: upgrade the ship depending on style
        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                applyUpgrade(styleLeft);
            }
        });

        // Right button click: upgrade the ship depending on style
        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                applyUpgrade(styleRight);
            }
        });

        // Add buttons to stage
        stage.addActor(leftButton);
        stage.addActor(rightButton);
    }

    /**
     * Helper method to apply an upgrade on the given style.
     */
    private void applyUpgrade(String styleName) {
        switch (styleName) {
            case "damageUpgrade":
                playerShip.upgradeDamage(1.2); // e.g. +20% damage
                Gdx.app.log("Upgrade", "Damage upgrade applied!");
                break;
            case "diagonalUpgrade":
                playerShip.upgradeNumberOfShotsDiagonal();
                Gdx.app.log("Upgrade", "Diagonal lasers +1!");
                break;
            case "verticalUpgrade":
                playerShip.upgradeNumberOfShotsVertical();
                Gdx.app.log("Upgrade", "Vertical laser +1!");
                break;
            case "fireRateUpgrade":
                float currentTimeBetweenShots = playerShip.laserTimeBetweenShots;
                playerShip.laserTimeBetweenShots = Math.max(currentTimeBetweenShots - 0.1f, 0.1f);
                Gdx.app.log("Upgrade", "Firing faster!");
                break;
            case "laserSpeedUpgrade":
                playerShip.laserSpeed *= 1.2f;
                Gdx.app.log("Upgrade", "Laser speed upgraded!");
                break;
        }

        //  switch back to the GameScreen
         ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener())
                .setScreen(new GameScreen());
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
