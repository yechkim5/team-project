package entity.moveyStuff;
import entity.BattleStats;
import entity.Move;
import entity.PokemonTeam;

import java.util.Random;

public class ForceSwitchBehaviour implements MoveBehaviour {
    public void execute(Move move,
                 PokemonTeam userTeam,
                 PokemonTeam targetTeam,
                 BattleStats userBattleStats,
                 BattleStats targetBattleStats){
        targetTeam.switchActivePokemon(getIndexOfNewActivePokemon(targetTeam));
    }

    //Move can only be called in battle so enemy team must have at least one Pokemon
    private int getIndexOfNewActivePokemon(PokemonTeam targetTeam) {
        if(targetTeam.getTeam().size()==1) return 0;
        else return new Random().nextInt(1, targetTeam.getTeam().size());
    }
}
