package interface_adapter.start_battle;

import entity.PokemonTeam;
import use_case.start_battle.StartBattleInputBoundary;
import use_case.start_battle.StartBattleInputData;

public class StartBattleController {
    private final StartBattleInputBoundary startBattleInteractor;

    public StartBattleController(StartBattleInputBoundary startBattleInteractor) {
        this.startBattleInteractor = startBattleInteractor;
    }

    public void startBattle(PokemonTeam team1, PokemonTeam team2) {
        StartBattleInputData inputData = new StartBattleInputData(team1, team2);
        startBattleInteractor.execute(inputData);
    }
}