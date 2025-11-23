package app;

import data_access.JsonGameRepository;
import entity.GameState;

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
                    new entity.PokemonTeam(),
                    new entity.PokemonTeam(),
                    null,
                    1,
                    0
            );
        }
        autoSave();
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