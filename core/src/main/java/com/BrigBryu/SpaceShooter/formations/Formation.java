package com.BrigBryu.SpaceShooter.formations;

import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;
import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.List;
import com.BrigBryu.SpaceShooter.FormationFireLasers.FormationFireLasers;
import com.BrigBryu.SpaceShooter.formationMovement.FormationMove;

public class Formation {
    protected FormationMove formationMove;
    protected FormationFireLasers formationFire;
    protected List<Ship> shipList;
    protected boolean isSetupMovement = false;
    protected boolean isSetupFire = false;
    protected boolean isSetupShipFormation = false;
    protected ShipFormation shipFormation;

    public Formation(FormationMove formationMove, FormationFireLasers formationFire, ShipFormation shipFormation) {
        this.formationMove = formationMove;
        this.formationFire = formationFire;
        this.shipFormation = shipFormation;
        this.shipList = shipFormation.getShips();
        
        isSetupMovement = formationMove != null;
        isSetupFire = formationFire != null;
        isSetupShipFormation = shipFormation != null && !shipList.isEmpty();
    }

    public List<Ship> getShipList(){
        if(!isSetupMovement || !isSetupFire || !isSetupShipFormation){
            throw new IllegalStateException("Formation is not setup. Movement: " + isSetupMovement + " Fire: " + isSetupFire + " ShipFormation: " + isSetupShipFormation);
        }
        return shipList;
    }

    public void resetShips(){
        if(!isSetupMovement || !isSetupFire || !isSetupShipFormation){
            throw new IllegalStateException("Formation is not setup. Movement: " + isSetupMovement + " Fire: " + isSetupFire + " ShipFormation: " + isSetupShipFormation);
        }
        shipList = shipFormation.getShips();
    }

    public void draw(SpriteBatch batch){
        if(!isSetupMovement || !isSetupFire || !isSetupShipFormation){
            throw new IllegalStateException("Formation is not setup. Movement: " + isSetupMovement + 
                " Fire: " + isSetupFire + " ShipFormation: " + isSetupShipFormation);
        }
        for(Ship ship: shipList) {
            ship.draw(batch);
        }
    }

    public void update(float deltaTime) {
        if(!isSetupMovement || !isSetupFire || !isSetupShipFormation){
            throw new IllegalStateException("Formation is not setup. Movement: " + isSetupMovement + " Fire: " + isSetupFire + " ShipFormation: " + isSetupShipFormation);
        }
        formationMove.update(deltaTime, shipList);
    }
    
    public List<Laser> fireLasers(float deltaTime) {
        if(!isSetupMovement || !isSetupFire || !isSetupShipFormation){
            throw new IllegalStateException("Formation is not setup. Movement: " + isSetupMovement + " Fire: " + isSetupFire + " ShipFormation: " + isSetupShipFormation);
        }
        return formationFire.update(deltaTime, shipList);
    }
}
