package interface_adapter.use_move;

import entity.Battle;
import entity.Move;
import use_case.use_move.UseMoveInputBoundary;
import use_case.use_move.UseMoveInputData;

public class UseMoveController {
    private final UseMoveInputBoundary useMoveInteractor;

    public UseMoveController(UseMoveInputBoundary useMoveInteractor) {
        this.useMoveInteractor = useMoveInteractor;
    }

    public void useMove(Battle battle, Move move, int moveIndex) {
        UseMoveInputData inputData = new UseMoveInputData(battle, move, moveIndex);
        useMoveInteractor.execute(inputData);
    }
}
