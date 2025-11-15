package interface_adapter;

import entity.Trainer;
import use_case.SaveProgress;

public class SaveProgressController {

    private final SaveProgress saveProgressUseCase;

    public SaveProgressController(SaveProgress saveProgressUseCase) {
        this.saveProgressUseCase = saveProgressUseCase;
    }

    /**
     * Called when the user clicks the "Save" button
     */
    public SaveProgress.Output save(Trainer playerTrainer, int currentTowerLevel) {
        return saveProgressUseCase.execute(playerTrainer, currentTowerLevel);
    }
}