package poke_api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


public class pokemonFetcher {

    public static void main(String[] args) throws IOException {
        pokemonFetcher pf = new pokemonFetcher();
        JSONObject oj = pf.getPokemon("charmander");
        System.out.println(pf.getPokemonName(oj));

<<<<<<< HEAD
        //System.out.println(pf.getPokemonMoves(oj));
        // System.out.println(pf.getPokemonType(oj));
        // System.out.println(Arrays.toString(pf.getPokemonSprite(oj)));
        // System.out.println(pf.getPokemonType(oj));
        System.out.println(pf.getPokemonStats(oj));
=======
        System.out.println(pf.getPokemonMoves(oj));
        // System.out.println(pf.getPokemonType(oj));
        // System.out.println(Arrays.toString(pf.getPokemonSprite(oj)));
>>>>>>> 7ebd430 (stash)
    }

    public final OkHttpClient client = new OkHttpClient();

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
            JSONObject json = new JSONObject(body);

            return json;


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
        HashMap<String, HashMap<String, Object>> move = new HashMap<>(4);

        for (int i = 0; i < moves.length(); i++) {

            JSONObject move_entry = moves.getJSONObject(i);
            JSONObject move_object = move_entry.getJSONObject("move");
            String url = move_object.getString("url");

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try  (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String body = response.body().string();
                JSONObject json = new JSONObject(body);

                HashMap<String, Object> move_info = new HashMap<>();
                int pp = json.getInt("pp");
                String name = json.getString("name");

                // nul for status moves
                Integer power = json.isNull("power") ? null : json.getInt("power");

                JSONObject typeObject = json.getJSONObject("type");
                String type = typeObject.getString("name");

                move_info.put("type", type);
                move_info.put("power", power);
                move_info.put("pp", pp);

                move.put(name, move_info);

            } catch (IOException e1){
                System.out.println("IOexception" + e1);
            }

        }
        return move;
    }

    public String getPokemonType(JSONObject pokemon) {
        JSONArray t = pokemon.getJSONArray("types");
        JSONObject type = t.getJSONObject(0);
         JSONObject typeObject = type.getJSONObject("type");
         return typeObject.getString("name");
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

