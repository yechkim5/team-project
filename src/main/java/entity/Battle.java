package entity;

import java.util.HashMap;
import java.util.Map;

public class Battle {
    private final PokemonTeam team1;
    private final PokemonTeam team2;
    private final Map<Pokemon, BattleStats> battleStatsMap = new HashMap<>();

    // ADD THESE THREE FIELDS TO IMPLEMENT THE LOGIC FOR use_case 2:
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


    // ========== ADD THESE METHODS FOR use_case 2 ==========

    /**
     * Check if battle should end
     */
    public boolean checkBattleEnd() {
        boolean team1HasPokemon = hasRemainingPokemon(team1);
        boolean team2HasPokemon = hasRemainingPokemon(team2);

        if (!team1HasPokemon) {
            battleOngoing = false;
            winner = team2;
            return true;
        } else if (!team2HasPokemon) {
            battleOngoing = false;
            winner = team1;
            return true;
        }
        return false;
    }

    /**
     * Check if team has any alive Pokémon
     */
    private boolean hasRemainingPokemon(PokemonTeam team) {
        for (Pokemon pokemon : team.getTeam()) {
            if (pokemon.getCurrentHP() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handle when active Pokémon faints
     */
    public boolean handleFaintedPokemon(PokemonTeam team) {
        if (team.getActivePokemon().getCurrentHP() <= 0) {
            for (int i = 0; i < team.getTeam().size(); i++) {
                if (team.getTeam().get(i).getCurrentHP() > 0) {
                    team.switchActivePokemon(i);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Get current turn's Pokémon
     */
    public Pokemon getCurrentTurnPokemon() {
        return isTeam1Turn ? team1.getActivePokemon() : team2.getActivePokemon();
    }

    /**
     * Get opponent's Pokémon
     */
    public Pokemon getOpponentPokemon() {
        return isTeam1Turn ? team2.getActivePokemon() : team1.getActivePokemon();
    }

    /**
     * Get current turn's team
     */
    public PokemonTeam getCurrentTurnTeam() {
        return isTeam1Turn ? team1 : team2;
    }

    /**
     * Get opponent's team
     */
    public PokemonTeam getOpponentTeam() {
        return isTeam1Turn ? team2 : team1;
    }

    /**
     * Switch turns
     */
    public void switchTurn() {
        isTeam1Turn = !isTeam1Turn;
    }

    /**
     * Manually end battle
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
    public Map<Pokemon, BattleStats> getBattleStatsMap() {
        return battleStatsMap;
    }
}