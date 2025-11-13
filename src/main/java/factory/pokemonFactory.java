package factory;
import org.json.JSONObject;

import entity.*;
import poke_api.*;

public class pokemonFactory {
    String pokeid;

    public pokemonFactory(String pokeid){
        this.pokeid = pokeid;
        
    }

    public Pokemon getPokemon() {
        pokemonFetcher pf = new pokemonFetcher();
        JSONObject oj = pf.getPokemon("charmander");
        String name = pf.getPokemonName(oj);
    }
    
}
