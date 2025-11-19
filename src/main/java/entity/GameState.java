package entity;

import java.util.List;

/**
 * Immutable snapshot of the entire game.
 * This is what gets auto-saved to JSON.
 */
public record GameState(
        Screen currentScreen,
        Player activeTeamSelector,          // who is picking team
        PokemonTeam playerTeam,
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