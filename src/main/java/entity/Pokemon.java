package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pokemon {
    /** Entity representing Pokemon as seen in the Pokemon video game series.
     */
	private final String name;
    private HashMap<String, Integer> stats;
    private final List<String> types;
    private Move[] moves;

    /** Constructor for Pokemon
     * @param name the species name of the Pokemon.
     * @param hp integer representing hit points of the Pokemon
     * @param attack integer influencing power of physical attacks
     * @param defense integer influencing defense against physical attacks
     * @param specialAttack integer influencing power of special attacks
     * @param specialDefense integer influencing power defense against special attacks
     * @param speed integer used to determine turn order during battle
     * @param types string(s) representing Pokemon's type(s). Influences defense against certain attacks
     * @param moves list of type <Attack> representing the current move this Pokemon can use.
     *              Max number of possible moves is set four.
     */
    public Pokemon(String name,
                   int hp, int attack, int defense,
                   int specialAttack, int specialDefense, int speed,
                   List<String> types, Move[] moves) {
        this.name = name;
        this.types = new ArrayList<String>();
        this.moves = new Move[4];
        this.stats = new HashMap<String, Integer>(6);

        stats.put("hp", hp); stats.put("attack", attack);
        stats.put("defense", defense); stats.put("specialAttack", specialAttack);
        stats.put("specialDefense", specialDefense); stats.put("speed", speed);
    }

    public boolean isAlive() {
        return stats.get("hp") > 0;
    }

    //Requires implementation of move and potential clarifying what moves do.
    //public boolean UseMove(Attack move){}

    //public boolean CanUseMove(Attack move){}

    public String getName() {
        return name;
    }

    public HashMap<String, Integer> getStats() {
        return stats;
    }

    public List<String> getTypes() {
        return types;
    }

    public Move[] getMoves() {
        return moves;
    }
}