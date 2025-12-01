package interface_adapter.end_battle;

import interface_adapter.battle.BattleViewModel;
import interface_adapter.navigator.BattleEndNavigator;
import use_case.end_battle.EndBattleOutputBoundary;
import use_case.end_battle.EndBattleOutputData;

/**
 * Presenter for the End Battle use case.
 *
 * For now it simply notifies the BattleViewModel that the
 * end-of-battle choice has been handled. You can extend this
 * later to trigger navigation (Rematch vs New Game).
 */
public class EndBattlePresenter implements EndBattleOutputBoundary {

    private final BattleViewModel battleViewModel;
    private final BattleEndNavigator navigator;

    public EndBattlePresenter(BattleViewModel battleViewModel, BattleEndNavigator navigator) {
        this.battleViewModel = battleViewModel;
        this.navigator = navigator;
    }

    @Override
    public void prepareView(EndBattleOutputData outputData) {
        switch (outputData.getChoice()) {
            case REMATCH -> navigator.onRematch(outputData.getBattle());

            case NEW_GAME ->  navigator.onNewGame();
        }

        battleViewModel.firePropertyChanged();
    }
}
