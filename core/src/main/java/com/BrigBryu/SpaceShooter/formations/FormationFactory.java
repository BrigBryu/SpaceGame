package com.BrigBryu.SpaceShooter.formations;

public interface FormationFactory {
    public Formation createFormation(float difficulty);
}
/* What all factories should have
 GrayFormationFactory
    FireLasers()
    - FireBottomMost
    - FireTopMost
    - FireRandomBurst
    - FireSpiral
    Move()
    - FilterByType
    - MoveIn

 */