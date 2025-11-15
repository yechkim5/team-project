// ===================================================================
// 3. use_case/SaveProgressDataAccessInterface.java  (new file)
// ===================================================================
package use_case;

import entity.Trainer;
import java.util.Optional;

/**
 * Clean Architecture port.
 * The use case only knows about this interface – never about files or CSV.
 */
public interface SaveProgressDataAccessInterface {

    void saveProgress(Trainer playerTrainer, int currentTowerLevel, int highScore);

    Optional<ProgressData> loadProgress();

    /** Simple immutable record returned when loading */
    record ProgressData(
            Trainer trainer,
            int currentTowerLevel,
            int highScore
    ) {}
}