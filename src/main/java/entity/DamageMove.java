package entity;

/*
    Within Pokemon there are three major move classifications: Special, Physical, and Status.
The difference between these classifications comes down to what stats are used to
calculate the effect the move has. For example, a status move does no damage
and so damage and defense against damage is not considered when considering its effect.
    Additionally, there are various hybrid moves that combine properties from two of
the major move types above like a move that damages a target and lowers one of their
stats; however, these moves are all still said to belong to a single classification
    Within the Pokemon API, every single move has one these three classifications:
1 - status, 2 - physical, and 3 - special. Additionally, there exist 14
categories representing every single possible combination of effects that a move
can have on a targeted Pokemon as well as the user's Pokemon. The categories
are defined as follows:
    -damage: Any move that inflicts damage
    -ailment: A move that inflicts status effect without damage
    -net-good-stats: Affects stats of in play Pokemon in way that is beneficial
                     to the user. This could be raising user Pokemon stats
                     or lowering target Pokemon stats.
    -heal: A move that heals the user. In other words, a move that adds
           some integer value to the user pokemons health attribute.
    -damage+ailment: A move that inflicts both status ailment and damage
    -swagger: Inflicts status ailment but also raises stats of target.
              For example, a move that poisons and enemy but also increases their attack.
    -damage+lower: A move that deals damage and changes target stats. For example,
                   an attack that deals 20 damage and reduce the target's defense stat
    -damage+raise: A move that deals damage to a target and changes one or more
                   of its user's stats. For example, a move that deals damage and
                   raise or lowers the users attack would fall into this category
    -damage+heal: A move that deals damage and also heals the user based on the damage
                  dealt. Think of it as a life leech move.
    -One-hit KO: Any move that deals damage equivalent to the max HP of its target.
                 As a side note, it is essential accuracy is taken into account
                 So that this move cannot always land and trivialize the game.
    -whole-field-effect: These are moves that effect all Pokemon in play. That said,
                         we may not implement moves of this category as many of them
                         require additional game mechanics beyond the scope of this
                         project.
    -Effect on one side of the field: Self explanatory, may not include for same
                                      reasoning as above.
    -force-switch: Forces target to switch to bench.
    -unique: Moves that do not fall into any of the above categories. By the
             Pokemon API, there are supposedly 105 unique moves. This presents
             an extreme challenge as there is unlikely to be a single pattern
             that is share by all moves that fall into this category

    As you might have felt by now, there is an overwhelming number of categories
that any given move can fall into. Even worse, many moves do not fall into any category
at all and are considered unique. So, I believe the most feasible solution for this problem is to limit
the available Pokemon to 151 or to only Pokemon only from the first Pokemon game.
This will consequently limit the number of moves I need to account for to 165 and
ensure that I am able to implement all unique moves. It will exclude all moves
within the swagger category, damage+raise category, almost all moves in the haze
category.
    Thus, during week ten I will complete an abstract class move, an interface
for stats change functionality, and all classes that inherit or extend the
aforementioned to account for moves belonging to all categories. By Monday of
week eleven, a user should functionally be able to use all Pokemon moves available
in generation one of Pokemon to battle another team. For the time being, I will
focus less on the architecture of our project as we can refactor our code once
it is functional.

If the accuracy of a move is null, there is a 100% chance that it hits.

If the power of a move is null and the move is not status class, the move does
not depend on stats but  a fixed amount or variable amount dependent on some other value.
All moves of status class have power of null.
 */
/* This class is just for recording data stuff right now
import java.util.HashMap;

public class DamageMove{
    /**Class representing a move that can be made by a Pokemon
     * Class is kept abstract to allow for distinction of Special Attacks from Physical Attacks
     *
    private final String moveName;
    private final String moveType;
    private final String moveDescription;
    private final int movePower;
    private int pp;
    private final HashMap<String, Integer[]> statAilmentChanceChange;
    private boolean multitarget;
    private final int moveAccuracy;

    public DamageMove(String moveName, String moveType, String moveDescription, int power, int pp) {
        this.moveName = moveName;
        this.moveDescription = moveDescription;
        this.moveType = moveType;
        this.movePower = power;
        this.pp = pp;
        moveAccuracy = 0;
        statAilmentChanceChange = new HashMap<>();
    }

    public boolean canUseMove(){return pp > 0;}

}*/