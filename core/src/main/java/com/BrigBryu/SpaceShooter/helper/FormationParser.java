package com.BrigBryu.SpaceShooter.helper;

import com.BrigBryu.SpaceShooter.Ship;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.List;

public class FormationParser {
    private int width, height;
    private float gridSpacingX, gridSpacingY;
    private float offsetX, offsetY;

    public FormationParser(int width, int height, float gridSpacingX, float gridSpacingY, float offsetX, float offsetY) {
        this.width = width;
        this.height = height;
        this.gridSpacingX = gridSpacingX;
        this.gridSpacingY = gridSpacingY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public FormationParser() {
        this(12, 12, 8f, 8f, 10f, 88f);
    }

    public List<Ship> getShips(String filePath) {
        FileHandle file = Gdx.files.internal(filePath);
        if (!file.exists()) {
            System.out.println("cant find formation");
            return new ArrayList<>();
        }

        String[][] formation = createArray(file);
        return getShips(formation);
    }

    private String[][] createArray(FileHandle file) {
        String[][] formation = new String[height][width];
        String[] lines = file.readString().split("\\r?\\n");//is for window and other

        for (int i = 0; i < height; i++) {
            if (i >= lines.length) {
                System.out.println( "need more row need: " + height + " found: " + lines.length);
                break;
            }
            String[] splitRow = lines[i].trim().split("\\s+"); //is for window and other
            for (int j = 0; j < width; j++) {
                if (j < splitRow.length) {
                    formation[i][j] = splitRow[j];
                } else {
                    formation[i][j] = "0";
                    System.out.println( "need more col need: " + width + " found: " + formation[i].length);
                }
            }
        }

        return formation;
    }

    private List<Ship> getShips(String[][] array) {
        List<Ship> ships = new ArrayList<>();

        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                String enemyType = array[row][col];
                if (!enemyType.equals("0")) {
                    float x = offsetX + col * gridSpacingX;
                    float y = offsetY + row * gridSpacingY;
                    Ship enemy = EnemyFactory.createEnemy(enemyType, x, y);
                    ships.add(enemy);
                }
            }
        }

        return ships;
    }

    public Ship[][] shipsToGrid(List<Ship> ships) {
        Ship[][] grid = new Ship[height][width];

        for (Ship ship : ships) {
            float x = ship.getBoundingBox().x;
            float y = ship.getBoundingBox().y;


            int col = (int)((x - offsetX + gridSpacingX / 2) / gridSpacingX); //add /2 to get in
            int row = (int)((y - offsetY + gridSpacingY / 2) / gridSpacingY);

            if (col >= 0 && col < width && row >= 0 && row < height) {
                grid[row][col] = ship;
            } else {
                System.out.println("Ship at (" + x + ", " + y + ") is out of grid bounds.");
            }
        }

        return grid;
    }

    public List<Ship> gridToShips(Ship[][] grid) {
        List<Ship> ships = new ArrayList<>();

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                Ship ship = grid[row][col];
                if (ship != null) {
                    float newX = offsetX + col * gridSpacingX;
                    float newY = offsetY + row * gridSpacingY;
                    ship.getBoundingBox().x = newX;
                    ship.getBoundingBox().y = newY;
                    ships.add(ship);
                }
            }
        }

        return ships;
    }

}
