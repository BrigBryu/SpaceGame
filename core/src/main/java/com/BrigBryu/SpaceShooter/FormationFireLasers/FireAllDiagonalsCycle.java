package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FireAllDiagonalsCycle extends FormationFireLasers {
    private float timeSinceLastShot = 0;
    private float timeBetweenShots = 0.8f; 
    private int currentDirection = 0; // cycle through 0,1,2,3
    private int currentDiagonalIndex = 0;

    // Each entry in allDiagonals[direction] is a list of start points (row,col) for that direction
    private List<List<int[]>> allDiagonals;

    // Directions:
    // 0: top-left to bottom-right (\)
    // 1: bottom-right to top-left (\ reversed)
    // 2: top-right to bottom-left (/)
    // 3: bottom-left to top-right (/ reversed)

    public FireAllDiagonalsCycle(float difficulty) {
        super();
        timeBetweenShots *= difficulty;
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

        // Initialize diagonals if not done yet
        if (allDiagonals == null) {
            allDiagonals = new ArrayList<>();
            // Compute diagonals for each direction
            // Direction 0: (\) from top-left to bottom-right
            allDiagonals.add(computeDiagonals0(rows, cols));
            // Direction 1: (\) from bottom-right to top-left
            allDiagonals.add(computeDiagonals1(rows, cols));
            // Direction 2: (/) from top-right to bottom-left
            allDiagonals.add(computeDiagonals2(rows, cols));
            // Direction 3: (/) from bottom-left to top-right
            allDiagonals.add(computeDiagonals3(rows, cols));
        }

        // If not enough time has passed, don't fire yet
        if (timeSinceLastShot < timeBetweenShots) {
            return lasers;
        }

        // Reset timer and fire the current diagonal in the current direction
        timeSinceLastShot = 0;

        List<int[]> diagonalsForDirection = allDiagonals.get(currentDirection);
        // Get the start points for this diagonal
        int[] startPoint = diagonalsForDirection.get(currentDiagonalIndex);

        // Fire all ships along this diagonal
        // Depending on direction, we step differently
        int r = startPoint[0];
        int c = startPoint[1];

        boolean fired = false;
        while (r >= 0 && r < rows && c >= 0 && c < cols) {
            Ship ship = formation[r][c];
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
                    r++;
                    c++;
                    break;
                case 1: // up-left
                    r--;
                    c--;
                    break;
                case 2: // down-left
                    r++;
                    c--;
                    break;
                case 3: // up-right
                    r--;
                    c++;
                    break;
            }
        }

        // Move to the next diagonal in this direction
        currentDiagonalIndex++;
        // If we have reached the end of the diagonals for this direction, switch to next direction
        if (currentDiagonalIndex >= diagonalsForDirection.size()) {
            currentDiagonalIndex = 0;
            currentDirection = (currentDirection + 1) % 4;
        }

        if(!fired){
            timeSinceLastShot = timeBetweenShots;
        }
        return lasers;
    }

    // Compute diagonals for direction 0: top-left to bottom-right
    // Start points are first row and first column
    private List<int[]> computeDiagonals0(int rows, int cols) {
        List<int[]> starts = new ArrayList<>();
        // top row
        for (int c = 0; c < cols; c++) {
            starts.add(new int[]{0, c});
        }
        // left column, starting from row 1 because (0,0) already included
        for (int r = 1; r < rows; r++) {
            starts.add(new int[]{r, 0});
        }
        return starts;
    }

    // Compute diagonals for direction 1: bottom-right to top-left
    // Start points are bottom row and rightmost column
    private List<int[]> computeDiagonals1(int rows, int cols) {
        List<int[]> starts = new ArrayList<>();
        // bottom row
        for (int c = cols - 1; c >= 0; c--) {
            starts.add(new int[]{rows - 1, c});
        }
        // right column, starting from second to last row upwards
        for (int r = rows - 2; r >= 0; r--) {
            starts.add(new int[]{r, cols - 1});
        }
        return starts;
    }

    // Compute diagonals for direction 2: top-right to bottom-left
    // Start points are top row and rightmost column
    private List<int[]> computeDiagonals2(int rows, int cols) {
        List<int[]> starts = new ArrayList<>();
        // top row, starting from rightmost column to left
        for (int c = cols - 1; c >= 0; c--) {
            starts.add(new int[]{0, c});
        }
        // right column, from row 1 downward
        for (int r = 1; r < rows; r++) {
            starts.add(new int[]{r, cols - 1});
        }
        return starts;
    }

    // Compute diagonals for direction 3: bottom-left to top-right
    // Start points are bottom row and leftmost column
    private List<int[]> computeDiagonals3(int rows, int cols) {
        List<int[]> starts = new ArrayList<>();
        // bottom row, left to right
        for (int c = 0; c < cols; c++) {
            starts.add(new int[]{rows - 1, c});
        }
        // left column, bottom-1 row up to top
        for (int r = rows - 2; r >= 0; r--) {
            starts.add(new int[]{r, 0});
        }
        return starts;
    }
}
