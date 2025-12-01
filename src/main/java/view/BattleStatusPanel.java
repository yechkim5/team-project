package view;

import entity.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Status panel showing both Pokemon in battle.
 * NOW WORKS WITH REAL POKEMON AND LOADS SPRITES!
 */
public class BattleStatusPanel extends JPanel {

    private final JLabel enemyNameLabel = new JLabel();
    private final JLabel enemyHpLabel = new JLabel();
    private final JLabel enemyImgLabel = new JLabel();

    private final JLabel yourImgLabel = new JLabel();
    private final JLabel yourNameLabel = new JLabel();
    private final JLabel yourHpLabel = new JLabel();

    public BattleStatusPanel(Pokemon yourPokemon, Pokemon enemyPokemon) {
        setLayout(new GridLayout(2, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Enemy info panel (top left)
        JPanel enemyInfo = new JPanel();
        enemyInfo.setLayout(new BoxLayout(enemyInfo, BoxLayout.Y_AXIS));
        enemyInfo.setBorder(BorderFactory.createTitledBorder("Opponent"));
        enemyNameLabel.setFont(enemyNameLabel.getFont().deriveFont(Font.BOLD, 18f));
        enemyHpLabel.setFont(enemyHpLabel.getFont().deriveFont(16f));
        enemyInfo.add(enemyNameLabel);
        enemyInfo.add(enemyHpLabel);

        // Enemy image (top right)
        enemyImgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        enemyImgLabel.setFont(enemyImgLabel.getFont().deriveFont(16f));
        enemyImgLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        enemyImgLabel.setPreferredSize(new Dimension(150, 150));

        // Your image (bottom left)
        yourImgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        yourImgLabel.setFont(yourImgLabel.getFont().deriveFont(16f));
        yourImgLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        yourImgLabel.setPreferredSize(new Dimension(150, 150));

        // Your info panel (bottom right)
        JPanel yourInfo = new JPanel();
        yourInfo.setLayout(new BoxLayout(yourInfo, BoxLayout.Y_AXIS));
        yourInfo.setBorder(BorderFactory.createTitledBorder("Your Pokemon"));
        yourNameLabel.setFont(yourNameLabel.getFont().deriveFont(Font.BOLD, 18f));
        yourHpLabel.setFont(yourHpLabel.getFont().deriveFont(16f));
        yourInfo.add(yourNameLabel);
        yourInfo.add(yourHpLabel);

        add(enemyInfo);
        add(enemyImgLabel);
        add(yourImgLabel);
        add(yourInfo);

        showStatus(yourPokemon, enemyPokemon);
    }

    public void showStatus(Pokemon yours, Pokemon enemy) {
        if (enemy != null) {
            enemyNameLabel.setText(enemy.getName());
            enemyHpLabel.setText("HP: " + enemy.getCurrentHP() + "/" + enemy.getBaseStats().getMaxHp());
            loadPokemonImage(enemyImgLabel, enemy.getFrontSpriteUrl());
        }

        if (yours != null) {
            yourNameLabel.setText(yours.getName());
            yourHpLabel.setText("HP: " + yours.getCurrentHP() + "/" + yours.getBaseStats().getMaxHp());
            loadPokemonImage(yourImgLabel, yours.getBackSpriteUrl());
        }
    }

    private void loadPokemonImage(JLabel label, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty() || "null".equals(imageUrl)) {
            label.setIcon(null);
            label.setText("[No Image]");
            return;
        }

        // Load image asynchronously
        new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                ImageIcon icon = new ImageIcon(url);
                Image image = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(image);

                SwingUtilities.invokeLater(() -> {
                    label.setIcon(scaledIcon);
                    label.setText("");
                });
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    label.setIcon(null);
                    label.setText("[Image Error]");
                });
            }
        }).start();
    }
}