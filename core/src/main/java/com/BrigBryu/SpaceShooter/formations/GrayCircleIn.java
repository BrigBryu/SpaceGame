package com.BrigBryu.SpaceShooter.formations;

import com.BrigBryu.SpaceShooter.FormationFireLasers.*;
import com.BrigBryu.SpaceShooter.formationMovement.*;

public class GrayCircleIn extends Formation{


    public GrayCircleIn() {
//        super(new MoveIn(parser), new FireIfGroup(parser));
        //difficulty higer than 1 makes more time between shots lower than 1 makes less time between shots
        super(new MoveWeakUp(parser), new FireAllDiagonalsCycle(parser, 1f));

    }

    @Override
    public void initializeShips() {
        shipList = parser.getShips("assets/formationsSpawnPoints/grayCircle");
    }

}
