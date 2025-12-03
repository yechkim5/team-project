package poke_api;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface Fetcher {
    JSONObject getPokemon(String id);
    String getPokemonName(JSONObject pokemon);
    HashMap<String, HashMap<String, Object>> getPokemonMoves(JSONObject pokemon) throws IOException;
    String[] getPokemonType(JSONObject pokemon);
    String[] getPokemonSprite(JSONObject pokemon);
    Map<String, Object> getPokemonStats(JSONObject pokemon);

}
