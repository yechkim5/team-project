package usecase.use_move;

import entity.Battle;
import entity.Pokemon;
import entity.PokemonTeam;

/**
 * Output Data for Use Move Use Case
 *
 * Contains the results of executing a move.
 */
public class UseMoveOutputData {
    private final Battle battle;
    private final String message;
    private final boolean battleEnded;
    private final PokemonTeam winner;
    private final Pokemon attacker;
    private final Pokemon defender;
    private final String moveName;
    private final int damageDealt;

    public UseMoveOutputData(Battle battle, String message, boolean battleEnded,
                             PokemonTeam winner, Pokemon attacker, Pokemon defender,
                             String moveName, int damageDealt) {
        this.battle = battle;
        this.message = message;
        this.battleEnded = battleEnded;
        this.winner = winner;
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

    public boolean isBattleEnded() {
        return battleEnded;
    }

    public PokemonTeam getWinner() {
        return winner;
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