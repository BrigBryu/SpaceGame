package com.BrigBryu.SpaceShooter.formations;

import com.BrigBryu.SpaceShooter.FormationFireLasers.FormationFireLasers;
import com.BrigBryu.SpaceShooter.formationMovement.FormationMove;

public class FormationBuilder {
    private FormationMove formationMove;
    private FormationFireLasers formationFire;
    private ShipFormation shipFormation;

    //Builder pattern for setting up the formation section
    public FormationBuilder(){
    }

    public void setMovementPattern(FormationMove formationMove){
        this.formationMove = formationMove;
    }

    public void setFirePattern(FormationFireLasers formationFire){
        this.formationFire = formationFire;
    }

    public void setShipFormation(ShipFormation shipFormation){
        this.shipFormation = shipFormation;
    }

    public Formation buildFormation(){
        Formation formation = new Formation(formationMove, formationFire, shipFormation);
        return formation;
    }
}   