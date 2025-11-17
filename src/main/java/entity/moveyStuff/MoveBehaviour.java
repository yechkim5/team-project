package entity.moveyStuff;

import entity.BattleStats;
import entity.Move;
import entity.Pokemon;

/* This is an interface that will be used as the wellspring for a strategy
design pattern. The number of possible behaviours will be around 14 to account
for all the differnt types listed in the API. See move class for greater detail.
 */
public interface MoveBehaviour {
    void execute(Move move,
                 Pokemon user,
                 Pokemon target,
                 BattleStats userBattleStats,
                 BattleStats targetBattleStats);
}
