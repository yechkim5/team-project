package interface_adapter.start_battle;

import use_case.start_battle.StartBattleOutputBoundary;
import use_case.start_battle.StartBattleOutputData;
import interface_adapter.start_battle.BattleViewModel;

/**
 * Presenter for Start Battle use case.
 * Updates the BattleViewModel with the newly created Battle.
 */
public class StartBattlePresenter implements StartBattleOutputBoundary {
    private final BattleViewModel viewModel;

    public StartBattlePresenter(BattleViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(StartBattleOutputData outputData) {
        viewModel.setBattle(outputData.getBattle());
        viewModel.setMessage(outputData.getMessage());
    }

    @Override
    public void prepareFailView(String error) {
        System.err.println("Battle start failed: " + error);
        viewModel.setMessage("Failed to start battle: " + error);
    }
}
