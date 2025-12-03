package view;

import entity.GameState;
import entity.Pokemon;
import entity.Move;
import entity.PokemonTeam;
import factory.pokemonFactory;
import interface_adapter.select_team.SelectTeamController;
import interface_adapter.select_team.SelectTeamViewModel;

import use_case.game_state_persistence.SaveGameInteractor;
import use_case.select_team.SelectTeamOutputBoundary;
import interface_adapter.select_team.SelectTeamPresenter;
import use_case.select_team.SelectTeamInteractor;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.filechooser.*; //file selector window(should handle both loading and saving)

public class TeamSelectionScreen extends JPanel implements PropertyChangeListener {
    // List of 20 pokemon names (using names as IDs)
    private static final String[] POKEMON_NAMES = {
        "pikachu", "charizard", "blastoise", "venusaur", "lucario",
        "gengar", "dragonite", "tyranitar", "metagross", "garchomp",
        "salamence", "gardevoir", "machamp", "alakazam", "gyarados",
        "snorlax", "arcanine", "lapras", "aerodactyl", "mewtwo"
    };
    private static String[] pokemonNames;

    private static final int MAX_TEAM_SIZE = 6;

    // LEFT-TOP: Pokemon info Panel
    private final PokemonDetailPanel detailPanel;

    // LEFT-BOTTOM: Current team + Selected moves
    private final DefaultListModel<String> teamModel = new DefaultListModel<>();
    private final DefaultListModel<String> selectedMovesModel = new DefaultListModel<>();

    // MIDDLE: moves list model
    private final DefaultListModel<String> moveListModel = new DefaultListModel<>();

    // RIGHT: Pokemon Species List
    private final DefaultListModel<String> pokemonListModel = new DefaultListModel<>();

    // UI Components
    private final JLabel titleLabel;
    private final JButton addToTeamButton;
    private final JButton finalizeTeamButton;
    private final JScrollPane teamScroll;

    // Data storage (view layer only - for UI state)
    private final Map<String, Pokemon> pokemonCache = new HashMap<>();
    private Pokemon currentlySelectedPokemon = null;
    private String currentlySelectedPokemonName = null;
    private final List<Move> currentlySelectedMoves = new ArrayList<>();
    private final Map<String, List<Move>> pokemonMovesCache = new HashMap<>();

    // Clean Architecture dependencies
    private final SelectTeamController controller;
    private final SelectTeamViewModel viewModel;
    private int currentPlayerNumber = 1;

    public TeamSelectionScreen(SelectTeamController controller, SelectTeamViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.currentPlayerNumber = viewModel.getPlayerNumber();

        // Listen to view model changes
        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // TITLE
        titleLabel = new JLabel("Player " + currentPlayerNumber + " - Select your Pokemon (Max " + MAX_TEAM_SIZE + ")", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));
        add(titleLabel, BorderLayout.NORTH);

        // CENTER
        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        add(centerPanel, BorderLayout.CENTER);

        // LEFT COLUMN
        JPanel leftColumn = new JPanel(new BorderLayout(0, 10));
        add(leftColumn, BorderLayout.WEST);

        // LEFT-TOP: Pokemon Info
        detailPanel = new PokemonDetailPanel();
        leftColumn.add(detailPanel, BorderLayout.NORTH);

        // LEFT-BOTTOM: Current team + selected moves + add button
        JPanel bottomLeft = new JPanel();
        bottomLeft.setLayout(new BoxLayout(bottomLeft, BoxLayout.Y_AXIS));
        leftColumn.add(bottomLeft, BorderLayout.CENTER);

        // Current Team list
        JList<String> teamList = new JList<>(teamModel);
        teamScroll = new JScrollPane(teamList);
        teamScroll.setBorder(BorderFactory.createTitledBorder("Current Team (Max " + MAX_TEAM_SIZE + ")"));
        teamScroll.setPreferredSize(new Dimension(300, 150));

        // Selected moves list
        JList<String> selectedMovesList = new JList<>(selectedMovesModel);
        JScrollPane selectedMovesScroll = new JScrollPane(selectedMovesList);
        selectedMovesScroll.setBorder(BorderFactory.createTitledBorder("Selected Moves (Max 4)"));
        selectedMovesScroll.setPreferredSize(new Dimension(300, 200));

