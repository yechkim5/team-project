package use_case.generate_random_battle;

import entity.Battle;
import entity.Pokemon;
import entity.PokemonTeam;
import repository.PokemonRepository;
import use_case.start_battle.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateRandomBattleInteractor implements GenerateRandomBattleInputBoundary {

    private final GenerateRandomBattleOutputBoundary presenter;
    private final PokemonRepository repo;
    private final StartBattleInputBoundary startBattleInteractor;

    public GenerateRandomBattleInteractor(
            GenerateRandomBattleOutputBoundary presenter,
            PokemonRepository repo,
            StartBattleInputBoundary startBattleInteractor) {

        this.presenter = presenter;
        this.repo = repo;
        this.startBattleInteractor = startBattleInteractor;
    }

    @Override
    public void generateRandomBattle() {

        // Create two random 6-Pokémon teams
        PokemonTeam team1 = generateRandomTeam();
        PokemonTeam team2 = generateRandomTeam();

        // Pass these teams into the StartBattle Use Case
        StartBattleInputData data = new StartBattleInputData(team1, team2);
        startBattleInteractor.execute(data);

        // After StartBattleInteractor fills its output,
        // presenter is notified automatically
    }

    private PokemonTeam generateRandomTeam() {
        List<Pokemon> all = repo.getAllPokemon();
        Collections.shuffle(all);

        List<Pokemon> team = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            Pokemon p = all.get(i).deepCopy();
            p.assignRandomMoves();
            team.add(p);
        }
        return new PokemonTeam(team);
    }
}
