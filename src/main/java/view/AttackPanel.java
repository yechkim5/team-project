package view;

import entity.Battle;
import entity.Move;
import entity.Pokemon;
import interface_adapter.start_battle.BattleController;

import javax.swing.*;
import java.awt.*;

/**
 * Move selection screen (Fight -> Moves).
 * NOW ACTUALLY EXECUTES MOVES!
 *
 * Layout:
 *  - Top: "Choose a move for X"
 *  - Center: 2x2 grid of big move buttons
 *  - Bottom: Back button
 */
public class AttackPanel extends JPanel {

    private final Battle battle;
    private final BattleController controller;
    private final Runnable onBack;

    public AttackPanel(Battle battle, BattleController controller, Runnable onBack) {
        this.battle = battle;
        this.controller = controller;
        this.onBack = onBack;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Pokemon attacker = battle.getCurrentTurnPokemon();

        // Title at top
        JLabel title = new JLabel("Choose a move for " + attacker.getName(), SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);

        // Center: 2x2 grid of big buttons
        JPanel grid = new JPanel(new GridLayout(2, 2, 10, 10));
        add(grid, BorderLayout.CENTER);

        Move[] moves = attacker.getMoves();
        int maxButtons = Math.min(4, moves.length);

        for (int i = 0; i < maxButtons; i++) {
            if (moves[i] != null) {
                Move move = moves[i];
                String label = move.getMoveName() +
                        "\n(PP " + move.getCurrentPp() + "/" + move.getMaxPp() + ")";
                JButton moveButton = new JButton("<html><center>" +
                        move.getMoveName() + "<br>PP: " +
                        move.getCurrentPp() + "/" + move.getMaxPp() +
                        "</center></html>");

                // Make buttons bigger
                moveButton.setFont(moveButton.getFont().deriveFont(Font.BOLD, 14f));
                moveButton.setPreferredSize(new Dimension(200, 80));

                // Enable only if PP > 0
                moveButton.setEnabled(move.getCurrentPp() > 0);

                // ACTUALLY EXECUTE THE MOVE!
                final int moveIndex = i;
                moveButton.addActionListener(e -> {
                    controller.useMove(battle, move, moveIndex);
                    // onBack will be called by the view model update
                });

                grid.add(moveButton);
            } else {
                grid.add(new JLabel()); // empty cell
            }
        }

        // Fill remaining slots with empty labels
        for (int i = maxButtons; i < 4; i++) {
            grid.add(new JLabel());
        }

        // Bottom: Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(backButton.getFont().deriveFont(14f));
        backButton.addActionListener(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(backButton);

        add(bottom, BorderLayout.SOUTH);
    }
}