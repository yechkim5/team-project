package view;

import interface_adapter.SaveProgressController;
import entity.Trainer;
import use_case.SaveProgress;

import javax.swing.*;
import java.awt.*;

public class BattlePanel extends JPanel {  // this is your Panel 3

    // injected via constructor
    private final SaveProgressController saveController;
    private final Trainer playerTrainer;
    private final BattleState battleState;  // your class that holds currentTowerLevel

    public BattlePanel(SaveProgressController saveController,
                       Trainer playerTrainer,
                       BattleState battleState) {
        this.saveController = saveController;
        this.playerTrainer = playerTrainer;
        this.battleState = battleState;

        // ... your existing layout code for enemy/player Pokémon ...

        // Bottom action bar
        JPanel bottomMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));

        JButton fightButton = new JButton("Fight");
        JButton teamButton  = new JButton("Team");
        JButton saveButton  = new JButton("Save");   // ← NEW: right of "Team"

        // your existing listeners for fightButton and teamButton ...

        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setPreferredSize(new Dimension(120, 50));
        saveButton.addActionListener(e -> {
            SaveProgress.Output result = saveController.save(playerTrainer, battleState.getCurrentTowerLevel());

            JOptionPane.showMessageDialog(BattlePanel.this,
                    result.message(),
                    "Save Game",
                    result.success() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        });

        bottomMenu.add(fightButton);
        bottomMenu.add(teamButton);
        bottomMenu.add(saveButton);  // ← placed immediately right of Team

        add(bottomMenu, BorderLayout.SOUTH);
    }
}