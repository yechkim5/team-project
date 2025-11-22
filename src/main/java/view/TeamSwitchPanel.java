package view;

import view.demo_entity.DemoPokemon;

import javax.swing.*;
import java.awt.*;

/**
 * Team switch screen (Battle -> Team).
 *
 * Layout:
 *  - Top: "Choose a Pokémon to switch in"
 *  - Center: 2x2 grid of Pokémon buttons
 *  - Bottom: Back button
 *
 * For demo only – buttons don't actually switch.
 * Later, you'll wire this to your switch use case and disable fainted mons.
 */
public class TeamSwitchPanel extends JPanel {

    private final DemoPokemon[] team;
    private final Runnable onBack;

    public TeamSwitchPanel(DemoPokemon[] team, Runnable onBack) {
        this.team = team;
        this.onBack = onBack;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Choose a Pokémon to switch in", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);

        // Center: 2x2 grid of buttons (up to 4 Pokémon)
        JPanel grid = new JPanel(new GridLayout(2, 2, 10, 10));
        add(grid, BorderLayout.CENTER);

        int maxButtons = Math.min(4, team.length);

        for (int i = 0; i < maxButtons; i++) {
            DemoPokemon p = team[i];
            if (p == null) {
                grid.add(new JLabel());
                continue;
            }

            String label = p.getName() + " (HP " + p.getCurrentHp() + "/" + p.getMaxHp() + ")";
            JButton btn = new JButton(label);

            // Make buttons bigger
            btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f));
            btn.setPreferredSize(new Dimension(200, 80));

            grid.add(btn);
        }

        // Fill remaining cells if fewer than 4 Pokémon
        for (int i = maxButtons; i < 4; i++) {
            grid.add(new JLabel());
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
