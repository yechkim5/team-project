package view.demo_entity;

import java.util.List;

public class DemoPokemon {
    private final String name;
    private final String type;
    private final int maxHp;
    private final int currentHp;
    private final int attack;
    private final int defense;
    private final List<DemoMove> moves;

    public DemoPokemon(String name, String type, int maxHp, int currentHp, int attack, int defense, List<DemoMove> moves) {
        this.name = name;
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = currentHp;
        this.attack = attack;
        this.defense = defense;
        this.moves = moves;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }
    public java.util.List<DemoMove> getMoves() {
        return moves;
    }
}
