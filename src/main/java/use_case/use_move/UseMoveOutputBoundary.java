package use_case.use_move;

public interface UseMoveOutputBoundary {
    void prepareSuccessView(UseMoveOutputData outputData);
    void prepareFailView(String error);
}