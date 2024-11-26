package com.BrigBryu.SpaceShooter.formations;

import com.BrigBryu.SpaceShooter.Laser;
import com.BrigBryu.SpaceShooter.Ship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrayCircleIn extends Formation{
    float timeSinceLastMove = 0;
    float updateFrequency = 1;
    float timeSinceLastShot = 0;
    float timeBetweenShots = 3;
    float timeSinceStart = 0;
    float initialUpdateWaitTime = 1.5f;

    @Override
    public void initializeShips() {
        shipList = parser.getShips("assets/formationsSpawnPoints/grayCircle");
    }
    @Override
    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
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

    @Override
    public List<Laser> fireLasers() {
        if(timeBetweenShots > timeSinceLastShot) {
            return new ArrayList<>();
        }
        timeSinceLastShot = 0;
        List<Laser> lasers = new ArrayList<>();
        Ship[][] formation = parser.shipsToGrid(shipList);
        int size = 3; // Size of the square to check

        // Iterate from 1 to length - 2 to ensure a complete 3x3 square
        for (int i = 1; i < formation.length - 1; i++) {
            for (int j = 1; j < formation[i].length - 1; j++) {
                Ship ship = formation[i][j];
                if (ship != null && filledSquare(size, formation, i, j)) {
                    // Fire lasers from the center ship
                    lasers.addAll(Arrays.asList(ship.fireLasers()));
                    System.out.println("Ship at (" + i + ", " + j + ") fired lasers.");
                }
            }
        }
        return lasers;
    }

}
