package use_case.select_team;

/**
 * Input Boundary (Interface) for Select Team Use Case
 *
 * This interface defines how the outside world (controllers, UI)
 * can call the Select Team use case.
 *
 * Follows Dependency Inversion Principle - controllers depend on this interface,
 * not the concrete interactor.
 */
public interface SelectTeamInputBoundary {
    /**
     * Execute the Select Team use case
     *
     * @param inputData The data needed to select a team (pokemon name, selected moves)
     */
    void execute(SelectTeamInputData inputData);
    
    /**
     * Get the current team for the specified player
     *
     * @param playerNumber The player number (1 or 2)
     * @return The current team
     */
    void getCurrentTeam(int playerNumber);
    
    /**
     * Finalize the team for the specified player
     *
     * @param playerNumber The player number (1 or 2)
     */
    void finalizeTeam(int playerNumber);
}

