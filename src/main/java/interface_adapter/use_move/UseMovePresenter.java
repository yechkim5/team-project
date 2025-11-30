package interface_adapter.use_move;

import use_case.use_move.UseMoveOutputBoundary;
import use_case.use_move.UseMoveOutputData;

public class UseMovePresenter implements UseMoveOutputBoundary {
    private final UseMoveViewModel viewModel;

    public UseMovePresenter(UseMoveViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(UseMoveOutputData outputData) {
        System.out.println("[UseMovePresenter] Preparing success view");

        viewModel.setBattle(outputData.getBattle());
        viewModel.setMessage(outputData.getMessage());
        viewModel.setLastDamage(outputData.getDamageDealt());
        viewModel.setAttacker(outputData.getAttacker());
        viewModel.setDefender(outputData.getDefender());
        viewModel.setMoveName(outputData.getMoveName());

        viewModel.firePropertyChanged();  // ✅ CRITICAL - Must be called!

        System.out.println("[UseMovePresenter] Property change fired");
    }

    @Override
    public void prepareFailView(String error) {
        System.out.println("[UseMovePresenter] Preparing fail view: " + error);

        viewModel.setMessage(error);
        viewModel.firePropertyChanged();  // ✅ CRITICAL - Must be called!
    }
}