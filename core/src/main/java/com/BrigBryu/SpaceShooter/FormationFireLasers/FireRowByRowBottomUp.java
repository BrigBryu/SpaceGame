package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FireRowByRowBottomUp extends FormationFireLasers {
    private float timeSinceLastShot = 0;
    private float timeBetweenShots = 2f; // Delay between firing each row
    private int currentRow = 0;          // Start at the top row

    public FireRowByRowBottomUp(FormationParser parser, float difficulty) {
        super(parser);
        timeBetweenShots *= difficulty;
    }

    @Override
    protected List<Laser> fireLasers(float deltaTime) {
        timeSinceLastShot += deltaTime;

        // If not enough time has passed, don't fire yet
        if (timeSinceLastShot < timeBetweenShots) {
            return new ArrayList<>();
        }

        // Reset timer and prepare to fire from the current row
        timeSinceLastShot = 0;

        List<Laser> lasers = new ArrayList<>();
        Ship[][] formation = parser.shipsToGrid(shipList);

        if (formation.length == 0 || formation[0].length == 0) {
            return lasers;
        }

        int rows = formation.length;
        int cols = formation[0].length;

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

        // Move down to the next row, looping back to the top when reaching the bottom
        currentRow++;
        if (currentRow >= rows) {
            currentRow = 0; // Loop back to the top
        }

        return lasers;
    }
}
