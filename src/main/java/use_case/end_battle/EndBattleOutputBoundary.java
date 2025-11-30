package use_case.end_battle;

public interface EndBattleOutputBoundary {
    void prepareSuccessView(EndBattleOutputData outputData);
    void prepareFailView(String error);
}
