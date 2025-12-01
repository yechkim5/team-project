package data_access;

import entity.GameState;
import org.json.JSONObject;
import use_case.game_state_persistence.SaveGameInteractor;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class JsonGameRepository {

    private static String saveFile = "resources/autosave.json";

    public static void save(GameState state) {
        try {
            // Create parent directory if missing (e.g. "resources" or "test_resources")
            Files.createDirectories(Paths.get(saveFile).getParent());

            JSONObject json = SaveGameInteractor.toJson(state);
            Files.writeString(Paths.get(saveFile), json.toString(4)); // pretty print
        } catch (Exception e) {
            System.err.println("Auto-save failed: " + e.getMessage());
            // Optional: e.printStackTrace(); // uncomment during debugging
        }
    }

    public static GameState load() {
        try {
            // Explicitly handle missing file instead of letting readString() throw
            if (!Files.exists(Paths.get(saveFile))) {
                return null;
            }

            String content = Files.readString(Paths.get(saveFile));

            if (content == null || content.isBlank()) {
                return null;
            }

            JSONObject json = new JSONObject(content);

            // REMOVED THIS LINE → IT WAS BREAKING TESTS:
            // System.out.print(content);

            return SaveGameInteractor.fromJson(json);

        } catch (NoSuchFileException e) {
            // File doesn't exist → normal case
            return null;
        } catch (Exception e) {
            System.err.println("Auto-load failed (" + saveFile + "): " + e.getMessage());
            return null; // corrupt or unreadable file
        }
    }

    // Used when user selects a file via JFileChooser
    public static void setSaveFile(String newSaveFile) {
        saveFile = newSaveFile;
    }

    // Optional: helpful for debugging / testing
    public static String getCurrentSavePath() {
        return saveFile;
    }
}