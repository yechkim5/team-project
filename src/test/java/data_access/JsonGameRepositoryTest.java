package data_access;

import entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.Assert.*;

/**
 * 100% coverage for JsonGameRepository
 * Tests: save(), load(), setSaveFile(), directory creation, empty/corrupt file handling
 */
public class JsonGameRepositoryTest {

    private static final Path TEST_DIR = Paths.get("test_resources");
    private static final Path TEST_FILE = TEST_DIR.resolve("test_save.json");

    @Before
    @After
    public void cleanup() throws IOException {
        Path dir = Paths.get("test_resources");

        if (Files.exists(dir)) {
            // Walk through the directory and delete everything inside-out
            Files.walk(dir)
                    .sorted(java.util.Comparator.reverseOrder())  // deletes files first, then folders
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }

        // Reset the save path back to default
        JsonGameRepository.setSaveFile("resources/autosave.json");
    }

    @Test
    public void save_ShouldCreateDirectoryAndWritePrettyJson() throws IOException {
        // Arrange
        JsonGameRepository.setSaveFile(TEST_FILE.toString());
        GameState state = createSampleGameState();

        // Act
        JsonGameRepository.save(state);

        // Assert
        assertTrue("Save directory should be created", Files.exists(TEST_DIR));
        assertTrue("Save file should exist", Files.exists(TEST_FILE));

        String content = Files.readString(TEST_FILE);
        assertTrue("JSON should be pretty-printed (contain newlines)", content.contains("\n"));
        assertTrue("JSON should contain expected fields", content.contains("currentScreen"));
        assertTrue("JSON should contain tower level", content.contains("42"));
    }

    @Test
    public void save_WhenDirectoryDoesNotExist_ShouldCreateIt() {
        // Arrange
        JsonGameRepository.setSaveFile(TEST_FILE.toString());
        assertFalse("Test directory should not exist yet", Files.exists(TEST_DIR));

        // Act
        JsonGameRepository.save(createSampleGameState());

        // Assert
        assertTrue("Directory 'test_resources' should be created automatically", Files.exists(TEST_DIR));
    }

    @Test
    public void load_WhenValidSaveExists_ShouldReturnCorrectGameState() {
        // Arrange
        JsonGameRepository.setSaveFile(TEST_FILE.toString());
        GameState expected = createSampleGameState();
        JsonGameRepository.save(expected);

        // Act
        GameState loaded = JsonGameRepository.load();

        // Assert
        assertNotNull(loaded);
        assertEquals(GameState.Screen.BATTLE, loaded.currentScreen());
        assertEquals(42, loaded.currentTowerLevel());
        assertEquals(999, loaded.highScore());
        assertNotNull(loaded.battlePhase());
        assertEquals(GameState.Turn.PLAYER2, loaded.battlePhase().currentTurn());
    }

    @Test
    public void load_WhenFileDoesNotExist_ShouldReturnNull() {
        // Arrange
        JsonGameRepository.setSaveFile(TEST_FILE.toString());
        assertFalse(Files.exists(TEST_FILE));

        // Act
        GameState result = JsonGameRepository.load();

        // Assert
        assertNull("load() should return null when no file exists", result);
    }

    @Test
    public void load_WhenFileIsEmpty_ShouldReturnNull() throws IOException {
        // Arrange
        JsonGameRepository.setSaveFile(TEST_FILE.toString());
        Files.createDirectories(TEST_DIR);
        Files.writeString(TEST_FILE, ""); // empty file

        // Act
        GameState result = JsonGameRepository.load();

        // Assert
        assertNull("load() should return null on empty file", result);
    }

    @Test
    public void load_WhenFileIsCorrupt_ShouldReturnNull() throws IOException {
        // Arrange
        JsonGameRepository.setSaveFile(TEST_FILE.toString());
        Files.createDirectories(TEST_DIR);
        Files.writeString(TEST_FILE, "{ invalid json }");

        // Act
        GameState result = JsonGameRepository.load();

        // Assert
        assertNull("load() should return null on corrupt JSON", result);
    }

    @Test
    public void setSaveFile_ShouldChangePathForFutureOperations() {
        // Arrange
        String newPath = "test_resources/custom_save.json";
        JsonGameRepository.setSaveFile(newPath);

        GameState state = createSampleGameState();
        JsonGameRepository.save(state);

        // Assert
        Path expectedPath = Paths.get(newPath);
        assertTrue("Save should go to new path", Files.exists(expectedPath));
    }

    // Helper
    private GameState createSampleGameState() {
        return new GameState(
                GameState.Screen.BATTLE,
                GameState.Player.PLAYER1,
                new PokemonTeam(),
                new PokemonTeam(),
                new GameState.BattlePhase(GameState.Turn.PLAYER2, 1, 3),
                42,
                999
        );
    }
}