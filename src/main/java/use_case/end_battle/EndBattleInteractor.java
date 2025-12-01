package use_case.end_battle;

import entity.Battle;
import entity.PokemonTeam;

/**
 * End Battle Interactor.
 *
 * This use case receives the user's end-of-battle choice
 * (REMATCH or NEW_GAME) and prepares Output Data.
 *
 * Domain logic can be expanded here later (e.g., resetting teams for rematch).
 */
public class EndBattleInteractor implements EndBattleInputBoundary {

    private final EndBattleOutputBoundary presenter;

    public EndBattleInteractor(EndBattleOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(EndBattleInputData inputData) {
        if (inputData == null || inputData.getChoice() == null) {
            throw new IllegalArgumentException("EndBattleInputData and choice must not be null.");
        }

        Battle battle = inputData.getBattle();
        PokemonTeam winner = (battle != null) ? battle.getWinner() : null;

        // In a more advanced version you could:
        //  - reset Pokemon HP/PP for REMATCH here, or
        //  - update some GameState entity for NEW_GAME.
        // For now, we just package the data and pass it to the Presenter.

        EndBattleOutputData outputData =
                new EndBattleOutputData(inputData.getChoice(), battle, winner);

        presenter.prepareView(outputData);
    }
}
