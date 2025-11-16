package entity;

/**
 * Represents the mutable battle stats of a creature.
 * Fully enum-based: no switch/case needed.
 * Handles stage modifiers, multipliers, and HP.
 */
public enum StatType {
    ATTACK,
    DEFENSE,
    SPECIAL_ATTACK,
    SPECIAL_DEFENSE,
    SPEED,
    ACCURACY,
    EVASION;
}
