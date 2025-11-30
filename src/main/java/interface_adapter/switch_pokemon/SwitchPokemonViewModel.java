package interface_adapter.switch_pokemon;

import entity.Battle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SwitchPokemonViewModel {
    public static final String SWITCH_PROPERTY = "switch";
    public static final String MESSAGE_PROPERTY = "message";

    private Battle battle;
    private String oldPokemonName;
    private String newPokemonName;
    private String message;
    private boolean switchSuccessful;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        support.firePropertyChange(SWITCH_PROPERTY, null, this.switchSuccessful);
        support.firePropertyChange(MESSAGE_PROPERTY, null, this.message);
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public String getOldPokemonName() {
        return oldPokemonName;
    }

    public void setOldPokemonName(String oldPokemonName) {
        this.oldPokemonName = oldPokemonName;
    }

    public String getNewPokemonName() {
        return newPokemonName;
    }

    public void setNewPokemonName(String newPokemonName) {
        this.newPokemonName = newPokemonName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSwitchSuccessful() {
        return switchSuccessful;
    }

    public void setSwitchSuccessful(boolean switchSuccessful) {
        this.switchSuccessful = switchSuccessful;
    }
}