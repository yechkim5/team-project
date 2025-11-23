package view.demo_entity;

public class DemoMove {
    private final String name;
    private final int power;
    private final int maxPP;
    private final int currentPP;

    public DemoMove(String name, int power, int maxPP) {
        this(name, power, maxPP, maxPP);
    }

    public DemoMove(String name, int power, int maxPP, int currentPP) {
        this.name = name;
        this.power = power;
        this.maxPP = maxPP;
        this.currentPP = currentPP;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public int getMaxPP() {
        return maxPP;
    }

    public int getCurrentPP() {
        return currentPP;
    }
}
