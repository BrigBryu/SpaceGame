package com.BrigBryu.SpaceShooter.formations;

import com.BrigBryu.SpaceShooter.FormationFireLasers.FormationFireLasers;
import com.BrigBryu.SpaceShooter.formationMovement.FormationMove;
import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public abstract class Formation {
    protected FormationMove formationMove;
    protected FormationFireLasers formationFire;
    protected static FormationParser parser = new FormationParser();
    protected List<Ship> shipList;

    public Formation(FormationMove formationMove, FormationFireLasers formationFire){
        this.formationMove = formationMove;
        this.formationFire = formationFire;
        initializeShips();
    }
    public List<Ship> getShipList(){
        return shipList;
    }

    public void resetShips(){
        initializeShips();
    }
    public void draw(SpriteBatch batch){
        for(Ship ship: shipList) {
            ship.draw(batch);
        }
    }

    public void update(float deltaTime) {
        formationMove.update(deltaTime, shipList);
    }
    public List<Laser> fireLasers(float deltaTime) {
        return formationFire.update(deltaTime, shipList);
    }

    protected abstract void initializeShips();
}
