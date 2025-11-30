package use_case.switch_pokemon;

import entity.Battle;
import entity.PokemonTeam;

public class SwitchPokemonOutputData {
    private final Battle battle;
    private final String oldPokemonName;
    private final String newPokemonName;
    private final PokemonTeam currentTeam;
    private final String message;

    public SwitchPokemonOutputData(Battle battle, String oldPokemonName,
                                   String newPokemonName, PokemonTeam currentTeam,
                                   String message) {
        this.battle = battle;
        this.oldPokemonName = oldPokemonName;
        this.newPokemonName = newPokemonName;
        this.currentTeam = currentTeam;
        this.message = message;
    }

    public Battle getBattle() {
        return battle;
    }

    public String getOldPokemonName() {
        return oldPokemonName;
    }

    public String getNewPokemonName() {
        return newPokemonName;
    }

    public PokemonTeam getCurrentTeam() {
        return currentTeam;
    }

    public String getMessage() {
        return message;
    }
}
