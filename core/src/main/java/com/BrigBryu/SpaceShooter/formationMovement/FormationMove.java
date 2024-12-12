package com.BrigBryu.SpaceShooter.formationMovement;

import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.List;

public abstract class FormationMove {
    protected FormationParser parser;
    protected List<Ship> shipList;
    
    public FormationMove() {
        this.parser = FormationParser.getInstance();
    }

    public void update(float deltaTime, List<Ship> shipList){
        this.shipList = shipList;
        updateShips(deltaTime);
    }

    public abstract void updateShips(float deltaTime);
}
