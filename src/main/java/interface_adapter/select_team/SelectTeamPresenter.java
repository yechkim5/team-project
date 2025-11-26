package interface_adapter.select_team;

import use_case.select_team.SelectTeamOutputBoundary;
import use_case.select_team.SelectTeamOutputData;
import entity.*;

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
        // Set all properties first (these will fire individual property change events)
        viewModel.setSuccess(true);
        viewModel.setPlayerNumber(outputData.getPlayerNumber());
        viewModel.setTeamSize(outputData.getTeamSize());
        viewModel.setAddedPokemon(outputData.getAddedPokemon());
        // Set team and message last - these fire property change events
        // Don't call firePropertyChanged() as setters already fire events
        viewModel.updateCurrentTeam(outputData.getTeam());
        viewModel.setMessage(outputData.getMessage());
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setSuccess(false);
        // Only set message - it will fire property change event
        viewModel.setMessage(error);
    }

    @Override
    public void prepareTeamFinalizedView(SelectTeamOutputData outputData) {
        viewModel.setSuccess(true);
        viewModel.setPlayerNumber(outputData.getPlayerNumber());
        viewModel.setTeamFinalized(true);
        viewModel.setReadyForNextPlayer(outputData.isReadyForNextPlayer());
        // Set team and message last - these fire property change events
        viewModel.updateCurrentTeam(outputData.getTeam());
        viewModel.setMessage(outputData.getMessage());

        // AUTO-SAVE AFTER ANY PLAYER FINALIZES THEIR TEAM
        // This runs when Player 1 or Player 2 clicks "Finalize Team"
        int finalizedPlayer = outputData.getPlayerNumber();

        // Determine next player (or battle soon)
        GameState.Player nextSelector = (finalizedPlayer == 1)
                ? GameState.Player.PLAYER2
                : GameState.Player.PLAYER1;  // After Player 2 → battle next

        // Save current state with correct teams from ViewModel
        app.GameOrchestrator.updateState(new entity.GameState(
                entity.GameState.Screen.TEAM_SELECTION,
                nextSelector,
                viewModel.getPlayer1Team(),   // Always up-to-date
                viewModel.getPlayer2Team(),   // Always up-to-date
                null,                         // no battle yet
                89,                           // your tower level (or make dynamic later)
                0                             // high score
        ));

        System.out.println("[Auto-Save] Player " + finalizedPlayer + " finalized team → saved to autosave.json");
    }

    @Override
    public void prepareNextPlayerView(int playerNumber) {
        viewModel.setPlayerNumber(playerNumber);
        viewModel.setTeamFinalized(false);
        // Only set message - it will fire property change event
        viewModel.setMessage("Player " + playerNumber + " - Select your Pokemon");
        viewModel.setCurrentPlayer(playerNumber);
    }
}

