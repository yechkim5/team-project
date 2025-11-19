package entity;

import java.util.HashMap;
import java.util.Map;

public class Battle {
    private final PokemonTeam team1;
    private final PokemonTeam team2;
    private final Map<Pokemon, BattleStats> battleStatsMap = new HashMap<>();

    public Battle(PokemonTeam team1, PokemonTeam team2) {
        this.team1 = team1;
        this.team2 = team2;

        for(Pokemon pokemon : team1.getTeam()){
            battleStatsMap.put(pokemon, pokemon.createBattleStats());
        }

        for(Pokemon pokemon : team2.getTeam()){
            battleStatsMap.put(pokemon, pokemon.createBattleStats());
        }
    }

}