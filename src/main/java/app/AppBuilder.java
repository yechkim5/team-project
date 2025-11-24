// In your main GUI class (or wherever you set up the buttons):

// 1. Create the player ONCE (your single MP3 file)
//Mp3Player player = new SimpleMp3Player("C:/music/mySong.mp3");
// or "music/mySong.mp3" if it is in your project folder

// 2. When the Start button is clicked → play music
//startButton.addActionListener(e -> {
//player.play();
//});

// 3. When the Exit button is clicked → stop music and close program
//exitButton.addActionListener(e -> {
//player.stop();
//System.exit(0);   // if you really want to close the whole app
//});


package app;

import entity.Trainer;
import interface_adapter.controller.SaveProgressController;
import use_case.SaveProgress;
import use_case.SaveProgressDataAccessInterface;
import view.BattlePanel;
import view.TeamSelectionScreen;  // Updated to use your main team selection panel

import javax.swing.*;
import java.awt.*;

/**
 * Central application builder – wires all layers together using Clean Architecture.
 * This is the ONLY place where concrete classes are instantiated.
 */
public class AppBuilder {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppBuilder::buildAndShow);
    }

    private static void buildAndShow() {
        JFrame frame = new JFrame("Team Rocket – Pokémon Battle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        // ===================================================================
        // 1. DATA ACCESS LAYER (DAO)
        // ===================================================================
        // This saves to resources/progress.csv – same style as FileUserDataAccessObject
        SaveProgressDataAccessInterface progressDAO = new FileGameProgressDataAccessObject();

        // ===================================================================
        // 2. USE CASE LAYER
        // ===================================================================
        SaveProgress saveUseCase = new SaveProgress(progressDAO);

        // ===================================================================
        // 3. CONTROLLER LAYER (Interface Adapter)
        // ===================================================================
        SaveProgressController saveController = new SaveProgressController(saveUseCase);

        // ===================================================================
        // 4. CREATE GAME STATE
        // ===================================================================
        Trainer player1Trainer = new Trainer("Player1");  // Player 1's team
        Trainer player2Trainer = new Trainer("Player2");  // Player 2's team
        int startingTowerLevel = 1;
        BattleState battleState = new BattleState(startingTowerLevel);

        // ===================================================================
        // 5. START WITH POKÉMON LIST (Team Selection – Use Case 1)
        // ===================================================================
        startTeamSelection(frame, player1Trainer, player2Trainer, battleState, saveController);
    }

    /**
     * Handles team selection for both players in PvP mode.
     * Starts with Player 1, then switches to Player 2 when done.
     */
    private static void startTeamSelection(JFrame frame,
                                           Trainer player1Trainer,
                                           Trainer player2Trainer,
                                           BattleState battleState,
                                           SaveProgressController saveController) {

        // Create the team selection panel – it handles picking for one player at a time
        TeamSelectionScreen teamPanel = new TeamSelectionScreen();  // Your main team selection panel

        // PvP Switching Logic: Start with Player 1
        teamPanel.setPlayer(GameState.Player.PLAYER1);  // Switch title to Player 1
        frame.setContentPane(teamPanel);
        frame.revalidate();
        frame.repaint();

        // Add a "Done" button to finish one player's team and switch to the next
        JButton doneButton = new JButton("Done Selecting Team");
        doneButton.addActionListener(e -> {
            // Save the current player's team
            Trainer currentTrainer = teamPanel.getActivePlayer() == GameState.Player.PLAYER1 ? player1Trainer : player2Trainer;
            // Update currentTrainer with selected Pokémon – add logic here to get team from panel
            // e.g., currentTrainer.setTeam(teamPanel.getSelectedTeam());

            // Switch to next player or start battle
            if (teamPanel.getActivePlayer() == GameState.Player.PLAYER1) {
                // Switch to Player 2
                teamPanel.setPlayer(GameState.Player.PLAYER2);
                frame.revalidate();
            } else {
                // Both players done – start battle
                showBattleScreen(frame, player1Trainer, player2Trainer, battleState, saveController);
            }
        });

        // Add the Done button to the panel (adjust location based on your layout)
        teamPanel.add(doneButton, BorderLayout.SOUTH);
    }

    /**
     * Called when user clicks "Start Battle" – switches to Panel 3
     */
    private static void showBattleScreen(JFrame frame,
                                         Trainer player1Trainer,
                                         Trainer player2Trainer,
                                         BattleState battleState,
                                         SaveProgressController saveController) {

        // Create Panel 3 – this is where the battle happens (no more CPU – PvP only)
        BattlePanel battlePanel = new BattlePanel(
                player1Trainer,
                player2Trainer,  // ← Player 2 instead of CPU
                battleState,
                saveController   // ← INJECTED HERE
                // other controllers...
        );

        frame.setContentPane(battlePanel);
        frame.revalidate();
        frame.repaint();
    }
}
