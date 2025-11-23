package use_case.auto_save;

import entity.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class OrgJsonGameStateSerializer {

    public static JSONObject toJson(GameState state) {
        JSONObject obj = new JSONObject();
        obj.put("currentScreen", state.currentScreen().name());
        obj.put("activeTeamSelector", state.activeTeamSelector().name());
        obj.put("currentTowerLevel", state.currentTowerLevel());
        obj.put("highScore", state.highScore());

        obj.put("playerTeam", teamToJson(state.playerTeam()));
        obj.put("opponentTeam", teamToJson(state.opponentTeam()));

        if (state.battlePhase() != null) {
            JSONObject bp = new JSONObject();
            bp.put("currentTurn", state.battlePhase().currentTurn().name());
            bp.put("playerActiveIndex", state.battlePhase().playerActiveIndex());
            bp.put("opponentActiveIndex", state.battlePhase().opponentActiveIndex());
            obj.put("battlePhase", bp);
        }
        return obj;
    }

    public static GameState fromJson(JSONObject obj) {
        GameState.Screen screen = GameState.Screen.valueOf(obj.getString("currentScreen"));
        GameState.Player selector = GameState.Player.valueOf(obj.getString("activeTeamSelector"));
        int level = obj.getInt("currentTowerLevel");
        int highScore = obj.getInt("highScore");

        PokemonTeam playerTeam = jsonToTeam(obj.getJSONObject("playerTeam"));
        PokemonTeam opponentTeam = jsonToTeam(obj.getJSONObject("opponentTeam"));

        GameState.BattlePhase battlePhase = null;
        if (obj.has("battlePhase")) {
            JSONObject bp = obj.getJSONObject("battlePhase");
            GameState.Turn turn = GameState.Turn.valueOf(bp.getString("currentTurn"));
            int pIdx = bp.getInt("playerActiveIndex");
            int oIdx = bp.getInt("opponentActiveIndex");
            battlePhase = new GameState.BattlePhase(turn, pIdx, oIdx);
        }

        return new GameState(screen, selector, playerTeam, opponentTeam, battlePhase, level, highScore);
    }

    private static JSONObject teamToJson(PokemonTeam team) {
        JSONObject t = new JSONObject();
        JSONArray arr = new JSONArray();
        for (Pokemon p : team.getTeam()) {
            JSONObject poke = new JSONObject();
            poke.put("name", p.getName());
            poke.put("currentHP", p.getCurrentHP());
            poke.put("baseStats", statsToJson(p.getBaseStats()));
            poke.put("types", new JSONArray(p.getTypes()));
            // Moves + PP would go here if needed
            arr.put(poke);
        }
        t.put("pokemons", arr);
        return t;
    }

    private static PokemonTeam jsonToTeam(JSONObject t) {
        PokemonTeam team = new PokemonTeam();
        JSONArray arr = t.getJSONArray("pokemons");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject p = arr.getJSONObject(i);
            // You'd recreate Pokemon via factory here in full version
            // For now: just placeholder
        }
        return team;
    }

    private static JSONObject statsToJson(BaseLevelStats stats) {
        JSONObject s = new JSONObject();
        s.put("maxHp", stats.getMaxHp());
        s.put("attack", stats.getAttack());
        s.put("defense", stats.getDefense());
        s.put("specialAttack", stats.getSpecialAttack());
        s.put("specialDefense", stats.getSpecialDefense());
        s.put("speed", stats.getSpeed());
        return s;
    }
}