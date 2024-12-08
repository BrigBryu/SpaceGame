package com.BrigBryu.SpaceShooter.formations;

import com.BrigBryu.SpaceShooter.FormationFireLasers.*;
import com.BrigBryu.SpaceShooter.FormationMove.MoveIn;

public class GrayCircleIn extends Formation{


    public GrayCircleIn() {
//        super(new MoveIn(parser), new FireIfGroup(parser));
        super(new MoveIn(parser), new FireAllDiagonalsCycle(parser));

    }

    @Override
    public void initializeShips() {
        shipList = parser.getShips("assets/formationsSpawnPoints/grayCircle");
    }

}
