package view;

import view.demo_entity.DemoPokemon;

import javax.swing.*;
import java.awt.*;

/**
 * Full battle screen demo.
 *
 * Top:    BattleStatusPanel (enemy + your Pokémon, like your drawing)
 * Bottom: CardLayout with:
 *          - CHOICE: Fight / Team buttons
 *          - MOVES : AttackPanel (2x2 move buttons + Back)
 *          - TEAM  : TeamSwitchPanel (2x2 Pokémon buttons + Back)
 *
 * For now, no actual battle logic – this is purely UI to show your team.
 */
public class BattlePanel extends JPanel {

    private final DemoPokemon yourPokemon;
    private final DemoPokemon enemyPokemon;
    private final DemoPokemon[] yourTeamDemo; // demo team for team switch screen

    private final BattleStatusPanel statusPanel;

    private final JPanel bottomCards = new JPanel(new CardLayout());
    private JPanel choicePanel;
    private AttackPanel attackPanel;
    private TeamSwitchPanel teamSwitchPanel;

    public BattlePanel(DemoPokemon yourPokemon, DemoPokemon enemyPokemon) {
        this.yourPokemon = yourPokemon;
        this.enemyPokemon = enemyPokemon;

        // For demo: create a simple team array with 4 Pokémon
        this.yourTeamDemo = new DemoPokemon[] {
                yourPokemon,
                enemyPokemon,
                DemoData.createsByName("Gengar"),
                DemoData.createsByName("Lucario")
        };

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel title = new JLabel("Battle Demo", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        add(title, BorderLayout.NORTH);

        // Top: status area like your sketch
        statusPanel = new BattleStatusPanel(this.yourPokemon, this.enemyPokemon);
        add(statusPanel, BorderLayout.CENTER);

        // Bottom: CardLayout with choice/moves/team
        setupBottomPanels();

        add(bottomCards, BorderLayout.SOUTH);

        showChoice(); // start with Fight/Team visible
    }

    private void setupBottomPanels() {
        // CHOICE panel: Fight / Team buttons
        choicePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton fightButton = new JButton("Fight");
        JButton teamButton  = new JButton("Team");

        fightButton.setFont(fightButton.getFont().deriveFont(16f));
        teamButton.setFont(teamButton.getFont().deriveFont(16f));

        fightButton.addActionListener(e -> showMoves());
        teamButton.addActionListener(e -> showTeam());

        choicePanel.add(fightButton);
        choicePanel.add(teamButton);

        // MOVES panel: AttackPanel with Back wired to showChoice()
        attackPanel = new AttackPanel(this.yourPokemon, this::showChoice);

        // TEAM panel: TeamSwitchPanel with Back wired to showChoice()
        teamSwitchPanel = new TeamSwitchPanel(this.yourTeamDemo, this::showChoice);

        bottomCards.add(choicePanel,   "CHOICE");
        bottomCards.add(attackPanel,   "MOVES");
        bottomCards.add(teamSwitchPanel, "TEAM");
    }

    // CardLayout helpers
    private void showChoice() {
        CardLayout cl = (CardLayout) bottomCards.getLayout();
        cl.show(bottomCards, "CHOICE");
        revalidate();
        repaint();
    }

    private void showMoves() {
        CardLayout cl = (CardLayout) bottomCards.getLayout();
        cl.show(bottomCards, "MOVES");
        revalidate();
        repaint();
    }

    private void showTeam() {
        CardLayout cl = (CardLayout) bottomCards.getLayout();
        cl.show(bottomCards, "TEAM");
        revalidate();
        repaint();
    }

    // OPTIONAL: later call this when HP changes due to real battle logic
    public void refreshStatus() {
        statusPanel.showStatus(yourPokemon, enemyPokemon);
    }
}
