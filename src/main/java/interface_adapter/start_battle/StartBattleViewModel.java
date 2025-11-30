package interface_adapter.start_battle;

import entity.Battle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class StartBattleViewModel {
    public static final String BATTLE_PROPERTY = "battle";
    public static final String MESSAGE_PROPERTY = "message";

    private Battle battle;
    private String message;
    private boolean success;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
