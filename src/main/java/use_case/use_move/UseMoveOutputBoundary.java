package use_case.use_move;

/**
 * Output Boundary for Use Move Use Case
 *
 * Defines how the use case communicates results back to the presenter.
 */
public interface UseMoveOutputBoundary {
    /**
     * Prepare the view for a successful move execution
     * @param outputData The result of the move
     */
    void prepareSuccessView(UseMoveOutputData outputData);

    /**
     * Prepare the view for a failed move execution
     * @param error The error message
     */
    void prepareFailView(String error);

    /**
     * Prepare the view for battle end
     * @param outputData The final battle state
     */
    void prepareBattleEndView(UseMoveOutputData outputData);
}