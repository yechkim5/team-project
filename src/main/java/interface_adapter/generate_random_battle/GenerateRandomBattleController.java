package interface_adapter.generate_random_battle;

import use_case.generate_random_battle.GenerateRandomBattleInputBoundary;

public class GenerateRandomBattleController {

    private final GenerateRandomBattleInputBoundary interactor;

    public GenerateRandomBattleController(GenerateRandomBattleInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }
}
