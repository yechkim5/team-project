package view;

import entity.*;
import factory.pokemonFactory;
import interface_adapter.start_battle.*;
import interface_adapter.use_move.*;
import interface_adapter.switch_pokemon.*;
import interface_adapter.end_battle.*;
import use_case.start_battle.*;
import use_case.use_move.*;
import use_case.switch_pokemon.*;
import use_case.end_battle.*;
import use_case.select_team.SelectTeamInteractor;
import use_case.select_team.SelectTeamOutputBoundary;
import interface_adapter.select_team.SelectTeamController;
import interface_adapter.select_team.SelectTeamPresenter;
import interface_adapter.select_team.SelectTeamViewModel;

import javax.swing.*;

/**
 * Demo frame for testing different UI screens with Clean Architecture.
 * Updated to use 4 separate use cases: StartBattle, UseMove, SwitchPokemon, EndBattle
 */
public class DemoFrame extends JFrame {

    public enum Screen {
        TEAM_SELECT,
        DETAIL,
        BATTLE,
        ATTACK
    }

    public DemoFrame(Screen screen) {
        setTitle("Pokemon UI Demo - Clean Architecture");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screen == Screen.TEAM_SELECT ? 1200 : 900, screen == Screen.TEAM_SELECT ? 700 : 600);
        setLocationRelativeTo(null);

