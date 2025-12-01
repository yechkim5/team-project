package use_case.game_state_persistance;

import entity.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import use_case.game_state_persistence.SaveGameInteractor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SaveGameInteractorTest {

    @Test
    void testRoundTrip_EmptyTeams_NoBattlePhase() {
        GameState original = new GameState(
                GameState.Screen.TEAM_SELECTION,
                GameState.Player.PLAYER1,
                new PokemonTeam(),
                new PokemonTeam(),
                null,
                1,
                0
        );

        JSONObject json = SaveGameInteractor.toJson(original);
        GameState deserialized = SaveGameInteractor.fromJson(json);

        assertGameStatesEqual(original, deserialized);
    }

    @Test
    void testRoundTrip_WithMissingOptionalFields() {
        // Create minimal JSON without battlePhase or optional move fields
        String minimalJson = """
            {
                "currentScreen": "TEAM_SELECTION",
                "activeTeamSelector": "PLAYER1",
                "player1Team": {"pokemons": []},
                "player2Team": {"pokemons": []},
                "currentTowerLevel": 3,
                "highScore": 500
            }
            """;

        GameState deserialized = SaveGameInteractor.fromJson(new JSONObject(minimalJson));
        JSONObject serializedAgain = SaveGameInteractor.toJson(deserialized);

        assertEquals(GameState.Screen.TEAM_SELECTION, deserialized.currentScreen());
        assertNull(deserialized.battlePhase());
        assertEquals(3, deserialized.currentTowerLevel());
        assertEquals(500, deserialized.highScore());
    }

    @Test
    void testPokemonWithNullMovesAreHandledGracefully() {
        PokemonTeam team = new PokemonTeam();
        Pokemon pikachu = new Pokemon("Pikachu",
                new BaseLevelStats.BaseLevelStatsBuilder().maxHp(100).attack(55).build(),
                List.of("Electric"));
        pikachu.setMoves(new Move[4]); // all null
        team.addPokemon(pikachu);

        GameState state = new GameState(
                GameState.Screen.TEAM_SELECTION,
                GameState.Player.PLAYER1,
                team,
                new PokemonTeam(),
                null, 1, 0
        );

        JSONObject json = SaveGameInteractor.toJson(state);
        GameState deserialized = SaveGameInteractor.fromJson(json);

        assertEquals(1, deserialized.player1Team().getTeam().size());
        assertEquals("Pikachu", deserialized.player1Team().getTeam().get(0).getName());
    }

    // === Helper Methods ===

    private GameState createComplexGameState() {
        PokemonTeam team1 = new PokemonTeam();
        PokemonTeam team2 = new PokemonTeam();

        Pokemon charizard = createPokemon("Charizard", "Fire", "Flying");
        charizard.setCurrentHP(150);
        charizard.setMoves(new Move[]{
                new Move("Flamethrower", "Fire", 15, "Strong fire attack", "special", 90, 12),
                new Move("Dragon Claw", "Dragon", 15, "Sharp claws", "physical", 100, 15),
                null, null
        });
        team1.addPokemon(charizard);

        Pokemon blastoise = createPokemon("Blastoise", "Water");
        blastoise.setCurrentHP(200);
        team2.addPokemon(blastoise);

        GameState.BattlePhase phase = new GameState.BattlePhase(
                GameState.Turn.PLAYER1, 0, 0
        );

        return new GameState(
                GameState.Screen.BATTLE,
                GameState.Player.PLAYER2,
                team1, team2,
                phase,
                5,
                2500
        );
    }

    private Pokemon createPokemon(String name, String... types) {
        BaseLevelStats stats = new BaseLevelStats.BaseLevelStatsBuilder()
                .maxHp(100).attack(80).defense(70)
                .specialAttack(90).specialDefense(85).speed(78)
                .build();
        return new Pokemon(name, stats, List.of(types));
    }

    private void assertGameStatesEqual(GameState expected, GameState actual) {
        assertEquals(expected.currentScreen(), actual.currentScreen());
        assertEquals(expected.activeTeamSelector(), actual.activeTeamSelector());
        assertEquals(expected.currentTowerLevel(), actual.currentTowerLevel());
        assertEquals(expected.highScore(), actual.highScore());

        assertPokemonTeamsEqual(expected.player1Team(), actual.player1Team());
        assertPokemonTeamsEqual(expected.player2Team(), actual.player2Team());

        if (expected.battlePhase() == null) {
            assertNull(actual.battlePhase());
        } else {
            assertNotNull(actual.battlePhase());
            assertEquals(expected.battlePhase().currentTurn(), actual.battlePhase().currentTurn());
            assertEquals(expected.battlePhase().player1ActiveIndex(), actual.battlePhase().player1ActiveIndex());
            assertEquals(expected.battlePhase().player2ActiveIndex(), actual.battlePhase().player2ActiveIndex());
        }
    }

    private void assertPokemonTeamsEqual(PokemonTeam expected, PokemonTeam actual) {
        List<Pokemon> eList = expected.getTeam();
        List<Pokemon> aList = actual.getTeam();
        assertEquals(eList.size(), aList.size());

        for (int i = 0; i < eList.size(); i++) {
            assertPokemonEqual(eList.get(i), aList.get(i));
        }
    }

    private void assertPokemonEqual(Pokemon expected, Pokemon actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCurrentHP(), actual.getCurrentHP());
        assertEquals(expected.getTypes(), actual.getTypes());

        assertBaseStatsEqual(expected.getBaseStats(), actual.getBaseStats());

        Move[] eMoves = expected.getMoves();
        Move[] aMoves = actual.getMoves();
        assertEquals(eMoves.length, aMoves.length);

        for (int i = 0; i < eMoves.length; i++) {
            if (eMoves[i] == null) {
                assertNull(aMoves[i]);
            } else {
                assertNotNull(aMoves[i]);
                assertEquals(eMoves[i].getMoveName(), aMoves[i].getMoveName());
                assertEquals(eMoves[i].getCurrentPp(), aMoves[i].getCurrentPp());
                assertEquals(eMoves[i].getMoveClass(), aMoves[i].getMoveClass());
            }
        }
    }

    private void assertBaseStatsEqual(BaseLevelStats expected, BaseLevelStats actual) {
        assertEquals(expected.getMaxHp(), actual.getMaxHp());
        assertEquals(expected.getAttack(), actual.getAttack());
        assertEquals(expected.getDefense(), actual.getDefense());
        assertEquals(expected.getSpecialAttack(), actual.getSpecialAttack());
        assertEquals(expected.getSpecialDefense(), actual.getSpecialDefense());
        assertEquals(expected.getSpeed(), actual.getSpeed());
    }
}