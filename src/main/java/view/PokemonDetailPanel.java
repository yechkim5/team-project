package view;

import entity.Pokemon;
import view.demo_entity.DemoPokemon;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonDetailPanel extends JPanel {
    private final JLabel nameLabel = new JLabel();
    private final JLabel typeLabel = new JLabel();
    private final JLabel hpLabel = new JLabel();
    private final JLabel atkLabel = new JLabel();
    private final JLabel defLabel = new JLabel();
    private final JLabel imageLabel = new JLabel();

    public PokemonDetailPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 24f));
        hpLabel.setFont(hpLabel.getFont().deriveFont(16f));
        atkLabel.setFont(atkLabel.getFont().deriveFont( 16f));
        defLabel.setFont(defLabel.getFont().deriveFont( 16f));
        typeLabel.setFont(typeLabel.getFont().deriveFont( 16f));

        info.add(nameLabel);
        info.add(Box.createVerticalStrut(5));
        info.add(typeLabel);
        info.add(Box.createVerticalStrut(5));
        info.add(hpLabel);
        info.add(atkLabel);
        info.add(defLabel);

        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 200));
        imageLabel.setText("[Pokemon Image Here]");
        imageLabel.setFont(imageLabel.getFont().deriveFont(16f));

        add(info, BorderLayout.WEST);
        add(imageLabel, BorderLayout.CENTER);
    }

    // Overloaded constructor for backward compatibility with DemoPokemon
    public PokemonDetailPanel(DemoPokemon p) {
        this();
        showPokemon(p);
    }

    public void showPokemon(DemoPokemon p) {
        nameLabel.setText(p.getName());
        typeLabel.setText("Type " + p.getType());
        hpLabel.setText("HP " + p.getCurrentHp() + "/" + p.getMaxHp());
        atkLabel.setText("Attack " + p.getAttack());
        defLabel.setText("Defense " + p.getDefense());
    }

    public void showPokemon(Pokemon p) {
        if (p == null) {
            nameLabel.setText("No Pokemon Selected");
            typeLabel.setText("");
            hpLabel.setText("");
            atkLabel.setText("");
            defLabel.setText("");
            imageLabel.setIcon(null);
            imageLabel.setText("[Pokemon Image Here]");
            return;
        }

        nameLabel.setText(p.getName());
        
        // Format types
        List<String> types = p.getTypes();
        String typeText = types.stream().collect(Collectors.joining("/"));
        typeLabel.setText("Type: " + typeText);
        
        // Get stats
        int maxHp = p.getBaseStats().getMaxHp();
        int currentHp = p.getCurrentHP();
        int attack = p.getBaseStats().getAttack();
        int defense = p.getBaseStats().getDefense();
        
        hpLabel.setText("HP: " + currentHp + "/" + maxHp);
        atkLabel.setText("Attack: " + attack);
        defLabel.setText("Defense: " + defense);
        
        // Load and display Pokemon image
        loadPokemonImage(p.getFrontSpriteUrl());
    }

    private void loadPokemonImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty() || "null".equals(imageUrl)) {
            imageLabel.setIcon(null);
            imageLabel.setText("[Image Not Available]");
            return;
        }

        // Load image asynchronously to avoid blocking UI
        new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                ImageIcon imageIcon = new ImageIcon(url);
                
                // Scale image to fit nicely in the panel
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                // Update UI on EDT
                SwingUtilities.invokeLater(() -> {
                    imageLabel.setIcon(scaledIcon);
                    imageLabel.setText("");
                });
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    imageLabel.setIcon(null);
                    imageLabel.setText("[Image Load Error]");
                });
                System.err.println("Error loading Pokemon image: " + e.getMessage());
            }
        }).start();
    }
}
