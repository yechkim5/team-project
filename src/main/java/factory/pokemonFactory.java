package factory;

import entity.*;
import poke_api.pokemonFetcher;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory that creates a real Pokemon from the PokeAPI.
 * Usage: new pokemonFactory("pikachu").getPokemon()
 */
public class pokemonFactory {

    private final String pokeid;           // "pikachu", "25", etc.
    private final pokemonFetcher fetcher;
    private final JSONObject pokemonData;  // Raw JSON from PokeAPI

    public pokemonFactory(String pokeid) {
        this.pokeid = pokeid.toLowerCase();  // API is case-insensitive
        this.fetcher = new pokemonFetcher();
        this.pokemonData = fetcher.getPokemon(pokeid);
    }

    /**
     * Creates and returns a fully built Pokemon with stats + 4 moves.
     */
    public Pokemon getPokemon() {
        String name = fetcher.getPokemonName(pokemonData);
        List<String> types = fetcher.getPokemonType(pokemonData);  // Fixed: returns List

        Map<String, Integer> stats = fetcher.getPokemonStats(pokemonData);
        BaseLevelStats baseStats = new BaseLevelStats.Builder()
                .maxHp(stats.get("hp"))
                .attack(stats.get("attack"))
                .defense(stats.get("defense"))
                .specialAttack(stats.get("special-attack"))
                .specialDefense(stats.get("special-defense"))
                .speed(stats.get("speed"))
                .build();

        List<Move> moves = movesFactory();

        Pokemon pokemon = new Pokemon(name, baseStats, types);
        pokemon.setMoves(moves.toArray(new Move[0]));  // Set the 4 moves
        return pokemon;
    }

    /**
     * Gets up to 4 real moves from the API.
     * Returns a List<Move> with actual data.
     */
    public List<Move> movesFactory() {
        HashMap<String, HashMap<String, Object>> moveMap = fetcher.getPokemonMoves(pokemonData);
        List<Move> moveList = new ArrayList<>();

        // Limit to first 4 moves (most Pokémon have way more)
        int count = 0;
        for (String moveName : moveMap.keySet()) {
            if (count >= 4) break;

            HashMap<String, Object> info = moveMap.get(moveName);

            String name = moveName;
            String type = (String) info.get("type");
            Integer powerObj = (Integer) info.get("power");
            String power = powerObj != null ? powerObj.toString() : "—";
            int pp = (Integer) info.get("pp");
            String description = info.containsKey("description")
                    ? (String) info.get("description")
                    : "No description available.";

            // Handle accuracy: null means always hits
            Integer accuracy = info.get("accuracy") instanceof Integer
                    ? (Integer) info.get("accuracy")
                    : 100;

            Move move = new Move(name, type, pp, description, "physical", accuracy);
            // You may want to detect physical/special later
            moveList.add(move);
            count++;
        }

        return moveList;
    }
}