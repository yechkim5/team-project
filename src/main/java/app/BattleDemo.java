package app;

import entity.*;
import factory.pokemonFactory;
import interface_adapter.start_battle.*;
import interface_adapter.use_move.*;
import interface_adapter.switch_pokemon.*;
import interface_adapter.end_battle.*;
import use_case.start_battle.*;
import use_case.use_move.*;
import use_case.switch_pokemon.*;
import use_case.end_battle.*;
import view.BattlePanel;

import javax.swing.*;

/**
 * Demo app to test the battle system with Clean Architecture.
 * Demonstrates all 4 use cases: StartBattle, UseMove, SwitchPokemon, EndBattle
 * Each use case has its own Controller, Presenter, and ViewModel.
 */
public class BattleDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create two demo teams with 6 Pokemon each
            PokemonTeam team1 = createDemoTeam1();
            PokemonTeam team2 = createDemoTeam2();

            System.out.println("\n=== BATTLE DEMO - WIRING 4 USE CASES ===");

            // ============================================================
            // USE CASE 1: START BATTLE
            // ============================================================
            StartBattleViewModel startBattleViewModel = new StartBattleViewModel();
            StartBattleOutputBoundary startBattlePresenter = new StartBattlePresenter(startBattleViewModel);
            StartBattleInputBoundary startBattleInteractor = new StartBattleInteractor(startBattlePresenter);
            StartBattleController startBattleController = new StartBattleController(startBattleInteractor);

            // Execute: Create the battle
            startBattleController.startBattle(team1, team2);

            Battle battle = startBattleViewModel.getBattle();

            if (battle == null) {
                String errorMsg = startBattleViewModel.getMessage();
                JOptionPane.showMessageDialog(null, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("✓ StartBattle use case wired");

            // ============================================================
            // USE CASE 4: END BATTLE
            // ============================================================
            EndBattleViewModel endBattleViewModel = new EndBattleViewModel();
            EndBattleOutputBoundary endBattlePresenter = new EndBattlePresenter(endBattleViewModel);
            EndBattleInputBoundary endBattleInteractor = new EndBattleInteractor(endBattlePresenter);

            System.out.println("✓ EndBattle use case wired");

            // ============================================================
            // USE CASE 2: USE MOVE
            // ============================================================
            UseMoveViewModel useMoveViewModel = new UseMoveViewModel();
            useMoveViewModel.setBattle(battle);

            UseMoveOutputBoundary useMovePresenter = new UseMovePresenter(useMoveViewModel);
            UseMoveInputBoundary useMoveInteractor = new UseMoveInteractor(
                    useMovePresenter,
                    endBattleInteractor  // INJECT EndBattle dependency
            );
            UseMoveController useMoveController = new UseMoveController(useMoveInteractor);

            System.out.println("✓ UseMove use case wired (with EndBattle injected)");

            // ============================================================
            // USE CASE 3: SWITCH POKEMON
            // ============================================================
            SwitchPokemonViewModel switchPokemonViewModel = new SwitchPokemonViewModel();
            SwitchPokemonOutputBoundary switchPokemonPresenter = new SwitchPokemonPresenter(switchPokemonViewModel);
            SwitchPokemonInputBoundary switchPokemonInteractor = new SwitchPokemonInteractor(switchPokemonPresenter);
            SwitchPokemonController switchPokemonController = new SwitchPokemonController(switchPokemonInteractor);

            System.out.println("✓ SwitchPokemon use case wired");

            // ============================================================
            // VIEW LAYER
            // ============================================================
            JFrame frame = new JFrame("Pokemon Battle Demo - Clean Architecture");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setLocationRelativeTo(null);

            // Create BattlePanel with 3 Controllers + 3 ViewModels
            BattlePanel battlePanel = new BattlePanel(
                    battle,                      // The battle entity
                    useMoveController,           // Controller for UseMove
                    switchPokemonController,     // Controller for SwitchPokemon
                    useMoveViewModel,            // ViewModel for UseMove
                    switchPokemonViewModel,      // ViewModel for SwitchPokemon
                    endBattleViewModel           // ViewModel for EndBattle
            );

            frame.setContentPane(battlePanel);
            frame.setVisible(true);

            System.out.println("✓ View layer initialized");
            System.out.println("=== ALL 4 USE CASES WIRED SUCCESSFULLY ===\n");
        });
    }

    /**
     * Creates demo team 1 with 6 Pokemon
     */
    private static PokemonTeam createDemoTeam1() {
        PokemonTeam team = new PokemonTeam();

        try {
            // Pokemon 1: Pikachu
            pokemonFactory factory1 = new pokemonFactory("pikachu");
            Pokemon pikachu = factory1.getPokemon();
            team.addPokemon(pikachu);

            // Pokemon 2: Charizard
            pokemonFactory factory2 = new pokemonFactory("charizard");
            Pokemon charizard = factory2.getPokemon();
            team.addPokemon(charizard);

            // Pokemon 3: Blastoise
            pokemonFactory factory3 = new pokemonFactory("blastoise");
            Pokemon blastoise = factory3.getPokemon();
            team.addPokemon(blastoise);

            // Pokemon 4: Venusaur
            pokemonFactory factory4 = new pokemonFactory("venusaur");
            Pokemon venusaur = factory4.getPokemon();
            team.addPokemon(venusaur);

            // Pokemon 5: Snorlax
            pokemonFactory factory5 = new pokemonFactory("snorlax");
            Pokemon snorlax = factory5.getPokemon();
            team.addPokemon(snorlax);

            // Pokemon 6: Mewtwo
            pokemonFactory factory6 = new pokemonFactory("mewtwo");
            Pokemon mewtwo = factory6.getPokemon();
            team.addPokemon(mewtwo);

            System.out.println("Team 1 created: 6 Pokemon");

        } catch (Exception e) {
            System.err.println("Error creating team 1: " + e.getMessage());
            e.printStackTrace();
        }

        return team;
    }

    /**
     * Creates demo team 2 with 6 Pokemon
     */
    private static PokemonTeam createDemoTeam2() {
        PokemonTeam team = new PokemonTeam();

        try {
            // Pokemon 1: Gengar
            pokemonFactory factory1 = new pokemonFactory("gengar");
            Pokemon gengar = factory1.getPokemon();
            team.addPokemon(gengar);

            // Pokemon 2: Dragonite
            pokemonFactory factory2 = new pokemonFactory("dragonite");
            Pokemon dragonite = factory2.getPokemon();
            team.addPokemon(dragonite);

            // Pokemon 3: Gyarados
            pokemonFactory factory3 = new pokemonFactory("gyarados");
            Pokemon gyarados = factory3.getPokemon();
            team.addPokemon(gyarados);

            // Pokemon 4: Alakazam
            pokemonFactory factory4 = new pokemonFactory("alakazam");
            Pokemon alakazam = factory4.getPokemon();
            team.addPokemon(alakazam);

            // Pokemon 5: Machamp
            pokemonFactory factory5 = new pokemonFactory("machamp");
            Pokemon machamp = factory5.getPokemon();
            team.addPokemon(machamp);

            // Pokemon 6: Articuno
            pokemonFactory factory6 = new pokemonFactory("articuno");
            Pokemon articuno = factory6.getPokemon();
            team.addPokemon(articuno);

            System.out.println("Team 2 created: 6 Pokemon");

        } catch (Exception e) {
            System.err.println("Error creating team 2: " + e.getMessage());
            e.printStackTrace();
        }

        return team;
    }
}