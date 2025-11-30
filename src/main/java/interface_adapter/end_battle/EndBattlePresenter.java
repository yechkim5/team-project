package interface_adapter.end_battle;

import use_case.end_battle.EndBattleOutputBoundary;
import use_case.end_battle.EndBattleOutputData;

public class EndBattlePresenter implements EndBattleOutputBoundary {
    private final EndBattleViewModel viewModel;

    public EndBattlePresenter(EndBattleViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(EndBattleOutputData outputData) {
        viewModel.setBattle(outputData.getBattle());
        viewModel.setWinner(outputData.getWinner());
        viewModel.setWinnerName(outputData.getWinnerName());
        viewModel.setRemainingPokemon(outputData.getRemainingPokemon());
        viewModel.setMessage(outputData.getMessage());
        viewModel.setBattleEnded(true);

        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setMessage(error);
        viewModel.setBattleEnded(false);
        viewModel.firePropertyChanged();
    }
}