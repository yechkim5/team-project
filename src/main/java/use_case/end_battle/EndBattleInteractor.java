package use_case.end_battle;

import entity.Battle;
import entity.Pokemon;
import entity.PokemonTeam;

public class EndBattleInteractor implements EndBattleInputBoundary {

    private final EndBattleOutputBoundary outputBoundary;

    public EndBattleInteractor(EndBattleOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(EndBattleInputData inputData) {
        Battle battle = inputData.getBattle();

        if (!battle.checkBattleEnd()) {
            outputBoundary.prepareFailView("Battle is not over yet!");
            return;
        }

        PokemonTeam winner = battle.getWinner();
        if (winner == null) {
            outputBoundary.prepareFailView("No winner determined!");
            return;
        }

        String winnerName = (winner == battle.getTeam1()) ? "Player 1" : "Player 2";
        int remainingPokemon = countAlivePokemon(winner);

        StringBuilder message = new StringBuilder();
        message.append("🎉 BATTLE OVER! 🎉\n\n");
        message.append(winnerName).append(" WINS!\n\n");
        message.append("Remaining Pokemon: ").append(remainingPokemon).append("/6\n");

        EndBattleOutputData outputData = new EndBattleOutputData(
                battle,
                winner,
                winnerName,
                remainingPokemon,
                message.toString()
        );

        outputBoundary.prepareSuccessView(outputData);
    }

    private int countAlivePokemon(PokemonTeam team) {
        int count = 0;
        for (Pokemon pokemon : team.getTeam()) {
            if (pokemon.getCurrentHP() > 0) {
                count++;
            }
        }
        return count;
    }
}
