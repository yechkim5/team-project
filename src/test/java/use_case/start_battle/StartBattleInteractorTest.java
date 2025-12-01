package use_case.start_battle;

import entity.BaseLevelStats;
import entity.Battle;
import entity.Pokemon;
import entity.PokemonTeam;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Comprehensive test suite for StartBattleInteractor
 * Provides 100% line and branch coverage
 */
public class StartBattleInteractorTest {

    private static class CapturingPresenter implements StartBattleOutputBoundary {
        StartBattleOutputData lastSuccess;
        String lastFail;

        @Override
        public void prepareSuccessView(StartBattleOutputData outputData) {
            lastSuccess = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            lastFail = error;
        }

        void reset() {
            lastSuccess = null;
            lastFail = null;
        }
    }

    private CapturingPresenter presenter;
    private StartBattleInteractor interactor;

    @Before
    public void setUp() {
        presenter = new CapturingPresenter();
        interactor = new StartBattleInteractor(presenter);
    }

    // === HELPER METHODS ===

    private BaseLevelStats sampleStats() {
        return new BaseLevelStats.BaseLevelStatsBuilder()
                .maxHp(100).attack(50).defense(40)
                .specialAttack(60).specialDefense(50).speed(70)
                .build();
    }

    private Pokemon createPokemon(String name, int currentHP) {
        Pokemon p = new Pokemon(name, sampleStats(), List.of("normal"));
        p.setCurrentHP(currentHP);
        return p;
    }

    private PokemonTeam createValidTeam(String prefix) {
        PokemonTeam team = new PokemonTeam();
        team.addPokemon(createPokemon(prefix + "1", 100));
        return team;
    }

    // === NULL VALIDATION TESTS ===

