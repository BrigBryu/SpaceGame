package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

    //graphics
    private SpriteBatch spriteBatch;
    private TextureAtlas textureAtlasOld;
    private TextureAtlas textureAtlas;

    private TextureRegion[] backGrounds;

    private TextureRegion playerShipTextureRegion, playerShieldTextureRegion, enemyShipTextureRegion,
        enemyShieldTextureRegion, playerLaserTextureRegion, enemyLaserTextureRegion;

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
//    private final int WORLD_WIDTH = 72;
//    private final int WORLD_HEIGHT = 128;

    //game objects
    private PlayerShip playerShip;
    private LinkedList<EnemyShip> enemyShips;
    private LinkedList<Explosion> explosions;

    private LinkedList<Laser> playerLasers;
    private LinkedList<Laser> enemyLasers;

    public GameScreen(){
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);

        textureAtlasOld = new TextureAtlas("SpaceImiages.atlas");
        textureAtlas = new TextureAtlas("gray1.atlas");
        backGrounds = new TextureRegion[3];
        backGrounds[0] = textureAtlas.findRegion("black");
        backGrounds[1] = textureAtlas.findRegion("large");
        backGrounds[2] = textureAtlas.findRegion("small");

        orangeExplosion = new TextureRegion[4];
        orangeExplosion[0] = textureAtlas.findRegion("explosionOrange1");
        orangeExplosion[1] = textureAtlas.findRegion("explosionOrange2");
        orangeExplosion[2] = textureAtlas.findRegion("explosionOrange3");
        orangeExplosion[3] = textureAtlas.findRegion("explosionOrange4");

//        backGrounds[0] = textureAtlasOld.findRegion("Starscape00");
//        backGrounds[1] = textureAtlasOld.findRegion("Starscape01");
//        backGrounds[2] = textureAtlasOld.findRegion("Starscape02");
//        backGrounds[3] = textureAtlasOld.findRegion("Starscape03");

        backGroundMaxScrollingSpeed = (float) WORLD_HEIGHT / 4;


        //Texture regions
        playerShipTextureRegion = textureAtlas.findRegion("playerShipGray");
        enemyShipTextureRegion = textureAtlas.findRegion("shipGray1");
        playerShieldTextureRegion = textureAtlas.findRegion("shield");
        enemyShieldTextureRegion = textureAtlas.findRegion("shield");
        playerLaserTextureRegion = textureAtlas.findRegion("bulletArc1");
        enemyLaserTextureRegion = textureAtlas.findRegion("bulletGray1");
