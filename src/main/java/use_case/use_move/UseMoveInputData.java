package use_case.use_move;

import entity.Battle;
import entity.Move;

/**
 * Input Data for Use Move Use Case
 *
 * Contains the battle context and the move to execute.
 */
public class UseMoveInputData {
    private final Battle battle;
    private final Move move;
    private final int moveIndex; // Which move slot (0-3)

    public UseMoveInputData(Battle battle, Move move, int moveIndex) {
        this.battle = battle;
        this.move = move;
        this.moveIndex = moveIndex;
    }

    public Battle getBattle() {
        return battle;
    }

    public Move getMove() {
        return move;
    }

    public int getMoveIndex() {
        return moveIndex;
    }
}