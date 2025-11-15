// ===================================================================
// 6. use_case/SaveProgress.java
// ===================================================================
package use_case;

import entity.Trainer;

public class SaveProgress {
    private final SaveProgressDataAccessInterface dao;

    public SaveProgress(SaveProgressDataAccessInterface dao) {
        this.dao = dao;
    }

    public record Output(boolean success, String message, int highScore) {}

    public Output execute(Trainer player, int currentLevel) {
        try {
            var loaded = dao.loadProgress();
            int highScore = loaded.map(p -> Math.max(p.highScore(), currentLevel))
                    .orElse(currentLevel);

            dao.saveProgress(player, currentLevel, highScore);
            return new Output(true, "Saved! High score: " + highScore, highScore);
        } catch (Exception e) {
            return new Output(false, "Save failed: " + e.getMessage(), 0);
        }
    }
}