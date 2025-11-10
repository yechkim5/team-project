package poke_api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class pokemonFetcher {
    private final OkHttpClient client = new OkHttpClient();

    private JSONObject getPokemon(String id) throws IOException {
        // Private class just to get the raw pokemon response object
        // other classes will use the JSON object to get more information

        Request request = new Request.Builder()
                .url("https://pokeapi.co/api/v2/ability/" + id)
                .build();
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String body = response.body().string();
            JSONObject json = new JSONObject(body);

            if (!json.getString("status").equals("success")) {
                throw new IOException("Unexpected status " + json.getString("status"));
            }

            return json;


    } catch (IOException e) {
        throw new IOException(e);
        }
    }

    private String getPokemonName(JSONObject pokemon) {
        return pokemon.getString("name");
    }

    private HashMap<String, String> getPokemonMoves(JSONObject pokemon) {
        JSONArray moves = pokemon.getJSONArray("moves");
        HashMap<String, String> move = new HashMap<>(4, 4);
        for (int i = 0; i < moves.length(); i++) {
            String url = moves.getJSONObject(i).getString("url");
        }
        return 1;
    }
}

