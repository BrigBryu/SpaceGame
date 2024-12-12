package com.BrigBryu.SpaceShooter.formationMovement;


import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

public class MoveCellularUpdateNeighbors extends FormationMove {

    private float timeSinceLastMove = 0;
    private float updateFrequency = 1;
    private float timeSinceStart = 0;
    private float initialUpdateWaitTime = 1.5f;
    private int neededNeighbors = 8;
    private float timeSinceUpdateMovement = 0;
    private float updateMovementFrequency = 2.5f;
    public MoveCellularUpdateNeighbors() {
        super();
        randomize();
    }

    private void randomize(){
        neededNeighbors = Math.max(5, ((int)(Math.random() * 9))); //smallest 5 largest 8
        updateMovementFrequency = (float) Math.max(2, ((int)(Math.random() * 5))); //smallest 2 largest 4
    }

    @Override
    public void updateShips(float deltaTime) {
        timeSinceUpdateMovement += deltaTime;
        if(timeSinceUpdateMovement > updateMovementFrequency) {
            timeSinceUpdateMovement = 0;
            neededNeighbors = Math.max(0, neededNeighbors - 1);
        }
        if (timeSinceStart < initialUpdateWaitTime) {
            timeSinceStart += deltaTime;
            return;
        }

        Ship[][] formation = parser.shipsToGrid(shipList);

        if (timeSinceLastMove < updateFrequency) {
            timeSinceLastMove += deltaTime;
            return;
        }

        timeSinceLastMove = 0;
        formation = parser.shipsToGrid(shipList);
        int maxI = formation.length;
        int maxJ = formation[0].length;
        Ship[][] newFormation = new Ship[maxI][maxJ];

        for (int i = 0; i < formation.length; i++) {
            for (int j = 0; j < formation[i].length; j++) {
                Ship ship = formation[i][j];
                if (ship != null) {
                    // Check if ship has any neighbors
                    int neighbors = 0;
                
                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            if (di == 0 && dj == 0) continue; 
                            
                            int ni = i + di;
                            int nj = j + dj;
                            
                            if (ni >= 0 && ni < maxI && nj >= 0 && nj < maxJ && formation[ni][nj] != null) {
                                neighbors++;
                            }
                        }
                        if (neighbors > neededNeighbors) break;
                    }
                    boolean stayInPlace = false;
                    if(neighbors == 0) {
                        stayInPlace = Math.random() < 0.8;
                    }

                    if (stayInPlace || neighbors < neededNeighbors) {
                        // If not enough neighbors, stay in place
                        newFormation[i][j] = ship;
                    } else {
                        // If has neighbors, try to move in a random valid direction
                        int attempts = 0;
                        boolean moved = false;
                        while (!moved && attempts < 8) {
                            // Pick random direction (-1, 0, or 1 for both i and j)
                            int di = (int)(Math.random() * 3) - 1;
                            int dj = (int)(Math.random() * 3) - 1;
                            
                            // Skip if no movement
                            if (di == 0 && dj == 0) {
                                attempts++;
                                continue;
                            }
                            
                            int newI = i + di;
                            int newJ = j + dj;
                            
                            // Check if new position is valid and empty
                            if (newI >= 0 && newI < maxI && 
                                newJ >= 0 && newJ < maxJ && 
                                newFormation[newI][newJ] == null) {
                                newFormation[newI][newJ] = ship;
                                moved = true;
                            }
                            
                            attempts++;
                        }
                        
                        // If couldn't move after all attempts, stay in place
                        if (!moved) {
                            newFormation[i][j] = ship;
                        }
                    }
                }
            }
        }

        shipList = parser.gridToShips(newFormation);
    }

}