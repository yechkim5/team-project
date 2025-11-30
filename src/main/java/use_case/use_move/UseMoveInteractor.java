package use_case.use_move;

import entity.*;
import use_case.end_battle.EndBattleInputBoundary;
import use_case.end_battle.EndBattleInputData;

public class UseMoveInteractor implements UseMoveInputBoundary {

    private final UseMoveOutputBoundary outputBoundary;
    private final EndBattleInputBoundary endBattleInteractor;

    public UseMoveInteractor(UseMoveOutputBoundary outputBoundary,
                             EndBattleInputBoundary endBattleInteractor) {
        this.outputBoundary = outputBoundary;
        this.endBattleInteractor = endBattleInteractor;
    }

    @Override
    public void execute(UseMoveInputData inputData) {
        Battle battle = inputData.getBattle();
        Move move = inputData.getMove();

        if (move.getCurrentPp() <= 0) {
            outputBoundary.prepareFailView("Move has no PP left!");
            return;
        }

        Pokemon attacker = battle.getCurrentTurnPokemon();
        Pokemon defender = battle.getOpponentPokemon();
        PokemonTeam attackerTeam = battle.getCurrentTurnTeam();
        PokemonTeam defenderTeam = battle.getOpponentTeam();

        int defenderHpBefore = defender.getCurrentHP();

        BattleStats attackerStats = battle.getBattleStatsMap().get(attacker);
        BattleStats defenderStats = battle.getBattleStatsMap().get(defender);

        move.useMove(attackerTeam, defenderTeam, attackerStats, defenderStats);

        int damageDealt = defenderHpBefore - defender.getCurrentHP();

        StringBuilder message = new StringBuilder();
        message.append(attacker.getName()).append(" used ").append(move.getMoveName()).append("!\n");

        if (damageDealt > 0) {
            message.append("Dealt ").append(damageDealt).append(" damage!\n");
        }

        if (defender.getCurrentHP() <= 0) {
            message.append(defender.getName()).append(" fainted!\n");

            boolean hasSwitched = battle.handleFaintedPokemon(defenderTeam);

            if (hasSwitched) {
                message.append(defenderTeam.getActivePokemon().getName()).append(" was sent out!\n");
            }
        }

        // Check if battle ended - DELEGATE to EndBattle
        if (battle.checkBattleEnd()) {
            EndBattleInputData endBattleInput = new EndBattleInputData(battle);
            endBattleInteractor.execute(endBattleInput);
            return;
        }

        battle.switchTurn();

        UseMoveOutputData outputData = new UseMoveOutputData(
                battle, message.toString(),
                attacker, defender, move.getMoveName(), damageDealt
        );

        outputBoundary.prepareSuccessView(outputData);
    }
}