package use_case.end_battle;

import entity.Battle;

/**
 * Input Data for the End Battle use case.
 * Encapsulates the user's choice (REMATCH or NEW_GAME)
 * and the current Battle (if needed).
 */
public class EndBattleInputData {

    public enum Choice {
        REMATCH,
        NEW_GAME
    }

    private final Choice choice;
    private final Battle battle;

    public EndBattleInputData(Choice choice, Battle battle) {
        this.choice = choice;
        this.battle = battle;
    }

    public Choice getChoice() {
        return choice;
    }

    public Battle getBattle() {
        return battle;
    }
}
