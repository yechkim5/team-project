package entity;

import java.util.List;

/**
 * GameState is an IMMUTABLE snapshot of the ENTIRE game at any moment.
 * Every time the player does something (picks a Pokémon, attacks, switches, etc.),
 * we create a NEW GameState and auto-save it to JSON.
 *
 * Why immutable + record?
 * → Prevents accidental changes
 */
public record GameState(
        Screen currentScreen,  // Which major screen the player is currently on

        // During team selection: whose turn is it to pick a Pokémon?
        // PLAYER1 = human player, PLAYER2 = opponent (or second player in 2P mode)
        Player activeTeamSelector,          // who is picking team
        PokemonTeam playerTeam, // Player's team — max 5 Pokémon, with current HP, moves, PP
        PokemonTeam opponentTeam,
        BattlePhase battlePhase,            // null if not in battle
        int currentTowerLevel,
        int highScore
) {
    public enum Screen { TEAM_SELECTION, BATTLE, GAME_OVER }
    public enum Player { PLAYER1, PLAYER2 }
    public enum Turn { PLAYER, OPPONENT }

    public record BattlePhase(
            Turn currentTurn,
    int playerActiveIndex,
    int opponentActiveIndex
    ) {}
}