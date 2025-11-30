package use_case.use_move;

import entity.Battle;
import entity.Pokemon;

public class UseMoveOutputData {
    private final Battle battle;
    private final String message;
    private final Pokemon attacker;
    private final Pokemon defender;
    private final String moveName;
    private final int damageDealt;

    public UseMoveOutputData(Battle battle, String message,
                             Pokemon attacker, Pokemon defender,
                             String moveName, int damageDealt) {
        this.battle = battle;
        this.message = message;
        this.attacker = attacker;
        this.defender = defender;
        this.moveName = moveName;
        this.damageDealt = damageDealt;
    }

    public Battle getBattle() {
        return battle;
    }

    public String getMessage() {
        return message;
    }

    public Pokemon getAttacker() {
        return attacker;
    }

    public Pokemon getDefender() {
        return defender;
    }

    public String getMoveName() {
        return moveName;
    }

    public int getDamageDealt() {
        return damageDealt;
    }
}