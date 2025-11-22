package view;

import view.demo_entity.DemoPokemon;

import javax.swing.*;

public class DemoFrame extends JFrame {

    public enum Screen {
        TEAM_SELECT,
        DETAIL,
        BATTLE,
        ATTACK
    }

    public DemoFrame(Screen screen) {
        setTitle("Pokemon UI Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        DemoPokemon pikachu = DemoData.createDemoPokemon();
        DemoPokemon charizard = DemoData.createDemoEnemy();

       switch (screen) {
           case TEAM_SELECT:
               setContentPane(new TeamSelectionScreen());
               break;

           case DETAIL:
               setContentPane(new PokemonDetailPanel(pikachu));
               break;

           case BATTLE:
               setContentPane(new BattlePanel(pikachu, charizard));
               break;

           case ATTACK:
               setContentPane(new AttackPanel(pikachu, () -> {}));
               break;

           default:
               setContentPane(new JPanel());
       }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DemoFrame frame = new DemoFrame(Screen.TEAM_SELECT);
                //DemoFrame frame = new DemoFrame(Screen.BATTLE);

                frame.setVisible(true);
            }
        });
    }
}
