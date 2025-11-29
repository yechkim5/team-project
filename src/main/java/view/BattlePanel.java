package view;

import entity.*;
import interface_adapter.start_battle.*;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Full battle screen - NOW CONNECTED TO USE CASES!
 *
 * Top:    BattleStatusPanel (enemy + your Pokémon)
 * Center: Battle log (text area)
 * Bottom: CardLayout with:
 *          - CHOICE: Fight / Team buttons
 *          - MOVES : AttackPanel (2x2 move buttons + Back)
 *          - TEAM  : TeamSwitchPanel (2x2 Pokémon buttons + Back)
 */
public class BattlePanel extends JPanel implements PropertyChangeListener {

    private final Battle battle;
    private final BattleController controller;
    private final BattleViewModel viewModel;

    private final BattleStatusPanel statusPanel;
    private final JTextArea battleLog = new JTextArea(10, 40);

    private final JPanel bottomCards = new JPanel(new CardLayout());
    private JPanel choicePanel;
    private AttackPanel attackPanel;
    private TeamSwitchPanel teamSwitchPanel;

    public BattlePanel(Battle battle, BattleController controller, BattleViewModel viewModel) {
        this.battle = battle;
        this.controller = controller;
        this.viewModel = viewModel;

        // Listen to view model changes
        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel title = new JLabel("Battle Arena", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        add(title, BorderLayout.NORTH);

        // Top: status area with Pokemon info
        Pokemon yourPokemon = battle.getTeam1().getActivePokemon();
        Pokemon enemyPokemon = battle.getTeam2().getActivePokemon();
        statusPanel = new BattleStatusPanel(yourPokemon, enemyPokemon);

        // Create vertical split: status on top, log in middle
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(statusPanel, BorderLayout.NORTH);

        // Battle log
        battleLog.setEditable(false);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setFont(battleLog.getFont().deriveFont(14f));
        JScrollPane logScroll = new JScrollPane(battleLog);
        logScroll.setBorder(BorderFactory.createTitledBorder("Battle Log"));
        topSection.add(logScroll, BorderLayout.CENTER);

        add(topSection, BorderLayout.CENTER);

        // Bottom: CardLayout with choice/moves/team
        setupBottomPanels();
        add(bottomCards, BorderLayout.SOUTH);

        showChoice(); // start with Fight/Team visible

        // Initial battle message
        battleLog.append("Battle Start!\n");
        battleLog.append(yourPokemon.getName() + " vs " + enemyPokemon.getName() + "\n\n");
    }

    private void setupBottomPanels() {
        // CHOICE panel: Fight / Team buttons
        choicePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton fightButton = new JButton("Fight");
        JButton teamButton  = new JButton("Team");

        fightButton.setFont(fightButton.getFont().deriveFont(16f));
        teamButton.setFont(teamButton.getFont().deriveFont(16f));


        fightButton.addActionListener(e -> {
            // Allow both players to see moves on their turn!
            showMoves();
        });

        teamButton.addActionListener(e -> showTeam());

        choicePanel.add(fightButton);
        choicePanel.add(teamButton);

        // MOVES panel: AttackPanel - NOW CONNECTED TO CONTROLLER!
        Pokemon yourPokemon = battle.getTeam1().getActivePokemon();
        attackPanel = new AttackPanel(battle, controller, this::showChoice);

        // TEAM panel: TeamSwitchPanel with Back wired to showChoice()
        Pokemon[] teamArray = battle.getTeam1().getTeam().toArray(new Pokemon[0]);
        teamSwitchPanel = new TeamSwitchPanel(teamArray, this::showChoice);

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
        // Refresh attack panel with current Pokemon's moves
        bottomCards.remove(attackPanel);
        attackPanel = new AttackPanel(battle, controller, this::showChoice);
        bottomCards.add(attackPanel, "MOVES");

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

    // Called when battle state changes (move executed)
    public void refreshStatus() {
        Pokemon yourPokemon = battle.getTeam1().getActivePokemon();
        Pokemon enemyPokemon = battle.getTeam2().getActivePokemon();
        statusPanel.showStatus(yourPokemon, enemyPokemon);
        showChoice(); // Return to main menu after move
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(BattleViewModel.MESSAGE_PROPERTY)) {
            String message = viewModel.getMessage();
            if (message != null && !message.isEmpty()) {
                battleLog.append(message + "\n");
                battleLog.setCaretPosition(battleLog.getDocument().getLength());
            }
        }

        if (evt.getPropertyName().equals(BattleViewModel.BATTLE_PROPERTY)) {
            refreshStatus();

            // Check if battle ended
            if (viewModel.isBattleEnded()) {
                PokemonTeam winner = viewModel.getWinner();
                String winnerName = winner == battle.getTeam1() ? "Player 1" : "Player 2";

                JOptionPane.showMessageDialog(
                        this,
                        winnerName + " wins the battle!",
                        "Battle Over",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }
}