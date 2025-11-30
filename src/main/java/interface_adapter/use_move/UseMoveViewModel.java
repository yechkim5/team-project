package interface_adapter.use_move;

import entity.Battle;
import entity.Pokemon;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UseMoveViewModel {
    public static final String BATTLE_PROPERTY = "battle";
    public static final String MESSAGE_PROPERTY = "message";

    private Battle battle;
    private String message;
    private int lastDamage;
    private Pokemon attacker;
    private Pokemon defender;
    private String moveName;

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

    public int getLastDamage() {
        return lastDamage;
    }

    public void setLastDamage(int lastDamage) {
        this.lastDamage = lastDamage;
    }

    public Pokemon getAttacker() {
        return attacker;
    }

    public void setAttacker(Pokemon attacker) {
        this.attacker = attacker;
    }

    public Pokemon getDefender() {
        return defender;
    }

    public void setDefender(Pokemon defender) {
        this.defender = defender;
    }

    public String getMoveName() {
        return moveName;
    }

    public void setMoveName(String moveName) {
        this.moveName = moveName;
    }
}
