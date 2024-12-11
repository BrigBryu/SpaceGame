package com.BrigBryu.SpaceShooter.FormationFireLasers;

import com.BrigBryu.SpaceShooter.gameObjects.Laser;
import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FireRandomBurst extends FormationFireLasers {
    private float timeSinceLastShot = 0;
    private float timeBetweenShots = 1.5f;
    private int burstSize = 5; // number of ships to fire each time
    private Random random = new Random();

    public FireRandomBurst(FormationParser parser, float difficulty) {
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

        if (shipList == null || shipList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Laser> lasers = new ArrayList<>();

        // Randomly pick burstSize ships to fire
        for (int i = 0; i < burstSize; i++) {
            int index = random.nextInt(shipList.size());
            Ship chosenShip = shipList.get(index);
            Laser[] firedLasers = chosenShip.fireLasers();
            if (firedLasers != null) {
                Collections.addAll(lasers, firedLasers);
            }
        }

        return lasers;
    }
}
