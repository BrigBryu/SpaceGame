package com.BrigBryu.SpaceShooter.screens;

import com.BrigBryu.SpaceShooter.formations.Formation;
import com.BrigBryu.SpaceShooter.formations.FormationFactory;
import com.BrigBryu.SpaceShooter.formations.GrayFormationFactory;
import com.BrigBryu.SpaceShooter.gameObjects.*;
import com.BrigBryu.SpaceShooter.helper.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen implements Screen {

    // screen
    private Camera camera;
    private Viewport viewport;

    // graphics
    private SpriteBatch spriteBatch;
    private TextureRegion[] backGrounds;

    // animations
    private TextureRegion[] orangeExplosion;
    private float orangeExplosionTime = 0.7f;

    // timing
    private float[] backGroundOffsets = {0,0,0};
    private float backGroundMaxScrollingSpeed;
    private final float waveBreakTime = 1;
    private float timeSinceEnemiesKilled = 0;

    // world parameters
    private final int WORLD_WIDTH = 108;
    private final int WORLD_HEIGHT = 192;

    // game objects
    private PlayerShip playerShip;
    private Formation formation;
    private FormationFactory formationFactory;
    private LinkedList<Explosion> explosions;
    private LinkedList<Laser> playerLasers;
    private LinkedList<Laser> enemyLasers;

    // temp
    private boolean deathTimerRun = false;
    private float timeSinceDeath = 0;

    // upgrade timing
    private float wavesSinceLastUpgrade = 0;
    private static final float UPGRADE_INTERVAL = 1; // show upgrade screen every N waves

    public GameScreen(PlayerShip playerShip) {
        this();
        this.playerShip = playerShip;
    }
    public GameScreen(){
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);

        TextureManager.load("gray1.atlas");
        backGrounds = new TextureRegion[3];
        backGrounds[0] = TextureManager.getTexture("black");
        backGrounds[1] = TextureManager.getTexture("large");
        backGrounds[2] = TextureManager.getTexture("small");

        orangeExplosion = new TextureRegion[4];
        orangeExplosion[0] = TextureManager.getTexture("explosionOrange1");
        orangeExplosion[1] = TextureManager.getTexture("explosionOrange2");
        orangeExplosion[2] = TextureManager.getTexture("explosionOrange3");
        orangeExplosion[3] = TextureManager.getTexture("explosionOrange4");

        backGroundMaxScrollingSpeed = (float) WORLD_HEIGHT / 4;

        // simple PlayerShip
        int size = 8;
        playerShip = new PlayerShip(
            WORLD_WIDTH / 2, WORLD_HEIGHT / 4,
            size, size,
            36,
            5,
            25,
            2,
            1,
            3, 2,
            75, 0.7f,
            TextureManager.getTexture("playerShipGray"),
            TextureManager.getTexture("shield"),
            TextureManager.getTexture("bulletArc1")
        );

        formationFactory = new GrayFormationFactory();
        formation = formationFactory.createFormation(1.0f);

        playerLasers = new LinkedList<>();
        enemyLasers = new LinkedList<>();
        explosions = new LinkedList<>();

        spriteBatch = new SpriteBatch();
    }

    @Override
    public void render(float deltaTime) {
        // death
        if(deathTimerRun) {
            if (timeSinceDeath > 2) {
                throw new RuntimeException("You Died!!!");
            } else {
                timeSinceDeath += deltaTime;
            }
        }

        // Begin drawing
        spriteBatch.begin();
        renderBackGround(deltaTime);

        formation.draw(spriteBatch);
        playerShip.draw(spriteBatch);

        // Update gameplay if not dead
        if(!deathTimerRun) {
            detectInput(deltaTime);
            playerShip.update(deltaTime);
            spawnEnemyFormation(deltaTime);

            formation.update(deltaTime);
            enemyLasers.addAll(formation.fireLasers(deltaTime));

            renderLasers(deltaTime);
            detectCollisions();
            updateAndRenderExplosions(deltaTime);
        }

        spriteBatch.end();

        checkUpgrades();
    }

    /**
     * If enough waves have passed, switch to the UpgradeSelectionScreen.
     */
    private void checkUpgrades() {
        if (wavesSinceLastUpgrade >= UPGRADE_INTERVAL) {
            // Reset wave counter
            wavesSinceLastUpgrade = 0;

            // Switch to the UpgradeSelectionScreen playerShip
            ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener())
                .setScreen(new UpgradeSelectionScreen(playerShip));
        }
    }

    private void updateAndRenderExplosions(float deltaTime){
        ListIterator<Explosion> explosionListIterator = explosions.listIterator();
        while(explosionListIterator.hasNext()) {
            Explosion explosion = explosionListIterator.next();
            explosion.update(deltaTime);
            if(explosion.finished()){
                explosionListIterator.remove();
            } else {
                explosion.draw(spriteBatch);
            }
        }
    }

    private void spawnEnemyFormation(float deltaTime) {
        if(formation.getShipList().isEmpty()) {
            timeSinceEnemiesKilled += deltaTime;
        }

        if(timeSinceEnemiesKilled > waveBreakTime) {
            System.out.println("Creating new formation...");

            float difficulty = Math.min(2.0f, 1.0f + timeSinceEnemiesKilled / 60.0f);
            formation = formationFactory.createFormation(difficulty);
            timeSinceEnemiesKilled = 0;

            wavesSinceLastUpgrade++;
        }
    }

    private void detectCollisions(){
        // Player lasers vs. enemies
        ListIterator<Laser> laserListIterator = playerLasers.listIterator();
        while(laserListIterator.hasNext()){
            Laser laser = laserListIterator.next();
            ListIterator<Ship> enemyShipListIterator = formation.getShipList().listIterator();
            boolean hitOnce = false;
            while (enemyShipListIterator.hasNext()) {
                Ship enemyShip = enemyShipListIterator.next();
                if (enemyShip.intersects(laser.getBoundingBox())) {
                    if(enemyShip.takeDamageAndCheckDestroyed(laser)){
                        enemyShipListIterator.remove();

                        // create an Explosion
                        Rectangle rectangle = new Rectangle(
                            enemyShip.getBoundingBox().x,
                            enemyShip.getBoundingBox().y,
                            enemyShip.getBoundingBox().height,
                            enemyShip.getBoundingBox().height
                        );
                        if(enemyShip.getBoundingBox().width > enemyShip.getBoundingBox().height) {
                            rectangle = new Rectangle(
                                enemyShip.getBoundingBox().x,
                                enemyShip.getBoundingBox().y,
                                enemyShip.getBoundingBox().width,
                                enemyShip.getBoundingBox().width
                            );
                        }
                        explosions.add(new Explosion(orangeExplosion, rectangle, orangeExplosionTime));
                    }
                    if(!hitOnce) {
                        laserListIterator.remove();
                        hitOnce = true;
                    }
                }
            }
        }

        // Enemy lasers vs. player
        laserListIterator = enemyLasers.listIterator();
        while(laserListIterator.hasNext() && !deathTimerRun){
            Laser laser = laserListIterator.next();
            if(playerShip.intersects(laser.getBoundingBox())){
                if(playerShip.takeDamageAndCheckDestroyed(laser)){
                    // create an Explosion
                    Rectangle rectangle = new Rectangle(
                        playerShip.getBoundingBox().x,
                        playerShip.getBoundingBox().y,
                        playerShip.getBoundingBox().height,
                        playerShip.getBoundingBox().height
                    );
                    if(playerShip.getBoundingBox().width > playerShip.getBoundingBox().height) {
                        rectangle = new Rectangle(
                            playerShip.getBoundingBox().x,
                            playerShip.getBoundingBox().y,
                            playerShip.getBoundingBox().width,
                            playerShip.getBoundingBox().width
                        );
                    }
                    explosions.add(new Explosion(orangeExplosion, rectangle, orangeExplosionTime));
                    deathTimerRun = true;
                }
                laserListIterator.remove();
            }
        }
    }

    private void detectInput(float deltaTime) {
        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -playerShip.getBoundingBox().x;
        downLimit = -playerShip.getBoundingBox().y;
        rightLimit = WORLD_WIDTH - playerShip.getBoundingBox().x - playerShip.getBoundingBox().width;
        upLimit = WORLD_HEIGHT/2f - playerShip.getBoundingBox().y - playerShip.getBoundingBox().height;

        if(Gdx.input.isKeyPressed(Input.Keys.D) && rightLimit > 0) {
            playerShip.translate(Math.min(playerShip.getMovementSpeed() * deltaTime, rightLimit), 0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W) && upLimit > 0) {
            playerShip.translate(0f, Math.min(playerShip.getMovementSpeed() * deltaTime, upLimit));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A) && leftLimit < 0) {
            playerShip.translate(Math.max(-playerShip.getMovementSpeed() * deltaTime, leftLimit), 0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S) && downLimit < 0) {
            playerShip.translate(0f, Math.max(-playerShip.getMovementSpeed() * deltaTime, downLimit));
        }
    }

    private void renderLasers(float deltaTime){
        // Fire player lasers
        if(playerShip.canFireLaser()){
            Laser[] lasers = playerShip.fireLasers();
            playerLasers.addAll(Arrays.asList(lasers));
        }

        // Update/draw player lasers
        ListIterator<Laser> iterator = playerLasers.listIterator();
        while(iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(spriteBatch);
            laser.update(deltaTime, true);
            if(laser.getBoundingBox().y > WORLD_HEIGHT) {
                iterator.remove();
            }
        }

        // Update/draw enemy lasers
        iterator = enemyLasers.listIterator();
        while(iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(spriteBatch);
            laser.update(deltaTime, false);
            if(laser.getBoundingBox().y + laser.getBoundingBox().height < 0) {
                iterator.remove();
            }
        }
    }

    private void renderBackGround(float deltaTime){
        // Parallax-like background
        backGroundOffsets[0] += 0;
        backGroundOffsets[1] += deltaTime * (backGroundMaxScrollingSpeed/4);
        backGroundOffsets[2] += deltaTime * (backGroundMaxScrollingSpeed/2);

        for(int i = 0; i < backGroundOffsets.length; i++){
            if(backGroundOffsets[i] > WORLD_HEIGHT) {
                backGroundOffsets[i] = 0;
            }
            //draw first part
            spriteBatch.draw(backGrounds[i], 0, -backGroundOffsets[i],
                WORLD_WIDTH, WORLD_HEIGHT);
            //draw second part
            spriteBatch.draw(backGrounds[i], 0, -backGroundOffsets[i] + WORLD_HEIGHT,
                WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void show() { }
    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
