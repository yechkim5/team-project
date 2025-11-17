package entity;

import java.util.List;

/* By the lab of week eleven, the Pokemon class should be entirely complete so
that users can possess a Pokemon with all the properties outlined in the
project blueprint. This Pokemon class should emulate its functionality within
the Pokemon games and have the ability to interact with other Pokemon using Moves.
The effect of moves that cause ailments or change the stats of Pokemon will
be tracked through the battle class as opposed to the Pokemon instance individually.
 */

public class Pokemon {
    /** Entity representing Pokemon as seen in the Pokemon video game series.
     */
	private final String name;
    private int currentHP;
    private BaseLevelStats baseStats;
    private final List<String> types;
    private Move[] moves;

    public Pokemon(String name, BaseLevelStats stats,
                   List<String> types) {
        this.name = name;
        this.types = types;
        this.moves = new Move[4];
        this.baseStats = stats;
        this.currentHP = stats.getMaxHp();
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void takeDamage(int amount) {
        currentHP = Math.max(0, currentHP - amount);
    }

    public void heal(int amount) {
        currentHP = Math.min(baseStats.getMaxHp(), currentHP + amount);
    }

    public BattleStats createBattleStats() {
        return new BattleStats(baseStats);
    }

    public String getName() {
        return name;
    }

    public BaseLevelStats getBaseStats() {
        return baseStats;
    }

    public List<String> getTypes() {
        return types;
    }

    public Move[] getMoves() {
        return moves;
    }
}