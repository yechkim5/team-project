package view;

import view.demo_entity.DemoPokemon;

import javax.swing.*;
import java.awt.*;

public class PokemonDetailPanel extends JPanel {
    private final JLabel nameLabel = new JLabel();
    private final JLabel typeLabel = new JLabel();
    private final JLabel hpLabel = new JLabel();
    private final JLabel atkLabel = new JLabel();
    private final JLabel defLabel = new JLabel();

    public PokemonDetailPanel(DemoPokemon p) {
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

        JLabel img = new JLabel ("[Pokemon Image Here]");
        img.setHorizontalAlignment(JLabel.CENTER);
        img.setFont(img.getFont().deriveFont(16f));

        add(info, BorderLayout.WEST);
        add(img, BorderLayout.CENTER);

        showPokemon(p);
    }

    public void showPokemon(DemoPokemon p) {
        nameLabel.setText(p.getName());
        typeLabel.setText("Type " + p.getType());
        hpLabel.setText("HP " + p.getCurrentHp() + "/" + p.getMaxHp());
        atkLabel.setText("Attack " + p.getAttack());
        defLabel.setText("Defense " + p.getDefense());
    }
}
