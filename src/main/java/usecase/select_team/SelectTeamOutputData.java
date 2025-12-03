package usecase.select_team;

import entity.Pokemon;
import entity.PokemonTeam;

/**
 * Output Data for Select Team Use Case
 *
 * This class encapsulates the data returned from the use case:
 * - The updated team
 * - Success message
 * - Player number
 */
public class SelectTeamOutputData {
    private final PokemonTeam team;
    private final String message;
    private final boolean success;
    private final int playerNumber;
    private final int teamSize;
    private final Pokemon addedPokemon;
    private final boolean teamFinalized;
    private final boolean readyForNextPlayer;

    /**
     * Constructor for successful pokemon addition
     */
    public SelectTeamOutputData(PokemonTeam team, Pokemon addedPokemon, int playerNumber, String message) {
        this.team = team;
        this.addedPokemon = addedPokemon;
        this.playerNumber = playerNumber;
        this.message = message;
        this.success = true;
        this.teamSize = team.getTeam().size();
        this.teamFinalized = false;
        this.readyForNextPlayer = false;
    }

    /**
     * Constructor for team finalization
     */
    public SelectTeamOutputData(PokemonTeam team, int playerNumber, boolean teamFinalized, boolean readyForNextPlayer) {
        this.team = team;
        this.playerNumber = playerNumber;
        this.teamFinalized = teamFinalized;
        this.readyForNextPlayer = readyForNextPlayer;
        this.message = "Team finalized for Player " + playerNumber;
        this.success = true;
        this.teamSize = team.getTeam().size();
        this.addedPokemon = null;
    }

    public PokemonTeam getTeam() {
        return team;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public Pokemon getAddedPokemon() {
        return addedPokemon;
    }

    public boolean isTeamFinalized() {
        return teamFinalized;
    }

    public boolean isReadyForNextPlayer() {
        return readyForNextPlayer;
    }
}

