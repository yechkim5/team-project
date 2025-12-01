package use_case.end_battle;

/**
 * Input Boundary for the End Battle use case.
 * The Controller calls this interface.
 */
public interface EndBattleInputBoundary {

    void execute(EndBattleInputData inputData);
}
