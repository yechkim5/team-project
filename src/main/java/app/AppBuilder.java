package app;

import entity.Trainer;
import interface_adapter.controller.SaveProgressController;
import view.BattlePanel;
import view.PokemonList;

import javax.swing.*;

/**
 * Central application builder – wires all layers together using Clean Architecture.
 * This is the ONLY place where concrete classes are instantiated.
 */
public class AppBuilder {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppBuilder::buildAndShow);
    }

    private static void buildAndShow() {
        JFrame frame = new JFrame("Team Rocket – Pokémon Battle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        // ===================================================================
        // 1. DATA ACCESS LAYER (DAO)
        // ===================================================================
        // This saves to resources/progress.csv – same style as FileUserDataAccessObject
        SaveProgressDataAccessInterface progressDAO = new FileGameProgressDataAccessObject();

        // ===================================================================
        // 2. USE CASE LAYER
        // ===================================================================
        SaveProgress saveUseCase = new SaveProgress(progressDAO);

        // ===================================================================
        // 3. CONTROLLER LAYER (Interface Adapter)
        // ===================================================================
        SaveProgressController saveController = new SaveProgressController(saveUseCase);

        // ===================================================================
        // 4. CREATE GAME STATE
        // ===================================================================
        Trainer playerTrainer = new Trainer("Player");  // will be filled in team selection
        int startingTowerLevel = 1;
        BattleState battleState = new BattleState(startingTowerLevel);

        // ===================================================================
        // 5. START WITH POKÉMON LIST (Team Selection – Use Case 1)
        // ===================================================================
        PokemonList pokemonListPanel = new PokemonList(
                playerTrainer,
                () -> showBattleScreen(frame, playerTrainer, battleState, saveController)
        );

        frame.setContentPane(pokemonListPanel);
        frame.setVisible(true);
    }

    /**
     * Called when user clicks "Start Battle" – switches to Panel 3
     */
    private static void showBattleScreen(JFrame frame,
                                         Trainer playerTrainer,
                                         BattleState battleState,
                                         SaveProgressController saveController) {

        // Generate CPU opponent (you can replace with real AI later)
        Trainer cpuTrainer = generateCpuTrainer();

        // Create Panel 3 – this is where the Save button lives
        BattlePanel battlePanel = new BattlePanel(
                playerTrainer,
                cpuTrainer,
                battleState,
                saveController   // ← INJECTED HERE
        );

        frame.setContentPane(battlePanel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Temporary CPU team generator – replace with real logic
     */
    private static Trainer generateCpuTrainer() {
        Trainer cpu = new Trainer("Team Rocket Grunt");
        // Add 5 random Pokémon – reuse your pokemonFetcher
        // (Implement PokemonCreator.fromRandom() or similar)
        return cpu;
    }
}