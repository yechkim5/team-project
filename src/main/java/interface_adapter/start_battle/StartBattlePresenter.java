package interface_adapter.start_battle;

import use_case.start_battle.StartBattleOutputBoundary;
import use_case.start_battle.StartBattleOutputData;

public class StartBattlePresenter implements StartBattleOutputBoundary {
    private final StartBattleViewModel viewModel;

    public StartBattlePresenter(StartBattleViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(StartBattleOutputData outputData) {
        viewModel.setBattle(outputData.getBattle());
        viewModel.setSuccess(true);
        viewModel.setMessage(outputData.getMessage());
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setBattle(null);
        viewModel.setSuccess(false);
        viewModel.setMessage(error);
        viewModel.firePropertyChanged();
    }
}
