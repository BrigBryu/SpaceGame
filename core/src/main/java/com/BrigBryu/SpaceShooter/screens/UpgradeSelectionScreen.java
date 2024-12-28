package com.BrigBryu.SpaceShooter.screens;

import com.BrigBryu.SpaceShooter.gameObjects.PlayerShip;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpgradeSelectionScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private PlayerShip playerShip;

    private final String[] upgradeStyles = {
        "damageUpgrade",
        "diagonalUpgrade",
        "verticalUpgrade",
        "fireRateUpgrade",
        "laserSpeedUpgrade"
    };

    public UpgradeSelectionScreen(PlayerShip playerShip) {
        this.playerShip = playerShip;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // 1) Load the atlas manually so we can set Nearest filtering for pixel art
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("fixedUpgradeSkin.atlas"));
        for (AtlasRegion region : atlas.getRegions()) {
            region.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        skin = new Skin(Gdx.files.internal("fixedUpgradeSkin.json"), atlas);

        // 2) Randomly pick two upgrade styles
        List<String> styleList = new ArrayList<>();
        Collections.addAll(styleList, upgradeStyles);
        Collections.shuffle(styleList);
        String styleTop = styleList.get(0);
        String styleBottom = styleList.get(1);

        // 3) Create two ImageButtons
        ImageButton topButton    = new ImageButton(skin, styleTop);
        ImageButton bottomButton = new ImageButton(skin, styleBottom);

        // 4) Make each button a large square to act as the frame
        float stageWidth  = stage.getViewport().getWorldWidth();
        float stageHeight = stage.getViewport().getWorldHeight();
        float sideLen     = Math.min(stageWidth, stageHeight) * 0.4f;

        topButton.setSize(sideLen, sideLen);
        bottomButton.setSize(sideLen, sideLen);

        // Position top vs. bottom
        topButton.setPosition(
            (stageWidth - sideLen) / 2,
            stageHeight - sideLen - 50
        );
        bottomButton.setPosition(
            (stageWidth - sideLen) / 2,
            50
        );

        // 5) Make the *icon* inside each button smaller
        //    so you can actually see the frame around it.
        //    For example, 60% of the button's size.
        float iconFraction = 0.6f;
        float iconSize     = sideLen * iconFraction;

        topButton.getImageCell()
            .size(iconSize)               // keep it square
            .expand()
            .center();
        topButton.getImage().setScaling(Scaling.fit);

        bottomButton.getImageCell()
            .size(iconSize)
            .expand()
            .center();
        bottomButton.getImage().setScaling(Scaling.fit);

        // 6) Listeners
        topButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                applyUpgrade(styleTop);
            }
        });
        bottomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                applyUpgrade(styleBottom);
            }
        });

        stage.addActor(topButton);
        stage.addActor(bottomButton);
    }

    private void applyUpgrade(String styleName) {
        switch (styleName) {
            case "damageUpgrade":
                playerShip.upgradeDamage(1.2f);
                break;
            case "diagonalUpgrade":
                playerShip.upgradeNumberOfShotsDiagonal();
                break;
            case "verticalUpgrade":
                playerShip.upgradeNumberOfShotsVertical();
                break;
            case "fireRateUpgrade":
                float timeBetween = playerShip.laserTimeBetweenShots;
                playerShip.laserTimeBetweenShots = Math.max(timeBetween - 0.1f, 0.1f);
                break;
            case "laserSpeedUpgrade":
                playerShip.laserSpeed *= 1.2f;
                break;
        }
        ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener())
            .setScreen(new GameScreen(playerShip));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
