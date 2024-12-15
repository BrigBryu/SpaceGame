package com.BrigBryu.SpaceShooter;

import com.BrigBryu.SpaceShooter.formations.Formation;
import com.BrigBryu.SpaceShooter.formations.FormationFactory;
import com.BrigBryu.SpaceShooter.formations.GrayFormationFactory;
import com.BrigBryu.SpaceShooter.gameObjects.*;
import com.BrigBryu.SpaceShooter.gameObjects.uiElements.InputUpgradeSystem;
import com.BrigBryu.SpaceShooter.gameObjects.uiElements.LibGDXUpgradeUI;
import com.BrigBryu.SpaceShooter.gameObjects.uiElements.ShipUpgradeSystem;
import com.BrigBryu.SpaceShooter.gameObjects.uiElements.UpgradeUI;
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

    //screen
    private Camera camera;
    private Viewport viewport;

    //Helper
    private TextureManager textureManager;

    //graphics
    private SpriteBatch spriteBatch;
    private TextureRegion[] backGrounds;


    //Animations
    private TextureRegion[] orangeExplosion;
    private float orangeExplosionTime = 0.7f;

    //timing
    private float[] backGroundOffsets = {0,0,0};
    private float backGroundMaxScrollingSpeed;
    private final float waveBreakTime = 1;
    private float timeSinceEnemiesKilled = 0;

    //world parameters
    private final int WORLD_WIDTH = 108;
    private final int WORLD_HEIGHT = 192;

    //game objects
    private PlayerShip playerShip;
    private Formation formation;
    private FormationFactory formationFactory;
    private LinkedList<Explosion> explosions;

    private LinkedList<Laser> playerLasers;
    private LinkedList<Laser> enemyLasers;

    //temp
    private boolean deathTimerRun = false;
    private  float timeSinceDeath = 0;

    //upgrades
    private ShipUpgradeSystem upgradeSystem;
    private float wavesSinceLastUpgrade = 0;
    private static final float UPGRADE_INTERVAL = 1;
    private boolean upgradeMenuActive = false;

    public GameScreen(){
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);

        //textureAtlas = new TextureAtlas("gray1.atlas");
        TextureManager.load("gray1.atlas");
        backGrounds = new TextureRegion[3];
        backGrounds[0] = TextureManager.getTexture("black");
        backGrounds[1] = TextureManager.getTexture("large");
        backGrounds[2] = TextureManager.getTexture("small");
//        backGrounds[0] = textureAtlas.findRegion("black");
//        backGrounds[1] = textureAtlas.findRegion("large");
//        backGrounds[2] = textureAtlas.findRegion("small");

        orangeExplosion = new TextureRegion[4];
        orangeExplosion[0] = TextureManager.getTexture("explosionOrange1");
        orangeExplosion[1] = TextureManager.getTexture("explosionOrange2");
        orangeExplosion[2] = TextureManager.getTexture("explosionOrange3");
        orangeExplosion[3] = TextureManager.getTexture("explosionOrange4");
//        orangeExplosion[0] = textureAtlas.findRegion("explosionOrange1");
//        orangeExplosion[1] = textureAtlas.findRegion("explosionOrange2");
//        orangeExplosion[2] = textureAtlas.findRegion("explosionOrange3");
//        orangeExplosion[3] = textureAtlas.findRegion("explosionOrange4");

        backGroundMaxScrollingSpeed = (float) WORLD_HEIGHT / 4;


        //Texture regions
