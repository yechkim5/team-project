package usecase.select_team;

import entity.Move;
import entity.Pokemon;
import entity.PokemonTeam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Select Team Interactor (Use Case)
 *
 * This class contains the BUSINESS LOGIC for selecting a Pokémon team.
 *
 * RESPONSIBILITIES:
 * 1. Validate pokemon and moves before adding to team
 * 2. Manage teams for both players
 * 3. Create Pokemon with selected moves
 * 4. Validate team completion
 *
 * DESIGN PATTERNS:
 * - Implements SelectTeamInputBoundary (Dependency Inversion)
 * - Uses SelectTeamOutputBoundary to return results (Dependency Inversion)
 * - Single Responsibility: ONLY handles team selection logic
 */
public class SelectTeamInteractor implements SelectTeamInputBoundary {

    private final SelectTeamOutputBoundary outputBoundary;
    private final Map<Integer, PokemonTeam> playerTeams;
    private static final int MAX_TEAM_SIZE = 6;
    private static final int MAX_SELECTED_MOVES_PER_POKEMON = 4; // User can select up to 4 moves per pokemon

    /**
     * Constructor - Dependency Injection
     *
     * @param outputBoundary The presenter that will handle the results
     */
    public SelectTeamInteractor(SelectTeamOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.playerTeams = new HashMap<>();
        // Initialize teams for both players
        this.playerTeams.put(1, new PokemonTeam());
        this.playerTeams.put(2, new PokemonTeam());
    }

    /**
     * Execute the Select Team use case - add a pokemon to team
     */
    @Override
    public void execute(SelectTeamInputData inputData) {
        Pokemon pokemon = inputData.getPokemon();
        List<Move> selectedMoves = inputData.getSelectedMoves();
        int playerNumber = inputData.getPlayerNumber();

        // Validation
        if (pokemon == null) {
            outputBoundary.prepareFailView("Please select a Pokemon first!");
            return;
        }

        PokemonTeam team = playerTeams.get(playerNumber);
        if (team == null) {
            outputBoundary.prepareFailView("Invalid player number!");
            return;
        }

        if (team.getTeam().size() >= MAX_TEAM_SIZE) {
            outputBoundary.prepareFailView("You can't add more than " + MAX_TEAM_SIZE + " Pokemon to your team!");
            return;
        }

        if (selectedMoves == null || selectedMoves.isEmpty()) {
            outputBoundary.prepareFailView("Please select at least one move for this Pokemon!");
            return;
        }

        if (selectedMoves.size() > MAX_SELECTED_MOVES_PER_POKEMON) {
            outputBoundary.prepareFailView("You can't select more than " + MAX_SELECTED_MOVES_PER_POKEMON + " moves!");
            return;
        }

        // Create pokemon with selected moves
        Pokemon pokemonToAdd = createPokemonWithMoves(pokemon, selectedMoves);

        try {
            team.addPokemon(pokemonToAdd);
            
            String message = pokemonToAdd.getName() + " added to team!";
            SelectTeamOutputData outputData = new SelectTeamOutputData(
                team, pokemonToAdd, playerNumber, message
            );
            outputBoundary.prepareSuccessView(outputData);
        } catch (IllegalArgumentException e) {
            outputBoundary.prepareFailView(e.getMessage());
        }
    }

    /**
     * Get the current team for a player
     */
    @Override
    public void getCurrentTeam(int playerNumber) {
        PokemonTeam team = playerTeams.get(playerNumber);
        if (team == null) {
            outputBoundary.prepareFailView("Invalid player number!");
            return;
        }

        // Return team through success view
        SelectTeamOutputData outputData = new SelectTeamOutputData(
            team, null, playerNumber, ""
        );
        outputBoundary.prepareSuccessView(outputData);
    }

    /**
     * Finalize the team for a player
     */
    @Override
    public void finalizeTeam(int playerNumber) {
        PokemonTeam team = playerTeams.get(playerNumber);
        if (team == null) {
            outputBoundary.prepareFailView("Invalid player number!");
            return;
        }

        if (team.getTeam().isEmpty()) {
            outputBoundary.prepareFailView("Cannot finalize an empty team!");
            return;
        }

        // Check if we can proceed to next player
        boolean readyForNextPlayer = (playerNumber == 1 && team.getTeam().size() == MAX_TEAM_SIZE);

        SelectTeamOutputData outputData = new SelectTeamOutputData(
            team, playerNumber, true, readyForNextPlayer
        );
        outputBoundary.prepareTeamFinalizedView(outputData);

        // If player 1 is done, prepare for player 2
        if (readyForNextPlayer) {
            outputBoundary.prepareNextPlayerView(2);
        }
    }

    /**
     * Create a new Pokemon with selected moves
     */
    private Pokemon createPokemonWithMoves(Pokemon original, List<Move> selectedMoves) {
        Pokemon newPokemon = new Pokemon(
            original.getName(),
            original.getBaseStats(),
            original.getTypes(),
            original.getFrontSpriteUrl(),
            original.getBackSpriteUrl()
        );

        // Set the selected moves (up to MAX_SELECTED_MOVES_PER_POKEMON = 4)
        Move[] movesArray = newPokemon.getMoves();
        for (int i = 0; i < Math.min(MAX_SELECTED_MOVES_PER_POKEMON, selectedMoves.size()); i++) {
            movesArray[i] = selectedMoves.get(i);
        }

        return newPokemon;
    }

    /**
     * Get a team for a player (for external access)
     */
    public PokemonTeam getTeam(int playerNumber) {
        return playerTeams.get(playerNumber);
    }

    /**
     * ONLY FOR SAVE/LOAD — restores a saved team into the interactor
     * Safe because it only replaces the internal list content, not the map itself
     */
    public void restoreTeam(int playerNumber, PokemonTeam savedTeam) {
        PokemonTeam team = this.playerTeams.get(playerNumber);
        if (team != null) {
            team.getClass() // dummy to avoid warning
                    .getDeclaredFields()[0] // assuming pokemons is first field
                    .setAccessible(true);
            try {
                java.lang.reflect.Field field = team.getClass().getDeclaredField("pokemons");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                List<Pokemon> list = (List<Pokemon>) field.get(team);
                list.clear();
                list.addAll(savedTeam.getTeam());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

