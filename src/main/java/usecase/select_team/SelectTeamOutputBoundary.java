package usecase.select_team;

/**
 * Output Boundary (Interface) for Select Team Use Case
 *
 * This interface defines how the use case communicates results
 * back to the presentation layer (presenters).
 *
 * Follows Dependency Inversion Principle - use case doesn't depend on concrete presenter.
 */
public interface SelectTeamOutputBoundary {
    /**
     * Prepare the view for a successful pokemon addition
     *
     * @param outputData The output data containing the updated team
     */
    void prepareSuccessView(SelectTeamOutputData outputData);
    
    /**
     * Prepare the view for a failed operation
     *
     * @param error The error message explaining why the operation failed
     */
    void prepareFailView(String error);
    
    /**
     * Prepare the view for team finalization
     *
     * @param outputData The output data containing the finalized team
     */
    void prepareTeamFinalizedView(SelectTeamOutputData outputData);
    
    /**
     * Prepare the view for switching to the next player
     *
     * @param playerNumber The next player number
     */
    void prepareNextPlayerView(int playerNumber);
}

