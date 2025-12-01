package interface_adapter.navigator;

import entity.Battle;

public interface BattleEndNavigator {

    void onRematch(Battle battle);

    void onNewGame();
}
