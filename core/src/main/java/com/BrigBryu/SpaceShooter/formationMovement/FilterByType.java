package com.BrigBryu.SpaceShooter.formationMovement;

import com.BrigBryu.SpaceShooter.gameObjects.Enemy;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

public class MoveWeakUp extends FormationMove {
    float timeSinceLastMove = 0;
    float updateFrequency = 1;
    float timeSinceStart = 0;
    float initialUpdateWaitTime = 1.5f;
    boolean moveAwayFromPlayer = false;
    boolean weakerUp = true;

    public MoveWeakUp(FormationParser parser) {
        super(parser);        
    }

    @Override
    public void updateShips(float deltaTime) {
        if (timeSinceStart < initialUpdateWaitTime) {
            timeSinceStart += deltaTime;
            return;
        }

        if (timeSinceLastMove < updateFrequency) {
            timeSinceLastMove += deltaTime;
            return;
        }

        timeSinceLastMove = 0;
        Ship[][] formation = parser.shipsToGrid(shipList);
        Ship[][] newFormation = new Ship[formation.length][formation[0].length];

        // Copy existing ships to new formation
        for (int i = 0; i < formation.length; i++) {
            for (int j = 0; j < formation[i].length; j++) {
                newFormation[i][j] = formation[i][j];
            }
        }

        int halfHeight = formation.length;

        if (moveAwayFromPlayer) {
            // Process from top to bottom when moving towards player
            for (int i = halfHeight - 1; i > 0; i--) {
                for (int j = 0; j < formation[i].length; j++) {
                    if (newFormation[i][j] instanceof Enemy) {
                        // If space below is empty and we're not at the bottom
                        if (i < halfHeight - 1 && newFormation[i + 1][j] == null) {
                            newFormation[i + 1][j] = newFormation[i][j];
                            newFormation[i][j] = null;
                        }
                        // If there's an enemy below
                        else if (i < halfHeight - 1 && newFormation[i + 1][j] instanceof Enemy) {
                            Enemy currentEnemy = (Enemy) newFormation[i][j];
                            Enemy belowEnemy = (Enemy) newFormation[i + 1][j];
                            
                            // When moving towards player, swap based on weakerUp setting
                            if ((weakerUp && currentEnemy.type > belowEnemy.type) ||
                                (!weakerUp && currentEnemy.type < belowEnemy.type)) {
                                newFormation[i + 1][j] = currentEnemy;
                                newFormation[i][j] = belowEnemy;
                            }
                        }
                    }
                }
            }
        } else {
            // Process from bottom to top when moving away from player
            for (int i = 1; i < halfHeight; i++) {
                for (int j = 0; j < formation[i].length; j++) {
                    if (newFormation[i][j] instanceof Enemy) {
                        // If space above is empty and we're not at the top
                        if (i > 0 && newFormation[i - 1][j] == null) {
                            newFormation[i - 1][j] = newFormation[i][j];
                            newFormation[i][j] = null;
                        }
                        // If there's an enemy above
                        else if (i > 0 && newFormation[i - 1][j] instanceof Enemy) {
                            Enemy currentEnemy = (Enemy) newFormation[i][j];
                            Enemy aboveEnemy = (Enemy) newFormation[i - 1][j];
                            
                            // When moving away from player, swap based on weakerUp setting
                            if ((weakerUp && currentEnemy.type < aboveEnemy.type) ||
                                (!weakerUp && currentEnemy.type > aboveEnemy.type)) {
                                newFormation[i - 1][j] = currentEnemy;
                                newFormation[i][j] = aboveEnemy;
                            }
                        }
                    }
                }
            }
        }

        shipList = parser.gridToShips(newFormation);
    }

    public void setMoveAwayFromPlayer(boolean moveTowards) {
        this.moveAwayFromPlayer = moveTowards;
    }

    public void setWeakerUp(boolean weakerUp) {
        this.weakerUp = weakerUp;
    }
}
