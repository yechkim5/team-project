package entity;

import java.util.HashMap;
import java.util.Map;

public class Battle {
    private final PokemonTeam team1;
    private final PokemonTeam team2;
    private final Map<Pokemon, BattleStats> battleStatsMap = new HashMap<>();

    // Turn tracking
    //mannat added this
    private boolean isTeam1Turn = true;
    private boolean battleOngoing = true;
    private PokemonTeam winner = null;

    public Battle(PokemonTeam team1, PokemonTeam team2) {
        this.team1 = team1;
        this.team2 = team2;

        for(Pokemon pokemon : team1.getTeam()){
            battleStatsMap.put(pokemon, pokemon.createBattleStats());
        }

        for(Pokemon pokemon : team2.getTeam()){
            battleStatsMap.put(pokemon, pokemon.createBattleStats());
        }
    }




    // ========== ADD ALL THESE METHODS by mannat ==========

    /**
     * Check if battle should end
     * Battle ends when one team has no Pokémon with HP > 0
     *
     * @return true if battle ended, false if it continues
     */
    public boolean checkBattleEnd() {
        boolean team1HasPokemon = hasRemainingPokemon(team1);
        boolean team2HasPokemon = hasRemainingPokemon(team2);

        if (!team1HasPokemon) {
            // Team 1 lost (no Pokémon left)
            battleOngoing = false;
            winner = team2;
            return true;
        } else if (!team2HasPokemon) {
            // Team 2 lost (no Pokémon left)
            battleOngoing = false;
            winner = team1;
            return true;
        }

        // Battle continues
        return false;
    }

    /**
     * Check if a team has any Pokémon that can still fight
     *
     * @param team The team to check
     * @return true if at least one Pokémon has HP > 0
     */
    private boolean hasRemainingPokemon(PokemonTeam team) {
        for (Pokemon pokemon : team.getTeam()) {
            if (pokemon.getCurrentHP() > 0) {
                return true; // Found at least one alive Pokémon
            }
        }
        return false; // All Pokémon fainted
    }

    /**
     * Handle when active Pokémon faints
     * Automatically switches to next alive Pokémon
     *
     * @param team The team whose Pokémon might have fainted
     * @return true if switch successful, false if no Pokémon left
     */
    public boolean handleFaintedPokemon(PokemonTeam team) {
        // Check if current active Pokémon is fainted
        if (team.getActivePokemon().getCurrentHP() <= 0) {
            // Search for first alive Pokémon
            for (int i = 0; i < team.getTeam().size(); i++) {
                if (team.getTeam().get(i).getCurrentHP() > 0) {
                    // Found one! Switch to it
                    team.switchActivePokemon(i);
                    return true;
                }
            }
            // No alive Pokémon found - team lost
            return false;
        }
        // Active Pokémon is still alive
        return false;
    }

    /**
     * Get the Pokémon whose turn it is right now
     *
     * @return The active Pokémon of the current turn's team
     */
    public Pokemon getCurrentTurnPokemon() {
        return isTeam1Turn ? team1.getActivePokemon() : team2.getActivePokemon();
    }

    /**
     * Get the opponent's current Pokémon
     *
     * @return The active Pokémon of the non-current-turn team
     */
    public Pokemon getOpponentPokemon() {
        return isTeam1Turn ? team2.getActivePokemon() : team1.getActivePokemon();
    }

    /**
     * Get the team whose turn it is
     *
     * @return Current turn's team
     */
    public PokemonTeam getCurrentTurnTeam() {
        return isTeam1Turn ? team1 : team2;
    }

    /**
     * Get the opposing team
     *
     * @return Opponent's team
     */
    public PokemonTeam getOpponentTeam() {
        return isTeam1Turn ? team2 : team1;
    }

    /**
     * Switch to the other team's turn
     */
    public void switchTurn() {
        isTeam1Turn = !isTeam1Turn;
    }

    /**
     * Manually end the battle
     * Used for forfeit or error cases
     */
    public void endBattle() {
        battleOngoing = false;
    }

    // ========== ADD THESE GETTERS ==========

    public PokemonTeam getTeam1() { return team1; }
    public PokemonTeam getTeam2() { return team2; }
    public boolean isTeam1Turn() { return isTeam1Turn; }
    public boolean isBattleOngoing() { return battleOngoing; }
    public PokemonTeam getWinner() { return winner; }

    public BattleStats getBattleStats(Pokemon pokemon) {
        return battleStatsMap.get(pokemon);
    }

}