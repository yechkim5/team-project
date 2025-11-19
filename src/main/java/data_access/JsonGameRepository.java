package data_access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.GameState;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Auto-saves and loads the entire GameState to/from resources/autosave.json
 */
public class JsonGameRepository {

    private static final String SAVE_FILE = "resources/autosave.json";
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(GameState.class, new use_case.auto_save.GameStateTypeAdapter())
            .create();

    public static void save(GameState state) {
        try {
            Files.createDirectories(Paths.get("resources"));
            Files.writeString(Paths.get(SAVE_FILE), gson.toJson(state));
        } catch (Exception e) {
            System.err.println("Auto-save failed: " + e.getMessage());
        }
    }

    public static GameState load() {
        try {
            String json = Files.readString(Paths.get(SAVE_FILE));
            return gson.fromJson(json, GameState.class);
        } catch (Exception e) {
            return null; // no save exists
        }
    }
}