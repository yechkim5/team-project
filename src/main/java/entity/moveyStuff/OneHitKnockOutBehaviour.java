package entity.moveyStuff;

import entity.BattleStats;
import entity.Move;
import entity.PokemonTeam;

public class OneHitKnockOutBehaviour extends DamageBehaviour {

    @Override
    public void execute(Move move,
                        PokemonTeam userTeam,
                        PokemonTeam targetTeam,
                        BattleStats userBattleStats,
                        BattleStats targetBattleStats) {
        super.execute(move, userTeam, targetTeam, userBattleStats, targetBattleStats);
    }

    @Override
    protected int getBaseDamage(Move move,
                                PokemonTeam userTeam,
                                PokemonTeam targetTeam,
                                BattleStats userBattleStats,
                                BattleStats targetBattleStats){
        return targetTeam.getActivePokemon().getBaseStats().getMaxHp();
    }
}
