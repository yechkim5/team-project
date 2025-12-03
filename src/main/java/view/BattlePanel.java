package view;

import app.Main;
import entity.*;
import interface_adapter.battle.*;
import interface_adapter.end_battle.EndBattleController;

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
    private final EndBattleController endBattleController;

    private final BattleStatusPanel statusPanel;
    private final JTextArea battleLog = new JTextArea(10, 40);

    private final JPanel bottomCards = new JPanel(new CardLayout());
    private JPanel choicePanel;
    private AttackPanel attackPanel;
    private TeamSwitchPanel teamSwitchPanel;
    private JPanel endPanel;

    private boolean battleOverHandled = false; //ensures battle doesn't repeat after ending

    public BattlePanel(Battle battle, BattleController controller, BattleViewModel viewModel,
                       EndBattleController endBattleController) {
        this.battle = battle;
        this.controller = controller;
        this.viewModel = viewModel;
        this.endBattleController = endBattleController;

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

        // TEAM panel: TeamSwitchPanel with Back AND battle reference for switching
        Pokemon[] teamArray = battle.getCurrentTurnTeam().getTeam().toArray(new Pokemon[0]);
        teamSwitchPanel = new TeamSwitchPanel(teamArray, battle, this::showChoice, this::handleSwitch);

        //END panel
        endPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton rematchButton = new JButton("Rematch");
        JButton newGameButton = new JButton("New Game");

        rematchButton.setFont(rematchButton.getFont().deriveFont(16f));
        newGameButton.setFont(newGameButton.getFont().deriveFont(16f));

        rematchButton.addActionListener(e -> endBattleController.executeRematch(battle));
        newGameButton.addActionListener(e -> endBattleController.executeNewGame());

        endPanel.add(rematchButton);
        endPanel.add(newGameButton);

        bottomCards.add(choicePanel,   "CHOICE");
        bottomCards.add(attackPanel,   "MOVES");
        bottomCards.add(teamSwitchPanel, "TEAM");
        bottomCards.add(endPanel, "END");
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
        // Refresh team panel with current team's Pokemon
        bottomCards.remove(teamSwitchPanel);
        Pokemon[] teamArray = battle.getCurrentTurnTeam().getTeam().toArray(new Pokemon[0]);
        teamSwitchPanel = new TeamSwitchPanel(teamArray, battle, this::showChoice, this::handleSwitch);
        bottomCards.add(teamSwitchPanel, "TEAM");

        CardLayout cl = (CardLayout) bottomCards.getLayout();
        cl.show(bottomCards, "TEAM");
        revalidate();
        repaint();
    }

    private void showEndOptions(){
        CardLayout cl = (CardLayout) bottomCards.getLayout();
        cl.show(bottomCards, "END");
        revalidate();
        repaint();
    }

    // Called when a Pokemon is switched
    private void handleSwitch() {
        refreshStatus();
        // Log the switch in battle log
        Pokemon newActive = battle.getCurrentTurnTeam().getActivePokemon();
        battleLog.append("Go, " + newActive.getName() + "!\n");
        battleLog.append("It's now " + (battle.isTeam1Turn() ? "Player 1" : "Player 2") + "'s turn.\n\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }

    // Called when battle state changes (move executed)
    public void refreshStatus() {
        Pokemon yourPokemon = battle.getTeam1().getActivePokemon();
        Pokemon enemyPokemon = battle.getTeam2().getActivePokemon();
        statusPanel.showStatus(yourPokemon, enemyPokemon);
        if (viewModel.isBattleEnded()){
            showEndOptions();
        } else {
            showChoice(); // Return to main menu after move
        }
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
            if (viewModel.isBattleEnded() && !battleOverHandled) {
                battleOverHandled = true;

                PokemonTeam winner = viewModel.getWinner();
                String winnerName = winner == battle.getTeam1() ? "Player 1" : "Player 2";

                battleLog.append("\nBattle Over!" + winnerName + "wins!\n");
                battleLog.setCaretPosition(battleLog.getDocument().getLength());

                Main.battleMusic.stopMusic();

                showEndOptions();
            }
        }
    }
}