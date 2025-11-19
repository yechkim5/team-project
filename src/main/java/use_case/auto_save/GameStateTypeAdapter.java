package use_case.auto_save;

import com.google.gson.*;
import entity.*;

import java.lang.reflect.Type;

/**
 * Makes Gson serialize/deserialize your complex objects (Pokemon, Move, BaseLevelStats, etc.)
 */
public class GameStateTypeAdapter implements JsonSerializer<GameState>, JsonDeserializer<GameState> {

    @Override
    public JsonElement serialize(GameState src, Type type, JsonSerializationContext ctx) {
        JsonObject obj = new JsonObject();
        obj.addProperty("currentScreen", src.currentScreen().name());
        obj.addProperty("activeTeamSelector", src.activeTeamSelector().name());
        obj.add("playerTeam", ctx.serialize(src.playerTeam()));
        obj.add("opponentTeam", ctx.serialize(src.opponentTeam()));
        obj.addProperty("currentTowerLevel", src.currentTowerLevel());
        obj.addProperty("highScore", src.highScore());

        if (src.battlePhase() != null) {
            JsonObject bp = new JsonObject();
            bp.addProperty("currentTurn", src.battlePhase().currentTurn().name());
            bp.addProperty("playerActiveIndex", src.battlePhase().playerActiveIndex());
            bp.addProperty("opponentActiveIndex", src.battlePhase().opponentActiveIndex());
            obj.add("battlePhase", bp);
        }
        return obj;
    }

    @Override
    public GameState deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        GameState.Screen screen = GameState.Screen.valueOf(obj.get("currentScreen").getAsString());
        GameState.Player selector = GameState.Player.valueOf(obj.get("activeTeamSelector").getAsString());

        PokemonTeam playerTeam = ctx.deserialize(obj.get("playerTeam"), PokemonTeam.class);
        PokemonTeam opponentTeam = ctx.deserialize(obj.get("opponentTeam"), PokemonTeam.class);

        int level = obj.get("currentTowerLevel").getAsInt();
        int highScore = obj.get("highScore").getAsInt();

        GameState.BattlePhase battlePhase = null;
        if (obj.has("battlePhase")) {
            JsonObject bp = obj.getAsJsonObject("battlePhase");
            GameState.Turn turn = GameState.Turn.valueOf(bp.get("currentTurn").getAsString());
            int pIdx = bp.get("playerActiveIndex").getAsInt();
            int oIdx = bp.get("opponentActiveIndex").getAsInt();
            battlePhase = new GameState.BattlePhase(turn, pIdx, oIdx);
        }

        return new GameState(screen, selector, playerTeam, opponentTeam, battlePhase, level, highScore);
    }
}