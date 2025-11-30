package use_case.end_battle;

import entity.Battle;
import entity.PokemonTeam;

public class EndBattleOutputData {
    private final Battle battle;
    private final PokemonTeam winner;
    private final String winnerName;
    private final int remainingPokemon;
    private final String message;

    public EndBattleOutputData(Battle battle, PokemonTeam winner, String winnerName,
                               int remainingPokemon, String message) {
        this.battle = battle;
        this.winner = winner;
        this.winnerName = winnerName;
        this.remainingPokemon = remainingPokemon;
        this.message = message;
    }

    public Battle getBattle() {
        return battle;
    }

    public PokemonTeam getWinner() {
        return winner;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public int getRemainingPokemon() {
        return remainingPokemon;
    }

    public String getMessage() {
        return message;
    }
}
