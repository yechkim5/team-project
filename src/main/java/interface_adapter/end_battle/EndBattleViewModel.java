package interface_adapter.end_battle;

import entity.Battle;
import entity.PokemonTeam;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EndBattleViewModel {
    public static final String BATTLE_END_PROPERTY = "battleEnd";
    public static final String MESSAGE_PROPERTY = "message";

    private Battle battle;
    private PokemonTeam winner;
    private String winnerName;
    private int remainingPokemon;
    private String message;
    private boolean battleEnded;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        support.firePropertyChange(BATTLE_END_PROPERTY, null, this.battleEnded);
        support.firePropertyChange(MESSAGE_PROPERTY, null, this.message);
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public PokemonTeam getWinner() {
        return winner;
    }

    public void setWinner(PokemonTeam winner) {
        this.winner = winner;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public int getRemainingPokemon() {
        return remainingPokemon;
    }

    public void setRemainingPokemon(int remainingPokemon) {
        this.remainingPokemon = remainingPokemon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBattleEnded() {
        return battleEnded;
    }

    public void setBattleEnded(boolean battleEnded) {
        this.battleEnded = battleEnded;
    }
}