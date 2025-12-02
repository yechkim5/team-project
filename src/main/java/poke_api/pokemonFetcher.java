package poke_api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


public class pokemonFetcher {

    public final OkHttpClient client = new OkHttpClient();
    public final int pokemonMaxMoves = 4;

    public JSONObject getPokemon(String id) {
        // Private class just to get the raw pokemon response object
        // other classes will use this JSON object to get more information

        Request request = new Request.Builder()
                .url("https://pokeapi.co/api/v2/pokemon/" + id)
                .build();
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String body = response.body().string();
            return new JSONObject(body);

        } catch (IOException e) {
            System.out.println("IOexception" + e);
            return new JSONObject();
            }
        }

    public String getPokemonName(JSONObject pokemon) {
        return pokemon.getString("name");
    }

    public HashMap<String, HashMap<String, Object>> getPokemonMoves(JSONObject pokemon) throws IOException {
        JSONArray moves = pokemon.getJSONArray("moves");
        HashMap<String, HashMap<String, Object>> move = new HashMap<>(pokemonMaxMoves);

        for (int i = 0; i < moves.length(); i++) {

            JSONObject moveEntry = moves.getJSONObject(i);
            JSONObject moveObject = moveEntry.getJSONObject("move");
            String url = moveObject.getString("url");

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try  (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String body = response.body().string();
                JSONObject json = new JSONObject(body);

                HashMap<String, Object> moveInfo = new HashMap<>();
                int pp = json.getInt("pp");
                String name = json.getString("name");

                // nul for status moves
                Integer power = json.isNull("power") ? null : json.getInt("power");

                JSONObject typeObject = json.getJSONObject("type");
                String type = typeObject.getString("name");

                moveInfo.put("type", type);
                moveInfo.put("power", power);
                moveInfo.put("pp", pp);

                move.put(name, moveInfo);

            } catch (IOException e1) {
                System.out.println("IOexception" + e1);
            }

        }
        return move;
    }

    public String[] getPokemonType(JSONObject pokemon) {
        JSONArray t = pokemon.getJSONArray("types");
        String[] types = new String[t.length()];
        
        for (int i = 0; i < t.length(); i++) {
            types[i] = t.getJSONObject(i).getJSONObject("type").getString("name");
        }
        return types;
    }

    public String[] getPokemonSprite(JSONObject pokemon) {
        // returns [front, back] pngs for pokemon sprite
        JSONObject spriteObj = pokemon.getJSONObject("sprites");
        String front = spriteObj.getString("front_default");
        String back = spriteObj.getString("back_default");
        return new String[] {front, back};
    }

    public Map<String, Object> getPokemonStats(JSONObject pokemon) {
        HashMap<String, Object> stats = new HashMap<>();
        JSONArray statsArray = pokemon.getJSONArray("stats");

        for (int i = 0; i < statsArray.length(); i++) {
            JSONObject stat = statsArray.getJSONObject(i);
            int baseStat = stat.getInt("base_stat");
            
            JSONObject hpObject = stat.getJSONObject("stat");
            String statName = hpObject.getString("name");

            stats.put(statName, baseStat);

        }

        return stats;

    }

}

