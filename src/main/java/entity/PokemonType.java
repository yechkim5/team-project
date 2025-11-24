package entity;

import java.util.List;
import java.util.Map;

public enum PokemonType {
    NORMAL(Map.of(
            "rock", 0.5, "ghost", 0.0, "steel", 0.5
    )),

    FIRE(Map.of(
            "fire", 0.5, "water", 0.5, "grass", 2.0, "ice", 2.0,
            "bug", 2.0, "rock", 0.5, "dragon", 0.5, "steel", 2.0
    )),

    WATER(Map.of(
            "fire", 2.0, "water", 0.5, "grass", 0.5,
            "ground", 2.0, "rock", 2.0, "dragon", 0.5
    )),

    ELECTRIC(Map.of(
            "water", 2.0, "electric", 0.5, "grass", 0.5,
            "ground", 0.0, "flying", 2.0, "dragon", 0.5
    )),

    GRASS(Map.of(
            "fire", 0.5, "water", 2.0, "grass", 0.5, "poison", 0.5,
            "ground", 2.0, "flying", 0.5, "bug", 0.5,
            "rock", 2.0, "dragon", 0.5, "steel", 0.5
    )),

    ICE(Map.of(
            "fire", 0.5, "water", 0.5, "ice", 0.5, "ground", 2.0,
            "flying", 2.0, "dragon", 2.0, "steel", 0.5
    )),

    FIGHTING(Map.of(
            "normal", 2.0, "ice", 2.0, "rock", 2.0, "dark", 2.0, "steel", 2.0,
            "poison", 0.5, "flying", 0.5, "psychic", 0.5, "bug", 0.5,
            "ghost", 0.0
    )),

    POISON(Map.of(
            "grass", 2.0, "fairy", 2.0, "poison", 0.5, "ground", 0.5,
            "rock", 0.5, "ghost", 0.5, "steel", 0.0
    )),

    GROUND(Map.of(
            "fire", 2.0, "electric", 2.0, "grass", 0.5, "poison", 2.0,
            "rock", 2.0, "bug", 0.5, "flying", 0.0, "steel", 2.0
    )),

    FLYING(Map.of(
            "electric", 0.5, "grass", 2.0, "fighting", 2.0, "bug", 2.0,
            "rock", 0.5, "steel", 0.5
    )),

    PSYCHIC(Map.of(
            "fighting", 2.0, "poison", 2.0, "psychic", 0.5,
            "steel", 0.5, "dark", 0.0
    )),

    BUG(Map.of(
            "grass", 2.0, "psychic", 2.0, "dark", 2.0,
            "fire", 0.5, "fighting", 0.5, "poison", 0.5,
            "flying", 0.5, "ghost", 0.5, "steel", 0.5, "fairy", 0.5
    )),

    ROCK(Map.of(
            "fire", 2.0, "ice", 2.0, "flying", 2.0, "bug", 2.0,
            "fighting", 0.5, "ground", 0.5, "steel", 0.5
    )),

    GHOST(Map.of(
            "ghost", 2.0, "psychic", 2.0, "dark", 0.5, "normal", 0.0
    )),

    DRAGON(Map.of(
            "dragon", 2.0, "steel", 0.5, "fairy", 0.0
    )),

    DARK(Map.of(
            "psychic", 2.0, "ghost", 2.0,
            "fighting", 0.5, "dark", 0.5, "fairy", 0.5
    )),

    STEEL(Map.of(
            "fire", 0.5, "water", 0.5, "electric", 0.5,
            "ice", 2.0, "rock", 2.0, "fairy", 2.0, "steel", 0.5
    )),

    FAIRY(Map.of(
            "fighting", 2.0, "dragon", 2.0, "dark", 2.0,
            "fire", 0.5, "poison", 0.5, "steel", 0.5
    ));

    private final Map<String, Double> effectiveness;

    PokemonType(Map<String, Double> effectiveness) {
        this.effectiveness = effectiveness;
    }


    //Add STAB modifier if you have time
    public double getEffectivenessAgainst(List<String> targetTypes) {
        double modifier = 1;
        for (String targetType : targetTypes) {
            modifier *= effectiveness.get(targetType);
        }
        return modifier;
    }
}
