
package entity;

/**
 * GameState is an IMMUTABLE snapshot of the entire game at any moment.
 * Every time anything changes (pick Pokémon, attack, switch, etc.),
 * we create a NEW GameState and auto-save it to JSON.
 *
 * <p>
 * Why a record?
 * <ul>
 *   <li>100% immutable (no bugs from accidental changes)</li>
 *   <li>Super clean and modern Java</li>
 *   <li>Perfect for saving/loading</li>
 * </ul>
 *
 * @param currentScreen        the current screen being displayed
 * @param activeTeamSelector   whose turn it is to pick during team selection
 * @param player1Team          Player 1's complete Pokémon team
 * @param player2Team          Player 2's complete Pokémon team (PvP only)
 * @param battlePhase          current battle details, or {@code null} if not in battle
 * @param currentTowerLevel    the tower level the player has reached
 * @param highScore            the highest tower level ever reached
 */
public record GameState(
        // What screen are we on?
        Screen currentScreen,

        // During team selection: whose turn is it to pick?
        Player activeTeamSelector,

        // Player 1's full team (max 5 Pokémon, with HP, moves, PP)
        PokemonTeam player1Team,

        // Player 2's full team (max 5 Pokémon) — PvP only, no AI
        PokemonTeam player2Team,

        // Battle details — null if not in battle
        BattlePhase battlePhase,

        // Current tower level (progress in the game)
        int currentTowerLevel,

        // Highest level ever reached
        int highScore
) {

    /** The three main screens in the game. */
    public enum Screen {
        TEAM_SELECTION,
        BATTLE,
        GAME_OVER
    }

    /** The two human players — this is PvP only. */
    public enum Player {
        PLAYER1,
        PLAYER2
    }

    /** Whose turn it is during battle. */
    public enum Turn {
        PLAYER1,
        PLAYER2
    }

    /**
     * All info needed while a battle is running.
     *
     * @param currentTurn           whose turn it is to act
     * @param player1ActiveIndex    index of the currently active Pokémon for player 1
     * @param player2ActiveIndex    index of the currently active Pokémon for player 2
     */
    public record BattlePhase(
            Turn currentTurn,
            int player1ActiveIndex,
            int player2ActiveIndex
    ) {

    }
}
