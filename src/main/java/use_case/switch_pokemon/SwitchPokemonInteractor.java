package use_case.switch_pokemon;

import entity.Battle;
import entity.Pokemon;
import entity.PokemonTeam;

public class SwitchPokemonInteractor implements SwitchPokemonInputBoundary {

    private final SwitchPokemonOutputBoundary outputBoundary;

    public SwitchPokemonInteractor(SwitchPokemonOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(SwitchPokemonInputData inputData) {
        Battle battle = inputData.getBattle();
        int newIndex = inputData.getNewPokemonIndex();

        PokemonTeam currentTeam = battle.getCurrentTurnTeam();

        // VALIDATION 1: Check index is valid
        if (newIndex < 0 || newIndex >= currentTeam.getTeam().size()) {
            outputBoundary.prepareFailView("Invalid Pokemon index!");
            return;
        }

        Pokemon newPokemon = currentTeam.getTeam().get(newIndex);

        // VALIDATION 2: Check Pokemon is alive
        if (newPokemon.getCurrentHP() <= 0) {
            outputBoundary.prepareFailView(newPokemon.getName() + " has fainted and cannot battle!");
            return;
        }

        // VALIDATION 3: Check not already active
        Pokemon currentActive = currentTeam.getActivePokemon();
        if (newPokemon == currentActive) {
            outputBoundary.prepareFailView(newPokemon.getName() + " is already in battle!");
            return;
        }

        // PERFORM THE SWITCH
        String oldPokemonName = currentActive.getName();
        currentTeam.switchActivePokemon(newIndex);

        // Build success message
        StringBuilder message = new StringBuilder();
        message.append(oldPokemonName).append(", come back!\n");
        message.append("Go, ").append(newPokemon.getName()).append("!\n");

        // Switch turns
        battle.switchTurn();

        String currentPlayerName = battle.isTeam1Turn() ? "Player 1" : "Player 2";
        message.append("\nIt's now ").append(currentPlayerName).append("'s turn.");

        // Return success
        SwitchPokemonOutputData outputData = new SwitchPokemonOutputData(
                battle,
                oldPokemonName,
                newPokemon.getName(),
                currentTeam,
                message.toString()
        );

        outputBoundary.prepareSuccessView(outputData);
    }
}