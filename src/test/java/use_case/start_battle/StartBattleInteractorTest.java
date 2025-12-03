package use_case.start_battle;

import entity.BaseLevelStats;
import entity.Battle;
import entity.Pokemon;
import entity.PokemonTeam;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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

    /**
     * CRITICAL: Custom team where getActivePokemon() returns a DIFFERENT pokemon
     * than what's in the getTeam() list. This allows us to test Check 5's final return false.
     */
    private static class SplitTeam extends PokemonTeam {
        private final List<Pokemon> teamList = new ArrayList<>();
        private final Pokemon activePokemon;

        public SplitTeam(Pokemon activePokemon) {
            this.activePokemon = activePokemon;
        }

        public void addToList(Pokemon p) {
            teamList.add(p);
        }

        @Override
        public List<Pokemon> getTeam() {
            return teamList;
        }

        @Override
        public Pokemon getActivePokemon() {
            return activePokemon;
        }
    }

    // Helper inside test class
    private static class NoActiveTeam extends PokemonTeam {
        private final List<Pokemon> teamList = new ArrayList<>();

        void addToList(Pokemon p) {
            teamList.add(p);
        }

        @Override
        public List<Pokemon> getTeam() {
            return teamList;
        }

        @Override
        public Pokemon getActivePokemon() {
            return null; // forces Check 3 to fail
        }
    }



    private CapturingPresenter presenter;
    private StartBattleInteractor interactor;

    @Before
    public void setUp() {
        presenter = new CapturingPresenter();
        interactor = new StartBattleInteractor(presenter);
    }

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

    // === NULL TESTS ===
    @Test
    public void execute_bothTeamsNull_fail() {
        interactor.execute(new StartBattleInputData(null, null));
        assertEquals("Error: Both teams must be selected!", presenter.lastFail);
    }

    @Test
    public void execute_userTeamNull_fail() {
        interactor.execute(new StartBattleInputData(null, createValidTeam("Opp")));
        assertEquals("Error: Both teams must be selected!", presenter.lastFail);
    }

    @Test
    public void execute_opponentTeamNull_fail() {
        interactor.execute(new StartBattleInputData(createValidTeam("User"), null));
        assertEquals("Error: Both teams must be selected!", presenter.lastFail);
    }

    // === EMPTY TEAMS ===
    @Test
    public void execute_userTeamEmpty_fail() {
        interactor.execute(new StartBattleInputData(new PokemonTeam(), createValidTeam("Opp")));
        assertEquals("Error: Your team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
    }

    @Test
    public void execute_opponentTeamEmpty_fail() {
        interactor.execute(new StartBattleInputData(createValidTeam("User"), new PokemonTeam()));
        assertEquals("Error: Opponent team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
    }

    // === ACTIVE POKEMON HP = 0 ===
    @Test
    public void execute_userActiveZeroHP_fail() {
        PokemonTeam team = new PokemonTeam();
        team.addPokemon(createPokemon("Dead", 0));
        interactor.execute(new StartBattleInputData(team, createValidTeam("Opp")));
        assertEquals("Error: Your team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
    }

    @Test
    public void execute_opponentActiveZeroHP_fail() {
        PokemonTeam team = new PokemonTeam();
        team.addPokemon(createPokemon("Dead", 0));
        interactor.execute(new StartBattleInputData(createValidTeam("User"), team));
        assertEquals("Error: Opponent team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
    }

    // === ACTIVE POKEMON HP < 0 ===
    @Test
    public void execute_userActiveNegativeHP_fail() {
        PokemonTeam team = new PokemonTeam();
        Pokemon p = createPokemon("Neg", 100);
        p.setCurrentHP(-10);
        team.addPokemon(p);
        interactor.execute(new StartBattleInputData(team, createValidTeam("Opp")));
        assertEquals("Error: Your team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
    }

    @Test
    public void execute_opponentActiveNegativeHP_fail() {
        PokemonTeam team = new PokemonTeam();
        Pokemon p = createPokemon("Neg", 100);
        p.setCurrentHP(-5);
        team.addPokemon(p);
        interactor.execute(new StartBattleInputData(createValidTeam("User"), team));
        assertEquals("Error: Opponent team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
    }

    // === CRITICAL: Check 5 final return false (active alive, but list all dead) ===
    @Test
    public void execute_userActiveAliveButListAllDead_fail() {
        Pokemon aliveActive = createPokemon("Alive", 100);
        SplitTeam userTeam = new SplitTeam(aliveActive);
        userTeam.addToList(createPokemon("Dead1", 0));
        userTeam.addToList(createPokemon("Dead2", 0));

        interactor.execute(new StartBattleInputData(userTeam, createValidTeam("Opp")));
        assertEquals("Error: Your team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
    }

    @Test
    public void execute_opponentActiveAliveButListAllDead_fail() {
        Pokemon aliveActive = createPokemon("Alive", 100);
        SplitTeam oppTeam = new SplitTeam(aliveActive);
        oppTeam.addToList(createPokemon("Dead1", 0));
        oppTeam.addToList(createPokemon("Dead2", 0));

        interactor.execute(new StartBattleInputData(createValidTeam("User"), oppTeam));
        assertEquals("Error: Opponent team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
    }

    // === Check 5 loop TRUE branch (finds alive pokemon) ===
    @Test
    public void execute_userTeamWithAlivePokemon_success() {
        PokemonTeam team = new PokemonTeam();
        team.addPokemon(createPokemon("Alive", 50));
        team.addPokemon(createPokemon("Dead", 0));
        interactor.execute(new StartBattleInputData(team, createValidTeam("Opp")));
        assertNull(presenter.lastFail);
        assertNotNull(presenter.lastSuccess);
    }

    @Test
    public void execute_opponentTeamWithAlivePokemon_success() {
        PokemonTeam team = new PokemonTeam();
        team.addPokemon(createPokemon("Alive", 75));
        team.addPokemon(createPokemon("Dead", 0));
        interactor.execute(new StartBattleInputData(createValidTeam("User"), team));
        assertNull(presenter.lastFail);
        assertNotNull(presenter.lastSuccess);
    }

    // === SUCCESS TESTS ===
    @Test
    public void execute_bothValid_success() {
        PokemonTeam user = createValidTeam("Player");
        PokemonTeam opp = createValidTeam("Enemy");

        interactor.execute(new StartBattleInputData(user, opp));

        assertNull(presenter.lastFail);
        assertNotNull(presenter.lastSuccess);
        assertTrue(presenter.lastSuccess.isSuccess());
        assertNotNull(presenter.lastSuccess.getBattle());
        assertTrue(presenter.lastSuccess.getMessage().contains("Battle started!"));
        assertTrue(presenter.lastSuccess.getMessage().contains("Player1"));
        assertTrue(presenter.lastSuccess.getMessage().contains("Enemy1"));
    }

    @Test
    public void execute_multiPokemon_success() {
        PokemonTeam user = new PokemonTeam();
        user.addPokemon(createPokemon("P1", 80));
        user.addPokemon(createPokemon("P2", 90));

        PokemonTeam opp = new PokemonTeam();
        opp.addPokemon(createPokemon("O1", 75));

        interactor.execute(new StartBattleInputData(user, opp));
        assertNull(presenter.lastFail);
        assertNotNull(presenter.lastSuccess);
    }

    // === DATA CLASSES ===
    @Test
    public void inputData_getters() {
        PokemonTeam t1 = createValidTeam("A");
        PokemonTeam t2 = createValidTeam("B");
        StartBattleInputData data = new StartBattleInputData(t1, t2);
        assertSame(t1, data.getUserTeam());
        assertSame(t2, data.getOpponentTeam());
    }

    @Test
    public void outputData_success_getters() {
        PokemonTeam t1 = createValidTeam("A");
        PokemonTeam t2 = createValidTeam("B");
        Battle b = new Battle(t1, t2);
        StartBattleOutputData data = new StartBattleOutputData(b, true, "Msg");
        assertSame(b, data.getBattle());
        assertTrue(data.isSuccess());
        assertEquals("Msg", data.getMessage());
    }

    @Test
    public void outputData_fail_getters() {
        StartBattleOutputData data = new StartBattleOutputData(null, false, "Error");
        assertNull(data.getBattle());
        assertFalse(data.isSuccess());
        assertEquals("Error", data.getMessage());
    }

    @Test
    public void execute_userTeamNoActive_fail() {
        NoActiveTeam userTeam = new NoActiveTeam();
        userTeam.addToList(createPokemon("P1", 50)); // non-empty list, but no active

        interactor.execute(new StartBattleInputData(userTeam, createValidTeam("Opp")));

        assertEquals("Error: Your team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_opponentTeamNoActive_fail() {
        NoActiveTeam oppTeam = new NoActiveTeam();
        oppTeam.addToList(createPokemon("O1", 50)); // non-empty list, but no active

        interactor.execute(new StartBattleInputData(createValidTeam("User"), oppTeam));

        assertEquals("Error: Opponent team must have at least 1 Pokémon with HP > 0!", presenter.lastFail);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void isValidTeam_nullTeam_returnsFalse() {
        assertFalse(interactor.isValidTeamForTest(null));
    }

    @Test
    public void isValidTeam_nullTeam_usedForUserAndOpponent() {
        // This one is optional for lines; the first already hits the branch.
        assertFalse(interactor.isValidTeamForTest(null));
    }


}