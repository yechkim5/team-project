package use_case.end_battle;

/**
 * Output Boundary for the End Battle use case.
 * The Interactor calls this, and the Presenter implements it.
 */
public interface EndBattleOutputBoundary {

    void prepareView(EndBattleOutputData outputData);
}
