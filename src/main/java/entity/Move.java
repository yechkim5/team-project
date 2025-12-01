package entity;

import entity.moveyStuff.MoveBehaviour;

import java.util.ArrayList;
import java.util.List;

public class Move {
    private final String moveName;
    private final int movePower;
    private final String moveDescription;
    private final String moveType;
    private final int maxPp;
    private int currentPp;
    private final int moveAccuracy;
    private final List<MoveBehaviour> moveBehaviours;
    private String moveClass;

    //Will make this builder for ease of instantiation using API
    public Move(String moveName, String moveType, int pp, String moveDescription,
                String moveClass, int moveAccuracy,  List<MoveBehaviour> moveBehaviours,
                int movePower) {
        this.moveName = moveName;
        this.moveDescription = moveDescription;
        this.moveType = moveType;
        this.maxPp = pp;
        this.currentPp = maxPp;
        this.moveAccuracy = moveAccuracy;
        this.moveBehaviours = moveBehaviours;
        this.movePower = movePower;
        this.moveClass = moveClass;
    }

    public void useMove(PokemonTeam userTeam, PokemonTeam targetTeam,
                        BattleStats userStats, BattleStats targetStats) {

        for (MoveBehaviour behavior : moveBehaviours) {
            behavior.execute(this, userTeam, targetTeam, userStats, targetStats);
        }
        currentPp -= 1;

    }

    public int getMovePower() {
        return this.movePower;
    }

    public int getMoveAccuracy() {
        return moveAccuracy;
    }

    public String getMoveName() {
        return moveName;
    }

    public String getMoveDescription() {
        return moveDescription;
    }

    public String getMoveType() {
        return moveType;
    }

    public int getMaxPp() {
        return maxPp;
    }

    public int getCurrentPp() {
        return currentPp;
    }

    public String getMoveClass() {
        return moveClass;
    }

    public void resetPp() {this.currentPp = maxPp;}

    public Move(String moveName, String moveType, int maxPp, String moveDescription, String moveClass, int moveAccuracy, int currentPp) {
        this.moveName = moveName;
        this.moveType = moveType;
        this.maxPp = maxPp;
        this.moveDescription = moveDescription;
        this.moveClass = moveClass;
        this.moveAccuracy = moveAccuracy;
        this.currentPp = currentPp;
        this.movePower = 0;
        this.moveBehaviours = new ArrayList<MoveBehaviour>();
    }
}

