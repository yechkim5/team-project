package usecase.start_battle;

/**
 * Output Boundary (Interface) for Start Battle Use Case
 *
 * This interface defines how the use case communicates results
 * back to the presentation layer (presenters).
 *
 * WHY USE AN INTERFACE?
 * - Dependency Inversion Principle (the "D" in SOLID)
 * - Use case doesn't depend on concrete presenter
 * - Use case layer stays independent of view layer
 * - Makes testing easier
 *
 * The presenter implements this interface and decides how to
 * format/display the results to the user.
 */
public interface StartBattleOutputBoundary {
    /**
     * Prepare the view for a successful battle start
     *
     * @param outputData The output data containing the Battle object
     */
    void prepareSuccessView(StartBattleOutputData outputData);

    /**
     * Prepare the view for a failed battle start
     *
     * @param error The error message explaining why battle failed
     */
    void prepareFailView(String error);
}
