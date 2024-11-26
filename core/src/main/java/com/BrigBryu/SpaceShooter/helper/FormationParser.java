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

    public List<Ship> getSpawnPoints(String filePath) {
        FileHandle file = Gdx.files.internal(filePath);
        if (!file.exists()) {
            System.out.println("cant find formation");
            return new ArrayList<>();
        }

        String[][] formation = createArray(file);
        return getSpawnPoints(formation);
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

    private List<Ship> getSpawnPoints(String[][] array) {
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
}