    @Test
    public void execute_nullUserTeam_producesFail() {
        presenter.reset();
        PokemonTeam opponentTeam = createValidTeam("Opponent");

        interactor.execute(new StartBattleInputData(null, opponentTeam));

        assertEquals("Error: Both teams must be selected!", presenter.lastFail);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_nullOpponentTeam_producesFail() {
        presenter.reset();
        PokemonTeam userTeam = createValidTeam("User");

        interactor.execute(new StartBattleInputData(userTeam, null));

        assertEquals("Error: Both teams must be selected!", presenter.lastFail);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_bothTeamsNull_producesFail() {
        presenter.reset();

        interactor.execute(new StartBattleInputData(null, null));

        assertEquals("Error: Both teams must be selected!", presenter.lastFail);
        assertNull(presenter.lastSuccess);
    }

    // === EMPTY TEAM VALIDATION TESTS ===

    @Test
    public void execute_emptyUserTeam_producesFail() {
        presenter.reset();
        PokemonTeam userTeam = new PokemonTeam();
        PokemonTeam opponentTeam = createValidTeam("Opponent");

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertEquals("Error: Your team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_emptyOpponentTeam_producesFail() {
        presenter.reset();
        PokemonTeam userTeam = createValidTeam("User");
        PokemonTeam opponentTeam = new PokemonTeam();

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertEquals("Error: Opponent team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
        assertNull(presenter.lastSuccess);
    }

    // === HP VALIDATION TESTS ===

    @Test
    public void execute_userTeamActivePokemonFainted_producesFail() {
        presenter.reset();
        PokemonTeam userTeam = new PokemonTeam();
        userTeam.addPokemon(createPokemon("Fainted", 0)); // Active pokemon has 0 HP

        PokemonTeam opponentTeam = createValidTeam("Opponent");

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertEquals("Error: Your team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_opponentTeamActivePokemonFainted_producesFail() {
        presenter.reset();
        PokemonTeam userTeam = createValidTeam("User");

        PokemonTeam opponentTeam = new PokemonTeam();
        opponentTeam.addPokemon(createPokemon("Fainted", 0));

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertEquals("Error: Opponent team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_userTeamAllPokemonFainted_producesFail() {
        presenter.reset();
        PokemonTeam userTeam = new PokemonTeam();
        userTeam.addPokemon(createPokemon("Fainted1", 0));
        userTeam.addPokemon(createPokemon("Fainted2", 0));
        userTeam.addPokemon(createPokemon("Fainted3", 0));

        PokemonTeam opponentTeam = createValidTeam("Opponent");

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertEquals("Error: Your team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_opponentTeamAllPokemonFainted_producesFail() {
        presenter.reset();
        PokemonTeam userTeam = createValidTeam("User");

        PokemonTeam opponentTeam = new PokemonTeam();
        opponentTeam.addPokemon(createPokemon("Fainted1", 0));
        opponentTeam.addPokemon(createPokemon("Fainted2", 0));

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertEquals("Error: Opponent team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_userTeamHasOneAlivePokemonButNotActive_producesFail() {
        presenter.reset();
        PokemonTeam userTeam = new PokemonTeam();
        userTeam.addPokemon(createPokemon("Fainted", 0)); // Active is fainted
        userTeam.addPokemon(createPokemon("Alive", 50));   // But has one alive

        PokemonTeam opponentTeam = createValidTeam("Opponent");

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        // Should fail because active pokemon has 0 HP
        assertEquals("Error: Your team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
    }

    // === SUCCESS TESTS ===

    @Test
    public void execute_validTeams_createsSuccessfulBattle() {
        presenter.reset();
        PokemonTeam userTeam = createValidTeam("Player");
        PokemonTeam opponentTeam = createValidTeam("Enemy");

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertNull(presenter.lastFail);
        assertNotNull(presenter.lastSuccess);
        assertTrue(presenter.lastSuccess.isSuccess());
        assertNotNull(presenter.lastSuccess.getBattle());
        assertTrue(presenter.lastSuccess.getMessage().contains("Battle started!"));
        assertTrue(presenter.lastSuccess.getMessage().contains("Player1"));
        assertTrue(presenter.lastSuccess.getMessage().contains("Enemy1"));
    }

    @Test
    public void execute_multiPokemonTeams_succeeds() {
        presenter.reset();
        PokemonTeam userTeam = new PokemonTeam();
        userTeam.addPokemon(createPokemon("Pikachu", 80));
        userTeam.addPokemon(createPokemon("Charizard", 90));
        userTeam.addPokemon(createPokemon("Blastoise", 85));

        PokemonTeam opponentTeam = new PokemonTeam();
        opponentTeam.addPokemon(createPokemon("Gengar", 75));
        opponentTeam.addPokemon(createPokemon("Lucario", 95));

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertNull(presenter.lastFail);
        assertNotNull(presenter.lastSuccess);
        assertNotNull(presenter.lastSuccess.getBattle());

        Battle battle = presenter.lastSuccess.getBattle();
        assertEquals(userTeam, battle.getTeam1());
        assertEquals(opponentTeam, battle.getTeam2());
    }

    @Test
    public void execute_teamWithMixedHPPokemon_succeeds() {
        presenter.reset();
        PokemonTeam userTeam = new PokemonTeam();
        userTeam.addPokemon(createPokemon("Healthy", 100)); // Active
        userTeam.addPokemon(createPokemon("Wounded", 25));
        userTeam.addPokemon(createPokemon("Critical", 5));

        PokemonTeam opponentTeam = createValidTeam("Opponent");

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertNull(presenter.lastFail);
        assertNotNull(presenter.lastSuccess);
        assertTrue(presenter.lastSuccess.isSuccess());
    }

    @Test
    public void execute_fullTeamOf6Pokemon_succeeds() {
        presenter.reset();
        PokemonTeam userTeam = new PokemonTeam();
        for (int i = 1; i <= 6; i++) {
            userTeam.addPokemon(createPokemon("User" + i, 100));
        }

        PokemonTeam opponentTeam = new PokemonTeam();
        for (int i = 1; i <= 6; i++) {
            opponentTeam.addPokemon(createPokemon("Opp" + i, 100));
        }

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertNull(presenter.lastFail);
        assertNotNull(presenter.lastSuccess);
        assertTrue(presenter.lastSuccess.isSuccess());
    }

    // === INPUT DATA TESTS ===

    @Test
    public void inputData_constructor_storesTeamsCorrectly() {
        PokemonTeam team1 = createValidTeam("Team1");
        PokemonTeam team2 = createValidTeam("Team2");

        StartBattleInputData inputData = new StartBattleInputData(team1, team2);

        assertEquals(team1, inputData.getUserTeam());
        assertEquals(team2, inputData.getOpponentTeam());
    }

    @Test
    public void inputData_nullTeams_storesNull() {
        StartBattleInputData inputData = new StartBattleInputData(null, null);

        assertNull(inputData.getUserTeam());
        assertNull(inputData.getOpponentTeam());
    }

    @Test
    public void inputData_getters_returnCorrectValues() {
        PokemonTeam userTeam = createValidTeam("User");
        PokemonTeam opponentTeam = createValidTeam("Opponent");

        StartBattleInputData inputData = new StartBattleInputData(userTeam, opponentTeam);

        assertNotNull(inputData.getUserTeam());
        assertNotNull(inputData.getOpponentTeam());
        assertSame(userTeam, inputData.getUserTeam());
        assertSame(opponentTeam, inputData.getOpponentTeam());
    }

    // === OUTPUT DATA TESTS ===

    @Test
    public void outputData_successCase_storesAllFieldsCorrectly() {
        PokemonTeam team1 = createValidTeam("T1");
        PokemonTeam team2 = createValidTeam("T2");
        Battle battle = new Battle(team1, team2);
        String message = "Battle started!";

        StartBattleOutputData outputData = new StartBattleOutputData(battle, true, message);

        assertEquals(battle, outputData.getBattle());
        assertTrue(outputData.isSuccess());
        assertEquals(message, outputData.getMessage());
    }

    @Test
    public void outputData_failCase_storesNullBattle() {
        StartBattleOutputData outputData = new StartBattleOutputData(null, false, "Error!");

        assertNull(outputData.getBattle());
        assertFalse(outputData.isSuccess());
        assertEquals("Error!", outputData.getMessage());
    }

    @Test
    public void outputData_getters_returnCorrectValues() {
        PokemonTeam team1 = createValidTeam("A");
        PokemonTeam team2 = createValidTeam("B");
        Battle battle = new Battle(team1, team2);

        StartBattleOutputData outputData = new StartBattleOutputData(battle, true, "Success!");

        assertNotNull(outputData.getBattle());
        assertTrue(outputData.isSuccess());
        assertEquals("Success!", outputData.getMessage());
        assertSame(battle, outputData.getBattle());
    }

    // === EDGE CASES ===

    @Test
    public void execute_teamWithNegativeHP_treatsSameAsZeroHP() {
        presenter.reset();
        PokemonTeam userTeam = new PokemonTeam();
        Pokemon negativePokemon = createPokemon("Negative", 100);
        negativePokemon.setCurrentHP(-10); // Set to negative
        userTeam.addPokemon(negativePokemon);

        PokemonTeam opponentTeam = createValidTeam("Opponent");

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertEquals("Error: Your team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
    }

    @Test
    public void execute_messageContainsBothPokemonNames() {
        presenter.reset();
        PokemonTeam userTeam = new PokemonTeam();
        userTeam.addPokemon(createPokemon("Pikachu", 100));

        PokemonTeam opponentTeam = new PokemonTeam();
        opponentTeam.addPokemon(createPokemon("Charizard", 100));

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertNotNull(presenter.lastSuccess);
        String message = presenter.lastSuccess.getMessage();
        assertTrue(message.contains("Pikachu"));
        assertTrue(message.contains("Charizard"));
        assertTrue(message.contains("vs"));
        assertTrue(message.contains("Good luck!"));
    }

    @Test
    public void execute_battleObjectHasCorrectReferences() {
        presenter.reset();
        PokemonTeam userTeam = createValidTeam("User");
        PokemonTeam opponentTeam = createValidTeam("Opponent");

        interactor.execute(new StartBattleInputData(userTeam, opponentTeam));

        assertNotNull(presenter.lastSuccess);
        Battle battle = presenter.lastSuccess.getBattle();
        assertNotNull(battle);
        assertSame(userTeam, battle.getTeam1());
        assertSame(opponentTeam, battle.getTeam2());
        assertTrue(battle.isBattleOngoing());
        assertTrue(battle.isTeam1Turn());
    }

    @Test
    public void execute_battleObjectIsProperlyInitialized() {
        presenter.reset();
        PokemonTeam team1 = new PokemonTeam();
        team1.addPokemon(createPokemon("P1", 100));
        team1.addPokemon(createPokemon("P2", 80));

        PokemonTeam team2 = new PokemonTeam();
        team2.addPokemon(createPokemon("O1", 90));

        interactor.execute(new StartBattleInputData(team1, team2));

        Battle battle = presenter.lastSuccess.getBattle();
        assertNotNull(battle.getBattleStatsMap());
        assertFalse(battle.getBattleStatsMap().isEmpty());
        assertNull(battle.getWinner()); // ✅ FIXED: Should be null initially - battle hasn't ended yet
    }
}
