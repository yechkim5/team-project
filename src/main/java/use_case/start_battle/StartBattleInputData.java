package use_case.start_battle;

import entity.PokemonTeam;

/**
 * Input Data for Start Battle Use Case
 *
 * This class encapsulates the data needed to start a battle:
 * - User's team
 * - Opponent's team
 *
 * This follows the Clean Architecture pattern where use cases
 * receive data through simple data transfer objects (DTOs).
 */
public class StartBattleInputData {
    private final PokemonTeam userTeam;
    private final PokemonTeam opponentTeam;

    /**
     * Constructor for StartBattleInputData
     *
     * @param userTeam The player's team of Pokémon
     * @param opponentTeam The opponent's team of Pokémon
     */
    public StartBattleInputData(PokemonTeam userTeam, PokemonTeam opponentTeam) {
        this.userTeam = userTeam;
        this.opponentTeam = opponentTeam;
    }

    /**
     * Get the user's team
     * @return PokemonTeam of the user
     */
    public PokemonTeam getUserTeam() {
        return userTeam;
    }

    /**
     * Get the opponent's team
     * @return PokemonTeam of the opponent
     */
    public PokemonTeam getOpponentTeam() {
        return opponentTeam;
    }
}