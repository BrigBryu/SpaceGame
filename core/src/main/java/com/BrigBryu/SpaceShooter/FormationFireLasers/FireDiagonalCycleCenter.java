package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.Laser;
import com.BrigBryu.SpaceShooter.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FireDiagonalCycleCenter extends FormationFireLasers {
    private float timeSinceLastShot = 0;
    private float timeBetweenShots = 0.8f; // Delay between firing each diagonal
    private int currentDirection = 0; // will cycle through 0,1,2,3

    // direction:
    // 0: top-left to bottom-right
    // 1: bottom-right to top-left
    // 2: top-right to bottom-left
    // 3: bottom-left to top-right

    public FireDiagonalCycleCenter(FormationParser parser) {
        super(parser);
    }

    @Override
    protected List<Laser> fireLasers(float deltaTime) {
        timeSinceLastShot += deltaTime;

        List<Laser> lasers = new ArrayList<>();
        Ship[][] formation = parser.shipsToGrid(shipList);

        if (formation.length == 0 || formation[0].length == 0) {
            return lasers; // no formation to fire from
        }

        int rows = formation.length;
        int cols = formation[0].length;

        // If not enough time has passed, don't fire yet
        if (timeSinceLastShot < timeBetweenShots) {
            return lasers;
        }

        // Reset the timer, we're firing now
        timeSinceLastShot = 0;

        // Determine starting position based on currentDirection
        int currentRow, currentCol;
        switch (currentDirection) {
            case 0: // top-left to bottom-right
                currentRow = 0;
                currentCol = 0;
                break;
            case 1: // bottom-right to top-left
                currentRow = rows - 1;
                currentCol = cols - 1;
                break;
            case 2: // top-right to bottom-left
                currentRow = 0;
                currentCol = cols - 1;
                break;
            case 3: // bottom-left to top-right
                currentRow = rows - 1;
                currentCol = 0;
                break;
            default:
                currentRow = 0;
                currentCol = 0;
                currentDirection = 0;
                break;
        }

        boolean fired = false;
        // Traverse along the selected diagonal and fire all ships found
        while (currentRow >= 0 && currentRow < rows && currentCol >= 0 && currentCol < cols) {
            Ship ship = formation[currentRow][currentCol];
            if (ship != null) {
                Laser[] firedLasers = ship.fireLasers();
                if (firedLasers != null) {
                    fired = true;
                    Collections.addAll(lasers, firedLasers);
                }
            }

            // Move along the diagonal based on currentDirection
            switch (currentDirection) {
                case 0: // down-right
                    currentRow++;
                    currentCol++;
                    break;
                case 1: // up-left
                    currentRow--;
                    currentCol--;
                    break;
                case 2: // down-left
                    currentRow++;
                    currentCol--;
                    break;
                case 3: // up-right
                    currentRow--;
                    currentCol++;
                    break;
            }
        }

        // After firing one diagonal, move to the next direction
        currentDirection = (currentDirection + 1) % 4;

        if(!fired){
            timeSinceLastShot = timeBetweenShots;
        }

        return lasers;
    }
}
