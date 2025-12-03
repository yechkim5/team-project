package usecase.start_battle;

import entity.Battle;

/**
 * Output Data for Start Battle Use Case
 *
 * This class encapsulates the results of attempting to start a battle:
 * - The Battle object (if successful)
 * - Success/failure status
 * - A message explaining the result
 *
 * This follows Clean Architecture - the use case returns data
 * through a simple DTO, not by directly calling the view.
 */
public class StartBattleOutputData {
    private final Battle battle;
    private final boolean success;
    private final String message;

    /**
     * Constructor for StartBattleOutputData
     *
     * @param battle The Battle object created (or null if failed)
     * @param success Whether the battle was successfully started
     * @param message A message describing the result
     */
    public StartBattleOutputData(Battle battle, boolean success, String message) {
        this.battle = battle;
        this.success = success;
        this.message = message;
    }

    /**
     * Get the Battle object
     * @return Battle object (null if battle failed to start)
     */
    public Battle getBattle() {
        return battle;
    }

    /**
     * Check if battle started successfully
     * @return true if battle started, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Get the result message
     * @return String message describing the result
     */
    public String getMessage() {
        return message;
    }
}
