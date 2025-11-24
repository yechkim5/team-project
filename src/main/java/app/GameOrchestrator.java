package app;

import data_access.JsonGameRepository;
import entity.GameState;
import entity.PokemonTeam;

/**
 * Single source of truth for the entire game.
 * Call updateState() after ANY change → auto-saves instantly.
 */
public class GameOrchestrator {

    private static GameState current;

    public static void init() {
        GameState saved = JsonGameRepository.load();
        if (saved != null) {
            current = saved;
            System.out.println("[Auto-Load] Resumed from tower level " + saved.currentTowerLevel());
        } else {
            current = new GameState(
                    GameState.Screen.TEAM_SELECTION,
                    GameState.Player.PLAYER1,
                    new PokemonTeam(),
                    new PokemonTeam(),
                    null,
                    1,
                    0
            );
        }
        autoSave();  // Save initial state if new
    }

    public static void updateState(GameState newState) {
        current = newState;
        autoSave();
    }

    private static void autoSave() {
        JsonGameRepository.save(current);
    }

    public static GameState getCurrent() {
        return current;
    }
}