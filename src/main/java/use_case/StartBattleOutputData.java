package use_case;

import entity.Battle;

/**
 * Output Data for the Start Battle use case.
 * Contains the result of attempting to start a battle.
 */
public class StartBattleOutputData {
    private final Battle battle;
    private final boolean success;
    private final String message;

    /**
     * Constructor for StartBattleOutputData.
     * @param battle the created Battle object (null if failed)
     * @param success whether the battle started successfully
     * @param message status message about the battle start
     */
    public StartBattleOutputData(Battle battle, boolean success, String message) {
        this.battle = battle;
        this.success = success;
        this.message = message;
    }

    /**
     * Gets the battle object.
     * @return the Battle object, or null if battle creation failed
     */
    public Battle getBattle() {
        return battle;
    }

    /**
     * Checks if the battle started successfully.
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gets the status message.
     * @return the message describing the result
     */
    public String getMessage() {
        return message;
    }
}