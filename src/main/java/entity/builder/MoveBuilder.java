package entity.builder;

import entity.Move;
import entity.moveyStuff.MoveBehaviour;

import java.util.List;

public class MoveBuilder {
    private String moveName;
    private String moveType;
    private int pp;
    private String moveDescription;
    private String moveClass;
    private int moveAccuracy;
    private List<MoveBehaviour> moveBehaviours;
    private int movePower;

    public MoveBuilder setMoveName(String moveName) {
        this.moveName = moveName;
        return this;
    }

    public MoveBuilder setMoveType(String moveType) {
        this.moveType = moveType;
        return this;
    }

    public MoveBuilder setPp(int pp) {
        this.pp = pp;
        return this;
    }

    public MoveBuilder setMoveDescription(String moveDescription) {
        this.moveDescription = moveDescription;
        return this;
    }

    public MoveBuilder setMoveAccuracy(int moveAccuracy) {
        this.moveAccuracy = moveAccuracy;
        return this;
    }

    public MoveBuilder setMoveBehaviours(List<MoveBehaviour> moveBehaviours) {
        this.moveBehaviours = moveBehaviours;
        return this;
    }

    public MoveBuilder setMovePower(int movePower) {
        this.movePower = movePower;
        return this;
    }

    public Move createMove() {
        return new Move(moveName, moveType, pp, moveDescription, moveClass, moveAccuracy, moveBehaviours, movePower);
    }
}