package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FireCheckerboard extends FormationFireLasers {
    private float timeSinceLastShot = 0;
    private float timeBetweenShots = 3f;

    public FireCheckerboard(float difficulty) {
        super();
        timeBetweenShots *= difficulty;
    }

    @Override
    protected List<Laser> fireLasers(float deltaTime) {
        timeSinceLastShot += deltaTime;
        if (timeSinceLastShot < timeBetweenShots) {
            return new ArrayList<>();
        }
        timeSinceLastShot = 0;

        List<Laser> lasers = new ArrayList<>();
        Ship[][] formation = parser.shipsToGrid(shipList);

        for (int i = 0; i < formation.length; i++) {
            for (int j = 0; j < formation[i].length; j++) {
                Ship ship = formation[i][j];
                // Checkerboard pattern: fire from ships where (i + j) is even
                if (ship != null && ((i + j) % 2 == 0)) {
                    Laser[] firedLasers = ship.fireLasers();
                    if (firedLasers != null) {
                        Collections.addAll(lasers, firedLasers);
                    }
                }
            }
        }

        return lasers;
    }
}
