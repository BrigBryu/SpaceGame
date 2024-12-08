package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.Laser;
import com.BrigBryu.SpaceShooter.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FireRandomChuncks extends FormationFireLasers {
    private float timeSinceLastShot = 0;
    private float timeBetweenShots = 3;
    private boolean fireEvenRows;
    private Random random = new Random();

    private int rowWidth = 2;

    public FireRandomChuncks(FormationParser parser) {
        super(parser);
    }

    @Override
    protected List<Laser> fireLasers(float deltaTime) {
        timeSinceLastShot += deltaTime;
        if (timeSinceLastShot < timeBetweenShots) {
            return new ArrayList<>();
        }
        timeSinceLastShot = 0;
        fireEvenRows = random.nextBoolean();

        List<Laser> lasers = new ArrayList<>();
        Ship[][] formation = parser.shipsToGrid(shipList);

        for (int row = 0; row < formation.length; row++) {
            boolean isEven = (row % 2 == 0);
            if (fireEvenRows && isEven || (!fireEvenRows && !isEven)) {
                int columns = formation[row].length;
                if (columns < rowWidth) {//cant fit
                    continue;
                }

                // Random start column to fire from
                int startCol = random.nextInt(columns - (rowWidth - 1));

                // Fire from the chosen 2-column segment
                for (int col = startCol; col < startCol + rowWidth; col++) {
                    Ship ship = formation[row][col];
                    if (ship != null) {
                        // Fire lasers from this ship
                        Laser[] shipLasers = ship.fireLasers();
                        if (shipLasers != null) {
                            Collections.addAll(lasers, shipLasers);
                            System.out.println("Ship at (" + row + ", " + col + ") fired lasers.");
                        }
                    }
                }
            }
        }
        return lasers;
    }
}
