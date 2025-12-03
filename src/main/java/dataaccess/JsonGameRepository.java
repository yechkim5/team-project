
package dataaccess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import org.json.JSONObject;

import entity.GameState;
import use_case.game_state_persistence.SaveGameInteractor;

public class JsonGameRepository {

    private static String saveFile = "resources/autosave.json";

    /**
     * Saves the given GameState to the autosave file.
     *
     * @param state the GameState to save
     */
    public static void save(GameState state) {

        final int indentFactor = 4;

        try {
            // Create parent directory if missing (e.g. "resources" or "test_resources")
            Files.createDirectories(Paths.get(saveFile).getParent());

            final JSONObject json = SaveGameInteractor.toJson(state);
            Files.writeString(Paths.get(saveFile), json.toString(indentFactor));
        }
        catch (IOException exception) {
            System.err.println("Auto-save failed: " + exception.getMessage());
            // Optional: e.printStackTrace(); // uncomment during debugging
        }
    }

    /**
     * Loads the saved game state from disk.
     *
     * @return the loaded {@link GameState} if a save file exists, or {@code null} if no save file is found
     */
    public static GameState load() {
        try {
            // Explicitly handle missing file instead of letting readString() throw
            if (!Files.exists(Paths.get(saveFile))) {
                return null;
            }

            final String content = Files.readString(Paths.get(saveFile));

            if (content == null || content.isBlank()) {
                return null;
            }

            final JSONObject json = new JSONObject(content);

            return SaveGameInteractor.fromJson(json);

        }
        catch (NoSuchFileException exception) {
            // File doesn't exist → normal case
        }
        catch (IOException exception) {
            System.err.println("Auto-load failed (" + saveFile + "): " + exception.getMessage());
        }
        return null;
    }

    // Used when user selects a file via JFileChooser
    public static void setSaveFile(String newSaveFile) {
        saveFile = newSaveFile;
    }

}
