package usecase.use_move;

/**
 * Input Boundary for Use Move Use Case
 *
 * This interface defines how the battle controller can execute a move.
 */
public interface UseMoveInputBoundary {
    /**
     * Execute a move in battle
     * @param inputData The move execution data
     */
    void execute(UseMoveInputData inputData);
}