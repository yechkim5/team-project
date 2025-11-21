package use_case.start_battle;

/**
 * Input Boundary (Interface) for Start Battle Use Case
 *
 * This interface defines how the outside world (controllers, UI)
 * can call the Start Battle use case.
 *
 * WHY USE AN INTERFACE?
 * - Dependency Inversion Principle (the "D" in SOLID)
 * - Controllers depend on this interface, not the concrete interactor
 * - Makes testing easier (can create mock implementations)
 * - Allows swapping implementations without changing callers
 */
public interface StartBattleInputBoundary {
    /**
     * Execute the Start Battle use case
     *
     * @param inputData The data needed to start a battle (teams)
     */
    void execute(StartBattleInputData inputData);
}
