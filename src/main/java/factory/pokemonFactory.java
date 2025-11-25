package factory;
import java.util.*;
import java.io.IOException;

import entity.builder.MoveBuilder;
import entity.moveyStuff.MoveBehaviour;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import poke_api.pokemonFetcher;
import entity.*;
/**
 * Factory that creates a real Pokemon from the PokeAPI.
 * Usage: new pokemonFactory("pikachu").getPokemon()
 */
public class pokemonFactory {
    private final String pokeid;
    private final pokemonFetcher pf;
    private final JSONObject oj;
    private final moveBehaviourFactory moveBehaviourFactory;

    public pokemonFactory(String pokeid){
        this.pokeid = pokeid;
        this.pf = new pokemonFetcher();
        this.oj = pf.getPokemon(pokeid);
        this.moveBehaviourFactory = new moveBehaviourFactory();
    }

    /**
     * Creates and returns a fully built Pokemon with stats + 4 moves.
     */
    public Pokemon getPokemon() {
        // Get Pokemon name
        String name = pf.getPokemonName(oj);

        // Get types and convert String[] to List<String>
        String[] typesArray = pf.getPokemonType(oj);
        List<String> types = Arrays.asList(typesArray);

        // Get stats from API - note: API returns "special-attack" and "special-defense" with hyphens
        Map<String, Object> statsMap = pf.getPokemonStats(oj);

        // Build BaseLevelStats using builder pattern
        BaseLevelStats baseStats = new BaseLevelStats.BaseLevelStatsBuilder()
            .maxHp((Integer) statsMap.get("hp"))
            .attack((Integer) statsMap.get("attack"))
            .defense((Integer) statsMap.get("defense"))
            .specialAttack((Integer) statsMap.get("special-attack"))
            .specialDefense((Integer) statsMap.get("special-defense"))
            .speed((Integer) statsMap.get("speed"))
            .build();

        // Get sprite URLs
        String[] sprites = pf.getPokemonSprite(oj);
        String frontSpriteUrl = sprites[0];
        String backSpriteUrl = sprites[1];

        // Create Pokemon with proper constructor including sprite URLs
        Pokemon pokemon = new Pokemon(name, baseStats, types, frontSpriteUrl, backSpriteUrl);

        // Set moves (up to 10)
        List<Move> moves = movesFactory();
        Move[] movesArray = pokemon.getMoves();
        int movesToAdd = Math.min(10, moves.size());
        for (int i = 0; i < movesToAdd; i++) {
            movesArray[i] = moves.get(i);
        }

        return pokemon;
    }

    private List<Move> movesFactory(){
        List<Move> moveList = new ArrayList<>();

        try {
            HashMap<String, HashMap<String, Object>> moveMap = pf.getPokemonMoves(oj);

            // Iterate through moves (limit to first 10 for team selection)
            int count = 0;
            for (Map.Entry<String, HashMap<String, Object>> entry : moveMap.entrySet()) {
                if (count >= 10) break;

                String moveName = entry.getKey();
                HashMap<String, Object> moveInfo = entry.getValue();

                String moveType = (String) moveInfo.get("type");
                int pp = (Integer) moveInfo.get("pp");
                Object powerObj = moveInfo.get("power");
                int power = powerObj != null ? (Integer) powerObj : 0;

                // Note: Description not available in current API response structure
                // You may need to fetch it separately or use a default
                String description = "A Pokemon move"; // Placeholder

                // Get move behaviors - default to physical damage for now
                // In a full implementation, you'd fetch move category/class from API
                List<MoveBehaviour> behaviours = moveBehaviourFactory.getMoveBehaviours("damage", "physical");

                Move move = new MoveBuilder()
                    .setMoveName(moveName)
                    .setMoveType(moveType)
                    .setPp(pp)
                    .setMoveDescription(description)
                    .setMovePower(power)
                    .setMoveAccuracy(100) // Default accuracy, may need to fetch from API
                    .setMoveBehaviours(behaviours)
                    .createMove();

                moveList.add(move);
                count++;
            }
        } catch (IOException e) {
            System.err.println("Error fetching moves: " + e.getMessage());
        }

        return moveList;
    }
}
