package interface_adapter.select_team;

import use_case.select_team.SelectTeamOutputBoundary;
import use_case.select_team.SelectTeamOutputData;

/**
 * Presenter for Select Team Use Case
 *
 * This presenter is part of the Interface Adapter layer and acts as a boundary
 * between the use case layer and the view layer.
 *
 * RESPONSIBILITIES:
 * - Receives output data from the use case
 * - Formats the data for the view
 * - Updates the view model
 *
 * Follows Clean Architecture - use case depends on output boundary interface,
 * presenter implements it and updates the view model.
 */
public class SelectTeamPresenter implements SelectTeamOutputBoundary {
    private final SelectTeamViewModel viewModel;

    /**
     * Constructor - Dependency Injection
     *
     * @param viewModel The view model to update
     */
    public SelectTeamPresenter(SelectTeamViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SelectTeamOutputData outputData) {
        viewModel.setTeam(outputData.getTeam());
        viewModel.setMessage(outputData.getMessage());
        viewModel.setSuccess(true);
        viewModel.setPlayerNumber(outputData.getPlayerNumber());
        viewModel.setTeamSize(outputData.getTeamSize());
        viewModel.setAddedPokemon(outputData.getAddedPokemon());
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setMessage(error);
        viewModel.setSuccess(false);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareTeamFinalizedView(SelectTeamOutputData outputData) {
        viewModel.setTeam(outputData.getTeam());
        viewModel.setMessage(outputData.getMessage());
        viewModel.setSuccess(true);
        viewModel.setPlayerNumber(outputData.getPlayerNumber());
        viewModel.setTeamFinalized(true);
        viewModel.setReadyForNextPlayer(outputData.isReadyForNextPlayer());
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareNextPlayerView(int playerNumber) {
        viewModel.setPlayerNumber(playerNumber);
        viewModel.setTeamFinalized(false);
        viewModel.setMessage("Player " + playerNumber + " - Select your Pokemon");
        viewModel.firePropertyChanged();
    }
}

