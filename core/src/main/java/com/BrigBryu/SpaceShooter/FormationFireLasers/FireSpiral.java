package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FireSpiral extends FormationFireLasers {
    private float timeSinceLastShot = 0;
    private float timeBetweenShots = 0.2f;
    private List<Ship> spiralOrder = new ArrayList<>();
    private int currentShipIndex = 0;

    public FireSpiral(FormationParser parser, float difficulty) {
        super(parser);
        timeBetweenShots *= difficulty;
    }

    @Override
    protected List<Laser> fireLasers(float deltaTime) {
        timeSinceLastShot += deltaTime;
        if (timeSinceLastShot < timeBetweenShots) {
            return new ArrayList<>();
        }
        timeSinceLastShot = 0;

        if (spiralOrder.isEmpty() && shipList != null && !shipList.isEmpty()) {
            // Build the spiral order
            Ship[][] formation = parser.shipsToGrid(shipList);
            int top = 0;
            int bottom = formation.length - 1;
            int left = 0;
            int right = formation[0].length - 1;

            while (top <= bottom && left <= right) {
                for (int j = left; j <= right; j++) {
                    if (formation[top][j] != null)
                        spiralOrder.add(formation[top][j]);
                }
                top++;

                for (int i = top; i <= bottom; i++) {
                    if (formation[i][right] != null)
                        spiralOrder.add(formation[i][right]);
                }
                right--;

                if (top <= bottom) {
                    for (int j = right; j >= left; j--) {
                        if (formation[bottom][j] != null)
                            spiralOrder.add(formation[bottom][j]);
                    }
                    bottom--;
                }

                if (left <= right) {
                    for (int i = bottom; i >= top; i--) {
                        if (formation[i][left] != null)
                            spiralOrder.add(formation[i][left]);
                    }
                    left++;
                }
            }
        }

        List<Laser> lasers = new ArrayList<>();
        if (!spiralOrder.isEmpty()) {
            Ship currentShip = spiralOrder.get(currentShipIndex);
            Laser[] firedLasers = currentShip.fireLasers();
            if (firedLasers != null) {
                Collections.addAll(lasers, firedLasers);
            }
            currentShipIndex = (currentShipIndex + 1) % spiralOrder.size();
        }
        return lasers;
    }
}
