package interface_adapter.end_battle;

import entity.Battle;
import use_case.end_battle.EndBattleInputBoundary;
import use_case.end_battle.EndBattleInputData;

/**
 * Controller for the End Battle use case.
 *
 * Called by the BattlePanel when the user clicks
 * "Rematch" or "New Game".
 */
public class EndBattleController {

    private final EndBattleInputBoundary interactor;

    public EndBattleController(EndBattleInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void executeRematch(Battle battle) {
        EndBattleInputData inputData =
                new EndBattleInputData(EndBattleInputData.Choice.REMATCH, battle);
        interactor.execute(inputData);
    }

    public void executeNewGame() {
        // No need for Battle in NEW_GAME – we'll start fresh
        EndBattleInputData inputData =
                new EndBattleInputData(EndBattleInputData.Choice.NEW_GAME, null);
        interactor.execute(inputData);
    }
}
