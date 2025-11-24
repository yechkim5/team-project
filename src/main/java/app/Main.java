package app;

import entity.GameState;
import view.BattlePanel;  // Assuming view package
import view.TeamSelectionScreen;  // Assuming view package

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    private static SimpleMp3Player musicPlayer;

    public static void main(String[] args) {
        // Initialize game state (loads from save if exists)
        GameOrchestrator.init();
        GameState state = GameOrchestrator.getCurrent();

        // Set up music (adjust path to your MP3 file)
        musicPlayer = new SimpleMp3Player("music/battle_theme.mp3");  // Or wherever your MP3 is

        // Create main window
        JFrame frame = new JFrame("Pokemon Battle Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add window listener to stop music on close
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                musicPlayer.stop();
            }
        });

        // Resume based on loaded state
        if (state.currentScreen() == GameState.Screen.TEAM_SELECTION) {
            // Show team selection, inject current state (teams, active selector)
            TeamSelectionScreen teamScreen = new TeamSelectionScreen(state.activeTeamSelector(), state.player1Team(), state.player2Team());
            frame.add(teamScreen);
            // TODO: Wire buttons to call GameOrchestrator.updateState(newState) on changes
        } else if (state.currentScreen() == GameState.Screen.BATTLE) {
            // Recreate Battle from saved teams + phase
            entity.Battle battle = new entity.Battle(state.player1Team(), state.player2Team());
            battle.switchTurn();  // Set turn from phase
            if (state.battlePhase().currentTurn() == GameState.Turn.PLAYER2) {
                battle.switchTurn();  // Align to saved turn
            }
            state.player1Team().switchActivePokemon(state.battlePhase().player1ActiveIndex());
            state.player2Team().switchActivePokemon(state.battlePhase().player2ActiveIndex());

            BattlePanel battlePanel = new BattlePanel(battle);  // Assuming BattlePanel takes Battle
            frame.add(battlePanel);
            // TODO: Wire actions (moves, switches) to update HP/PP, then call GameOrchestrator.updateState(newState)
        } else if (state.currentScreen() == GameState.Screen.GAME_OVER) {
            // Show game over screen (add a view.GameOverPanel if needed)
            JLabel gameOverLabel = new JLabel("Game Over! High Score: " + state.highScore());
            frame.add(gameOverLabel);
        }

        frame.pack();
        frame.setVisible(true);

        // Play music (starts on battle or as needed)
        musicPlayer.play();
    }
}