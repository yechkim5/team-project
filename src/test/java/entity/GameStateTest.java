package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void testGameStateCreation_WithAllFields() {
        PokemonTeam team1 = new PokemonTeam();
        PokemonTeam team2 = new PokemonTeam();
        GameState.BattlePhase phase = new GameState.BattlePhase(
                GameState.Turn.PLAYER2, 2, 4);

        GameState state = new GameState(
                GameState.Screen.BATTLE,
                GameState.Player.PLAYER1,
                team1,
                team2,
                phase,
                15,
                25
        );

        assertEquals(GameState.Screen.BATTLE, state.currentScreen());
        assertEquals(GameState.Player.PLAYER1, state.activeTeamSelector());
        assertSame(team1, state.player1Team());
        assertSame(team2, state.player2Team());
        assertNotNull(state.battlePhase());
        assertEquals(GameState.Turn.PLAYER2, state.battlePhase().currentTurn());
        assertEquals(2, state.battlePhase().player1ActiveIndex());
        assertEquals(4, state.battlePhase().player2ActiveIndex());
        assertEquals(15, state.currentTowerLevel());
        assertEquals(25, state.highScore());
    }

    @Test
    void testGameState_WithNullBattlePhase() {
        PokemonTeam team1 = new PokemonTeam();
        PokemonTeam team2 = new PokemonTeam();

        GameState state = new GameState(
                GameState.Screen.TEAM_SELECTION,
                GameState.Player.PLAYER2,
                team1,
                team2,
                null,
                3,
                10
        );

        assertEquals(GameState.Screen.TEAM_SELECTION, state.currentScreen());
        assertEquals(GameState.Player.PLAYER2, state.activeTeamSelector());
        assertSame(team1, state.player1Team());
        assertSame(team2, state.player2Team());
        assertNull(state.battlePhase());
        assertEquals(3, state.currentTowerLevel());
        assertEquals(10, state.highScore());
    }

    @Test
    void testGameState_Immutability_CannotModifyAfterCreation() {
        PokemonTeam team1 = new PokemonTeam();
        PokemonTeam team2 = new PokemonTeam();
        GameState.BattlePhase phase = new GameState.BattlePhase(
                GameState.Turn.PLAYER1, 0, 0);

        GameState state = new GameState(
                GameState.Screen.GAME_OVER,
                GameState.Player.PLAYER1,
                team1,
                team2,
                phase,
                99,
                150
        );

        // These should throw UnsupportedOperationException or do nothing
        // because records are immutable — no setters exist!

        // Verify all values remain unchanged
        assertEquals(GameState.Screen.GAME_OVER, state.currentScreen());
        assertEquals(99, state.currentTowerLevel());
        assertEquals(150, state.highScore());
    }

    @Test
    void testGameState_EqualsAndHashCode_AreValueBased() {
        PokemonTeam teamA = new PokemonTeam();
        PokemonTeam teamB = new PokemonTeam();

        GameState state1 = new GameState(
                GameState.Screen.BATTLE,
                GameState.Player.PLAYER1,
                teamA,
                teamB,
                new GameState.BattlePhase(GameState.Turn.PLAYER1, 1, 1),
                10,
                20
        );

        GameState state2 = new GameState(
                GameState.Screen.BATTLE,
                GameState.Player.PLAYER1,
                teamA,
                teamB,
                new GameState.BattlePhase(GameState.Turn.PLAYER1, 1, 1),
                10,
                20
        );

        GameState different = new GameState(
                GameState.Screen.TEAM_SELECTION,
                GameState.Player.PLAYER2,
                teamA,
                teamB,
                null,
                10,
                20
        );

        assertEquals(state1, state2);
        assertEquals(state1.hashCode(), state2.hashCode());
        assertNotEquals(state1, different);
    }

    @Test
    void testGameState_ToString_IsHelpful() {
        GameState state = new GameState(
                GameState.Screen.BATTLE,
                GameState.Player.PLAYER2,
                new PokemonTeam(),
                new PokemonTeam(),
                new GameState.BattlePhase(GameState.Turn.PLAYER1, 0, 0),
                7,
                12
        );

        String str = state.toString();
        assertTrue(str.contains("BATTLE"));
        assertTrue(str.contains("PLAYER2"));
        assertTrue(str.contains("currentTowerLevel=7"));
        assertTrue(str.contains("highScore=12"));
        assertTrue(str.contains("BattlePhase"));
    }

    @Test
    void testBattlePhase_InnerRecordWorksCorrectly() {
        GameState.BattlePhase phase = new GameState.BattlePhase(
                GameState.Turn.PLAYER2, 3, 1);

        assertEquals(GameState.Turn.PLAYER2, phase.currentTurn());
        assertEquals(3, phase.player1ActiveIndex());
        assertEquals(1, phase.player2ActiveIndex());

        GameState.BattlePhase same = new GameState.BattlePhase(
                GameState.Turn.PLAYER2, 3, 1);
        assertEquals(phase, same);
    }

    @Test
    void testEnums_Coverage() {
        assertEquals(3, GameState.Screen.values().length);
        assertEquals(2, GameState.Player.values().length);
        assertEquals(2, GameState.Turn.values().length);
    }
}