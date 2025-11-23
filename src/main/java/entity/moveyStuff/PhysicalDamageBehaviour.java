package entity.moveyStuff;

import entity.BattleStats;
import entity.Move;
import entity.PokemonTeam;
import entity.StatType;

public class PhysicalDamageBehaviour extends DamageBehaviour {
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
        return (int)(2* move.getMovePower() *
                (userBattleStats.getStat(StatType.ATTACK)/
                        targetBattleStats.getStat(StatType.DEFENSE))/50 +2);
    }
}
