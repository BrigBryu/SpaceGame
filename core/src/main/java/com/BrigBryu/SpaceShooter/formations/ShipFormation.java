package com.BrigBryu.SpaceShooter.formations;

import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;
import java.util.List;

public class ShipFormation {
    private String pathToFormation;
    private List<Ship> ships;
    private FormationParser parser;

    public ShipFormation(String pathToFormation) {
        this.pathToFormation = pathToFormation;
        this.parser = FormationParser.getInstance();
        this.ships = parser.getShips(pathToFormation);
    }

    public List<Ship> getShips() {
        return ships;
    }
}