//        playerShipTextureRegion = textureAtlas.findRegion("playerShipGray");
//        enemyShipTextureRegion = textureAtlas.findRegion("shipGray1");
//        playerShieldTextureRegion = textureAtlas.findRegion("shield");
//        enemyShieldTextureRegion = textureAtlas.findRegion("shield");
//        playerLaserTextureRegion = textureAtlas.findRegion("bulletArc1");
//        enemyLaserTextureRegion = textureAtlas.findRegion("bulletGray1");
        //game objects
        int size = 8;
        int sizeLaser = 9;
        playerShip = new PlayerShip(WORLD_WIDTH/2, WORLD_HEIGHT/4,
            size, size, 36,5, 25, 2, 1,
            3, 2,75,.5f,
            TextureManager.getTexture("playerShipGray"),
            TextureManager.getTexture("shield"),
            TextureManager.getTexture("bulletArc1"));

        formationFactory = new GrayFormationFactory();
        formation = formationFactory.createFormation(1.0f);
        playerLasers = new LinkedList<>();
        enemyLasers = new LinkedList<>();
        explosions = new LinkedList<>();

        spriteBatch = new SpriteBatch(); //Collects all graphical changes and displays all at once

        UpgradeUI ui = new LibGDXUpgradeUI();
        upgradeSystem = new InputUpgradeSystem(ui);
    }

    @Override
