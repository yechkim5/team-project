package interface_adapter.select_team;

import entity.Move;
import entity.Pokemon;
import usecase.select_team.SelectTeamInputBoundary;
import usecase.select_team.SelectTeamInputData;

import java.util.List;

/**
 * Controller for Select Team Use Case
 *
 * This controller is part of the Interface Adapter layer and acts as a boundary
 * between the view layer and the use case layer.
 *
 * RESPONSIBILITIES:
 * - Receives user input from the view
 * - Converts view data into use case input data
 * - Calls the use case interactor
 *
 * Follows Clean Architecture - view depends on controller, controller depends on use case interface.
 */
public class SelectTeamController {
    private final SelectTeamInputBoundary selectTeamInteractor;

    /**
     * Constructor - Dependency Injection
     *
     * @param selectTeamInteractor The use case interactor
     */
    public SelectTeamController(SelectTeamInputBoundary selectTeamInteractor) {
        this.selectTeamInteractor = selectTeamInteractor;
    }

    /**
     * Add a pokemon to the team
     *
     * @param pokemon The pokemon to add
     * @param selectedMoves The moves selected for this pokemon
     * @param playerNumber The player number (1 or 2)
     */
    public void addPokemonToTeam(Pokemon pokemon, List<Move> selectedMoves, int playerNumber) {
        SelectTeamInputData inputData = new SelectTeamInputData(pokemon, selectedMoves, playerNumber);
        selectTeamInteractor.execute(inputData);
    }

    /**
     * Get the current team for a player
     *
     * @param playerNumber The player number (1 or 2)
     */
    public void getCurrentTeam(int playerNumber) {
        selectTeamInteractor.getCurrentTeam(playerNumber);
    }

    /**
     * Finalize the team for a player
     *
     * @param playerNumber The player number (1 or 2)
     */
    public void finalizeTeam(int playerNumber) {
        selectTeamInteractor.finalizeTeam(playerNumber);
    }
}

