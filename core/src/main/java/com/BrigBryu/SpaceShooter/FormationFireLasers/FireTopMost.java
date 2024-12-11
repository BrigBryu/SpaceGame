package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FireTopMost extends FormationFireLasers {
    private float timeSinceLastShot = 0;
    private float timeBetweenShots = .7f;
    private int currentRow = -1;

    public FireTopMost(FormationParser parser, float difficulty) {
        super(parser);
        timeBetweenShots *= difficulty;
    }

    @Override
    protected List<Laser> fireLasers(float deltaTime) {
        timeSinceLastShot += deltaTime;

        List<Laser> lasers = new ArrayList<>();
        Ship[][] formation = parser.shipsToGrid(shipList);

        if (formation.length == 0 || formation[0].length == 0) {
            return lasers;
        }

        int rows = formation.length;
        int cols = formation[0].length;

        // If currentRow not initialized, start at the bottom
        if (currentRow == -1) {
            currentRow = rows - 1;
        }

        if (timeSinceLastShot < timeBetweenShots) {
            // Not enough time has passed to move to the next row
            return lasers;
        }

        // Reset timer and fire from the current row
        timeSinceLastShot = 0;

        boolean fired = false;
        if (currentRow >= 0 && currentRow < rows) {
            // For each column, determine the bottom-most ship
            for (int col = 0; col < cols; col++) {
                int bottomMostRow = -1;
                for (int r = rows - 1; r >= 0; r--) {
                    if (formation[r][col] != null) {
                        bottomMostRow = r;
                        break;
                    }
                }

                // If currentRow is the bottom-most row for this column, fire from it
                if (bottomMostRow == currentRow) {
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

        // Move up to the next row, looping back to the bottom if desired
        currentRow--;
        if (currentRow < 0) {
            currentRow = rows - 1; // Loop back to bottom row
        }

        if(!fired) {
            timeSinceLastShot = timeBetweenShots;
        }

        return lasers;
    }
}
