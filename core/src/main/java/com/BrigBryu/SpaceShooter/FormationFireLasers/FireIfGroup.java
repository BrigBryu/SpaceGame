package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationHelper;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FireIfGroup extends FormationFireLasers {
    float timeSinceLastShot = 0;
    float timeBetweenShots = 3;

    public FireIfGroup(FormationParser parser, float difficulty) {
        super(parser);
        timeBetweenShots *= difficulty;
    }

    @Override
    public List<Laser> fireLasers(float deltaTime) {
        timeSinceLastShot += deltaTime;
        if(timeBetweenShots > timeSinceLastShot) {
            return new ArrayList<>();
        }
        timeSinceLastShot = 0;
        List<Laser> lasers = new ArrayList<>();
        Ship[][] formation = parser.shipsToGrid(shipList);
        int size = 3; // Size of the square to check

        // Iterate from 1 to length - 2 to ensure a complete 3x3 square
        for (int i = 1; i < formation.length - 1; i++) {
            for (int j = 1; j < formation[i].length - 1; j++) {
                Ship ship = formation[i][j];
                if (ship != null && FormationHelper.filledSquare(size, formation, i, j)) {
                    // Fire lasers from the center ship
                    lasers.addAll(Arrays.asList(ship.fireLasers()));
                    System.out.println("Ship at (" + i + ", " + j + ") fired lasers.");
                }
            }
        }
        return lasers;
    }
}
