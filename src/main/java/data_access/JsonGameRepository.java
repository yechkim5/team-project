package data_access;

import entity.GameState;
import org.json.JSONObject;
import use_case.auto_save.OrgJsonGameStateSerializer;

import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonGameRepository {

    private static String saveFile = "resources/autosave.json";

    public static void save(GameState state) {
        try {
            Files.createDirectories(Paths.get("resources"));
            JSONObject json = OrgJsonGameStateSerializer.toJson(state);
            Files.writeString(Paths.get(saveFile), json.toString(4)); // pretty-print
        } catch (Exception e) {
            System.err.println("Auto-save failed: " + e.getMessage());
        }
    }

    public static GameState load() {
        try {
            String content = Files.readString(Paths.get(saveFile));
            if (content.isBlank()) {
                return null;
            }
            JSONObject json = new JSONObject(content);
            System.out.print(content);
            return OrgJsonGameStateSerializer.fromJson(json);
        } catch (Exception e) {
            System.err.println("Auto-load failed: " + e.getMessage());
            return null; // no save exists or corrupt
        }
    }
    //Saves the new path that you get from the file selector (after loading file)
    public static void setSaveFile(String newSaveFile) {
        saveFile = newSaveFile;
    }
}