        addToTeamButton = new JButton("Add To Team");
        addToTeamButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addToTeamButton.addActionListener(e -> addPokemonToTeam());

        finalizeTeamButton = new JButton("Finalize Team");
        finalizeTeamButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        finalizeTeamButton.setEnabled(false);
        finalizeTeamButton.addActionListener(e -> finalizeTeam());

        JButton removeMoveButton = new JButton("Remove Selected Move");
        removeMoveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeMoveButton.addActionListener(e -> removeSelectedMove(selectedMovesList));

        bottomLeft.add(teamScroll);
        bottomLeft.add(Box.createVerticalStrut(10));
        bottomLeft.add(selectedMovesScroll);
        bottomLeft.add(Box.createVerticalStrut(10));
        bottomLeft.add(addToTeamButton);
        bottomLeft.add(Box.createVerticalStrut(5));
        bottomLeft.add(finalizeTeamButton);
        bottomLeft.add(Box.createVerticalStrut(5));
        bottomLeft.add(removeMoveButton);

        JButton saveButton = new JButton("Save Game");
        saveButton.setForeground(new Color(0, 100, 0));
        saveButton.setFont(saveButton.getFont().deriveFont(Font.BOLD, 14f));
        saveButton.addActionListener(e -> saveGame());

        JButton loadButton = new JButton("Load Game");
        loadButton.setForeground(new Color(0, 0, 139));
        loadButton.setFont(loadButton.getFont().deriveFont(Font.BOLD, 14f));
        loadButton.addActionListener(e -> loadGame());

        // Add spacing and buttons
        bottomLeft.add(Box.createVerticalStrut(20));
        bottomLeft.add(saveButton);
        bottomLeft.add(Box.createVerticalStrut(8));
        bottomLeft.add(loadButton);

        leftColumn.setPreferredSize(new Dimension(320, 0));

