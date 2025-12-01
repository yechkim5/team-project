package app;

import entity.*;
import factory.pokemonFactory;
import interface_adapter.battle.*;
import use_case.use_move.*;
import use_case.start_battle.*;
import view.BattlePanel;
import interface_adapter.end_battle.EndBattleController;
import use_case.end_battle.EndBattleInputBoundary;
import use_case.end_battle.EndBattleInputData;


import javax.swing.*;

/**
 * Demo app to test the battle system.
 * USES YOUR EXISTING UI (BattlePanel, AttackPanel, etc.)
 */
public class BattleDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create two teams
            PokemonTeam team1 = createDemoTeam1();
            PokemonTeam team2 = createDemoTeam2();

            // Start battle use case
            BattleViewModel battleViewModel = new BattleViewModel();
            StartBattleOutputBoundary startBattlePresenter = new StartBattlePresenter(battleViewModel);
            StartBattleInteractor startBattleInteractor = new StartBattleInteractor(startBattlePresenter);

            StartBattleInputData startInput = new StartBattleInputData(team1, team2);
            startBattleInteractor.execute(startInput);

            // Get the battle that was created
            Battle battle = battleViewModel.getBattle();

            if (battle == null) {
                JOptionPane.showMessageDialog(null, "Failed to start battle!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create use move components
            BattleViewModel useMoveViewModel = new BattleViewModel();
            useMoveViewModel.setBattle(battle);

            UseMoveOutputBoundary useMovePresenter = new interface_adapter.battle.BattlePresenter(useMoveViewModel);
            UseMoveInputBoundary useMoveInteractor = new UseMoveInteractor(useMovePresenter);
            BattleController battleController = new BattleController(useMoveInteractor);

            // Create and show battle view - USING YOUR EXISTING UI!
            JFrame frame = new JFrame("Pokemon Battle Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setLocationRelativeTo(null);

            // Dummy End Battle interactor for the demo app
            EndBattleInputBoundary demoEndBattleInteractor = new EndBattleInputBoundary() {
                @Override
                public void execute(EndBattleInputData inputData) {
                    // In the full app (Main), this triggers Rematch/New Game.
                    // In this demo, we just show a simple message.
                    JOptionPane.showMessageDialog(
                            frame,
                            "End-of-battle options (Rematch / New Game) are only wired in Main.\n" +
                                    "This BattleDemo is for testing moves only.",
                            "Battle End (Demo)",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            };

            EndBattleController demoEndBattleController = new EndBattleController(demoEndBattleInteractor);

// Use the 4-arg BattlePanel constructor
            BattlePanel battlePanel = new BattlePanel(battle, battleController, useMoveViewModel, demoEndBattleController);
            frame.setContentPane(battlePanel);

            frame.setContentPane(battlePanel);

            frame.setVisible(true);
        });
    }

    private static PokemonTeam createDemoTeam1() {
        PokemonTeam team = new PokemonTeam();

        try {
            // Add Pikachu
            pokemonFactory factory1 = new pokemonFactory("pikachu");
            Pokemon pikachu = factory1.getPokemon();
            team.addPokemon(pikachu);

            // Add Charizard
            pokemonFactory factory2 = new pokemonFactory("charizard");
            Pokemon charizard = factory2.getPokemon();
            team.addPokemon(charizard);

        } catch (Exception e) {
            System.err.println("Error creating team 1: " + e.getMessage());
            e.printStackTrace();
        }

        return team;
    }

    private static PokemonTeam createDemoTeam2() {
        PokemonTeam team = new PokemonTeam();

        try {
            // Add Blastoise
            pokemonFactory factory1 = new pokemonFactory("blastoise");
            Pokemon blastoise = factory1.getPokemon();
            team.addPokemon(blastoise);

            // Add Venusaur
            pokemonFactory factory2 = new pokemonFactory("venusaur");
            Pokemon venusaur = factory2.getPokemon();
            team.addPokemon(venusaur);

        } catch (Exception e) {
            System.err.println("Error creating team 2: " + e.getMessage());
            e.printStackTrace();
        }

        return team;
    }

    // Presenter for Start Battle
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
            JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}