package app;

import entity.GameState;
import entity.Battle;
import entity.PokemonTeam;
import use_case.select_team.*;
import use_case.start_battle.*;
import use_case.use_move.*;
import interface_adapter.select_team.*;
import interface_adapter.start_battle.*;
import interface_adapter.start_battle.StartBattlePresenter;
import view.*;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Main {

    public static void main(String[] args) {
        // Load or initialize game state (this handles save/load automatically)
        //GameOrchestrator.init();
        GameOrchestrator.forceNewGame();
        GameState state = GameOrchestrator.getCurrent();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pokémon Battle Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            // === RESUME FROM SAVED STATE ===
            if (state.currentScreen() == GameState.Screen.BATTLE && state.battlePhase() != null) {
                // Mid-battle resume (you can replace with real BattlePanel later)
                frame.setSize(1000, 700);
                frame.setContentPane(new JLabel(
                        "<html><h1>Battle Resumed!</h1><p>Turn: " + state.battlePhase().currentTurn() +
                                "<br>Player 1 Active: " + state.player1Team().getActivePokemon().getName() +
                                "<br>Player 2 Active: " + state.player2Team().getActivePokemon().getName() + "</p></html>",
                        SwingConstants.CENTER
                ));

            } else if (state.currentScreen() == GameState.Screen.GAME_OVER) {
                // Game over screen
                frame.setSize(600, 400);
                JPanel panel = new JPanel();
                panel.setLayout(new java.awt.GridBagLayout());
                JLabel label = new JLabel("<html><h1>Game Over!</h1><p>High Score: " + state.highScore() + "</p></html>");
                label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 28));
                panel.add(label);
                frame.setContentPane(panel);

            } else {
                // === TEAM SELECTION (with full save/resume support) ===
                frame.setSize(1200, 700);

                SelectTeamViewModel viewModel = new SelectTeamViewModel();
                SelectTeamOutputBoundary presenter = new SelectTeamPresenter(viewModel);
                SelectTeamInteractor interactor = new SelectTeamInteractor(presenter);

                // RESTORE SAVED TEAMS — this is the magic line
                interactor.restoreTeam(1, state.player1Team());
                interactor.restoreTeam(2, state.player2Team());

                SelectTeamController controller = new SelectTeamController(interactor);
                TeamSelectionScreen teamScreen = new TeamSelectionScreen(controller, viewModel);

                // Restore whose turn it is
                int currentPlayerNum = (state.activeTeamSelector() == GameState.Player.PLAYER1) ? 1 : 2;
                viewModel.setPlayerNumber(currentPlayerNum);

                // Force UI to show current player's team
                interactor.getCurrentTeam(currentPlayerNum);

                // === LISTEN FOR BOTH TEAMS FINALIZED ===
                viewModel.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        // Check if both teams are finalized
                        if (state.player1Team() != null && state.player1Team().getTeam().size() == 6 &&
                                state.player2Team() != null && state.player2Team().getTeam().size() == 6) {

                            // Both teams ready - START BATTLE!
                            SwingUtilities.invokeLater(() -> {
                                startBattle(frame, state.player1Team(), state.player2Team());
                            });
                        }
                    }
                });

                frame.setContentPane(teamScreen);
            }

            frame.pack();
            frame.setVisible(true);
        });
    }

    /**
     * Transitions from team selection to battle.
     * Wires up the Start Battle and Use Move use cases.
     */
    private static void startBattle(JFrame frame, PokemonTeam team1, PokemonTeam team2) {
        // Start battle use case - creates the Battle entity
        BattleViewModel battleViewModel = new BattleViewModel();
        StartBattlePresenter startPresenter = new StartBattlePresenter(battleViewModel);
        StartBattleInteractor startInteractor = new StartBattleInteractor(startPresenter);

        StartBattleInputData startInput = new StartBattleInputData(team1, team2);
        startInteractor.execute(startInput);

        Battle battle = battleViewModel.getBattle();

        if (battle == null) {
            JOptionPane.showMessageDialog(frame, "Failed to start battle!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Use move use case - handles battle actions
        BattleViewModel useMoveViewModel = new BattleViewModel();
        useMoveViewModel.setBattle(battle);

        StartBattlePresenter useMovePresenter = new StartBattlePresenter(useMoveViewModel);
        UseMoveInteractor useMoveInteractor = new UseMoveInteractor(useMovePresenter);
        BattleController battleController = new BattleController(useMoveInteractor);

        // Switch to battle view
        BattlePanel battlePanel = new BattlePanel(battle, battleController, useMoveViewModel);
        frame.setContentPane(battlePanel);
        frame.setSize(1000, 800);
        frame.revalidate();
        frame.repaint();
    }
}