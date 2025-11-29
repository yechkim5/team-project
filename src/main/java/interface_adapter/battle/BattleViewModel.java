package interface_adapter.battle;

import entity.Battle;
import entity.PokemonTeam;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View Model for Battle
 *
 * Holds the state for the battle view and notifies observers of changes.
 */
public class BattleViewModel {
    public static final String BATTLE_PROPERTY = "battle";
    public static final String MESSAGE_PROPERTY = "message";

    private Battle battle;
    private String message;
    private boolean battleEnded;
    private PokemonTeam winner;
    private int lastDamage;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        support.firePropertyChange(BATTLE_PROPERTY, null, this.battle);
        support.firePropertyChange(MESSAGE_PROPERTY, null, this.message);
    }

    // Getters and Setters
    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
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

    public PokemonTeam getWinner() {
        return winner;
    }

    public void setWinner(PokemonTeam winner) {
        this.winner = winner;
    }

    public int getLastDamage() {
        return lastDamage;
    }

    public void setLastDamage(int lastDamage) {
        this.lastDamage = lastDamage;
    }
}