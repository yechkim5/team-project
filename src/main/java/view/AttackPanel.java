package view;

import view.demo_entity.DemoMove;
import view.demo_entity.DemoPokemon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Move selection screen (Fight -> Moves).
 *
 * Layout:
 *  - Top: "Choose a move for X"
 *  - Center: 2x2 grid of big move buttons
 *  - Bottom: Back button
 *
 * For now, buttons don't execute attacks; they are just visual.
 * Later, you can attach listeners to each move button.
 */
public class AttackPanel extends JPanel {

    private final DemoPokemon attacker;
    private final Runnable onBack; // callback to go back to main battle menu

    public AttackPanel(DemoPokemon attacker, Runnable onBack) {
        this.attacker = attacker;
        this.onBack = onBack;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title at top
        JLabel title = new JLabel("Choose a move for " + attacker.getName(), SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);

        // Center: 2x2 grid of big buttons
        JPanel grid = new JPanel(new GridLayout(2, 2, 10, 10));
        add(grid, BorderLayout.CENTER);

        List<DemoMove> moves = attacker.getMoves();
        int maxButtons = Math.min(4, moves.size());

        for (int i = 0; i < maxButtons; i++) {
            DemoMove m = moves.get(i);
            String label = m.getName() + " (PP " + m.getCurrentPP() + "/" + m.getMaxPP() + ")";
            JButton moveButton = new JButton(label);

            // Make buttons bigger
            moveButton.setFont(moveButton.getFont().deriveFont(Font.BOLD, 14f));
            moveButton.setPreferredSize(new Dimension(200, 80));

            // For now, buttons do nothing. Later, add attack logic here.
            // moveButton.addActionListener(e -> { /* call battle use case */ });

            grid.add(moveButton);
        }

        // If fewer than 4 moves, fill remaining slots with empty labels
        for (int i = maxButtons; i < 4; i++) {
            grid.add(new JLabel()); // empty cell
        }

        // Bottom: Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(backButton.getFont().deriveFont(14f));
        backButton.addActionListener(e -> {
            if (onBack != null) {
                onBack.run(); // tell BattlePanel to go back to main menu
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(backButton);

        add(bottom, BorderLayout.SOUTH);
    }
}
