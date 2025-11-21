package use_case;

import entity.Battle;
import entity.Pokemon;
import entity.PokemonTeam;
import java.util.List;

/**
 * Interactor for the Start Battle use case.
 * Contains the business logic for validating teams and creating a battle.
 * Follows Clean Architecture - depends only on entities and boundaries.
 *
 * Responsibilities:
 * - Validate that both teams exist and are not null
 * - Validate that both teams have Pokemon
 * - Validate that both teams have at least one alive Pokemon
 * - Create a Battle object if all validations pass
 * - Communicate results through the output boundary
 */
public class StartBattleInteractor {
    private final StartBattleOutputBoundary outputBoundary;

    /**
     * Constructor for StartBattleInteractor.
     * @param outputBoundary the presenter that will handle the output
     */
    public StartBattleInteractor(StartBattleOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    /**
     * Executes the Start Battle use case.
     * Validates both teams and creates a battle if all checks pass.
     *
     * @param inputData contains both teams (user and opponent)
     */
    public void execute(StartBattleInputData inputData) {
        PokemonTeam userTeam = inputData.getUserTeam();
        PokemonTeam opponentTeam = inputData.getOpponentTeam();

        // Validation 1: Check if user team exists
        if (userTeam == null) {
            outputBoundary.prepareFailView("User team cannot be null.");
            return;
        }

        // Validation 2: Check if opponent team exists
        if (opponentTeam == null) {
            outputBoundary.prepareFailView("Opponent team cannot be null.");
            return;
        }

        // Validation 3: Check if user team has Pokemon
        List<Pokemon> userPokemonList = userTeam.getTeam();
        if (userPokemonList == null || userPokemonList.isEmpty()) {
            outputBoundary.prepareFailView("User team has no Pokemon.");
            return;
        }

        // Validation 4: Check if opponent team has Pokemon
        List<Pokemon> opponentPokemonList = opponentTeam.getTeam();
        if (opponentPokemonList == null || opponentPokemonList.isEmpty()) {
            outputBoundary.prepareFailView("Opponent team has no Pokemon.");
            return;
        }

        // Validation 5: Check if user has at least one Pokemon that can battle
        if (!hasAlivePokemon(userPokemonList)) {
            outputBoundary.prepareFailView(
                    "User team has no Pokemon that can battle. All Pokemon have fainted."
            );
            return;
        }

        // Validation 6: Check if opponent has at least one Pokemon that can battle
        if (!hasAlivePokemon(opponentPokemonList)) {
            outputBoundary.prepareFailView(
                    "Opponent team has no Pokemon that can battle. All Pokemon have fainted."
            );
            return;
        }

        // All validations passed - create the battle
        try {
            // Create battle object
            // Battle constructor automatically initializes BattleStats for all Pokemon
            Battle battle = new Battle(userTeam, opponentTeam);

            // Prepare success output
            String successMessage = "Battle started! User's team vs Opponent's team";

            StartBattleOutputData outputData = new StartBattleOutputData(
                    battle,
                    true,
                    successMessage
            );

            // Notify the presenter of success
            outputBoundary.prepareSuccessView(outputData);

        } catch (Exception e) {
            // Handle any unexpected errors during battle creation
            outputBoundary.prepareFailView(
                    "Failed to create battle: " + e.getMessage()
            );
        }
    }

    /**
     * Helper method to check if a list of Pokemon has at least one alive Pokemon.
     * A Pokemon is considered alive if its current HP is greater than 0.
     *
     * @param pokemonList the list of Pokemon to check
     * @return true if at least one Pokemon has HP > 0, false otherwise
     */
    private boolean hasAlivePokemon(List<Pokemon> pokemonList) {
        for (Pokemon pokemon : pokemonList) {
            // Check if Pokemon's current HP is greater than 0
            if (pokemon.getCurrentHP() > 0) {
                return true;
            }
        }
        return false;
    }
}
