package use_case.end_battle;

import entity.Battle;
import entity.PokemonTeam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndBattleInteractorTest {

    // Spy presenter to capture what Interactor sends
    private static class PresenterSpy implements EndBattleOutputBoundary {

        EndBattleOutputData lastOutputData;
        int callCount = 0;

        @Override
        public void prepareView(EndBattleOutputData outputData) {
            this.lastOutputData = outputData;
            this.callCount++;
        }
    }

    private Battle makeDummyBattle() {
        PokemonTeam team1 = new PokemonTeam();
        PokemonTeam team2 = new PokemonTeam();
        return new Battle(team1, team2);
    }

    @Test
    void executeWithValidInputAndNonNullBattleCallsPresenter() {
        // Given
        Battle battle = makeDummyBattle();
        EndBattleInputData.Choice choice = EndBattleInputData.Choice.REMATCH;
        EndBattleInputData inputData = new EndBattleInputData(choice, battle);

        PresenterSpy spy = new PresenterSpy();
        EndBattleInteractor interactor = new EndBattleInteractor(spy);

        // When
        interactor.execute(inputData);

        // Then
        assertEquals(1, spy.callCount);
        assertNotNull(spy.lastOutputData);

        assertEquals(choice, spy.lastOutputData.getChoice());
        assertSame(battle, spy.lastOutputData.getBattle());
        // winner is whatever battle.getWinner() returns (likely null here), we just ensure no crash
    }

    @Test
    void executeWithValidInputAndNullBattleSetsBattleAndWinnerToNull() {
        // Given
        EndBattleInputData.Choice choice = EndBattleInputData.Choice.NEW_GAME;
        EndBattleInputData inputData = new EndBattleInputData(choice, null);

        PresenterSpy spy = new PresenterSpy();
        EndBattleInteractor interactor = new EndBattleInteractor(spy);

        // When
        interactor.execute(inputData);

        // Then
        assertEquals(1, spy.callCount);
        assertNotNull(spy.lastOutputData);

        assertEquals(choice, spy.lastOutputData.getChoice());
        assertNull(spy.lastOutputData.getBattle());
        assertNull(spy.lastOutputData.getWinner());
    }

    @Test
    void executeWithNullInputThrowsIllegalArgumentException() {
        PresenterSpy spy = new PresenterSpy();
        EndBattleInteractor interactor = new EndBattleInteractor(spy);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> interactor.execute(null)
        );

        assertTrue(ex.getMessage().contains("must not be null"));
        assertEquals(0, spy.callCount);
    }

    @Test
    void executeWithNullChoiceThrowsIllegalArgumentException() {
        // Given
        Battle battle = makeDummyBattle();
        EndBattleInputData badData = new EndBattleInputData(null, battle);

        PresenterSpy spy = new PresenterSpy();
        EndBattleInteractor interactor = new EndBattleInteractor(spy);

        // When / Then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> interactor.execute(badData)
        );

        assertTrue(ex.getMessage().contains("must not be null"));
        assertEquals(0, spy.callCount);
    }
}
