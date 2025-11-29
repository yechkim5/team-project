package view;

import entity.Battle;
import entity.Pokemon;
import entity.PokemonTeam;

import javax.swing.*;
import java.awt.*;

/**
 * Team switch screen (Battle -> Team).
 * NOW ACTUALLY SWITCHES POKEMON!
 *
 * Layout:
 *  - Top: "Choose a Pokémon to switch in"
 *  - Center: 2x3 grid of Pokémon buttons
 *  - Bottom: Back button
 */
public class TeamSwitchPanel extends JPanel {

    private final Pokemon[] team;
    private final Battle battle;
    private final Runnable onBack;
    private final Runnable onSwitch;

    public TeamSwitchPanel(Pokemon[] team, Battle battle, Runnable onBack, Runnable onSwitch) {
        this.team = team;
        this.battle = battle;
        this.onBack = onBack;
        this.onSwitch = onSwitch;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Choose a Pokémon to switch in", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);

        // Center: 2x3 grid of buttons (up to 6 Pokémon)
        JPanel grid = new JPanel(new GridLayout(2, 3, 10, 10));
        add(grid, BorderLayout.CENTER);

        int maxButtons = Math.min(6, team.length);
        PokemonTeam currentTeam = battle.getCurrentTurnTeam();
        Pokemon activePokemon = currentTeam.getActivePokemon();

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

            // Disable if fainted or already active
            if (p.getCurrentHP() <= 0) {
                btn.setEnabled(false);
                btn.setText("<html><center>" + p.getName() +
                        "<br>(Fainted)</center></html>");
            } else if (p == activePokemon) {
                btn.setEnabled(false);
                btn.setText("<html><center>" + p.getName() +
                        "<br>HP: " + p.getCurrentHP() + "/" + p.getBaseStats().getMaxHp() +
                        "<br>(Active)</center></html>");
            } else {
                // ACTUAL SWITCHING LOGIC!
                final int pokemonIndex = i;
                btn.addActionListener(e -> {
                    switchPokemon(pokemonIndex, p.getName());
                });
            }

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

    private void switchPokemon(int newIndex, String pokemonName) {
        PokemonTeam currentTeam = battle.getCurrentTurnTeam();
        Pokemon oldActive = currentTeam.getActivePokemon();

        // Perform the switch
        currentTeam.switchActivePokemon(newIndex);

        // Show confirmation message
        String message = oldActive.getName() + " was switched out!\n" +
                pokemonName + " was sent out!";
        JOptionPane.showMessageDialog(this,
                message,
                "Pokemon Switched",
                JOptionPane.INFORMATION_MESSAGE);

        // Switch turns (switching costs your turn)
        battle.switchTurn();

        // Notify parent to refresh and go back
        if (onSwitch != null) {
            onSwitch.run();
        }
        if (onBack != null) {
            onBack.run();
        }
    }
}