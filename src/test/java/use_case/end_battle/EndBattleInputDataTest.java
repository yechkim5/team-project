package use_case.end_battle;

import entity.Battle;
import entity.PokemonTeam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndBattleInputDataTest {

    @Test
    void constructorAndGettersWork() {
        // Given
        PokemonTeam team1 = new PokemonTeam();
        PokemonTeam team2 = new PokemonTeam();
        Battle battle = new Battle(team1, team2);

        EndBattleInputData.Choice choice = EndBattleInputData.Choice.REMATCH;

        // When
        EndBattleInputData inputData = new EndBattleInputData(choice, battle);

        // Then
        assertEquals(choice, inputData.getChoice());
        assertSame(battle, inputData.getBattle());
    }
}
