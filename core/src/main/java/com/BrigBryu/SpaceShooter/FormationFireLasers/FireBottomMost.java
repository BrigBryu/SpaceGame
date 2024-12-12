package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FireBottomMost extends FormationFireLasers {
    private float timeSinceLastShot = 0;
    private float timeBetweenShots = .5f;
    private int currentRow = 0;

    public FireBottomMost(float difficulty) {
        super();
        timeBetweenShots *= difficulty;
    }

    @Override
    protected List<Laser> fireLasers(float deltaTime) {
        timeSinceLastShot += deltaTime;

        if (timeSinceLastShot < timeBetweenShots) {
            // Not enough time has passed to move to the next row
            return new ArrayList<>();
        }

        // Reset timer and fire from the current row
        timeSinceLastShot = 0;

        List<Laser> lasers = new ArrayList<>();
        Ship[][] formation = parser.shipsToGrid(shipList);

        if (formation.length == 0 || formation[0].length == 0) {
            return lasers;
        }

        int rows = formation.length;
        int cols = formation[0].length;

        boolean fired = false;
        if (currentRow >= 0 && currentRow < rows) {
            // For each column, determine the top-most ship
            for (int col = 0; col < cols; col++) {
                // Find the top-most ship in this column
                int topMostRow = -1;
                for (int r = 0; r < rows; r++) {
                    if (formation[r][col] != null) {
                        topMostRow = r;
                        break;
                    }
                }

                // If the currentRow is the top-most row for this column, fire from it
                if (topMostRow == currentRow) {
                    Ship ship = formation[currentRow][col];
                    if (ship != null) {
                        Laser[] firedLasers = ship.fireLasers();
                        if (firedLasers != null) {
                            fired = true;
                            Collections.addAll(lasers, firedLasers);
                        }
                    }
                }
            }
        }

        // Move down to the next row, looping back to the top if desired
        currentRow++;
        if (currentRow >= rows) {
            currentRow = 0; // Loop back to top row
        }
        if(!fired) {
            timeSinceLastShot = timeBetweenShots;
        }
        return lasers;
    }
}
