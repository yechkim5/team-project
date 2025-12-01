package use_case.end_battle;

import entity.Battle;
import entity.PokemonTeam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndBattleOutputDataTest {

    @Test
    void constructorAndGettersWork() {
        // Given
        PokemonTeam team1 = new PokemonTeam();
        PokemonTeam team2 = new PokemonTeam();
        Battle battle = new Battle(team1, team2);

        EndBattleInputData.Choice choice = EndBattleInputData.Choice.NEW_GAME;
        PokemonTeam winner = team1;

        // When
        EndBattleOutputData outputData =
                new EndBattleOutputData(choice, battle, winner);

        // Then
        assertEquals(choice, outputData.getChoice());
        assertSame(battle, outputData.getBattle());
        assertSame(winner, outputData.getWinner());
    }
}
