package view;

import entity.*;
import interface_adapter.use_move.*;
import interface_adapter.switch_pokemon.*;
import interface_adapter.end_battle.*;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BattlePanel extends JPanel implements PropertyChangeListener {

    private final Battle battle;
    private final UseMoveController useMoveController;
    private final SwitchPokemonController switchPokemonController;
    private final UseMoveViewModel useMoveViewModel;
    private final SwitchPokemonViewModel switchPokemonViewModel;
    private final EndBattleViewModel endBattleViewModel;

    private final BattleStatusPanel statusPanel;
    private final JTextArea battleLog = new JTextArea(10, 40);

    private final JPanel bottomCards = new JPanel(new CardLayout());
    private JPanel choicePanel;
    private AttackPanel attackPanel;
    private TeamSwitchPanel teamSwitchPanel;

    public BattlePanel(Battle battle,
                       UseMoveController useMoveController,
                       SwitchPokemonController switchPokemonController,
                       UseMoveViewModel useMoveViewModel,
                       SwitchPokemonViewModel switchPokemonViewModel,
                       EndBattleViewModel endBattleViewModel) {
        this.battle = battle;
        this.useMoveController = useMoveController;
        this.switchPokemonController = switchPokemonController;
        this.useMoveViewModel = useMoveViewModel;
        this.switchPokemonViewModel = switchPokemonViewModel;
        this.endBattleViewModel = endBattleViewModel;

        // Register listeners
        System.out.println("[BattlePanel] Registering property change listeners...");
        useMoveViewModel.addPropertyChangeListener(this);
        switchPokemonViewModel.addPropertyChangeListener(this);
        endBattleViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Battle Arena", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        add(title, BorderLayout.NORTH);

        Pokemon yourPokemon = battle.getTeam1().getActivePokemon();
        Pokemon enemyPokemon = battle.getTeam2().getActivePokemon();
        statusPanel = new BattleStatusPanel(yourPokemon, enemyPokemon);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(statusPanel, BorderLayout.NORTH);

        battleLog.setEditable(false);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setFont(battleLog.getFont().deriveFont(14f));
        JScrollPane logScroll = new JScrollPane(battleLog);
        logScroll.setBorder(BorderFactory.createTitledBorder("Battle Log"));
        topSection.add(logScroll, BorderLayout.CENTER);

        add(topSection, BorderLayout.CENTER);

        setupBottomPanels();
        add(bottomCards, BorderLayout.SOUTH);

        showChoice();

        battleLog.append("Battle Start!\n");
        battleLog.append(yourPokemon.getName() + " vs " + enemyPokemon.getName() + "\n\n");

        System.out.println("[BattlePanel] Initialized successfully");
    }

    private void setupBottomPanels() {
        choicePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton fightButton = new JButton("Fight");
        JButton teamButton  = new JButton("Team");

        fightButton.setFont(fightButton.getFont().deriveFont(16f));
        teamButton.setFont(teamButton.getFont().deriveFont(16f));

        fightButton.addActionListener(e -> showMoves());
        teamButton.addActionListener(e -> showTeam());

        choicePanel.add(fightButton);
        choicePanel.add(teamButton);

        attackPanel = new AttackPanel(battle, useMoveController, this::showChoice);

        Pokemon[] teamArray = battle.getCurrentTurnTeam().getTeam().toArray(new Pokemon[0]);
        teamSwitchPanel = new TeamSwitchPanel(
                teamArray,
                battle,
                switchPokemonController,
                this::showChoice,
                this::handleSwitch
        );

        bottomCards.add(choicePanel,   "CHOICE");
        bottomCards.add(attackPanel,   "MOVES");
        bottomCards.add(teamSwitchPanel, "TEAM");
    }

    private void showChoice() {
        CardLayout cl = (CardLayout) bottomCards.getLayout();
        cl.show(bottomCards, "CHOICE");
        revalidate();
        repaint();
    }

    private void showMoves() {
        bottomCards.remove(attackPanel);
        attackPanel = new AttackPanel(battle, useMoveController, this::showChoice);
        bottomCards.add(attackPanel, "MOVES");

        CardLayout cl = (CardLayout) bottomCards.getLayout();
        cl.show(bottomCards, "MOVES");
        revalidate();
        repaint();
    }

    private void showTeam() {
        bottomCards.remove(teamSwitchPanel);
        Pokemon[] teamArray = battle.getCurrentTurnTeam().getTeam().toArray(new Pokemon[0]);
        teamSwitchPanel = new TeamSwitchPanel(
                teamArray,
                battle,
                switchPokemonController,
                this::showChoice,
                this::handleSwitch
        );
        bottomCards.add(teamSwitchPanel, "TEAM");

        CardLayout cl = (CardLayout) bottomCards.getLayout();
        cl.show(bottomCards, "TEAM");
        revalidate();
        repaint();
    }

    private void handleSwitch() {
        refreshStatus();
    }

    public void refreshStatus() {
        try {
            Pokemon yourPokemon = battle.getTeam1().getActivePokemon();
            Pokemon enemyPokemon = battle.getTeam2().getActivePokemon();
            statusPanel.showStatus(yourPokemon, enemyPokemon);
            showChoice();
        } catch (Exception e) {
            System.err.println("[BattlePanel] Error refreshing status: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            String propertyName = evt.getPropertyName();
            System.out.println("[BattlePanel] Property changed: " + propertyName + " from " + evt.getSource().getClass().getSimpleName());

            // Handle UseMove events
            if (evt.getSource() == useMoveViewModel) {
                if (UseMoveViewModel.MESSAGE_PROPERTY.equals(propertyName)) {
                    String message = useMoveViewModel.getMessage();
                    System.out.println("[BattlePanel] UseMove message: " + message);
                    if (message != null && !message.isEmpty()) {
                        appendToBattleLog(message);
                    }
                }

                if (UseMoveViewModel.BATTLE_PROPERTY.equals(propertyName)) {
                    System.out.println("[BattlePanel] Battle state changed, refreshing status");
                    refreshStatus();
                }
            }

            // Handle SwitchPokemon events
            if (evt.getSource() == switchPokemonViewModel) {
                if (SwitchPokemonViewModel.MESSAGE_PROPERTY.equals(propertyName)) {
                    String message = switchPokemonViewModel.getMessage();
                    System.out.println("[BattlePanel] Switch message: " + message);
                    if (message != null && !message.isEmpty()) {
                        appendToBattleLog(message);

                        if (switchPokemonViewModel.isSwitchSuccessful()) {
                            System.out.println("[BattlePanel] Switch successful, refreshing status");
                            refreshStatus();
                        }
                    }
                }
            }

            // Handle EndBattle events
            if (evt.getSource() == endBattleViewModel) {
                if (EndBattleViewModel.BATTLE_END_PROPERTY.equals(propertyName)) {
                    if (endBattleViewModel.isBattleEnded()) {
                        System.out.println("[BattlePanel] Battle ended!");
                        String message = endBattleViewModel.getMessage();

                        appendToBattleLog("\n" + message);

                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(
                                    this,
                                    message,
                                    "Battle Over",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        });
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("[BattlePanel] ERROR in propertyChange: " + e.getMessage());
            e.printStackTrace();

            // Try to append error to battle log
            try {
                appendToBattleLog("[ERROR] Something went wrong. Check console.");
            } catch (Exception e2) {
                System.err.println("[BattlePanel] Failed to append error to battle log: " + e2.getMessage());
            }
        }
    }

    /**
     * Safely append text to battle log with error handling
     */
    private void appendToBattleLog(String message) {
        try {
            if (message != null && !message.isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        battleLog.append(message + "\n");
                        battleLog.setCaretPosition(battleLog.getDocument().getLength());
                        System.out.println("[BattlePanel] Appended to battle log: " + message.substring(0, Math.min(50, message.length())) + "...");
                    } catch (Exception e) {
                        System.err.println("[BattlePanel] Failed to append to battle log: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("[BattlePanel] Error in appendToBattleLog: " + e.getMessage());
            e.printStackTrace();
        }
    }
}