package use_case.switch_pokemon;

import entity.Battle;

public class SwitchPokemonInputData {
    private final Battle battle;
    private final int newPokemonIndex;

    public SwitchPokemonInputData(Battle battle, int newPokemonIndex) {
        this.battle = battle;
        this.newPokemonIndex = newPokemonIndex;
    }

    public Battle getBattle() {
        return battle;
    }

    public int getNewPokemonIndex() {
        return newPokemonIndex;
    }
}