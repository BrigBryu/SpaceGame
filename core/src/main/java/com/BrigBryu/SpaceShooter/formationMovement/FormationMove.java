package com.BrigBryu.SpaceShooter.formationMovement;

import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.List;

public abstract class FormationMove {
    protected static FormationParser parser = new FormationParser();
    protected List<Ship> shipList;
    public FormationMove(FormationParser parser) {
        FormationMove.parser = parser;
    }

    public void update(float deltaTime, List<Ship> shipList){
        this.shipList = shipList;
        updateShips(deltaTime);
    }

    public abstract void updateShips(float deltaTime);
}
