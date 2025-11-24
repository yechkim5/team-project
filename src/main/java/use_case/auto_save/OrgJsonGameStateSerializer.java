package use_case.auto_save;

import entity.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts GameState ↔ JSON using only org.json
 * Handles full PvP save/load: teams, HP, moves + PP, turn, etc.
 */
public class OrgJsonGameStateSerializer {

    public static JSONObject toJson(GameState state) {
        JSONObject root = new JSONObject();

        root.put("currentScreen", state.currentScreen().name());
        root.put("activeTeamSelector", state.activeTeamSelector().name());
        root.put("currentTowerLevel", state.currentTowerLevel());
        root.put("highScore", state.highScore());

        root.put("player1Team", teamToJson(state.player1Team()));
        root.put("player2Team", teamToJson(state.player2Team()));

        if (state.battlePhase() != null) {
            JSONObject bp = new JSONObject();
            bp.put("currentTurn", state.battlePhase().currentTurn().name());
            bp.put("player1ActiveIndex", state.battlePhase().player1ActiveIndex());
            bp.put("player2ActiveIndex", state.battlePhase().player2ActiveIndex());
            root.put("battlePhase", bp);
        }

        return root;
    }

    public static GameState fromJson(JSONObject root) {
        GameState.Screen screen = GameState.Screen.valueOf(root.getString("currentScreen"));
        GameState.Player selector = GameState.Player.valueOf(root.getString("activeTeamSelector"));
        int level = root.getInt("currentTowerLevel");
        int highScore = root.getInt("highScore");

        PokemonTeam player1Team = jsonToTeam(root.getJSONObject("player1Team"));
        PokemonTeam player2Team = jsonToTeam(root.getJSONObject("player2Team"));

        GameState.BattlePhase battlePhase = null;
        if (root.has("battlePhase")) {
            JSONObject bp = root.getJSONObject("battlePhase");
            GameState.Turn turn = GameState.Turn.valueOf(bp.getString("currentTurn"));
            int p1Idx = bp.getInt("player1ActiveIndex");
            int p2Idx = bp.getInt("player2ActiveIndex");
            battlePhase = new GameState.BattlePhase(turn, p1Idx, p2Idx);
        }

        return new GameState(screen, selector, player1Team, player2Team, battlePhase, level, highScore);
    }

    // Convert a PokemonTeam → JSON
    private static JSONObject teamToJson(PokemonTeam team) {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();

        for (Pokemon p : team.getTeam()) {
            JSONObject poke = new JSONObject();
            poke.put("name", p.getName());
            poke.put("currentHP", p.getCurrentHP());

            // Save base stats
            poke.put("baseStats", statsToJson(p.getBaseStats()));

            // Save types
            poke.put("types", new JSONArray(p.getTypes()));

            // Save moves with current PP
            JSONArray movesArr = new JSONArray();
            for (Move m : p.getMoves()) {
                if (m != null) {
                    JSONObject moveJson = new JSONObject();
                    moveJson.put("name", m.getMoveName());
                    moveJson.put("currentPp", m.getCurrentPp());
                    moveJson.put("maxPp", m.getMaxPp());
                    moveJson.put("type", m.getMoveType());
                    moveJson.put("moveClass", m.getMoveClass());
                    movesArr.put(moveJson);
                }
            }
            poke.put("moves", movesArr);
            arr.put(poke);
        }

        obj.put("pokemons", arr);
        return obj;
    }

    // Convert JSON → PokemonTeam (reconstructs real Pokemon)
    private static PokemonTeam jsonToTeam(JSONObject teamJson) {
        PokemonTeam team = new PokemonTeam();
        JSONArray arr = teamJson.getJSONArray("pokemons");

        for (int i = 0; i < arr.length(); i++) {
            JSONObject pJson = arr.getJSONObject(i);

            String name = pJson.getString("name");
            int currentHp = pJson.getInt("currentHP");

            // Rebuild BaseLevelStats
            JSONObject statsJson = pJson.getJSONObject("baseStats");
            BaseLevelStats baseStats = new BaseLevelStats.Builder()
                    .maxHp(statsJson.getInt("maxHp"))
                    .attack(statsJson.getInt("attack"))
                    .defense(statsJson.getInt("defense"))
                    .specialAttack(statsJson.getInt("specialAttack"))
                    .specialDefense(statsJson.getInt("specialDefense"))
                    .speed(statsJson.getInt("speed"))
                    .build();

            // Rebuild types
            List<String> types = new ArrayList<>();
            JSONArray typesArr = pJson.getJSONArray("types");
            for (int j = 0; j < typesArr.length(); j++) {
                types.add(typesArr.getString(j));
            }

            // Rebuild moves
            Move[] moves = new Move[4];
            JSONArray movesArr = pJson.getJSONArray("moves");
            for (int j = 0; j < movesArr.length() && j < 4; j++) {
                JSONObject mJson = movesArr.getJSONObject(j);
                Move move = new Move(
                        mJson.getString("name"),
                        mJson.getString("type"),
                        mJson.getInt("maxPp"),
                        "Loaded from save",
                        mJson.getString("moveClass"),
                        100  // accuracy
                );
                // Restore current PP
                move.setCurrentPp(mJson.getInt("currentPp"));
                moves[j] = move;
            }

            // Create Pokemon and restore HP
            Pokemon pokemon = new Pokemon(name, baseStats, types);
            pokemon.setCurrentHp(currentHp);
            pokemon.setMoves(moves);

            team.addPokemon(pokemon);
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