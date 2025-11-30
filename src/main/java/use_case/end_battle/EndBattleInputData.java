package use_case.end_battle;

import entity.Battle;

public class EndBattleInputData {
    private final Battle battle;

    public EndBattleInputData(Battle battle) {
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }
}
