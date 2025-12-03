
package usecase.game_state_persistance;

import entity.*;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import usecase.game_state_persistence.SaveGameInteractor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(4, deserialized.player1Team().getTeam().get(0).getMoves().length);
        assertNull(deserialized.player1Team().getTeam().get(0).getMoves()[0]);
    }

    @Test
    void testToFromJson_ComplexState() {
        GameState original = createComplexGameState();

        JSONObject json = SaveGameInteractor.toJson(original);
        GameState deserialized = SaveGameInteractor.fromJson(json);

        assertGameStatesEqual(original, deserialized);
    }

    @Test
    void testToJsonPokemon_SkipsNullMoves_InMiddleOfArray() {
        Pokemon pokemon = createPokemon("Mewtwo", "Psychic");
        pokemon.setCurrentHP(80);
        pokemon.setMoves(new Move[]{
                new Move("Psychic", "Psychic", 10, "Powerful psychic blast", "special", 100, 8),
                null,
                new Move("Recover", "Normal", 5, "Heals HP", "status", 100, 5),
                null
        });

        PokemonTeam team = new PokemonTeam();
        team.addPokemon(pokemon);

        JSONObject jsonTeam = SaveGameInteractor.toJsonTeam(team);
        var movesArray = jsonTeam.getJSONArray("pokemons")
                .getJSONObject(0)
                .getJSONArray("moves");

        assertEquals(2, movesArray.length()); // Only non-null moves serialized
        assertEquals("Psychic", movesArray.getJSONObject(0).getString("moveName"));
        assertEquals("Recover", movesArray.getJSONObject(1).getString("moveName"));
    }

    @Test
    void testFromJsonPokemon_LessThanFourMoves_PadsWithNull() {
        // Simulate saved JSON with only 2 moves
        String jsonStr = """
            {
                "name": "Eevee",
                "currentHP": 55,
                "baseStats": {"maxHp": 55, "attack": 55, "defense": 50, "specialAttack": 45, "specialDefense": 65, "speed": 55},
                "types": ["Normal"],
                "moves": [
                    {"moveName": "Tackle", "moveType": "Normal", "maxPp": 35, "currentPp": 30, "moveDescription": "A physical attack", "moveAccuracy": 100, "moveClass": "physical"},
                    {"moveName": "Growl", "moveType": "Normal", "maxPp": 40, "currentPp": 38, "moveDescription": "Lowers attack", "moveAccuracy": 100, "moveClass": "status"}
                ]
            }
            """;

        Pokemon restored = SaveGameInteractor.fromJsonPokemon(new JSONObject(jsonStr));

        assertEquals("Eevee", restored.getName());
        assertEquals(55, restored.getCurrentHP());
        assertEquals(4, restored.getMoves().length);
        assertNotNull(restored.getMoves()[0]);
        assertNotNull(restored.getMoves()[1]);
        assertNull(restored.getMoves()[2]);
        assertNull(restored.getMoves()[3]);
        assertEquals("Tackle", restored.getMoves()[0].getMoveName());
        assertEquals("status", restored.getMoves()[1].getMoveClass());
    }

    @Test
    void testFromJsonMove_AllOptionalFieldsMissing_UsesSafeDefaults() {
        String minimalMoveJson = """
            {
                "moveName": "Splash",
                "moveType": "Normal",
                "maxPp": 40
            }
            """;

        Move move = SaveGameInteractor.fromJsonMove(new JSONObject(minimalMoveJson));

        assertEquals("Splash", move.getMoveName());
        assertEquals("Splash move", move.getMoveDescription());     // default
        assertEquals(100, move.getMoveAccuracy());                  // default
        assertEquals("physical", move.getMoveClass());              // default
        assertEquals(40, move.getCurrentPp());                      // defaults to maxPp
    }

    @Test
    void testFromJsonMove_SomeOptionalFieldsPresent_OthersDefaulted() {
        String partialMoveJson = """
            {
                "moveName": "Hyper Beam",
                "moveType": "Normal",
                "maxPp": 5,
                "moveDescription": "Powerful beam, needs recharge",
                "currentPp": 3
            }
            """;

        Move move = SaveGameInteractor.fromJsonMove(new JSONObject(partialMoveJson));

        assertEquals("Powerful beam, needs recharge", move.getMoveDescription());
        assertEquals(100, move.getMoveAccuracy());        // still default
        assertEquals("physical", move.getMoveClass());    // still default
        assertEquals(3, move.getCurrentPp());             // provided
    }

    // === Helper Methods (unchanged + complex state creator used) ===

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

        Pokemon pikachu = createPokemon("pikachu", "Fire", "Flying");
        pikachu.setCurrentHP(140);
        pikachu.setMoves(new Move[]{
                new Move("Lightning", "Fire", 15, "Strong fire attack", "special", 90, 12),
                new Move("Dragon Claw", "Dragon", 15, "Sharp claws", "physical", 100, 15),
                new Move("Dragon Claw2", "Dragon", 15, "Sharp claws", "physical", 100, 15),
                new Move("Dragon Claw3", "Dragon", 15, "Sharp claws", "physical", 100, 15)
        });
        team1.addPokemon(pikachu);


        Pokemon blastoise = createPokemon("Blastoise", "Water");
        blastoise.setCurrentHP(200);
        blastoise.setMoves(new Move[]{null, null, null, null});
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
                assertEquals(eMoves[i].getMoveDescription(), aMoves[i].getMoveDescription());
                assertEquals(eMoves[i].getMoveAccuracy(), aMoves[i].getMoveAccuracy());
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