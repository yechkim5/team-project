package view;

import entity.Battle;
import entity.Pokemon;
import entity.PokemonTeam;
import interface_adapter.switch_pokemon.SwitchPokemonController;

import javax.swing.*;
import java.awt.*;

public class TeamSwitchPanel extends JPanel {

    private final Pokemon[] team;
    private final Battle battle;
    private final SwitchPokemonController controller;
    private final Runnable onBack;
    private final Runnable onSwitch;

    public TeamSwitchPanel(Pokemon[] team,
                           Battle battle,
                           SwitchPokemonController controller,
                           Runnable onBack,
                           Runnable onSwitch) {
        this.team = team;
        this.battle = battle;
        this.controller = controller;
        this.onBack = onBack;
        this.onSwitch = onSwitch;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Choose a Pokémon to switch in", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);

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

            btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f));
            btn.setPreferredSize(new Dimension(200, 80));

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
                final int pokemonIndex = i;
                btn.addActionListener(e -> {
                    switchPokemon(pokemonIndex);
                });
            }

            grid.add(btn);
        }

        for (int i = maxButtons; i < 6; i++) {
            grid.add(new JLabel());
        }

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

    private void switchPokemon(int newIndex) {
        controller.switchPokemon(battle, newIndex);

        if (onSwitch != null) {
            onSwitch.run();
        }
        if (onBack != null) {
            onBack.run();
        }
    }
}