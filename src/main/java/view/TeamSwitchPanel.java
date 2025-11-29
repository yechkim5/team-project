package view;

import entity.Pokemon;

import javax.swing.*;
import java.awt.*;

/**
 * Team switch screen (Battle -> Team).
 * NOW WORKS WITH REAL POKEMON ENTITIES!
 *
 * Layout:
 *  - Top: "Choose a Pokémon to switch in"
 *  - Center: 2x3 grid of Pokémon buttons
 *  - Bottom: Back button
 */
public class TeamSwitchPanel extends JPanel {

    private final Pokemon[] team;
    private final Runnable onBack;

    public TeamSwitchPanel(Pokemon[] team, Runnable onBack) {
        this.team = team;
        this.onBack = onBack;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Choose a Pokémon to switch in", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);

        // Center: 2x3 grid of buttons (up to 6 Pokémon)
        JPanel grid = new JPanel(new GridLayout(2, 3, 10, 10));
        add(grid, BorderLayout.CENTER);

        int maxButtons = Math.min(6, team.length);

        for (int i = 0; i < maxButtons; i++) {
            Pokemon p = team[i];
            if (p == null) {
                grid.add(new JLabel());
                continue;
            }

            String label = "<html><center>" + p.getName() +
                    "<br>HP: " + p.getCurrentHP() + "/" + p.getBaseStats().getMaxHp() +
                    "</center></html>";
            JButton btn = new JButton(label);

            // Make buttons bigger
            btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f));
            btn.setPreferredSize(new Dimension(200, 80));

            // Disable if fainted
            if (p.getCurrentHP() <= 0) {
                btn.setEnabled(false);
                btn.setText("<html><center>" + p.getName() +
                        "<br>(Fainted)</center></html>");
            }

            // TODO: Implement switching logic here
            // For now, just show a message
            btn.addActionListener(e -> {
                if (p.getCurrentHP() > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Switching feature coming soon!\n" + p.getName() + " selected.",
                            "Switch Pokemon",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });

            grid.add(btn);
        }

        // Fill remaining cells if fewer than 6 Pokémon
        for (int i = maxButtons; i < 6; i++) {
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