package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FireRowByRowTopDown extends FormationFireLasers {
    private float timeSinceLastShot = 0;
    private float timeBetweenShots = .8f; // Delay between firing each row
    private int currentRow;              // Will start at the bottom row

    public FireRowByRowTopDown(float difficulty) {
        super();
        timeBetweenShots *= difficulty;
    }

    @Override
    protected List<Laser> fireLasers(float deltaTime) {
        timeSinceLastShot += deltaTime;

        // We'll initialize currentRow after we have the formation
        List<Laser> lasers = new ArrayList<>();
        Ship[][] formation = parser.shipsToGrid(shipList);

        if (formation.length == 0 || formation[0].length == 0) {
            return lasers;
        }

        int rows = formation.length;
        int cols = formation[0].length;

        // If currentRow is not set, start at the bottom row
        if (currentRow == 0 && timeSinceLastShot == deltaTime) {
            currentRow = rows - 1;
        }

        // If not enough time has passed, don't fire yet
        if (timeSinceLastShot < timeBetweenShots) {
            return lasers;
        }

        // Reset timer and prepare to fire from the current row
        timeSinceLastShot = 0;

        // Fire from every ship in the currentRow
        if (currentRow >= 0 && currentRow < rows) {
            for (int col = 0; col < cols; col++) {
                Ship ship = formation[currentRow][col];
                if (ship != null) {
                    Laser[] firedLasers = ship.fireLasers();
                    if (firedLasers != null) {
                        Collections.addAll(lasers, firedLasers);
                    }
                }
            }
        }

        // Move up to the next row, looping back to the bottom when reaching the top
        currentRow--;
        if (currentRow < 0) {
            currentRow = rows - 1; // Loop back to the bottom
        }

        return lasers;
    }
}
