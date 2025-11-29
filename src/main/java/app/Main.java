package app;

import entity.GameState;
import entity.Battle;
import entity.PokemonTeam;
import use_case.select_team.*;
import use_case.start_battle.*;
import use_case.use_move.*;
import interface_adapter.select_team.*;
import interface_adapter.start_battle.*;
import view.*;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Main entry point for Pokemon Battle Game
 *
 * FLOW:
 * 1. Load saved game (or start new if no save exists)
 * 2. Team Selection for Player 1 and Player 2
 * 3. Battle between the two teams
 * 4. Auto-saves progress after each action
 */
public class Main {

    public static void main(String[] args) {
        // Initialize game state - loads from save if exists
        GameOrchestrator.init();
        GameState state = GameOrchestrator.getCurrent();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pokémon Battle Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            // Check what screen we should show based on saved state
            if (state.currentScreen() == GameState.Screen.BATTLE && state.battlePhase() != null) {
                // Resume mid-battle (not implemented yet, show message)
                showBattleResume(frame, state);

            } else if (state.currentScreen() == GameState.Screen.GAME_OVER) {
                // Show game over screen
                showGameOver(frame, state);

            } else {
                // Team selection (default flow)
                showTeamSelection(frame, state);
            }

            frame.setVisible(true);
        });
    }

    /**
     * Show team selection screen with proper save/load integration
     */
    private static void showTeamSelection(JFrame frame, GameState state) {
        frame.setSize(1200, 700);

        // Set up Clean Architecture components
        SelectTeamViewModel viewModel = new SelectTeamViewModel();
        SelectTeamOutputBoundary presenter = new SelectTeamPresenter(viewModel);
        SelectTeamInteractor interactor = new SelectTeamInteractor(presenter);
        SelectTeamController controller = new SelectTeamController(interactor);

        // Restore saved teams from GameState
        interactor.restoreTeam(1, state.player1Team());
        interactor.restoreTeam(2, state.player2Team());

        // Create the team selection screen
        TeamSelectionScreen teamScreen = new TeamSelectionScreen(controller, viewModel);

        // Determine which player's turn it is
        int currentPlayerNum = (state.activeTeamSelector() == GameState.Player.PLAYER1) ? 1 : 2;
        viewModel.setPlayerNumber(currentPlayerNum);
        interactor.getCurrentTeam(currentPlayerNum);

        // Listen for both teams to be finalized
        viewModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // Both teams ready - 6 Pokemon each
                if (state.player1Team() != null && state.player1Team().getTeam().size() == 6 &&
                        state.player2Team() != null && state.player2Team().getTeam().size() == 6) {

                    int response = JOptionPane.showConfirmDialog(
                            frame,
                            "Both teams are ready! Start battle?",
                            "Ready to Battle",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (response == JOptionPane.YES_OPTION) {
                        SwingUtilities.invokeLater(() -> {
                            startBattle(frame, state.player1Team(), state.player2Team());
                        });
                    }
                }
            }
        });

        frame.setContentPane(teamScreen);
        frame.pack();
    }

    /**
     * Transition from team selection to battle
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
            JOptionPane.showMessageDialog(
                    frame,
                    "Failed to start battle!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Use move use case - handles battle actions
        BattleViewModel useMoveViewModel = new BattleViewModel();
        useMoveViewModel.setBattle(battle);

        StartBattlePresenter useMovePresenter = new StartBattlePresenter(useMoveViewModel);
        UseMoveInteractor useMoveInteractor = new UseMoveInteractor(useMovePresenter);
        BattleController battleController = new BattleController(useMoveInteractor);

        // Update game state to BATTLE screen
        GameOrchestrator.updateState(new GameState(
                GameState.Screen.BATTLE,
                GameState.Player.PLAYER1,
                team1,
                team2,
                new GameState.BattlePhase(GameState.Turn.PLAYER1, 0, 0),
                1,
                0
        ));

        // Switch to battle view
        BattlePanel battlePanel = new BattlePanel(battle, battleController, useMoveViewModel);
        frame.setContentPane(battlePanel);
        frame.setSize(1000, 800);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Show battle resume screen (placeholder for now)
     */
    private static void showBattleResume(JFrame frame, GameState state) {
        frame.setSize(600, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Battle in Progress");
        title.setFont(title.getFont().deriveFont(java.awt.Font.BOLD, 24f));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel info = new JLabel("<html><center>Battle resume not fully implemented yet.<br>" +
                "Player 1 Active: " + state.player1Team().getActivePokemon().getName() + "<br>" +
                "Player 2 Active: " + state.player2Team().getActivePokemon().getName() +
                "</center></html>");
        info.setFont(info.getFont().deriveFont(16f));
        info.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton newGameButton = new JButton("Start New Game");
        newGameButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        newGameButton.addActionListener(e -> {
            GameOrchestrator.forceNewGame();
            frame.dispose();
            main(new String[0]); // Restart
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(info);
        panel.add(Box.createVerticalStrut(30));
        panel.add(newGameButton);

        frame.setContentPane(panel);
    }

    /**
     * Show game over screen
     */
    private static void showGameOver(JFrame frame, GameState state) {
        frame.setSize(600, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Game Over!");
        title.setFont(title.getFont().deriveFont(java.awt.Font.BOLD, 32f));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel score = new JLabel("High Score: " + state.highScore());
        score.setFont(score.getFont().deriveFont(20f));
        score.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton newGameButton = new JButton("New Game");
        newGameButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        newGameButton.setFont(newGameButton.getFont().deriveFont(16f));
        newGameButton.addActionListener(e -> {
            GameOrchestrator.forceNewGame();
            frame.dispose();
            main(new String[0]); // Restart
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(score);
        panel.add(Box.createVerticalStrut(40));
        panel.add(newGameButton);

        frame.setContentPane(panel);
    }
}