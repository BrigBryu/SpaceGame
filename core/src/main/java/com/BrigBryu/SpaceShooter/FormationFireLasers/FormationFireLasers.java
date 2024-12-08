package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.Laser;
import com.BrigBryu.SpaceShooter.Ship;
import com.BrigBryu.SpaceShooter.FormationMove.FormationMove;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.List;

public abstract class FormationFireLasers {
    protected static FormationParser parser = new FormationParser();
    protected List<Ship> shipList;
    public FormationFireLasers(FormationParser parser) {
        FormationFireLasers.parser = parser;
    }

    public List<Laser> update(float deltaTime, List<Ship> shipList){
        this.shipList = shipList;
        return fireLasers(deltaTime);
    }

    protected abstract List<Laser> fireLasers(float deltaTime);
}
