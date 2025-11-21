package use_case;

import entity.PokemonTeam;

/**
 * Input Data for the Start Battle use case.
 * Contains the two Pokemon teams that will battle.
 */
public class StartBattleInputData {
    private final PokemonTeam userTeam;
    private final PokemonTeam opponentTeam;

    /**
     * Constructor for StartBattleInputData.
     * @param userTeam the user's Pokemon team
     * @param opponentTeam the opponent's Pokemon team
     */
    public StartBattleInputData(PokemonTeam userTeam, PokemonTeam opponentTeam) {
        this.userTeam = userTeam;
        this.opponentTeam = opponentTeam;
    }

    /**
     * Gets the user's team.
     * @return the user's PokemonTeam
     */
    public PokemonTeam getUserTeam() {
        return userTeam;
    }

    /**
     * Gets the opponent's team.
     * @return the opponent's PokemonTeam
     */
    public PokemonTeam getOpponentTeam() {
        return opponentTeam;
    }
}