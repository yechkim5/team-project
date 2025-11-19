package entity;

/*
Examples of good software design:
    -Move is an abstract class to make implementation of various
    different move types easier and also to prevent Pokemon class from relying
    directly on concrete lower subclass, enforcing DIP.

    -The only time that move can change is when it is used by the Pokemon actor
    to account for the number of times it has been used. This is to satisfy the SRP.


 */
public abstract class Move {
    private final String moveName;
    private final String moveDescription;
    private final String moveType;
    private final int maxPp;
    private int currentPp;
    private final int moveAccuracy;
    private final String moveClass;

    //Will make this builder for ease of instantiation using API
    public Move(String moveName, String moveType, int pp, String moveDescription, String moveClass, int moveAccuracy) {
        this.moveName = moveName;
        this.moveDescription = moveDescription;
        this.moveClass = moveClass;
        this.moveType = moveType;
        this.maxPp = pp;
        this.currentPp = maxPp;
        this.moveAccuracy = moveAccuracy;
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
}