package view;

import view.demo_entity.DemoPokemon;
import use_case.select_team.SelectTeamInteractor;
import use_case.select_team.SelectTeamOutputBoundary;
import interface_adapter.select_team.SelectTeamController;
import interface_adapter.select_team.SelectTeamPresenter;
import interface_adapter.select_team.SelectTeamViewModel;

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
        setSize(screen == Screen.TEAM_SELECT ? 1200 : 900, screen == Screen.TEAM_SELECT ? 700 : 600);
        setLocationRelativeTo(null);

        DemoPokemon pikachu = DemoData.createDemoPokemon();
        DemoPokemon charizard = DemoData.createDemoEnemy();

       switch (screen) {
           case TEAM_SELECT:
               // Wire up Clean Architecture dependencies
               SelectTeamViewModel viewModel = new SelectTeamViewModel();
               SelectTeamOutputBoundary presenter = new SelectTeamPresenter(viewModel);
               SelectTeamInteractor interactor = new SelectTeamInteractor(presenter);
               SelectTeamController controller = new SelectTeamController(interactor);
               setContentPane(new TeamSelectionScreen(controller, viewModel));
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
