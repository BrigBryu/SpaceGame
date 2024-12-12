package com.BrigBryu.SpaceShooter.formations;

import com.BrigBryu.SpaceShooter.FormationFireLasers.*;
import com.BrigBryu.SpaceShooter.formationMovement.FilterByType;
import com.BrigBryu.SpaceShooter.formationMovement.MoveIn;

public class GrayFormationFactory implements FormationFactory {
    @Override
    public Formation createFormation(float difficulty) {
        /*
        GrayFormationFactory
        FireLasers()
        - FireBottomMost 30%
        - FireTopMost 30%
        - FireRandomBurst 20%
        - FireSpiral 20%
        Move()
        - FilterByType 40%
        - MoveIn 60%
         */

        // Generate separate random values for fire and movement patterns
        // double fireRandom = Math.random();
        double fireRandom = .9;

        double moveRandom = Math.random();

        //FireLasers
        FormationBuilder formation = new FormationBuilder();
        if(fireRandom < 0.3){
            formation.setFirePattern(new FireBottomMost(difficulty));
            System.out.println("FireBottomMost");
        } else if (fireRandom < 0.6) {
            formation.setFirePattern(new FireTopMost(difficulty));
            System.out.println("FireTopMost");
        } else if (fireRandom < 0.8) {
            formation.setFirePattern(new FireRandomBurst(difficulty));
            System.out.println("FireRandomBurst");
        } else {
            formation.setFirePattern(new FireSpiral(difficulty));
            System.out.println("FireSpiral");
        }

        //Move
        if(moveRandom < 0.4){
            formation.setMovementPattern(new FilterByType());
            System.out.println("FilterByType");
        } else {
            formation.setMovementPattern(new MoveIn());
            System.out.println("MoveIn");
        }

        //ShipFormation
        formation.setShipFormation(new ShipFormation("assets/formationsSpawnPoints/grayCircle"));
        System.out.println("ShipFormation");


        return formation.buildFormation();
    }
}