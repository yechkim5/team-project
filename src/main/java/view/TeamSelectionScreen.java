package view;

import kotlin.random.Random;
import view.demo_entity.DemoPokemon;

import javax.swing.*;
import java.awt.*;

public class TeamSelectionScreen extends JPanel {
    // LEFT-TOP: Pokemon info Panel
    private final PokemonDetailPanel detailPanel;

    // LEFT-BOTTOM: Current team + Selected moves
    private final DefaultListModel<String> teamModel = new DefaultListModel<>();
    private final DefaultListModel<String> selectedMovesModel = new DefaultListModel<>();

    //MIDDLE: moves list model
    private final DefaultListModel<String> moveListModel = new DefaultListModel<>();

    //RIGHT: Pokemon Species List
    private final DefaultListModel<String> pokemonListModel = new DefaultListModel<>();
    // Demo pokemon for demo
    private final DemoPokemon demoPokemon = DemoData.createDemoPokemon(); // placeholder

    public TeamSelectionScreen() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // TITLE
        JLabel title = new JLabel("Player 1 - Select your Pokemon", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        add(title, BorderLayout.NORTH);

        //CENTER
        JPanel centerPanel = new JPanel(new BorderLayout(10,0));
        add(centerPanel, BorderLayout.CENTER);

        //LEFT COLUMN
        JPanel leftColumn = new JPanel(new BorderLayout(0,10));
        add(leftColumn, BorderLayout.WEST);

        //LEFT-TOP: Pokemon Info
        detailPanel = new PokemonDetailPanel(demoPokemon);
        leftColumn.add(detailPanel, BorderLayout.NORTH);

        //LEFT-BOTTOM: Current team + selected moves + add button
        JPanel bottomLeft = new JPanel();
        leftColumn.add(bottomLeft, BorderLayout.CENTER);

        //Current Team list (only placeholder )
        teamModel.addElement("1.Pikachu");
        teamModel.addElement("2.Charizard");

        JList <String> teamList = new JList<>(teamModel);
        JScrollPane teamScroll = new  JScrollPane(teamList);
        teamScroll.setBorder(BorderFactory.createTitledBorder("Current Team"));

        //Selected moves list
        selectedMovesModel.addElement("ThunderBolt (PP 15/15)");
        selectedMovesModel.addElement("Quick Attack (PP 30/30)");

        JList <String> selectedMovesList = new JList<>(selectedMovesModel);
        JScrollPane selectedMovesScroll = new  JScrollPane(selectedMovesList);
        selectedMovesScroll.setBorder(BorderFactory.createTitledBorder("Selected Moves (Max 4)"));

        JButton addToTeamButton = new JButton("Add To Team");
        addToTeamButton.setAlignmentX(Component.CENTER_ALIGNMENT); //does nothing for now needs to be hooked up

        bottomLeft.add(teamScroll);
        bottomLeft.add(Box.createVerticalStrut(10));
        bottomLeft.add(selectedMovesScroll);
        bottomLeft.add(Box.createVerticalStrut(10));

        JButton addMoveButton = new JButton("Add Move");
        addMoveButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        addMoveButton.addActionListener(e -> {
            if (selectedMovesModel.size() < 4) {
                selectedMovesModel.addElement("Placeholder Move (PP ??/??)");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "You can't add more than 4 Moves!",
                        "Move Limit",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        bottomLeft.add(addMoveButton);
        bottomLeft.add(Box.createVerticalStrut(10));

        bottomLeft.add(addToTeamButton);

        leftColumn.setPreferredSize(new Dimension(320,0));

        //MIDDLE COLUMN: Moves list
        //Placeholder move list for demo for actual game need to clear moveListModel and fill using fetcher and factories
        moveListModel.addElement("ThunderBolt (PP 15/15)");
        moveListModel.addElement("Quick Attack (PP 30/30)");
        moveListModel.addElement("Iron Tail (PP 15/15)");
        moveListModel.addElement("Volt Tackle (PP 5/5)");

        JList <String> moveList = new JList<>(moveListModel);
        moveList.setFont(moveList.getFont().deriveFont(14f));

        JScrollPane moveScroll = new  JScrollPane(moveList);
        moveScroll.setBorder(BorderFactory.createTitledBorder("Available Moves"));
        centerPanel.add(moveScroll, BorderLayout.CENTER);

        //RIGHT COLUMN: Pokemon List
        //Placeholder using DemoDAta, replace with actual list later
        String[] species = DemoData.getSpeciesList();
        for (String s : species) {
            pokemonListModel.addElement(s);
        }

        JList <String> pokemonList = new JList<>(pokemonListModel);
        pokemonList.setFont(pokemonList.getFont().deriveFont(14f));

        JScrollPane pokemonScroll = new  JScrollPane(pokemonList);
        pokemonScroll.setBorder(BorderFactory.createTitledBorder("Pokemon List"));
        pokemonScroll.setPreferredSize(new Dimension(180,0));

        centerPanel.add(pokemonScroll, BorderLayout.EAST);
    }
}
