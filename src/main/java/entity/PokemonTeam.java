package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokemonTeam {
    //Suggestion from ChatGPT makes sense as these makes changing team size
    //later on significantly easier
    private static final int MAX_TEAM_SIZE = 6;

    //private keyword is for encapsulation. Initially I was suprised that the
    //final keyword was used but given that we can mutate lists we can still
    //change the pokemon team with the additional enforcement of the final keyword
    private final List<Pokemon> pokemons = new ArrayList<>();

    public void addPokemon(Pokemon pokemon) {
        if (pokemons.size() >= MAX_TEAM_SIZE) {
            throw new IllegalArgumentException("A team can only have 6 Pokémon.");
        }
        pokemons.add(pokemon);
    }

    public void removePokemon(Pokemon pokemon) {
        pokemons.remove(pokemon);
    }

    public void swapPositionInTeam(Pokemon p1, Pokemon p2) {
        int index1 = pokemons.indexOf(p1);
        int index2 = pokemons.indexOf(p2);

        //This suggestion is for error handling in the situation that either of
        //the pokemon is not in the team. That said, this method should only be
        //called using pokemon that are in the pokemons list.
        if (index1 == -1 || index2 == -1) {
            throw new IllegalArgumentException("Both Pokémon must be in the team to swap.");
        }

        // Do the swap
        pokemons.set(index1, p2);
        pokemons.set(index2, p1);
    }

    /**
     * Returns a safe, unmodifiable version of the team list.
     */
    public List<Pokemon> getTeam() {
        return Collections.unmodifiableList(pokemons);
    }

    /* Originally I was planning on having two PokemonTeam style classes to represent
    a pokemon team in battle and a pokemon team outside of battle; however, the behaviour
    of a pokemon team does not change in battle and thus the SRP is already satsified by
    this class.
     */
    public Pokemon getActivePokemon() {
        if (pokemons.isEmpty()) return null;
        return pokemons.get(0);
    }

    /**
     * Switch the Pokémon at index to be the active Pokémon (index 0).
     */
    public void switchActivePokemon(int newIndex) {
        if (newIndex < 0 || newIndex >= pokemons.size()) {
            throw new IllegalArgumentException("Invalid switch index.");
        }
        Collections.swap(pokemons, 0, newIndex);
    }

}

/* My version of the code, which is for the most part identical to the final
version suggested by ChatGPT save for some minor additions and changes for
safety and convenience

public class PokemonTeam {
    ArrayList<Pokemon> pokemons;

    public PokemonTeam() {
        this.pokemons = new ArrayList<>();
    }

    public void addPokemon(Pokemon pokemon) {
        if (pokemons.size() < 6)
            pokemons.add(pokemon);
        else throw new IllegalArgumentException("There can only be six pokemon on a team");
    }


    public void removePokemon(Pokemon pokemon) {
        pokemons.remove(pokemon);
    }

    public void swapPositionInTeam(Pokemon pokemon1, Pokemon pokemon2) {
        int index1 = pokemons.indexOf(pokemon1);
        int index2 = pokemons.indexOf(pokemon2);
        pokemons.set(index1, pokemon2);
        pokemons.set(index2, pokemon1);
    }
}
*/