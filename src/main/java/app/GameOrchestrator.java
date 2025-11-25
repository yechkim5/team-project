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
        System.out.println(saved);
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
            autoSave();
        }

    }

    public static void forceNewGame() {
        // Delete autosave file and start completely fresh
        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("resources/autosave.json"));
            System.out.println("[New Game] Old save deleted — starting fresh!");
        } catch (Exception e) {
            // Ignore if file doesn't exist
        }

        // Create brand new empty state
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