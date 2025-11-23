package factory;

import org.json.JSONObject;
import poke_api.pokemonFetcher;

public class moveFactory {
    String moveID;
    pokemonFetcher pf;
    JSONObject oj;

    public moveFactory(String moveID) {
        this.moveID = moveID;
        this.pf = new pokemonFetcher();
        this.oj = pf.getPokemon(moveID);

    }

    
}


