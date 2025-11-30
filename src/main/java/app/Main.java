package app;

import entity.Battle;
import entity.GameState;
import entity.PokemonTeam;

// USE CASE IMPORTS - ALL 4 USE CASES
import use_case.select_team.*;
import use_case.start_battle.*;
import use_case.use_move.*;
import use_case.end_battle.*;
import use_case.switch_pokemon.*;

// INTERFACE ADAPTER IMPORTS - ALL 4 USE CASES
import interface_adapter.select_team.*;
import interface_adapter.start_battle.*;
import interface_adapter.use_move.*;
import interface_adapter.switch_pokemon.*;
import interface_adapter.end_battle.*;

// VIEW IMPORTS
import view.*;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
        System.out.println("\n=== STARTING BATTLE - WIRING 4 USE CASES ===");

        // ============================================================
        // CRITICAL DEBUG: Verify teams are different
        // ============================================================
        System.out.println("\n[DEBUG] TEAM VERIFICATION:");
        System.out.println("Team 1 size: " + team1.getTeam().size());
        System.out.println("Team 2 size: " + team2.getTeam().size());

        if (team1.getTeam().size() > 0) {
            System.out.println("Team 1 Active Pokemon: " + team1.getActivePokemon().getName());
            System.out.println("Team 1 Full Roster:");
            for (int i = 0; i < team1.getTeam().size(); i++) {
                System.out.println("  " + (i+1) + ". " + team1.getTeam().get(i).getName());
            }
        }

        if (team2.getTeam().size() > 0) {
            System.out.println("Team 2 Active Pokemon: " + team2.getActivePokemon().getName());
            System.out.println("Team 2 Full Roster:");
            for (int i = 0; i < team2.getTeam().size(); i++) {
                System.out.println("  " + (i+1) + ". " + team2.getTeam().get(i).getName());
            }
        }

        // Verify they're different objects
        System.out.println("Are teams the same object? " + (team1 == team2));
        System.out.println("Team1 hashCode: " + System.identityHashCode(team1));
        System.out.println("Team2 hashCode: " + System.identityHashCode(team2));
        System.out.println();

        // USE CASE 1: START BATTLE
        StartBattleViewModel startBattleViewModel = new StartBattleViewModel();
        StartBattleOutputBoundary startBattlePresenter = new StartBattlePresenter(startBattleViewModel);
        StartBattleInputBoundary startBattleInteractor = new StartBattleInteractor(startBattlePresenter);
        StartBattleController startBattleController = new StartBattleController(startBattleInteractor);

        startBattleController.startBattle(team1, team2);

        Battle battle = startBattleViewModel.getBattle();

        if (battle == null) {
            String errorMsg = startBattleViewModel.getMessage();
            JOptionPane.showMessageDialog(frame, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ============================================================
        // CRITICAL DEBUG: Verify battle has correct teams
        // ============================================================
        System.out.println("[DEBUG] BATTLE OBJECT VERIFICATION:");
        System.out.println("Battle Team1 Active: " + battle.getTeam1().getActivePokemon().getName());
        System.out.println("Battle Team2 Active: " + battle.getTeam2().getActivePokemon().getName());
        System.out.println("Battle Team1 Full Roster:");
        for (int i = 0; i < battle.getTeam1().getTeam().size(); i++) {
            System.out.println("  " + (i+1) + ". " + battle.getTeam1().getTeam().get(i).getName());
        }
        System.out.println("Battle Team2 Full Roster:");
        for (int i = 0; i < battle.getTeam2().getTeam().size(); i++) {
            System.out.println("  " + (i+1) + ". " + battle.getTeam2().getTeam().get(i).getName());
        }
        System.out.println("Are battle teams the same object? " + (battle.getTeam1() == battle.getTeam2()));
        System.out.println();

        // USE CASE 4: END BATTLE
        EndBattleViewModel endBattleViewModel = new EndBattleViewModel();
        EndBattleOutputBoundary endBattlePresenter = new EndBattlePresenter(endBattleViewModel);
        EndBattleInputBoundary endBattleInteractor = new EndBattleInteractor(endBattlePresenter);

        // USE CASE 2: USE MOVE
        UseMoveViewModel useMoveViewModel = new UseMoveViewModel();
        useMoveViewModel.setBattle(battle);

        UseMoveOutputBoundary useMovePresenter = new UseMovePresenter(useMoveViewModel);
        UseMoveInputBoundary useMoveInteractor = new UseMoveInteractor(
                useMovePresenter,
                endBattleInteractor
        );
        UseMoveController useMoveController = new UseMoveController(useMoveInteractor);

        // USE CASE 3: SWITCH POKEMON
        SwitchPokemonViewModel switchPokemonViewModel = new SwitchPokemonViewModel();
        SwitchPokemonOutputBoundary switchPokemonPresenter = new SwitchPokemonPresenter(switchPokemonViewModel);
        SwitchPokemonInputBoundary switchPokemonInteractor = new SwitchPokemonInteractor(switchPokemonPresenter);
        SwitchPokemonController switchPokemonController = new SwitchPokemonController(switchPokemonInteractor);

        // VIEW
        BattlePanel battlePanel = new BattlePanel(
                battle,
                useMoveController,
                switchPokemonController,
                useMoveViewModel,
                switchPokemonViewModel,
                endBattleViewModel
        );

        frame.setContentPane(battlePanel);
        frame.setSize(1000, 800);
        frame.revalidate();
        frame.repaint();

        System.out.println("=== ALL 4 USE CASES WIRED ===\n");
    }
}