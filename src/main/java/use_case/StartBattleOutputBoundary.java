package use_case;

/**
 * Output Boundary interface for the Start Battle use case.
 * The presenter will implement this to prepare the appropriate view.
 * This interface follows the Dependency Inversion Principle - the use case
 * depends on this abstraction, not on concrete presenter implementations.
 */
public interface StartBattleOutputBoundary {

    /**
     * Prepares the success view when battle starts successfully.
     * This is called by the interactor when both teams are valid
     * and the battle has been created successfully.
     * @param outputData the output data containing the battle object
     */
    void prepareSuccessView(StartBattleOutputData outputData);

    /**
     * Prepares the fail view when battle cannot start.
     * This is called by the interactor when validation fails
     * or an error occurs during battle creation.
     * @param errorMessage the error message explaining why the battle cannot start
     */
    void prepareFailView(String errorMessage);
}