        // MIDDLE COLUMN: Moves list
        JList<String> moveList = new JList<>(moveListModel);
        moveList.setFont(moveList.getFont().deriveFont(14f));
        moveList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                addMoveToSelection(moveList.getSelectedValue());
            }
        });

        JScrollPane moveScroll = new JScrollPane(moveList);
        moveScroll.setBorder(BorderFactory.createTitledBorder("Available Moves"));
        centerPanel.add(moveScroll, BorderLayout.CENTER);

        // RIGHT COLUMN: Pokemon List
        loadPokemonList();
        JList<String> pokemonList = new JList<>(pokemonListModel);
        pokemonList.setFont(pokemonList.getFont().deriveFont(14f));
        pokemonList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedName = pokemonList.getSelectedValue();
                if (selectedName != null) {
                    selectPokemon(selectedName);
                }
            }
        });

        JScrollPane pokemonScroll = new JScrollPane(pokemonList);
        pokemonScroll.setBorder(BorderFactory.createTitledBorder("Pokemon List"));
        pokemonScroll.setPreferredSize(new Dimension(180, 0));

        centerPanel.add(pokemonScroll, BorderLayout.EAST);
    }

    private void loadPokemonList() {
        // Load pokemon names into the list
        for (String name : POKEMON_NAMES) {
            pokemonListModel.addElement(name);
        }
    }

    private void selectPokemon(String pokemonName) {
        // Clear current selection
        currentlySelectedPokemon = null;
        currentlySelectedPokemonName = null;
        currentlySelectedMoves.clear();
        selectedMovesModel.clear();
        moveListModel.clear();

        // Load pokemon if not cached
        if (!pokemonCache.containsKey(pokemonName)) {
            try {
                pokemonFactory factory = new pokemonFactory(pokemonName);
                Pokemon pokemon = factory.getPokemon();
                pokemonCache.put(pokemonName, pokemon);

                // Cache moves
                Move[] moves = pokemon.getMoves();
                List<Move> moveList = new ArrayList<>();
                for (Move move : moves) {
                    if (move != null) {
                        moveList.add(move);
                    }
                }
                pokemonMovesCache.put(pokemonName, moveList);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error loading Pokemon: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }

        currentlySelectedPokemon = pokemonCache.get(pokemonName);
        currentlySelectedPokemonName = pokemonName;
        detailPanel.showPokemon(currentlySelectedPokemon);

        // Load available moves
        List<Move> moves = pokemonMovesCache.get(pokemonName);
        for (Move move : moves) {
            String moveDisplay = move.getMoveName() + " (PP " + move.getMaxPp() + "/" + move.getMaxPp() + ")";
            moveListModel.addElement(moveDisplay);
        }
    }

    private void addMoveToSelection(String moveDisplay) {
        if (moveDisplay == null || currentlySelectedPokemon == null) {
            return;
        }

        if (currentlySelectedMoves.size() >= 4) {
            JOptionPane.showMessageDialog(
                this,
                "You can't select more than 4 Moves!",
                "Move Limit",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Extract move name from display string
        String moveName = moveDisplay.substring(0, moveDisplay.indexOf(" (PP"));
        
        // Find the move in the pokemon's moves
        List<Move> availableMoves = pokemonMovesCache.get(currentlySelectedPokemonName);
        Move selectedMove = null;
        for (Move move : availableMoves) {
            if (move.getMoveName().equals(moveName)) {
                selectedMove = move;
                break;
            }
        }

        if (selectedMove != null && !currentlySelectedMoves.contains(selectedMove)) {
            currentlySelectedMoves.add(selectedMove);
            selectedMovesModel.addElement(moveDisplay);
        }
    }

    private void removeSelectedMove(JList<String> selectedMovesList) {
        int selectedIndex = selectedMovesList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < currentlySelectedMoves.size()) {
            currentlySelectedMoves.remove(selectedIndex);
            selectedMovesModel.remove(selectedIndex);
        }
    }

    private void addPokemonToTeam() {
        if (currentlySelectedPokemon == null) {
            JOptionPane.showMessageDialog(
                this,
                "Please select a Pokemon first!",
                "No Pokemon Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (currentlySelectedMoves.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please select at least one move for this Pokemon!",
                "No Moves Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Use controller to add pokemon (follows clean architecture)
        controller.addPokemonToTeam(currentlySelectedPokemon, currentlySelectedMoves, currentPlayerNumber);
    }

    private void finalizeTeam() {
        controller.finalizeTeam(currentPlayerNumber);
    }

    public void updateTeamDisplay(PokemonTeam team) {
        teamModel.clear();
        if (team != null) {
            List<Pokemon> pokemonList = team.getTeam();
            for (int i = 0; i < pokemonList.size(); i++) {
                Pokemon p = pokemonList.get(i);
                int moveCount = 0;
                for (Move move : p.getMoves()) {
                    if (move != null) moveCount++;
                }
                String teamEntry = (i + 1) + ". " + p.getName() + " (" + moveCount + " moves)";
                teamModel.addElement(teamEntry);
            }
            
            // Enable finalize button if team has 6 pokemon
            finalizeTeamButton.setEnabled(pokemonList.size() == MAX_TEAM_SIZE);
        }
    }

    private void switchToNextPlayer(int nextPlayerNumber) {
        currentPlayerNumber = nextPlayerNumber;
        titleLabel.setText("Player " + currentPlayerNumber + " - Select your Pokemon (Max " + MAX_TEAM_SIZE + ")");
        
        // Clear UI for next player
        currentlySelectedPokemon = null;
        currentlySelectedPokemonName = null;
        currentlySelectedMoves.clear();
        selectedMovesModel.clear();
        moveListModel.clear();
        teamModel.clear();
        detailPanel.showPokemon((Pokemon) null);
        finalizeTeamButton.setEnabled(false);
        
        // Reset message tracking
        lastShownMessage = "";
        
        // Get the new player's team
        controller.getCurrentTeam(currentPlayerNumber);
    }

    private String lastShownMessage = ""; // Track last shown message to prevent duplicates

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(SelectTeamViewModel.TEAM_PROPERTY)) {
            updateTeamDisplay(viewModel.getTeam());
        } else if (evt.getPropertyName().equals(SelectTeamViewModel.MESSAGE_PROPERTY)) {
            String message = viewModel.getMessage();

            // Skip showing popup for Player 2 finalization messages (handled in Main.java now)
            boolean isPlayer2Finalization = viewModel.isTeamFinalized() &&
                    viewModel.getPlayerNumber() == 2 &&
                    (message.contains("finalized") || message.contains("complete"));

            // Only show popup if message is not null, not empty, different from last shown, and not a Player 2 finalization message
            if (message != null && !message.isEmpty() && !message.equals(lastShownMessage) && !isPlayer2Finalization) {
                lastShownMessage = message; // Track this message

                if (viewModel.isSuccess()) {
                    JOptionPane.showMessageDialog(
                            this,
                            message,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    // Clear selection after successful add
                    if (viewModel.getAddedPokemon() != null) {
                        currentlySelectedPokemon = null;
                        currentlySelectedPokemonName = null;
                        currentlySelectedMoves.clear();
                        selectedMovesModel.clear();
                        moveListModel.clear();
                        detailPanel.showPokemon((Pokemon) null);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        message,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            
            // Check if ready for next player (only check once, not on every message change)
            if (viewModel.isReadyForNextPlayer() && viewModel.getPlayerNumber() == 1 && 
                viewModel.isTeamFinalized()) {
                int response = JOptionPane.showConfirmDialog(
                    this,
                    "Player 1's team is complete! Ready for Player 2?",
                    "Team Complete",
                    JOptionPane.YES_NO_OPTION
                );
                if (response == JOptionPane.YES_OPTION) {
                    switchToNextPlayer(2);
                }
            }
        }
    }

    public PokemonTeam getSelectedTeam(int playerNumber) {
        // Access through view model (which gets it from use case)
        return viewModel.getTeam();
    }

    // ==================== MANUAL SAVE / LOAD ====================

    private void saveGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("resources"));
        chooser.setDialogTitle("Save Your Pokémon Team");
        chooser.setFileFilter(new FileNameExtensionFilter("Pokémon Save File (*.json)", "json"));
        chooser.setSelectedFile(new File("My Team Save.json"));

        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String path = file.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".json")) {
                path += ".json";
            }

            if (new File(path).exists()) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "File already exists! Overwrite?", "Confirm",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) return;
            }

            try {
                GameState state = app.GameOrchestrator.getCurrent();
                String json = SaveGameInteractor.toJson(state).toString(4);
                Files.createDirectories(Paths.get("resources"));
                Files.writeString(Paths.get(path), json);

                JOptionPane.showMessageDialog(this,
                        "Saved successfully!\n" + new File(path).getName(),
                        "Save Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Save failed: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("resources"));
        chooser.setDialogTitle("Load a Saved Game");
        chooser.setFileFilter(new FileNameExtensionFilter("Pokémon Save File (*.json)", "json"));

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                String content = Files.readString(Paths.get(file.getAbsolutePath()));
                org.json.JSONObject json = new org.json.JSONObject(content);
                GameState loaded = SaveGameInteractor.fromJson(json);

                if (loaded != null) {
                    app.GameOrchestrator.updateState(loaded);
                    reloadTeamSelectionScreen();

                    JOptionPane.showMessageDialog(this,
                            "Loaded: " + file.getName() + "\nYour team is ready!",
                            "Load Complete", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Load failed: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void reloadTeamSelectionScreen() {
        GameState state = app.GameOrchestrator.getCurrent();
        int playerNum = (state.activeTeamSelector() == GameState.Player.PLAYER1) ? 1 : 2;

        SelectTeamViewModel viewModel = new SelectTeamViewModel();
        SelectTeamOutputBoundary presenter = new SelectTeamPresenter(viewModel);
        SelectTeamInteractor interactor = new SelectTeamInteractor(presenter);

        interactor.restoreTeam(1, state.player1Team());
        interactor.restoreTeam(2, state.player2Team());

        SelectTeamController controller = new SelectTeamController(interactor);
        viewModel.setPlayerNumber(playerNum);
        interactor.getCurrentTeam(playerNum);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.getContentPane().removeAll();

            TeamSelectionScreen newScreen = new TeamSelectionScreen(controller, viewModel);

            PokemonTeam currentTeam = playerNum == 1 ? state.player1Team() : state.player2Team();
            newScreen.updateTeamDisplay(currentTeam);
            newScreen.finalizeTeamButton.setEnabled(currentTeam.getTeam().size() == 6);

            frame.setContentPane(newScreen);
            frame.revalidate();
            frame.repaint();
            frame.setSize(1200, 700);
        }
    }

}
