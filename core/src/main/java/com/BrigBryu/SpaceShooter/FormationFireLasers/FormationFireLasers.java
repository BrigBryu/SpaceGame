package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.List;

public abstract class FormationFireLasers {
    protected List<Ship> shipList;
    protected FormationParser parser;
    
    public FormationFireLasers() {
        this.parser = FormationParser.getInstance();
    }

    public List<Laser> update(float deltaTime, List<Ship> shipList){
        this.shipList = shipList;
        return fireLasers(deltaTime);
    }

    protected abstract List<Laser> fireLasers(float deltaTime);
}
