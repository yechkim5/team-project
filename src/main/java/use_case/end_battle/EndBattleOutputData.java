package use_case.end_battle;

import entity.Battle;
import entity.PokemonTeam;

/**
 * Output Data for the End Battle use case.
 * Contains the user's choice and winner info for the Presenter / View.
 */
public class EndBattleOutputData {

    private final EndBattleInputData.Choice choice;
    private final Battle battle;
    private final PokemonTeam winner;

    public EndBattleOutputData(EndBattleInputData.Choice choice,
                               Battle battle,
                               PokemonTeam winner) {
        this.choice = choice;
        this.battle = battle;
        this.winner = winner;
    }

    public EndBattleInputData.Choice getChoice() {
        return choice;
    }

    public Battle getBattle() {
        return battle;
    }

    public PokemonTeam getWinner() {
        return winner;
    }
}
