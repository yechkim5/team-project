package app;

import data_access.JsonGameRepository;
import entity.GameState;
import entity.PokemonTeam;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * 100% line + branch coverage for GameOrchestrator
 * Tests all paths: load existing save, no save → new game, forceNewGame, updateState
 */
public class GameOrchestratorTest {

    private static final Path SAVE_FILE = Paths.get("resources/autosave.json");

    @Before
    @After
    public void cleanup() {
        // Ensure clean state before and after each test
        try {
            Files.deleteIfExists(SAVE_FILE);
        } catch (IOException ignored) {}
        GameOrchestratorTestHelper.clearCurrentState();
    }

    @Test
    public void init_WhenNoSaveFileExists_ShouldCreateNewGame() {
        // Act
        GameOrchestrator.init();

        // Assert
        GameState state = GameOrchestrator.getCurrent();
        assertNotNull(state);
        assertEquals(GameState.Screen.TEAM_SELECTION, state.currentScreen());
        assertEquals(GameState.Player.PLAYER1, state.activeTeamSelector());
        assertEquals(1, state.currentTowerLevel());
        assertEquals(0, state.highScore());
        assertTrue(Files.exists(SAVE_FILE)); // autoSave() was called
    }

    @Test
    public void init_WhenSaveFileExists_ShouldLoadIt() throws IOException {
        // Arrange: manually create a fake save file
        GameState fakeSavedState = new GameState(
                GameState.Screen.BATTLE,
                GameState.Player.PLAYER2,
                new PokemonTeam(),
                new PokemonTeam(),
                new GameState.BattlePhase(GameState.Turn.PLAYER1, 2, 3),
                42,
                100
        );
        JsonGameRepository.save(fakeSavedState); // creates the file

        // Act
        GameOrchestrator.init();

        // Assert
        GameState loaded = GameOrchestrator.getCurrent();
        assertEquals(GameState.Screen.BATTLE, loaded.currentScreen());
        assertEquals(42, loaded.currentTowerLevel());
        assertEquals(100, loaded.highScore());
        assertNotNull(loaded.battlePhase());
        assertEquals(2, loaded.battlePhase().player1ActiveIndex());
    }

    @Test
    public void forceNewGame_ShouldDeleteSaveAndResetToInitialState() throws IOException {
        // Arrange: create a save file first
        JsonGameRepository.save(new GameState(
                GameState.Screen.BATTLE, GameState.Player.PLAYER1,
                new PokemonTeam(), new PokemonTeam(), null, 99, 999));

        assertTrue(Files.exists(SAVE_FILE));

// Act
        GameOrchestrator.forceNewGame();

// Assert: save file should be deleted
        assertFalse("Save file should be deleted", Files.exists(SAVE_FILE));

        GameState state = GameOrchestrator.getCurrent();
        assertEquals(GameState.Screen.TEAM_SELECTION, state.currentScreen());
        assertEquals(1, state.currentTowerLevel());
        assertEquals(0, state.highScore());
    }

    @Test
    public void updateState_ShouldReplaceCurrentAndTriggerAutoSave() throws IOException {
        // Arrange
        GameOrchestrator.init(); // start with fresh state

        GameState newState = new GameState(
                GameState.Screen.GAME_OVER,
                GameState.Player.PLAYER2,
                new PokemonTeam(),
                new PokemonTeam(),
                null,
                999,
                5000
        );

        // Act
        GameOrchestrator.updateState(newState);

        // Assert
        assertSame(newState, GameOrchestrator.getCurrent());
        assertTrue(Files.exists(SAVE_FILE));

        // Verify file actually contains the new state
        GameState loadedAfterUpdate = JsonGameRepository.load();
        assertEquals(999, loadedAfterUpdate.currentTowerLevel());
        assertEquals(GameState.Screen.GAME_OVER, loadedAfterUpdate.currentScreen());
    }

    @Test
    public void getCurrent_ShouldNeverReturnNull_AfterInit() {
        GameOrchestrator.init();
        assertNotNull(GameOrchestrator.getCurrent());
    }
}