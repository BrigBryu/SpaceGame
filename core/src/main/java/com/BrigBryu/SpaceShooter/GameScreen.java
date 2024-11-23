package com.BrigBryu.SpaceShooter;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    //timing
    private float[] backGroundOffsets = {0,0,0};
    private float backGroundMaxScrollingSpeed;

    //world parameters
    private final int WORLD_WIDTH = 108;
    private final int WORLD_HEIGHT = 192;
//    private final int WORLD_WIDTH = 72;
//    private final int WORLD_HEIGHT = 128;

    //game objects
    private Ship playerShip, enemyShip;
    private LinkedList<Laser> playerLasers;
    private LinkedList<Laser> enemyLasers;

    public GameScreen(){
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);

        textureAtlasOld = new TextureAtlas("SpaceImiages.atlas");
        textureAtlas = new TextureAtlas("PixleSpace.atlas");
        backGrounds = new TextureRegion[3];
        backGrounds[0] = textureAtlas.findRegion("black");
        backGrounds[1] = textureAtlas.findRegion("large");
        backGrounds[2] = textureAtlas.findRegion("small");
//        backGrounds[0] = textureAtlasOld.findRegion("Starscape00");
//        backGrounds[1] = textureAtlasOld.findRegion("Starscape01");
//        backGrounds[2] = textureAtlasOld.findRegion("Starscape02");
//        backGrounds[3] = textureAtlasOld.findRegion("Starscape03");

        backGroundMaxScrollingSpeed = (float) WORLD_HEIGHT / 4;


        //Texture regions
        playerShipTextureRegion = textureAtlas.findRegion("playerShipGray");
        enemyShipTextureRegion = textureAtlas.findRegion("shipGray1");
        playerShieldTextureRegion = textureAtlasOld.findRegion("shield2");
        enemyShieldTextureRegion = textureAtlasOld.findRegion("shield1");
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
            size, size, 2,3,
            sizeLaser, sizeLaser,45,1f,
            playerShipTextureRegion,playerShieldTextureRegion, playerLaserTextureRegion);

        enemyShip = new EnemyShip(WORLD_WIDTH/2, WORLD_HEIGHT*3/4,
            size, size, 2,1,
            sizeLaser, sizeLaser,45,2f,
            enemyShipTextureRegion,enemyShieldTextureRegion, enemyLaserTextureRegion);

        playerLasers = new LinkedList<>();
        enemyLasers = new LinkedList<>();

        spriteBatch = new SpriteBatch(); //Collects all graphical changes and displays all at once
    }
    @Override
    public void render(float deltaTime) { //remember back to front
        spriteBatch.begin(); //begins building a batch to draw
        playerShip.update(deltaTime);
        enemyShip.update(deltaTime);
        renderBackGround(deltaTime);

        //enemies
        enemyShip.draw(spriteBatch);
        //player
        playerShip.draw(spriteBatch);
        //lasers create and draw
        //create lasers
        if(playerShip.canFireLaser()){
            Laser[] lasers = playerShip.fireLasers();
            playerLasers.addAll(Arrays.asList(lasers));
        }
        if(enemyShip.canFireLaser()){
            Laser[] lasers = enemyShip.fireLasers();
            enemyLasers.addAll(Arrays.asList(lasers));
        }
        //draw lasers and remove old lasers
        ListIterator<Laser> iterator = playerLasers.listIterator();
        while(iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(spriteBatch);
            laser.yPosition += laser.movementSpeed*deltaTime;
            if(laser.yPosition > WORLD_HEIGHT) { //is the bottom fo laser off screen
                iterator.remove(); //remove last served
            }
        }

        iterator = enemyLasers.listIterator();
        while(iterator.hasNext()){
            Laser laser = iterator.next();
            laser.draw(spriteBatch);
            laser.yPosition -= laser.movementSpeed*deltaTime;
            if(laser.yPosition + laser.height < 0) { //is the top of laser off screen
                iterator.remove();
            }
        }
        //explosions
        spriteBatch.end();
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
