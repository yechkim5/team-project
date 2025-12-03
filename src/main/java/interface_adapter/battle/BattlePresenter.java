package interface_adapter.battle;

import use_case.use_move.UseMoveOutputBoundary;
import use_case.use_move.UseMoveOutputData;

/**
 * Presenter for Battle Use Cases
 *
 * Formats use case output data for the view.
 */
public class BattlePresenter implements UseMoveOutputBoundary {
    private final BattleViewModel viewModel;

    public BattlePresenter(BattleViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(UseMoveOutputData outputData) {
        viewModel.setBattle(outputData.getBattle());
        viewModel.setMessage(outputData.getMessage());
        viewModel.setBattleEnded(false);
        viewModel.setLastDamage(outputData.getDamageDealt());

        // Notify view to update
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setMessage(error);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareBattleEndView(UseMoveOutputData outputData) {
        viewModel.setBattle(outputData.getBattle());
        viewModel.setMessage(outputData.getMessage());
        viewModel.setBattleEnded(true);
        viewModel.setWinner(outputData.getWinner());
        viewModel.setLastDamage(outputData.getDamageDealt());

        // Notify view to update
        viewModel.firePropertyChanged();
    }
}