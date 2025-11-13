package factory;
import java.util.*;

import org.json.JSONObject;

import entity.*;
import poke_api.*;

public class pokemonFactory {
    
    
    String pokeid;
        pokemonFetcher pf;
        JSONObject oj;

    public pokemonFactory(String pokeid){
        this.pokeid = pokeid;
        this.pf = new pokemonFetcher();
        this.oj = pf.getPokemon(pokeid);
        
    }

    public Pokemon getPokemon() {
        
        String name = pf.getPokemonName(oj);
        String types = pf.getPokemonType(oj);

        Map<String, Integer> stats = pf.getPokemonStats(oj);
        int hp = stats.get("hp");
        int attack = stats.get("attack");
        int defense = stats.get("defense");
        int specialAttack = stats.get("specialAttack");
        int specialDefense = stats.get("specialDefense");
        int speed = stats.get("speed");

        return new Pokemon(name, hp, attack, defense, specialAttack, specialDefense, speed, types, movesFactory());     

        
    }

    public List<Move> movesFactory(){
    HashMap<String, HashMap<String, Object>> moveMap = pf.getPokemonMoves(oj);
    ArrayList<Move> moveList = new ArrayList<>();

    ArrayList<String> moves = new ArrayList<>(moveMap.keySet());
    for (int i = 0; i < moves.size(); i++){
        String name = moves.get(i);
        String description = (String) moveMap.get(i).get("description");
        String power = (String) moveMap.get(i).get("power");
        int pp = (int) moveMap.get(i).get("pp");
        String type = (String) moveMap.get(i).get("type");
        Move move = new Move(name, type, description, power, pp);
    }

        
    }

    
    
}
