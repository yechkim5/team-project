package entity.moveyStuff;

import entity.BattleStats;
import entity.Move;
import entity.PokemonTeam;
import entity.StatType;

/**
 * Behavior for moves that modify stats (buff/debuff).
 * Examples: Swords Dance (raises Attack), Tail Whip (lowers Defense)
 */
public class StatModifierBehaviour implements MoveBehaviour {

    private final StatType statToModify;
    private final int stageChange;
    private final boolean targetsSelf;

    /**
     * Constructor for StatModifierBehaviour
     * @param statToModify Which stat to change (ATTACK, DEFENSE, etc.)
     * @param stageChange How many stages to change (-6 to +6)
     * @param targetsSelf true if affects user, false if affects target
     */
    public StatModifierBehaviour(StatType statToModify, int stageChange, boolean targetsSelf) {
        this.statToModify = statToModify;
        this.stageChange = stageChange;
        this.targetsSelf = targetsSelf;
    }

    @Override
    public void execute(Move move,
                        PokemonTeam userTeam,
                        PokemonTeam targetTeam,
                        BattleStats userBattleStats,
                        BattleStats targetBattleStats) {

        if (targetsSelf) {
            userBattleStats.modifyStat(statToModify, stageChange);
        } else {
            targetBattleStats.modifyStat(statToModify, stageChange);
        }
    }
}