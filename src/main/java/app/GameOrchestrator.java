
package app;

import dataaccess.JsonGameRepository;
import entity.GameState;
import entity.PokemonTeam;

/**
 * Single source of truth for the entire game.
 * Call updateState() after ANY change → auto-saves instantly.
 */
public class GameOrchestrator {

    private static GameState current;

    /**
     * Initializes the game state by loading from save or starting fresh.
     */
    public static void init() {
        final GameState saved = JsonGameRepository.load();
        System.out.println(saved);
        if (saved != null) {
            current = saved;
            System.out.println("[Auto-Load] Resumed from tower level " + saved.currentTowerLevel());
        }
        else {
            current = new GameState(
                    GameState.Screen.TEAM_SELECTION,
                    GameState.Player.PLAYER1,
                    new PokemonTeam(),
                    new PokemonTeam(),
                    null,
                    1,
                    0
            );
            autoSave();
        }

    }

    /**
     * Forces a completely new game by deleting the save file and resetting state.
     */
    public static void forceNewGame() {
        // Delete autosave file and start completely fresh
        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("resources/autosave.json"));
            System.out.println("[New Game] Old save deleted - starting fresh!");
        }
        catch (java.io.IOException exception) {
            System.out.println("file doesn't exist");
        }

        // Create brand-new empty state
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

    /**
     * Updates the current game state and automatically saves it.
     *
     * @param newState the new game state to set
     */
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
