package interface_adapter.switch_pokemon;

import entity.Battle;
import use_case.switch_pokemon.SwitchPokemonInputBoundary;
import use_case.switch_pokemon.SwitchPokemonInputData;

public class SwitchPokemonController {
    private final SwitchPokemonInputBoundary switchPokemonInteractor;

    public SwitchPokemonController(SwitchPokemonInputBoundary switchPokemonInteractor) {
        this.switchPokemonInteractor = switchPokemonInteractor;
    }

    public void switchPokemon(Battle battle, int newPokemonIndex) {
        SwitchPokemonInputData inputData = new SwitchPokemonInputData(battle, newPokemonIndex);
        switchPokemonInteractor.execute(inputData);
    }
}