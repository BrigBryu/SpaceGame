package com.BrigBryu.SpaceShooter.FormationMove;

import com.BrigBryu.SpaceShooter.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

public class MoveIn extends FormationMove {
    float timeSinceLastMove = 0;
    float updateFrequency = 1;

    float timeSinceStart = 0;
    float initialUpdateWaitTime = 1.5f;

    public MoveIn(FormationParser parser) {
        super(parser);
    }

    @Override
    public void updateShips(float deltaTime) {

        if(timeSinceStart < initialUpdateWaitTime) {
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

        for (int i = 0; i < formation.length; i++) {
            for (int j = 0; j < formation[i].length; j++) {
                Ship ship = formation[i][j];
                if (ship != null) {
                    int newI = i;
                    int newJ = j;

                    if (i < formation.length / 2 && i < formation.length - 1 && formation[i + 1][j] == null) {
                        newI = i + 1; // Move Down
                    } else if (i > formation.length / 2 && formation[i - 1][j] == null) {
                        newI = i - 1; // Move Up
                    }

                    if (j < formation[0].length / 2 && j < formation[0].length - 1 && formation[i][j + 1] == null) {
                        newJ = j + 1; // Move Right
                    } else if (j > formation[0].length / 2 && formation[i][j - 1] == null) {
                        newJ = j - 1; // Move Left
                    }

                    if (newFormation[newI][newJ] == null) {
                        newFormation[newI][newJ] = ship;
                    } else {
                        newFormation[i][j] = ship;
                    }
                }
            }
        }

        shipList = parser.gridToShips(newFormation);
    }
}
