package view;

import view.demo_entity.DemoPokemon;
import view.demo_entity.DemoMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemoData {

    public static DemoPokemon createDemoPokemon() {
        List<DemoMove> moves = Arrays.asList(
                new DemoMove("Thunderbolt", 90, 15),
                new DemoMove("Quick Attack", 40, 30),
                new DemoMove("Iron Tail", 100, 15),
                new DemoMove("Volt Tackle", 120, 5)
        );

        return new DemoPokemon(
                "Pikachu",
                "Electric",
                100,
                100,
                55,
                40,
                moves
        );
    }

    public static DemoPokemon createDemoEnemy() {
        List<DemoMove> moves = Arrays.asList(
                new DemoMove("Flamethrower", 90, 55),
                new DemoMove("Slash", 70, 20)
        );
        return new DemoPokemon(
                "Charizard",
                "Fire/Flying",
                120,
                90,
                70,
                50,
                moves
        );
    }
    public static String[] getSpeciesList() {
        return new String[]{
                "Pikachu", "Gengar", "Lucario", "Rayquaza", "Ho-Oh", "Lugia"
        };
    }

    public static DemoPokemon createsByName(String name){
        if("Pikachu".equals(name)){
            return createDemoPokemon();
        }
        if ("Charizard".equals(name)){
            return createDemoEnemy();
        }
        List<DemoMove> moves = Arrays.asList(
                new DemoMove("Tackle", 40, 35),
                new DemoMove("Growl", 0, 40)
        );

        return new DemoPokemon(name, "Normal", 100, 100, 50, 50, moves);
    }

}
