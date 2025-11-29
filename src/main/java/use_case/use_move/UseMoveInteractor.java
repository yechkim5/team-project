package use_case.use_move;

import entity.*;

/**
 * Use Move Interactor (Use Case)
 *
 * Contains the business logic for executing a move in battle.
 *
 * FLOW:
 * 1. Validate move can be used (PP > 0)
 * 2. Get attacker and defender Pokemon
 * 3. Get battle stats for both
 * 4. Execute the move
 * 5. Check if defender fainted
 * 6. Check if battle ended
 * 7. Switch turns
 * 8. Return results
 */
public class UseMoveInteractor implements UseMoveInputBoundary {

    private final UseMoveOutputBoundary outputBoundary;

    public UseMoveInteractor(UseMoveOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(UseMoveInputData inputData) {
        Battle battle = inputData.getBattle();
        Move move = inputData.getMove();

        // Validation: Check if move has PP
        if (move.getCurrentPp() <= 0) {
            outputBoundary.prepareFailView("Move has no PP left!");
            return;
        }

        // Get current turn's Pokemon and opponent
        Pokemon attacker = battle.getCurrentTurnPokemon();
        Pokemon defender = battle.getOpponentPokemon();
        PokemonTeam attackerTeam = battle.getCurrentTurnTeam();
        PokemonTeam defenderTeam = battle.getOpponentTeam();

        // Store HP before attack for damage calculation
        int defenderHpBefore = defender.getCurrentHP();

        // Get battle stats (these track stat modifications during battle)
        BattleStats attackerStats = battle.getBattleStatsMap().get(attacker);
        BattleStats defenderStats = battle.getBattleStatsMap().get(defender);

        // Execute the move!
        move.useMove(attackerTeam, defenderTeam, attackerStats, defenderStats);

        // Calculate damage dealt
        int damageDealt = defenderHpBefore - defender.getCurrentHP();

        // Build message
        StringBuilder message = new StringBuilder();
        message.append(attacker.getName()).append(" used ").append(move.getMoveName()).append("!\n");

        if (damageDealt > 0) {
            message.append("Dealt ").append(damageDealt).append(" damage!\n");
        }

        // Check if defender fainted
        if (defender.getCurrentHP() <= 0) {
            message.append(defender.getName()).append(" fainted!\n");

            // Try to auto-switch to next Pokemon
            boolean hasSwitched = battle.handleFaintedPokemon(defenderTeam);

            if (hasSwitched) {
                message.append(defenderTeam.getActivePokemon().getName()).append(" was sent out!\n");
            }
        }

        // Check if battle ended
        if (battle.checkBattleEnd()) {
            PokemonTeam winner = battle.getWinner();
            message.append("\nBattle Over! ").append(
                    winner == battle.getTeam1() ? "Player 1" : "Player 2"
            ).append(" wins!");

            UseMoveOutputData outputData = new UseMoveOutputData(
                    battle, message.toString(), true, winner,
                    attacker, defender, move.getMoveName(), damageDealt
            );

            outputBoundary.prepareBattleEndView(outputData);
            return;
        }

        // Switch turns
        battle.switchTurn();

        // Return success
        UseMoveOutputData outputData = new UseMoveOutputData(
                battle, message.toString(), false, null,
                attacker, defender, move.getMoveName(), damageDealt
        );

        outputBoundary.prepareSuccessView(outputData);
    }
}