//        playerShipTextureRegion = textureAtlasOld.findRegion("playerShip3_red");
//        enemyShipTextureRegion = textureAtlasOld.findRegion("enemyRed5");
//        playerShieldTextureRegion = textureAtlasOld.findRegion("shield2");
//        enemyShieldTextureRegion = textureAtlasOld.findRegion("shield1");
//        playerLaserTextureRegion = textureAtlasOld.findRegion("laserRed13");
//        enemyLaserTextureRegion = textureAtlasOld.findRegion("laserBlue03");
//        enemyShieldTextureRegion.flip(false,true); //make face down



        //game objects
        int size = 8;
        int sizeLaser = 9;
        playerShip = new PlayerShip(WORLD_WIDTH/2, WORLD_HEIGHT/4,
            size, size, 36,3,
            3, 2,45,1f,
            playerShipTextureRegion,playerShieldTextureRegion, playerLaserTextureRegion);

        enemyShips = new LinkedList<>();
        playerLasers = new LinkedList<>();
        enemyLasers = new LinkedList<>();
        explosions = new LinkedList<>();

        spriteBatch = new SpriteBatch(); //Collects all graphical changes and displays all at once
    }

    @Override
    public void render(float deltaTime) { //remember back to front
        spriteBatch.begin(); //begins building a batch to draw
        renderBackGround(deltaTime);

        detectInput(deltaTime);
        playerShip.update(deltaTime);

        spawnEnemyShips(deltaTime);

        ListIterator<EnemyShip> iterator = enemyShips.listIterator();
        while (iterator.hasNext()) {
            EnemyShip enemyShip = iterator.next();
            moveEnemy(enemyShip, deltaTime);
            fireEnemyLaser(enemyShip);
            enemyShip.update(deltaTime);
            enemyShip.draw(spriteBatch);
        }



        //player
        playerShip.draw(spriteBatch);

        //draw lasers and remove old lasers
        renderLasers(deltaTime);

        //laser and ship collision
        detectCollisions();
        //explosions
        updateAndRenderExplosions(deltaTime);

        spriteBatch.end();
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

    private void spawnEnemyShips(float deltaTime){
        if(enemyShips.isEmpty()) {
            timeSinceEnemiesKilled += deltaTime;
        }

        if(timeSinceEnemiesKilled > waveBreakTime) {
            //spawn wave
            enemyShips.add(new EnemyShip(WORLD_WIDTH / 2, WORLD_HEIGHT * 3 / 4,
                6, 4, 20, 1,
                3, 3, 45, 2f,
                enemyShipTextureRegion, enemyShieldTextureRegion, enemyLaserTextureRegion));
            timeSinceEnemiesKilled = 0;
        }
    }

    private void moveEnemy(EnemyShip enemyShip, float deltaTime){
        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -enemyShip.getBoundingBox().x;
        downLimit = WORLD_HEIGHT/2f-enemyShip.getBoundingBox().y; //bound sto top half of screen
        rightLimit = WORLD_WIDTH - enemyShip.getBoundingBox().x - enemyShip.getBoundingBox().width;
        upLimit = WORLD_HEIGHT - enemyShip.getBoundingBox().y - enemyShip.getBoundingBox().height;

        float moveX = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float moveY = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

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
            ListIterator<EnemyShip> enemyShipListIterator = enemyShips.listIterator();
            while (enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next();
                if (enemyShip.intersects(laser.getBoundingBox())) {
                    if(enemyShip.takeDamageAndCheckDestroyed(laser)){
                        enemyShipListIterator.remove();
                        Rectangle rectangle = new Rectangle(
                            enemyShip.boundingBox.x,
                            enemyShip.boundingBox.y,
                            enemyShip.boundingBox.height,
                            enemyShip.boundingBox.height);

                        if(enemyShip.boundingBox.width > enemyShip.boundingBox.height) {
                            rectangle = new Rectangle(
                                enemyShip.boundingBox.x,
                                enemyShip.boundingBox.y,
                                enemyShip.boundingBox.width,
                                enemyShip.boundingBox.width);
                        }
                        explosions.add(new Explosion(orangeExplosion, rectangle, orangeExplosionTime));
                    }
                    laserListIterator.remove();
                    //No break because a laser can hit multiple enemies
                }
            }
        }

        //check enemy laser intersects player
        laserListIterator = enemyLasers.listIterator();
        while(laserListIterator.hasNext()){
            Laser laser = laserListIterator.next();
            if(playerShip.intersects(laser.getBoundingBox())){
                if(playerShip.takeDamageAndCheckDestroyed(laser)){
                    Rectangle rectangle = new Rectangle(
                        playerShip.boundingBox.x,
                        playerShip.boundingBox.y,
                        playerShip.boundingBox.height,
                        playerShip.boundingBox.height);

                    if(playerShip.boundingBox.width > playerShip.boundingBox.height) {
                        rectangle = new Rectangle(
                            playerShip.boundingBox.x,
                            playerShip.boundingBox.y,
                            playerShip.boundingBox.width,
                            playerShip.boundingBox.width);
                    }
                    explosions.add(new Explosion(orangeExplosion, rectangle, orangeExplosionTime));
                    playerShip.shield = 3; //TODO quick fix
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
            playerShip.translate(Math.min(playerShip.movementSpeed * deltaTime, rightLimit), 0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W) && upLimit > 0) {
            playerShip.translate(0f, Math.min(playerShip.movementSpeed * deltaTime, upLimit));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A) && leftLimit < 0) {
            playerShip.translate(Math.max(-playerShip.movementSpeed * deltaTime, leftLimit), 0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S) && downLimit < 0) {
            playerShip.translate(0f,Math.max(-playerShip.movementSpeed * deltaTime, downLimit));
        }
    }

    private void fireEnemyLaser(EnemyShip enemyShip) {
        if(enemyShip.canFireLaser()){
            Laser[] lasers = enemyShip.fireLasers();
            enemyLasers.addAll(Arrays.asList(lasers));
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
            laser.boundingBox.y += laser.movementSpeed*deltaTime;
            if(laser.boundingBox.y > WORLD_HEIGHT) { //is the bottom fo laser off screen
                iterator.remove(); //remove last served
            }
        }

        iterator = enemyLasers.listIterator();
        while(iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(spriteBatch);
            laser.boundingBox.y -= laser.movementSpeed*deltaTime;
            if(laser.boundingBox.y + laser.boundingBox.height < 0) { //is the top of laser off screen
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