        switch (screen) {
            case TEAM_SELECT:
                // Wire up Clean Architecture dependencies for team selection
                SelectTeamViewModel viewModel = new SelectTeamViewModel();
                SelectTeamOutputBoundary presenter = new SelectTeamPresenter(viewModel);
                SelectTeamInteractor interactor = new SelectTeamInteractor(presenter);
                SelectTeamController controller = new SelectTeamController(interactor);
                setContentPane(new TeamSelectionScreen(controller, viewModel));
                break;

            case DETAIL:
                setContentPane(createDetailPanel());
                break;

            case BATTLE:
                // Create battle with real Pokemon and all 4 use cases
                setContentPane(createBattlePanel());
                setSize(1000, 800);
                break;

            case ATTACK:
                // Create attack panel demo (need battle context)
                setContentPane(createAttackPanelDemo());
                setSize(1000, 800);
                break;

            default:
                setContentPane(new JPanel());
        }
    }

    private JPanel createDetailPanel() {
        try {
            pokemonFactory factory = new pokemonFactory("pikachu");
            Pokemon pikachu = factory.getPokemon();
            return new PokemonDetailPanel(pikachu);
        } catch (Exception e) {
            e.printStackTrace();
            JPanel errorPanel = new JPanel();
            errorPanel.add(new JLabel("Error loading Pokemon: " + e.getMessage()));
            return errorPanel;
        }
    }

    private JPanel createBattlePanel() {
        try {
            System.out.println("\n=== DEMO FRAME - WIRING 4 USE CASES ===");

            // Create two demo teams with 6 Pokemon each
            PokemonTeam team1 = createDemoTeam1();
            PokemonTeam team2 = createDemoTeam2();

            // ============================================================
            // USE CASE 1: START BATTLE
            // ============================================================
            StartBattleViewModel startBattleViewModel = new StartBattleViewModel();
            StartBattleOutputBoundary startBattlePresenter = new StartBattlePresenter(startBattleViewModel);
            StartBattleInputBoundary startBattleInteractor = new StartBattleInteractor(startBattlePresenter);
            StartBattleController startBattleController = new StartBattleController(startBattleInteractor);

            // Execute: Create the battle
            startBattleController.startBattle(team1, team2);

            Battle battle = startBattleViewModel.getBattle();

            if (battle == null) {
                JPanel errorPanel = new JPanel();
                errorPanel.add(new JLabel("Failed to start battle: " + startBattleViewModel.getMessage()));
                return errorPanel;
            }

            // ============================================================
            // USE CASE 4: END BATTLE
            // ============================================================
            EndBattleViewModel endBattleViewModel = new EndBattleViewModel();
            EndBattleOutputBoundary endBattlePresenter = new EndBattlePresenter(endBattleViewModel);
            EndBattleInputBoundary endBattleInteractor = new EndBattleInteractor(endBattlePresenter);

            // ============================================================
            // USE CASE 2: USE MOVE
            // ============================================================
            UseMoveViewModel useMoveViewModel = new UseMoveViewModel();
            useMoveViewModel.setBattle(battle);

            UseMoveOutputBoundary useMovePresenter = new UseMovePresenter(useMoveViewModel);
            UseMoveInputBoundary useMoveInteractor = new UseMoveInteractor(
                    useMovePresenter,
                    endBattleInteractor  // Inject EndBattle dependency
            );
            UseMoveController useMoveController = new UseMoveController(useMoveInteractor);

            // ============================================================
            // USE CASE 3: SWITCH POKEMON
            // ============================================================
            SwitchPokemonViewModel switchPokemonViewModel = new SwitchPokemonViewModel();
            SwitchPokemonOutputBoundary switchPokemonPresenter = new SwitchPokemonPresenter(switchPokemonViewModel);
            SwitchPokemonInputBoundary switchPokemonInteractor = new SwitchPokemonInteractor(switchPokemonPresenter);
            SwitchPokemonController switchPokemonController = new SwitchPokemonController(switchPokemonInteractor);

            System.out.println("=== ALL 4 USE CASES WIRED ===\n");

            // Create BattlePanel with all controllers and view models
            return new BattlePanel(
                    battle,
                    useMoveController,
                    switchPokemonController,
                    useMoveViewModel,
                    switchPokemonViewModel,
                    endBattleViewModel
            );

        } catch (Exception e) {
            e.printStackTrace();
            JPanel errorPanel = new JPanel();
            errorPanel.add(new JLabel("Error creating battle: " + e.getMessage()));
            return errorPanel;
        }
    }

    private JPanel createAttackPanelDemo() {
        try {
            System.out.println("\n=== ATTACK PANEL DEMO - WIRING 4 USE CASES ===");

            // Create demo teams with 6 Pokemon each
            PokemonTeam team1 = createDemoTeam1();
            PokemonTeam team2 = createDemoTeam2();

            // ============================================================
            // USE CASE 1: START BATTLE
            // ============================================================
            StartBattleViewModel startBattleViewModel = new StartBattleViewModel();
            StartBattleOutputBoundary startBattlePresenter = new StartBattlePresenter(startBattleViewModel);
            StartBattleInputBoundary startBattleInteractor = new StartBattleInteractor(startBattlePresenter);
            StartBattleController startBattleController = new StartBattleController(startBattleInteractor);

            startBattleController.startBattle(team1, team2);
            Battle battle = startBattleViewModel.getBattle();

            if (battle == null) {
                JPanel errorPanel = new JPanel();
                errorPanel.add(new JLabel("Failed to create battle"));
                return errorPanel;
            }

            // ============================================================
            // USE CASE 4: END BATTLE
            // ============================================================
            EndBattleViewModel endBattleViewModel = new EndBattleViewModel();
            EndBattleOutputBoundary endBattlePresenter = new EndBattlePresenter(endBattleViewModel);
            EndBattleInputBoundary endBattleInteractor = new EndBattleInteractor(endBattlePresenter);

            // ============================================================
            // USE CASE 2: USE MOVE
            // ============================================================
            UseMoveViewModel useMoveViewModel = new UseMoveViewModel();
            useMoveViewModel.setBattle(battle);

            UseMoveOutputBoundary useMovePresenter = new UseMovePresenter(useMoveViewModel);
            UseMoveInputBoundary useMoveInteractor = new UseMoveInteractor(
                    useMovePresenter,
                    endBattleInteractor
            );
            UseMoveController useMoveController = new UseMoveController(useMoveInteractor);

            // ============================================================
            // USE CASE 3: SWITCH POKEMON
            // ============================================================
            SwitchPokemonViewModel switchPokemonViewModel = new SwitchPokemonViewModel();
            SwitchPokemonOutputBoundary switchPokemonPresenter = new SwitchPokemonPresenter(switchPokemonViewModel);
            SwitchPokemonInputBoundary switchPokemonInteractor = new SwitchPokemonInteractor(switchPokemonPresenter);
            SwitchPokemonController switchPokemonController = new SwitchPokemonController(switchPokemonInteractor);

            System.out.println("=== ALL 4 USE CASES WIRED ===\n");

            // Create just the AttackPanel for demo
            return new AttackPanel(battle, useMoveController, () -> {
                System.out.println("Back button clicked in demo");
            });

        } catch (Exception e) {
            e.printStackTrace();
            JPanel errorPanel = new JPanel();
            errorPanel.add(new JLabel("Error: " + e.getMessage()));
            return errorPanel;
        }
    }

    /**
     * Creates demo team 1 with 6 Pokemon
     */
    private PokemonTeam createDemoTeam1() throws Exception {
        PokemonTeam team = new PokemonTeam();

        // Add 6 Pokemon
        team.addPokemon(new pokemonFactory("pikachu").getPokemon());
        team.addPokemon(new pokemonFactory("charizard").getPokemon());
        team.addPokemon(new pokemonFactory("blastoise").getPokemon());
        team.addPokemon(new pokemonFactory("venusaur").getPokemon());
        team.addPokemon(new pokemonFactory("snorlax").getPokemon());
        team.addPokemon(new pokemonFactory("mewtwo").getPokemon());

        System.out.println("Demo Team 1 created: 6 Pokemon");
        return team;
    }

    /**
     * Creates demo team 2 with 6 Pokemon
     */
    private PokemonTeam createDemoTeam2() throws Exception {
        PokemonTeam team = new PokemonTeam();

        // Add 6 Pokemon
        team.addPokemon(new pokemonFactory("gengar").getPokemon());
        team.addPokemon(new pokemonFactory("dragonite").getPokemon());
        team.addPokemon(new pokemonFactory("gyarados").getPokemon());
        team.addPokemon(new pokemonFactory("alakazam").getPokemon());
        team.addPokemon(new pokemonFactory("machamp").getPokemon());
        team.addPokemon(new pokemonFactory("articuno").getPokemon());

        System.out.println("Demo Team 2 created: 6 Pokemon");
        return team;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Change this to test different screens:
            // Screen.TEAM_SELECT - Team selection UI
            // Screen.DETAIL - Pokemon detail panel
            // Screen.BATTLE - Full battle UI
            // Screen.ATTACK - Just attack panel

            DemoFrame frame = new DemoFrame(Screen.BATTLE);
            //DemoFrame frame = new DemoFrame(Screen.TEAM_SELECT);
            //DemoFrame frame = new DemoFrame(Screen.ATTACK);

            frame.setVisible(true);
        });
    }
}