package interface_adapter.battle;

import entity.Battle;
import entity.Move;
import usecase.use_move.UseMoveInputBoundary;
import usecase.use_move.UseMoveInputData;

/**
 * Controller for Battle Use Cases
 *
 * Handles user input from the battle view and delegates to use cases.
 */
public class BattleController {
    private final UseMoveInputBoundary useMoveInteractor;

    public BattleController(UseMoveInputBoundary useMoveInteractor) {
        this.useMoveInteractor = useMoveInteractor;
    }

    /**
     * Execute a move in battle
     * @param battle The current battle
     * @param move The move to use
     * @param moveIndex The index of the move (0-3)
     */
    public void useMove(Battle battle, Move move, int moveIndex) {
        UseMoveInputData inputData = new UseMoveInputData(battle, move, moveIndex);
        useMoveInteractor.execute(inputData);
    }
}