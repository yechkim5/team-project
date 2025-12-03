package view;

import entity.*;
import factory.pokemonFactory;
import interface_adapter.battle.*;
import usecase.use_move.*;
import usecase.start_battle.*;
import usecase.select_team.SelectTeamInteractor;
import usecase.select_team.SelectTeamOutputBoundary;
import interface_adapter.select_team.SelectTeamController;
import interface_adapter.select_team.SelectTeamPresenter;
import interface_adapter.select_team.SelectTeamViewModel;

import javax.swing.*;

public class DemoFrame extends JFrame {

    public enum Screen {
        TEAM_SELECT,
        DETAIL,
        BATTLE,
        ATTACK
    }

    public DemoFrame(Screen screen) {
        setTitle("Pokemon UI Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screen == Screen.TEAM_SELECT ? 1200 : 900, screen == Screen.TEAM_SELECT ? 700 : 600);
        setLocationRelativeTo(null);

        switch (screen) {
            case TEAM_SELECT:
                // Wire up Clean Architecture dependencies
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
                // Create battle with real Pokemon
                setContentPane(createBattlePanel());
                setSize(1000, 800);
                break;

            case ATTACK:
                // Create attack panel demo (need battle context)
                setContentPane(createAttackPanelDemo());
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
            // Create two demo teams
            PokemonTeam team1 = new PokemonTeam();
            PokemonTeam team2 = new PokemonTeam();

            // Team 1
            pokemonFactory factory1 = new pokemonFactory("pikachu");
            team1.addPokemon(factory1.getPokemon());

            pokemonFactory factory2 = new pokemonFactory("charizard");
            team1.addPokemon(factory2.getPokemon());

            // Team 2
            pokemonFactory factory3 = new pokemonFactory("blastoise");
            team2.addPokemon(factory3.getPokemon());

            pokemonFactory factory4 = new pokemonFactory("venusaur");
            team2.addPokemon(factory4.getPokemon());

            // Start battle use case
            BattleViewModel battleViewModel = new BattleViewModel();
            StartBattleOutputBoundary startPresenter = new StartBattlePresenter(battleViewModel);
            StartBattleInteractor startInteractor = new StartBattleInteractor(startPresenter);

            StartBattleInputData startInput = new StartBattleInputData(team1, team2);
            startInteractor.execute(startInput);

            Battle battle = battleViewModel.getBattle();

            if (battle == null) {
                return new JPanel();
            }

            // Create use move components
            BattleViewModel useMoveViewModel = new BattleViewModel();
            useMoveViewModel.setBattle(battle);

            UseMoveOutputBoundary useMovePresenter = new interface_adapter.battle.BattlePresenter(useMoveViewModel);
            UseMoveInputBoundary useMoveInteractor = new UseMoveInteractor(useMovePresenter);
            BattleController battleController = new BattleController(useMoveInteractor);

            // Create BattlePanel with proper dependencies
            return new BattlePanel(battle, battleController, useMoveViewModel);

        } catch (Exception e) {
            e.printStackTrace();
            JPanel errorPanel = new JPanel();
            errorPanel.add(new JLabel("Error creating battle: " + e.getMessage()));
            return errorPanel;
        }
    }

    private JPanel createAttackPanelDemo() {
        try {
            // Create a simple battle for demo
            PokemonTeam team1 = new PokemonTeam();
            pokemonFactory factory = new pokemonFactory("pikachu");
            team1.addPokemon(factory.getPokemon());

            PokemonTeam team2 = new PokemonTeam();
            pokemonFactory factory2 = new pokemonFactory("charizard");
            team2.addPokemon(factory2.getPokemon());

            Battle battle = new Battle(team1, team2);

            // Create controller
            BattleViewModel viewModel = new BattleViewModel();
            viewModel.setBattle(battle);
            UseMoveOutputBoundary presenter = new interface_adapter.battle.BattlePresenter(viewModel);
            UseMoveInputBoundary interactor = new UseMoveInteractor(presenter);
            BattleController controller = new BattleController(interactor);

            return new AttackPanel(battle, controller, () -> {});

        } catch (Exception e) {
            e.printStackTrace();
            JPanel errorPanel = new JPanel();
            errorPanel.add(new JLabel("Error: " + e.getMessage()));
            return errorPanel;
        }
    }

    // Presenter for Start Battle (inner class)
    private static class StartBattlePresenter implements StartBattleOutputBoundary {
        private final BattleViewModel viewModel;

        public StartBattlePresenter(BattleViewModel viewModel) {
            this.viewModel = viewModel;
        }

        @Override
        public void prepareSuccessView(StartBattleOutputData outputData) {
            viewModel.setBattle(outputData.getBattle());
            viewModel.setMessage(outputData.getMessage());
        }

        @Override
        public void prepareFailView(String error) {
            System.err.println("Battle start failed: " + error);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Change this to test different screens
            DemoFrame frame = new DemoFrame(Screen.BATTLE);
            //DemoFrame frame = new DemoFrame(Screen.TEAM_SELECT);
            frame.setVisible(true);
        });
    }
}