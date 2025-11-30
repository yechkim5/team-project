package app;

import entity.Battle;
import entity.GameState;
import entity.PokemonTeam;
import use_case.select_team.*;
import use_case.start_battle.*;
import use_case.use_move.*;
import interface_adapter.select_team.*;
import interface_adapter.battle.*;
import view.*;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//import music
import Music.MusicPlayer;
import Music.Mp3MusicPlayer;


public class Main {

    public static void main(String[] args) {
        // Load or initialize game state
        GameOrchestrator.forceNewGame();  // Start fresh
        GameState state = GameOrchestrator.getCurrent();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pokémon Battle Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            if (state.currentScreen() == GameState.Screen.TEAM_SELECTION) {
                frame.setSize(1200, 700);

                // Set up Clean Architecture components
                SelectTeamViewModel viewModel = new SelectTeamViewModel();
                SelectTeamOutputBoundary presenter = new SelectTeamPresenter(viewModel);
                SelectTeamInteractor interactor = new SelectTeamInteractor(presenter);

                // Restore saved teams (if any)
                interactor.restoreTeam(1, state.player1Team());
                interactor.restoreTeam(2, state.player2Team());

                SelectTeamController controller = new SelectTeamController(interactor);
                TeamSelectionScreen teamScreen = new TeamSelectionScreen(controller, viewModel);

                // Restore whose turn it is
                int currentPlayerNum = (state.activeTeamSelector() == GameState.Player.PLAYER1) ? 1 : 2;
                viewModel.setPlayerNumber(currentPlayerNum);
                interactor.getCurrentTeam(currentPlayerNum);

                // === CRITICAL FIX: Track battle start to prevent multiple triggers ===
                final boolean[] battleStarted = {false};

                viewModel.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        // Only check on team property changes
                        if (!evt.getPropertyName().equals(SelectTeamViewModel.TEAM_PROPERTY)) {
                            return;
                        }

                        // Prevent multiple battle starts
                        if (battleStarted[0]) {
                            return;
                        }

                        // Get teams from the INTERACTOR (source of truth)
                        PokemonTeam team1 = interactor.getTeam(1);
                        PokemonTeam team2 = interactor.getTeam(2);

                        // Debug logging - REMOVE AFTER TESTING
                        System.out.println("=== TEAM CHECK ===");
                        System.out.println("Team 1 size: " + team1.getTeam().size());
                        System.out.println("Team 2 size: " + team2.getTeam().size());

                        if (team1.getTeam().size() > 0) {
                            System.out.println("Team 1 first Pokemon: " + team1.getTeam().get(0).getName());
                        }
                        if (team2.getTeam().size() > 0) {
                            System.out.println("Team 2 first Pokemon: " + team2.getTeam().get(0).getName());
                        }

                        // Check if both teams are ready (6 Pokemon each)
                        if (team1.getTeam().size() == 6 && team2.getTeam().size() == 6) {
                            battleStarted[0] = true; // Set flag to prevent re-triggering

                            SwingUtilities.invokeLater(() -> {
                                int confirm = JOptionPane.showConfirmDialog(
                                        frame,
                                        "Both teams are ready! Start the battle?",
                                        "Ready for Battle",
                                        JOptionPane.YES_NO_OPTION
                                );

                                if (confirm == JOptionPane.YES_OPTION) {
                                    // start music
                                    MusicPlayer musicPlayer =
                                            new Mp3MusicPlayer("src/main/java/Music/BattleMusic.mp3"); // path to your mp3
                                    musicPlayer.playMusic();

                                    // Pass teams from interactor (source of truth)
                                    startBattle(frame, team1, team2);

                                } else {
                                    battleStarted[0] = false; // Reset if user declines
                                }
                            });
                        }
                    }
                });

                frame.setContentPane(teamScreen);
            }
            else if (state.currentScreen() == GameState.Screen.BATTLE && state.battlePhase() != null) {
                // Mid-battle resume
                frame.setSize(1000, 700);
                frame.setContentPane(new JLabel(
                        "<html><h1>Battle Resumed!</h1><p>Turn: " + state.battlePhase().currentTurn() +
                                "<br>Player 1 Active: " + state.player1Team().getActivePokemon().getName() +
                                "<br>Player 2 Active: " + state.player2Team().getActivePokemon().getName() + "</p></html>",
                        SwingConstants.CENTER
                ));
            }
            else if (state.currentScreen() == GameState.Screen.GAME_OVER) {
                // Game over screen
                frame.setSize(600, 400);
                JPanel panel = new JPanel();
                panel.setLayout(new java.awt.GridBagLayout());
                JLabel label = new JLabel("<html><h1>Game Over!</h1><p>High Score: " + state.highScore() + "</p></html>");
                label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 28));
                panel.add(label);
                frame.setContentPane(panel);
            }

            frame.setVisible(true);
        });
    }

    private static void startBattle(JFrame frame, PokemonTeam team1, PokemonTeam team2) {
        // === CRITICAL: Debug logging to verify teams are different ===
        System.out.println("\n=== STARTING BATTLE ===");
        System.out.println("Team 1 Pokemon:");
        for (int i = 0; i < team1.getTeam().size(); i++) {
            System.out.println("  " + (i+1) + ". " + team1.getTeam().get(i).getName());
        }

        System.out.println("Team 2 Pokemon:");
        for (int i = 0; i < team2.getTeam().size(); i++) {
            System.out.println("  " + (i+1) + ". " + team2.getTeam().get(i).getName());
        }

        // Verify teams are different objects
        System.out.println("Teams are same object? " + (team1 == team2));
        System.out.println("==================\n");

        // === USE CASE 1: START BATTLE ===
        BattleViewModel battleViewModel = new BattleViewModel();
        StartBattleOutputBoundary startPresenter = new StartBattlePresenter(battleViewModel);
        StartBattleInteractor startInteractor = new StartBattleInteractor(startPresenter);

        StartBattleInputData startInput = new StartBattleInputData(team1, team2);
        startInteractor.execute(startInput);

        Battle battle = battleViewModel.getBattle();

        if (battle == null) {
            JOptionPane.showMessageDialog(frame, "Failed to start battle!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // === USE CASE 2: USE MOVE ===
        BattleViewModel useMoveViewModel = new BattleViewModel();
        useMoveViewModel.setBattle(battle);

        UseMoveOutputBoundary useMovePresenter = new BattlePresenter(useMoveViewModel);
        UseMoveInputBoundary useMoveInteractor = new UseMoveInteractor(useMovePresenter);
        BattleController battleController = new BattleController(useMoveInteractor);

        // === CREATE BATTLE VIEW ===
        BattlePanel battlePanel = new BattlePanel(battle, battleController, useMoveViewModel);

        // Switch to battle screen
        frame.setContentPane(battlePanel);
        frame.setSize(1000, 800);
        frame.revalidate();
        frame.repaint();
    }

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
            viewModel.setMessage("Failed to start battle: " + error);
        }
    }
}