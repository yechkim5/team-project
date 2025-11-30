package interface_adapter.end_battle;

import entity.Battle;
import use_case.end_battle.EndBattleInputBoundary;
import use_case.end_battle.EndBattleInputData;

public class EndBattleController {
    private final EndBattleInputBoundary endBattleInteractor;

    public EndBattleController(EndBattleInputBoundary endBattleInteractor) {
        this.endBattleInteractor = endBattleInteractor;
    }

    public void endBattle(Battle battle) {
        EndBattleInputData inputData = new EndBattleInputData(battle);
        endBattleInteractor.execute(inputData);
    }
}
