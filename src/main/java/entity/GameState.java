package entity;

/**
 * GameState is an IMMUTABLE snapshot of the entire game at any moment.
 * Every time anything changes (pick Pokémon, attack, switch, etc.),
 * we create a NEW GameState and auto-save it to JSON.
 *
 * Why a record?
 * → 100% immutable (no bugs from accidental changes)
 * → Super clean and modern Java
 * → Perfect for saving/loading
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

    /** The three main screens in the game */
    public enum Screen {
        TEAM_SELECTION,  // Players are building their teams
        BATTLE,          // PvP battle is active
        GAME_OVER        // One player won or game ended
    }

    /** The two human players — this is PvP only */
    public enum Player {
        PLAYER1,
        PLAYER2
    }

    /** Whose turn it is during battle */
    public enum Turn {
        PLAYER1,
        PLAYER2
    }

    /** All info needed while a battle is running */
    public record BattlePhase(
            Turn currentTurn,           // Who moves now
            int player1ActiveIndex,     // Active Pokémon index for Player 1 (0–4)
            int player2ActiveIndex      // Active Pokémon index for Player 2 (0–4)
    ) {}
}