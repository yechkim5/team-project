package factory;

import entity.moveyStuff.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class moveBehaviourFactory {
    public List<MoveBehaviour> getMoveBehaviours(String moveCategory,
                                                 String moveDamageClass) {
        List<MoveBehaviour> moveBehaviours = new ArrayList<>();

        switch (moveCategory) {
            case "damage":
                moveBehaviours.add(getDamageClassBehaviour(moveDamageClass));
                break;
            case "ailment":
                throw new IllegalArgumentException("This damage category has not been implemented");
            case "net-good-stats":
                throw new IllegalArgumentException("This damage category has not been implemented");
            case "heal":
                throw new IllegalArgumentException("This damage category has not been implemented");
            case "damage+ailment":
                moveBehaviours.add(getDamageClassBehaviour(moveDamageClass));
                throw new IllegalArgumentException("This damage category has not been implemented");
            case "swagger":
                moveBehaviours.add(getDamageClassBehaviour(moveDamageClass));
                throw new IllegalArgumentException("This damage category has not been implemented");
            case "damage+lower":
                moveBehaviours.add(getDamageClassBehaviour(moveDamageClass));
                throw new IllegalArgumentException("This damage category has not been implemented");
            case "damage+raise":
                moveBehaviours.add(getDamageClassBehaviour(moveDamageClass));
                throw new IllegalArgumentException("This damage category has not been implemented");
            case "damage+heal":
                moveBehaviours.add(getDamageClassBehaviour(moveDamageClass));
                throw new IllegalArgumentException("This damage category has not been implemented");
            case "ohko":
                moveBehaviours.add(new OneHitKnockOutBehaviour());
                break;
            case "whole-field-effect":
                throw new IllegalArgumentException("This damage category has not been implemented");
            case "field-effect":
                throw new IllegalArgumentException("This damage category has not been implemented");
            case "force-switch":
                moveBehaviours.add(new ForceSwitchBehaviour());
                break;
            case "unique":
                throw new IllegalArgumentException("This damage category has not been implemented");
        }
        return moveBehaviours;
    }

    private DamageBehaviour getDamageClassBehaviour(String moveDamageClass) {
        if (Objects.equals(moveDamageClass, "physical")) {
            return new PhysicalDamageBehaviour();
        } else if (Objects.equals(moveDamageClass, "special")) {
            return new SpecialDamageBehaviour();
        } else {
            throw new IllegalArgumentException("Move Category not available");
        }
    }

}
