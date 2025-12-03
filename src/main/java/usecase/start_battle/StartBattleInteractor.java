package usecase.start_battle;

import entity.Battle;
import entity.Pokemon;
import entity.PokemonTeam;

/**
 * Start Battle Interactor (Use Case #2)
 *
 * This class contains the BUSINESS LOGIC for starting a Pokémon battle.
 *
 * RESPONSIBILITIES:
 * 1. Validate that both teams are ready for battle
 * 2. Create a Battle object
 * 3. Return success/failure through the output boundary
 *
 * DESIGN PATTERNS:
 * - Implements StartBattleInputBoundary (Dependency Inversion)
 * - Uses StartBattleOutputBoundary to return results (Dependency Inversion)
 * - Single Responsibility: ONLY handles starting battles
 *
 * This is YOUR use case from the project blueprint!
 */
public class StartBattleInteractor implements StartBattleInputBoundary {

    // The output boundary is injected through the constructor (Dependency Injection)
    private final StartBattleOutputBoundary outputBoundary;

    /**
     * Constructor - Dependency Injection
     *
     * @param outputBoundary The presenter that will handle the results
     */
    public StartBattleInteractor(StartBattleOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    /**
     * Execute the Start Battle use case
     *
     * FLOW:
     * 1. Extract teams from input data
     * 2. Validate user team
     * 3. Validate opponent team
     * 4. Create Battle object
     * 5. Return success through output boundary
     *
     * @param inputData Contains the two teams
     */
    @Override
    public void execute(StartBattleInputData inputData) {
        // Step 1: Extract the teams
        PokemonTeam userTeam = inputData.getUserTeam();
        PokemonTeam opponentTeam = inputData.getOpponentTeam();

        // Step 2: VALIDATION - Check if teams exist
        if (userTeam == null || opponentTeam == null) {
            outputBoundary.prepareFailView("Error: Both teams must be selected!");
            return;
        }

        // Step 3: VALIDATION - Check if user team is valid
        if (!isValidTeam(userTeam)) {
            outputBoundary.prepareFailView(
                    "Error: Your team must have at least 1 Pokémon with HP > 0!"
            );
            return;
        }

        // Step 4: VALIDATION - Check if opponent team is valid
        if (!isValidTeam(opponentTeam)) {
            outputBoundary.prepareFailView(
                    "Error: Opponent team must have at least 1 Pokémon with HP > 0!"
            );
            return;
        }

        // Step 5: CREATE BATTLE - All validations passed!
        Battle battle = new Battle(userTeam, opponentTeam);

        // Step 6: CREATE SUCCESS MESSAGE
        String message = String.format(
                "Battle started!\n%s vs %s\nGood luck!",
                userTeam.getActivePokemon().getName(),
                opponentTeam.getActivePokemon().getName()
        );

        // Step 7: RETURN SUCCESS
        StartBattleOutputData outputData = new StartBattleOutputData(battle, true, message);
        outputBoundary.prepareSuccessView(outputData);
    }

    /**
     * Validate that a team is ready for battle
     *
     * A valid team must:
     * 1. Not be null
     * 2. Have at least 1 Pokémon
     * 3. Have an active Pokémon (first in list)
     * 4. Have at least 1 Pokémon with HP > 0
     *
     * @param team The team to validate
     * @return true if team is valid, false otherwise
     */
    private boolean isValidTeam(PokemonTeam team) {
        // Check 1: Team exists
        if (team == null) {
            return false;
        }

        // Check 2: Team has Pokémon
        if (team.getTeam().isEmpty()) {
            return false;
        }

        // Check 3: Team has an active Pokémon
        if (team.getActivePokemon() == null) {
            return false;
        }

        // Check 4: Active Pokémon has HP
        if (team.getActivePokemon().getCurrentHP() <= 0) {
            return false;
        }

        // Check 5: At least ONE Pokémon is alive
        // (This ensures we can continue even if active Pokémon faints)
        for (Pokemon pokemon : team.getTeam()) {
            if (pokemon.getCurrentHP() > 0) {
                return true; // Found at least one alive Pokémon
            }
        }

        // No alive Pokémon found
        return false;
    }
}
