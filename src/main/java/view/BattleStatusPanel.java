package view;

import view.demo_entity.DemoPokemon;

import javax.swing.*;
import java.awt.*;

public class BattleStatusPanel extends JPanel {

    private final JLabel enemyNameLabel = new JLabel();
    private final JLabel enemyHpLabel = new JLabel();
    private final JLabel enemyImgLabel = new JLabel();

    private final JLabel yourImgLabel = new JLabel();
    private final JLabel yourNameLabel = new JLabel();
    private final JLabel yourHpLabel = new JLabel();

    public BattleStatusPanel(DemoPokemon yourPokemon, DemoPokemon enemyPokemon) {
        setLayout(new GridLayout(2, 2));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel enemyInfo = new JPanel();
        enemyInfo.setLayout(new BoxLayout(enemyInfo, BoxLayout.Y_AXIS));
        enemyNameLabel.setFont(enemyNameLabel.getFont().deriveFont(Font.BOLD, 18f));
        enemyHpLabel.setFont(enemyHpLabel.getFont().deriveFont(16f));
        enemyInfo.add(enemyNameLabel);
        enemyInfo.add(enemyHpLabel);

        enemyImgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        enemyImgLabel.setFont(enemyImgLabel.getFont().deriveFont(16f));

        yourImgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        yourImgLabel.setFont(yourImgLabel.getFont().deriveFont(16f));

        JPanel yourInfo = new JPanel();
        yourInfo.setLayout(new BoxLayout(yourInfo, BoxLayout.Y_AXIS));
        yourNameLabel.setFont(yourNameLabel.getFont().deriveFont(Font.BOLD, 18f));
        yourHpLabel.setFont(yourHpLabel.getFont().deriveFont(16f));
        yourInfo.add(yourNameLabel);
        yourInfo.add(yourHpLabel);

        add(enemyInfo);
        add(enemyImgLabel);
        add(yourImgLabel);
        add(yourInfo);

        showStatus(yourPokemon,enemyPokemon);
    }

    public void showStatus(DemoPokemon yours, DemoPokemon enemy){
        enemyNameLabel.setText("Enemy: " + enemy.getName());
        enemyHpLabel.setText("HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp());

        yourNameLabel.setText("Your Name: " + yours.getName());
        yourHpLabel.setText("HP: " + yours.getCurrentHp() + "/" + yours.getMaxHp());
    }

}
