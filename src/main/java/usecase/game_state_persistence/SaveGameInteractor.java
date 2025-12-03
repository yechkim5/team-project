
package usecase.game_state_persistence;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.*;

public class SaveGameInteractor {

    public static JSONObject toJson(GameState state) {
        final JSONObject json = new JSONObject();
        json.put("currentScreen", state.currentScreen().name());
        json.put("activeTeamSelector", state.activeTeamSelector().name());
        json.put("player1Team", toJsonTeam(state.player1Team()));
        json.put("player2Team", toJsonTeam(state.player2Team()));
        if (state.battlePhase() != null) {
            json.put("battlePhase", toJsonBattlePhase(state.battlePhase()));
        }
        json.put("currentTowerLevel", state.currentTowerLevel());
        json.put("highScore", state.highScore());
        return json;
    }

    public static GameState fromJson(JSONObject json) {
        final GameState.Screen screen = GameState.Screen.valueOf(json.getString("currentScreen"));
        final GameState.Player selector = GameState.Player.valueOf(json.getString("activeTeamSelector"));
        final PokemonTeam team1 = fromJsonTeam(json.getJSONObject("player1Team"));
        final PokemonTeam team2 = fromJsonTeam(json.getJSONObject("player2Team"));
        GameState.BattlePhase phase = null;
        if (json.has("battlePhase")) {
            phase = fromJsonBattlePhase(json.getJSONObject("battlePhase"));
        }
        final int towerLevel = json.getInt("currentTowerLevel");
        final int highScore = json.getInt("highScore");
        return new GameState(screen, selector, team1, team2, phase, towerLevel, highScore);
    }

    public static JSONObject toJsonTeam(PokemonTeam team) {
        final JSONObject jsonTeam = new JSONObject();
        JSONArray pokemons = new JSONArray();
        for (Pokemon p : team.getTeam()) {
            pokemons.put(toJsonPokemon(p));
        }
        jsonTeam.put("pokemons", pokemons);
        return jsonTeam;
    }

    public static PokemonTeam fromJsonTeam(JSONObject jsonTeam) {
        final PokemonTeam team = new PokemonTeam();
        JSONArray pokemons = jsonTeam.getJSONArray("pokemons");
        for (int i = 0; i < pokemons.length(); i++) {
            team.addPokemon(fromJsonPokemon(pokemons.getJSONObject(i)));
        }
        return team;
    }

    public static JSONObject toJsonPokemon(Pokemon pokemon) {
        JSONObject jsonP = new JSONObject();
        jsonP.put("name", pokemon.getName());
        jsonP.put("currentHP", pokemon.getCurrentHP());
        jsonP.put("baseStats", toJsonBaseStats(pokemon.getBaseStats()));
        jsonP.put("types", new JSONArray(pokemon.getTypes()));
        JSONArray moves = new JSONArray();
        for (Move m : pokemon.getMoves()) {
            if (m != null) {
                moves.put(toJsonMove(m));
            }
        }
        jsonP.put("moves", moves);
        return jsonP;
    }

    public static Pokemon fromJsonPokemon(JSONObject jsonP) {
        final int maxMoves = 4;
        final String name = jsonP.getString("name");
        final BaseLevelStats baseStats = fromJsonBaseStats(jsonP.getJSONObject("baseStats"));
        List<String> types = new ArrayList<>();
        JSONArray typesArr = jsonP.getJSONArray("types");
        for (int i = 0; i < typesArr.length(); i++) {
            types.add(typesArr.getString(i));
        }
        final Pokemon pokemon = new Pokemon(name, baseStats, types);
        pokemon.setCurrentHP(jsonP.getInt("currentHP"));

        Move[] moves = new Move[maxMoves];
        JSONArray movesArr = jsonP.getJSONArray("moves");

        for (int i = 0; i < Math.min(movesArr.length(), maxMoves); i++) {
            moves[i] = fromJsonMove(movesArr.getJSONObject(i));
        }
        pokemon.setMoves(moves);
        return pokemon;
    }

    public static JSONObject toJsonBaseStats(BaseLevelStats stats) {
        final JSONObject jsonS = new JSONObject();
        jsonS.put("maxHp", stats.getMaxHp());
        jsonS.put("attack", stats.getAttack());
        jsonS.put("defense", stats.getDefense());
        jsonS.put("specialAttack", stats.getSpecialAttack());
        jsonS.put("specialDefense", stats.getSpecialDefense());
        jsonS.put("speed", stats.getSpeed());
        return jsonS;
    }

    private static BaseLevelStats fromJsonBaseStats(JSONObject jsonS) {
        return new BaseLevelStats.BaseLevelStatsBuilder()
                .maxHp(jsonS.getInt("maxHp"))
                .attack(jsonS.getInt("attack"))
                .defense(jsonS.getInt("defense"))
                .specialAttack(jsonS.getInt("specialAttack"))
                .specialDefense(jsonS.getInt("specialDefense"))
                .speed(jsonS.getInt("speed"))
                .build();
    }

    private static JSONObject toJsonMove(Move move) {
        JSONObject jsonM = new JSONObject();
        jsonM.put("moveName", move.getMoveName());
        jsonM.put("moveDescription", move.getMoveDescription());
        jsonM.put("moveType", move.getMoveType());
        jsonM.put("maxPp", move.getMaxPp());
        jsonM.put("currentPp", move.getCurrentPp());
        jsonM.put("moveAccuracy", move.getMoveAccuracy());
        jsonM.put("moveClass", move.getMoveClass());
        return jsonM;
    }

    public static Move fromJsonMove(JSONObject jsonM) {
        final int moveAccuracy = 100;
        final String moveName = jsonM.getString("moveName");
        final String moveType = jsonM.getString("moveType");
        final int maxPp = jsonM.getInt("maxPp");
        final String description = jsonM.has("moveDescription") ? jsonM.getString("moveDescription") : moveName + " move";
        final int accuracy = jsonM.has("moveAccuracy") ? jsonM.getInt("moveAccuracy") : moveAccuracy;

        // DEFAULT to "physical" if moveClass is missing (safe fallback)
        final String moveClass = jsonM.has("moveClass") ? jsonM.getString("moveClass") : "physical";

        // Handle currentPp — default to maxPp if missing
        final int currentPp = jsonM.has("currentPp") ? jsonM.getInt("currentPp") : maxPp;

        // Use your existing overloaded constructor
        return new Move(moveName, moveType, maxPp, description, moveClass, accuracy, currentPp);
    }

    private static JSONObject toJsonBattlePhase(GameState.BattlePhase phase) {
        final JSONObject jsonPhase = new JSONObject();
        jsonPhase.put("currentTurn", phase.currentTurn().name());
        jsonPhase.put("player1ActiveIndex", phase.player1ActiveIndex());
        jsonPhase.put("player2ActiveIndex", phase.player2ActiveIndex());
        return jsonPhase;
    }

    private static GameState.BattlePhase fromJsonBattlePhase(JSONObject jsonPhase) {
        final GameState.Turn turn = GameState.Turn.valueOf(jsonPhase.getString("currentTurn"));
        final int p1Index = jsonPhase.getInt("player1ActiveIndex");
        final int p2Index = jsonPhase.getInt("player2ActiveIndex");
        return new GameState.BattlePhase(turn, p1Index, p2Index);
    }
}
