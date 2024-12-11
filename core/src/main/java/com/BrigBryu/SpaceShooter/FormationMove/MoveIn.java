package com.BrigBryu.SpaceShooter.FormationMove;

import com.BrigBryu.SpaceShooter.gameObjects.Ship;
import com.BrigBryu.SpaceShooter.helper.FormationParser;
import java.util.ArrayList;
import java.util.List;

public class MoveIn extends FormationMove {
    float timeSinceLastMove = 0;
    float updateFrequency = 1;
    float timeSinceStart = 0;
    float initialUpdateWaitTime = 1.5f;
    private List<int[]> goals;
    private int currentGoalIndex = 0;
    private float goalTimeElapsed = 0f;
    private float goalDuration = 5f;
    private boolean useDefaultMid = false;
    private int gridCenterI = 0;
    private int gridCenterJ = 0;

    public MoveIn(FormationParser parser) {
        super(parser);
        this.goals = new ArrayList<>();
        this.useDefaultMid = true;
    }

    public MoveIn(FormationParser parser, List<int[]> goals, float goalDuration) {
        super(parser);
        this.goals = goals;
        this.goalDuration = goalDuration;
    }

    @Override
    public void updateShips(float deltaTime) {
        if (timeSinceStart < initialUpdateWaitTime) {
            timeSinceStart += deltaTime;
            return;
        }

        Ship[][] formation = parser.shipsToGrid(shipList);
        if (useDefaultMid && goals.isEmpty()) {
            gridCenterI = formation.length / 2;
            gridCenterJ = formation[0].length / 2;
            goals.add(new int[]{gridCenterI, gridCenterJ});
        }

        goalTimeElapsed += deltaTime;
        if (goalTimeElapsed >= goalDuration) {
            goalTimeElapsed = 0f;
            currentGoalIndex++;
            if (currentGoalIndex >= goals.size()) currentGoalIndex = goals.size() - 1;
        }

        if (timeSinceLastMove < updateFrequency) {
            timeSinceLastMove += deltaTime;
            return;
        }

        timeSinceLastMove = 0;
        formation = parser.shipsToGrid(shipList);
        int[] currentGoal = goals.get(currentGoalIndex);
        int goalI = currentGoal[0];
        int goalJ = currentGoal[1];
        int maxI = formation.length;
        int maxJ = formation[0].length;
        Ship[][] newFormation = new Ship[maxI][maxJ];

        for (int i = 0; i < formation.length; i++) {
            for (int j = 0; j < formation[i].length; j++) {
                Ship ship = formation[i][j];
                if (ship != null) {
                    int newI = i;
                    int newJ = j;
                    if (i < goalI && i < maxI - 1 && formation[i + 1][j] == null) newI = i + 1;
                    else if (i > goalI && i > 0 && formation[i - 1][j] == null) newI = i - 1;

                    if (j < goalJ && j < maxJ - 1 && formation[i][j + 1] == null) newJ = j + 1;
                    else if (j > goalJ && j > 0 && formation[i][j - 1] == null) newJ = j - 1;

                    if (newFormation[newI][newJ] == null) newFormation[newI][newJ] = ship;
                    else newFormation[i][j] = ship;
                }
            }
        }

        shipList = parser.gridToShips(newFormation);
    }

    public void setGoals(List<int[]> newGoals, float newGoalDuration) {
        this.goals = newGoals;
        this.currentGoalIndex = 0;
        this.goalTimeElapsed = 0f;
        this.goalDuration = newGoalDuration;
        this.useDefaultMid = false;
    }

    public void goToMid() {
        this.goals.clear();
        this.useDefaultMid = true;
        this.currentGoalIndex = 0;
        this.goalTimeElapsed = 0f;
    }

    public void goEdgeToEdge(float newGoalDuration) {
        Ship[][] formation = parser.shipsToGrid(shipList);
        int centerI = formation.length / 2;
        int leftJ = 0;
        int rightJ = formation[0].length - 1;
        List<int[]> sideGoals = new ArrayList<>();
        sideGoals.add(new int[]{centerI, leftJ});
        sideGoals.add(new int[]{centerI, rightJ});
        this.setGoals(sideGoals, newGoalDuration);
    }
}
