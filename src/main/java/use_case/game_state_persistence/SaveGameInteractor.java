package use_case.game_state_persistence;

import entity.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SaveGameInteractor {

    public static JSONObject toJson(GameState state) {
        JSONObject json = new JSONObject();
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
        GameState.Screen screen = GameState.Screen.valueOf(json.getString("currentScreen"));
        GameState.Player selector = GameState.Player.valueOf(json.getString("activeTeamSelector"));
        PokemonTeam team1 = fromJsonTeam(json.getJSONObject("player1Team"));
        PokemonTeam team2 = fromJsonTeam(json.getJSONObject("player2Team"));
        GameState.BattlePhase phase = null;
        if (json.has("battlePhase")) {
            phase = fromJsonBattlePhase(json.getJSONObject("battlePhase"));
        }
        int towerLevel = json.getInt("currentTowerLevel");
        int highScore = json.getInt("highScore");
        return new GameState(screen, selector, team1, team2, phase, towerLevel, highScore);
    }

    private static JSONObject toJsonTeam(PokemonTeam team) {
        JSONObject jsonTeam = new JSONObject();
        JSONArray pokemons = new JSONArray();
        for (Pokemon p : team.getTeam()) {
            pokemons.put(toJsonPokemon(p));
        }
        jsonTeam.put("pokemons", pokemons);
        return jsonTeam;
    }

    private static PokemonTeam fromJsonTeam(JSONObject jsonTeam) {
        PokemonTeam team = new PokemonTeam();
        JSONArray pokemons = jsonTeam.getJSONArray("pokemons");
        for (int i = 0; i < pokemons.length(); i++) {
            team.addPokemon(fromJsonPokemon(pokemons.getJSONObject(i)));
        }
        return team;
    }

    private static JSONObject toJsonPokemon(Pokemon p) {
        JSONObject jsonP = new JSONObject();
        jsonP.put("name", p.getName());
        jsonP.put("currentHP", p.getCurrentHP());
        jsonP.put("baseStats", toJsonBaseStats(p.getBaseStats()));
        jsonP.put("types", new JSONArray(p.getTypes()));
        JSONArray moves = new JSONArray();
        for (Move m : p.getMoves()) {
            if (m != null) {
                moves.put(toJsonMove(m));
            }
        }
        jsonP.put("moves", moves);
        return jsonP;
    }

    private static Pokemon fromJsonPokemon(JSONObject jsonP) {
        String name = jsonP.getString("name");
        BaseLevelStats baseStats = fromJsonBaseStats(jsonP.getJSONObject("baseStats"));
        List<String> types = new ArrayList<>();
        JSONArray typesArr = jsonP.getJSONArray("types");
        for (int i = 0; i < typesArr.length(); i++) {
            types.add(typesArr.getString(i));
        }
        Pokemon pokemon = new Pokemon(name, baseStats, types);
        pokemon.setCurrentHP(jsonP.getInt("currentHP"));

        Move[] moves = new Move[4];
        JSONArray movesArr = jsonP.getJSONArray("moves");

        for (int i = 0; i < movesArr.length() && i < 4; i++) {
            moves[i] = fromJsonMove(movesArr.getJSONObject(i));
        }

        pokemon.setMoves(moves);
        return pokemon;
    }

    private static JSONObject toJsonBaseStats(BaseLevelStats s) {
        JSONObject jsonS = new JSONObject();
        jsonS.put("maxHp", s.getMaxHp());
        jsonS.put("attack", s.getAttack());
        jsonS.put("defense", s.getDefense());
        jsonS.put("specialAttack", s.getSpecialAttack());
        jsonS.put("specialDefense", s.getSpecialDefense());
        jsonS.put("speed", s.getSpeed());
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

    private static JSONObject toJsonMove(Move m) {
        JSONObject jsonM = new JSONObject();
        jsonM.put("moveName", m.getMoveName());
        jsonM.put("moveDescription", m.getMoveDescription());
        jsonM.put("moveType", m.getMoveType());
        jsonM.put("maxPp", m.getMaxPp());
        jsonM.put("currentPp", m.getCurrentPp());
        jsonM.put("moveAccuracy", m.getMoveAccuracy());
        jsonM.put("moveClass", m.getMoveClass());  // ← This line MUST exist
        return jsonM;
    }

    private static Move fromJsonMove(JSONObject jsonM) {
        String moveName = jsonM.getString("moveName");
        String moveType = jsonM.getString("moveType");
        int maxPp = jsonM.getInt("maxPp");
        String description = jsonM.has("moveDescription") ? jsonM.getString("moveDescription") : moveName + " move";
        int accuracy = jsonM.has("moveAccuracy") ? jsonM.getInt("moveAccuracy") : 100;

        // DEFAULT to "physical" if moveClass is missing (safe fallback)
        String moveClass = jsonM.has("moveClass") ? jsonM.getString("moveClass") : "physical";

        // Handle currentPp — default to maxPp if missing
        int currentPp = jsonM.has("currentPp") ? jsonM.getInt("currentPp") : maxPp;

        // Use your existing overloaded constructor
        return new Move(moveName, moveType, maxPp, description, moveClass, accuracy, currentPp);
    }

    private static JSONObject toJsonBattlePhase(GameState.BattlePhase phase) {
        JSONObject jsonPhase = new JSONObject();
        jsonPhase.put("currentTurn", phase.currentTurn().name());
        jsonPhase.put("player1ActiveIndex", phase.player1ActiveIndex());
        jsonPhase.put("player2ActiveIndex", phase.player2ActiveIndex());
        return jsonPhase;
    }

    private static GameState.BattlePhase fromJsonBattlePhase(JSONObject jsonPhase) {
        GameState.Turn turn = GameState.Turn.valueOf(jsonPhase.getString("currentTurn"));
        int p1Index = jsonPhase.getInt("player1ActiveIndex");
        int p2Index = jsonPhase.getInt("player2ActiveIndex");
        return new GameState.BattlePhase(turn, p1Index, p2Index);
    }
}