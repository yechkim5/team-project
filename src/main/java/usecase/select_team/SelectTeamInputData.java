package usecase.select_team;

import entity.Move;
import entity.Pokemon;

import java.util.List;

/**
 * Input Data for Select Team Use Case
 *
 * This class encapsulates the data needed to select a team:
 * - Pokemon to add
 * - Selected moves for that pokemon
 * - Player number (1 or 2)
 */
public class SelectTeamInputData {
    private final Pokemon pokemon;
    private final List<Move> selectedMoves;
    private final int playerNumber;
    private final String pokemonName;

    /**
     * Constructor for adding a pokemon to team
     *
     * @param pokemon The pokemon to add
     * @param selectedMoves The moves selected for this pokemon
     * @param playerNumber The player number (1 or 2)
     */
    public SelectTeamInputData(Pokemon pokemon, List<Move> selectedMoves, int playerNumber) {
        this.pokemon = pokemon;
        this.selectedMoves = selectedMoves;
        this.playerNumber = playerNumber;
        this.pokemonName = null;
    }

    /**
     * Constructor for getting current team
     *
     * @param playerNumber The player number (1 or 2)
     */
    public SelectTeamInputData(int playerNumber) {
        this.pokemon = null;
        this.selectedMoves = null;
        this.playerNumber = playerNumber;
        this.pokemonName = null;
    }

    /**
     * Constructor for finalizing team
     *
     * @param playerNumber The player number (1 or 2)
     * @param isFinalize True if finalizing team
     */
    public SelectTeamInputData(int playerNumber, boolean isFinalize) {
        this.pokemon = null;
        this.selectedMoves = null;
        this.playerNumber = playerNumber;
        this.pokemonName = null;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public List<Move> getSelectedMoves() {
        return selectedMoves;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getPokemonName() {
        return pokemonName;
    }
}

