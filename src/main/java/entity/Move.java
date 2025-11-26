package entity;

import entity.moveyStuff.MoveBehaviour;

import javax.lang.model.type.NullType;
import java.util.List;
import java.util.Objects;

public class Move {
    private final String moveName;
    private final int movePower;
    private final String moveDescription;
    private final String moveType;
    private final int maxPp;
    private int currentPp;
    private final int moveAccuracy;
    private final List<MoveBehaviour> moveBehaviours;

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
    }

    public void useMove(PokemonTeam userTeam, PokemonTeam targetTeam,
                        BattleStats userBattleStats, BattleStats targetBattleStats) {
        if(hitSucceeds(userBattleStats, targetBattleStats)){
        for (MoveBehaviour behavior : moveBehaviours) {
            behavior.execute(this, userTeam, targetTeam, userBattleStats, targetBattleStats);
        }}
        currentPp -= 1;
    }

    private boolean hitSucceeds(BattleStats userBattleStats,
                                BattleStats targetBattleStats) {
        if(Objects.isNull(moveAccuracy)) {
            //This requires further expansion as all moves either have accuracy and
            //can miss or do not have accuracy and always hit. Discuss with Ishaan
            //to determine how to represent moves with no accuracy using an integer
            return true;
        }
        return ((float) moveAccuracy / 100 *
                userBattleStats.getStat(StatType.ACCURACY) *
                targetBattleStats.getStat(StatType.EVASION) >
                (Math.random() * 256) / 255 );
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
}

