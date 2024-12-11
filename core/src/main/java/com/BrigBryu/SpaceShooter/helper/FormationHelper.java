package com.BrigBryu.SpaceShooter.helper;

import com.BrigBryu.SpaceShooter.gameObjects.Ship;

public class FormationHelper {
    //HELPER METHODS FOR FORMATION UPDATES
    public static boolean filledSquare(int size, Ship[][] formation, int row, int col){

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

    /**
     * Checks a square for enemies of size
     * @param sizeOfSearch size of square
     * @param formation formation to check
     * @param row bottom left of square
     * @param col bottom left of square
     * @return num enemies in square
     * {
     * {1,2,3},
     * {1,2,3}
     * {1,2,3}
     * }
     */
    public static int getNumberOfEnemies(int sizeOfSearch, Ship[][] formation, int row, int col) {
        int count = 0;
        if(row < 0 || col < 0 || row + sizeOfSearch >= formation.length || col + sizeOfSearch>= formation[0].length) {
            System.out.println("Index for getNumberOfEnemies out of bounds check FormationUpdate");
            return 0;
        }

        for (int i = row; i < row + sizeOfSearch; i++){
            for(int j = col; j < col + sizeOfSearch; j++) {
                if(formation[i][j] != null){
                    count++;
                }
            }
        }

        return count;
    }
}