public void render(float deltaTime) {
    if(deathTimerRun) {
        if (timeSinceDeath > 2) {
            throw new RuntimeException("You Died!!!");
        } else {
            timeSinceDeath += deltaTime;
        }
    }

    spriteBatch.begin();
    renderBackGround(deltaTime);

    if(!deathTimerRun && !upgradeMenuActive) {
        detectInput(deltaTime);
        playerShip.update(deltaTime);
        spawnEnemyFormation(deltaTime);
    }

    if(!upgradeMenuActive) {    
        formation.update(deltaTime);
        enemyLasers.addAll(formation.fireLasers(deltaTime));
    }

    formation.draw(spriteBatch);


    if(!deathTimerRun) {
        playerShip.draw(spriteBatch);
    }

    renderLasers(deltaTime);
    detectCollisions();
    updateAndRenderExplosions(deltaTime);

    spriteBatch.end();

        checkUpgrades(deltaTime);
    }

    private void checkUpgrades(float deltaTime) {
        // If it's time to show upgrades and not already active, show them
        if (wavesSinceLastUpgrade >= UPGRADE_INTERVAL && !upgradeMenuActive) {
            upgradeMenuActive = true;
            upgradeSystem.showUpgradeOptions();
        }

        // If the upgrade menu is active, check for key presses
        if (upgradeMenuActive) {
            for (int i = 1; i <= 6; i++) {
                int key = Input.Keys.NUM_1 + (i - 1); 
                // Alternatively: Input.Keys.NUM_1, NUM_2, etc. directly
                if (Gdx.input.isKeyJustPressed(key)) {
                    if (upgradeSystem.isUpgradeAvailable(i)) {
                        upgradeSystem.handleUpgrade(i, playerShip);
                        // Once upgrade chosen, resume the game
                        upgradeMenuActive = false;
                        wavesSinceLastUpgrade = 0;
                    } else {
                        // If not available, you could show a message
                    }
                    break;
                }
            }
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
    
        if(timeSinceEnemiesKilled > waveBreakTime && !upgradeMenuActive) {
            System.out.println("Creating new formation...");
    
            float difficulty = Math.min(2.0f, 1.0f + timeSinceEnemiesKilled / 60.0f);
            formation = formationFactory.createFormation(difficulty);
            timeSinceEnemiesKilled = 0;
            wavesSinceLastUpgrade++;
        }
    }    

    private void moveEnemy(EnemyShipOld enemyShip, float deltaTime){
        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -enemyShip.getBoundingBox().x;
        downLimit = WORLD_HEIGHT/2f-enemyShip.getBoundingBox().y; //bound sto top half of screen
        rightLimit = WORLD_WIDTH - enemyShip.getBoundingBox().x - enemyShip.getBoundingBox().width;
        upLimit = WORLD_HEIGHT - enemyShip.getBoundingBox().y - enemyShip.getBoundingBox().height;

        float moveX = enemyShip.getDirectionVector().x * enemyShip.getMovementSpeed() * deltaTime;
        float moveY = enemyShip.getDirectionVector().y * enemyShip.getMovementSpeed() * deltaTime;

        if(moveX > 0) {
            //moving right
            moveX = Math.min(moveX, rightLimit);
        } else {
            //moving left
            moveX = Math.max(moveX, leftLimit);
        }

        if(moveY > 0) {
            //moving up
            moveY = Math.min(moveY, upLimit);
        } else {
            //moving down
            moveY = Math.max(moveY, downLimit);
        }

        enemyShip.translate(moveX,moveY);
    }

    private void detectCollisions(){
        //check player laser intersects an emeny
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
                        //add a square explosion
                        Rectangle rectangle = new Rectangle(
                            enemyShip.getBoundingBox().x,
                            enemyShip.getBoundingBox().y,
                            enemyShip.getBoundingBox().height,
                            enemyShip.getBoundingBox().height);

                        if(enemyShip.getBoundingBox().width > enemyShip.getBoundingBox().height) {
                            rectangle = new Rectangle(
                                enemyShip.getBoundingBox().x,
                                enemyShip.getBoundingBox().y,
                                enemyShip.getBoundingBox().width,
                                enemyShip.getBoundingBox().width);
                        }
                        explosions.add(new Explosion(orangeExplosion, rectangle, orangeExplosionTime));
                    }
                    if(!hitOnce) {
                        laserListIterator.remove();
                        hitOnce = true;
                    }
                    //No break because a laser can hit multiple enemies
                }
            }
        }

        //check enemy laser intersects player
        laserListIterator = enemyLasers.listIterator();
        while(laserListIterator.hasNext() && !deathTimerRun){
            Laser laser = laserListIterator.next();
            if(playerShip.intersects(laser.getBoundingBox())){
                if(playerShip.takeDamageAndCheckDestroyed(laser)){
                    Rectangle rectangle = new Rectangle(
                        playerShip.getBoundingBox().x,
                        playerShip.getBoundingBox().y,
                        playerShip.getBoundingBox().height,
                        playerShip.getBoundingBox().height);

                    if(playerShip.getBoundingBox().width > playerShip.getBoundingBox().height) {
                        rectangle = new Rectangle(
                            playerShip.getBoundingBox().x,
                            playerShip.getBoundingBox().y,
                            playerShip.getBoundingBox().width,
                            playerShip.getBoundingBox().width);
                    }
                    explosions.add(new Explosion(orangeExplosion, rectangle, orangeExplosionTime));
                    deathTimerRun = true;
//                    playerShip.setShield(25); //TODO quick fix for death no death!
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
            playerShip.translate(0f,Math.max(-playerShip.getMovementSpeed() * deltaTime, downLimit));
        }
    }

    private void renderLasers(float deltaTime){

        //create lasers
        if(playerShip.canFireLaser()){
            Laser[] lasers = playerShip.fireLasers();
            playerLasers.addAll(Arrays.asList(lasers));
        }

        ListIterator<Laser> iterator = playerLasers.listIterator();
        while(iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(spriteBatch);
//            laser.boundingBox.y += laser.movementSpeed*deltaTime;
            laser.update(deltaTime, true);
            if(laser.getBoundingBox().y > WORLD_HEIGHT) { //is the bottom fo laser off screen
                iterator.remove(); //remove last served
            }
        }

        iterator = enemyLasers.listIterator();
        while(iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(spriteBatch);
//            laser.boundingBox.y -= laser.movementSpeed*deltaTime;
            laser.update(deltaTime, false);
            if(laser.getBoundingBox().y + laser.getBoundingBox().height < 0) { //is the top of laser off screen
                iterator.remove();
            }
        }
    }

    private void renderBackGround(float deltaTime){
        backGroundOffsets[0] += 0;
        backGroundOffsets[1] += deltaTime * (backGroundMaxScrollingSpeed/4);
        backGroundOffsets[2] += deltaTime * (backGroundMaxScrollingSpeed/2);

        for(int i = 0; i < backGroundOffsets.length; i++){
            if(backGroundOffsets[i] > WORLD_HEIGHT) {
                backGroundOffsets[i] = 0;
            }
            //draw first bit
            spriteBatch.draw(backGrounds[i], 0, -backGroundOffsets[i], WORLD_WIDTH,WORLD_HEIGHT);
            //draw second bit
            spriteBatch.draw(backGrounds[i], 0, -backGroundOffsets[i] + WORLD_HEIGHT, WORLD_WIDTH,WORLD_HEIGHT);
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true); 
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {

    }
}
