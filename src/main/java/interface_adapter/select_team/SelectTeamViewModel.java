package interface_adapter.select_team;

import entity.Pokemon;
import entity.PokemonTeam;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View Model for Select Team Use Case
 *
 * This view model holds the state for the team selection view.
 * It uses the Observer pattern to notify views of changes.
 */
public class SelectTeamViewModel {
    public static final String TITLE_LABEL = "Select Team";
    public static final String TEAM_PROPERTY = "team";
    public static final String MESSAGE_PROPERTY = "message";

    private PokemonTeam team;
    private String message;
    private boolean success;
    private int playerNumber = 1;
    private int teamSize = 0;
    private Pokemon addedPokemon;
    private boolean teamFinalized = false;
    private boolean readyForNextPlayer = false;
    private int teamUpdateCounter = 0; // Counter to ensure property change always fires

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        support.firePropertyChange(TEAM_PROPERTY, null, this.team);
        support.firePropertyChange(MESSAGE_PROPERTY, null, this.message);
    }

    // Getters and Setters
    public PokemonTeam getTeam() {
        return team;
    }

    public void setTeam(PokemonTeam team) {
        this.team = team;
        // Always fire property change by using a counter
        // This ensures UI updates even when the same PokemonTeam object is modified
        int oldCounter = teamUpdateCounter;
        teamUpdateCounter++; // Increment counter to ensure property change fires
        support.firePropertyChange(TEAM_PROPERTY, oldCounter, teamUpdateCounter);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        String oldMessage = this.message;
        this.message = message;
        support.firePropertyChange(MESSAGE_PROPERTY, oldMessage, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public Pokemon getAddedPokemon() {
        return addedPokemon;
    }

    public void setAddedPokemon(Pokemon addedPokemon) {
        this.addedPokemon = addedPokemon;
    }

    public boolean isTeamFinalized() {
        return teamFinalized;
    }

    public void setTeamFinalized(boolean teamFinalized) {
        this.teamFinalized = teamFinalized;
    }

    public boolean isReadyForNextPlayer() {
        return readyForNextPlayer;
    }

    public void setReadyForNextPlayer(boolean readyForNextPlayer) {
        this.readyForNextPlayer = readyForNextPlayer;
    }


    private entity.PokemonTeam player1Team = new entity.PokemonTeam();
    private entity.PokemonTeam player2Team = new entity.PokemonTeam();
    private int currentPlayer = 1;

    public entity.PokemonTeam getPlayer1Team() { return player1Team; }
    public entity.PokemonTeam getPlayer2Team() { return player2Team; }

    //called every time a team is updated
    public void updateCurrentTeam(entity.PokemonTeam team) {
        if (currentPlayer == 1) {
            player1Team = team;
        } else {
            player2Team = team;
        }
        setTeam(team); // Keep old behavior for UI
    }

    public void setCurrentPlayer(int player) {
        this.currentPlayer = player;
        setPlayerNumber(player);
    }
}

