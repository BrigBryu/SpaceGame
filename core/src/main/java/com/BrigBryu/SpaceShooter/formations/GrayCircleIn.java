package com.BrigBryu.SpaceShooter.formations;

import com.BrigBryu.SpaceShooter.FormationFireLasers.FireIfGroup;
import com.BrigBryu.SpaceShooter.FormationMove.MoveIn;

public class GrayCircleIn extends Formation{


    public GrayCircleIn() {
        super(new MoveIn(parser), new FireIfGroup(parser));
    }

    @Override
    public void initializeShips() {
        shipList = parser.getShips("assets/formationsSpawnPoints/grayCircle");
        formationMove.updateShips(shipList);
    }

}
