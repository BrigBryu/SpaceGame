package com.BrigBryu.SpaceShooter.formations;

import com.BrigBryu.SpaceShooter.Laser;
import com.BrigBryu.SpaceShooter.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public abstract class Formation {
    protected static FormationParser parser = new FormationParser();
    protected List<Ship> shipList;

    public Formation(){
        initializeShips();
    }
    public List<Ship> getShipList(){
        return shipList;
    }

    public void resetShips(){
        initializeShips();
    }
    public void draw(SpriteBatch batch){
        for(Ship ship: shipList) {
            ship.draw(batch);
        }
    }

    protected boolean filledSquare(int size, Ship[][] formation, int row, int col){

        int offset = size / 2; //  size=3, offset=1

        if(row - offset < 0 || row + offset >= formation.length ||
            col - offset < 0 || col + offset >= formation[0].length){
            return false;
        }

        for(int i = row - offset; i <= row + offset; i++) {
            for (int j = col - offset; j <= col + offset; j++){
                if(formation[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }


    public abstract void update(float deltaTime);
    public abstract List<Laser> fireLasers();
    protected abstract void initializeShips();
}
