package entity.moveyStuff;

import entity.BattleStats;
import entity.Move;
import entity.PokemonTeam;

/**
 * Behavior for moves that heal the user Pokemon.
 * Examples: Recover, Synthesis, Roost
 */
public class HealBehaviour implements MoveBehaviour {

    private final double healPercentage;

    /**
     * Constructor for HealBehaviour
     * @param healPercentage Percentage of max HP to heal (e.g., 0.5 for 50%)
     */
    public HealBehaviour(double healPercentage) {
        this.healPercentage = healPercentage;
    }

    /**
     * Default constructor - heals 50% of max HP
     */
    public HealBehaviour() {
        this(0.5);
    }

    @Override
    public void execute(Move move,
                        PokemonTeam userTeam,
                        PokemonTeam targetTeam,
                        BattleStats userBattleStats,
                        BattleStats targetBattleStats) {

        int maxHp = userTeam.getActivePokemon().getBaseStats().getMaxHp();
        int healAmount = (int) Math.ceil(maxHp * healPercentage);

        userTeam.getActivePokemon().heal(healAmount);
    }
}