// ===================================================================
// 4. data_access/FileGameProgressDataAccessObject.java  (new file – CSV exactly like FileUserDataAccessObject)
// ===================================================================
package data_access;

import entity.*;
import poke_api.pokemonFetcher;
import use_case.SaveProgressDataAccessInterface;

import java.io.*;
import java.util.Optional;

public class FileGameProgressDataAccessObject implements SaveProgressDataAccessInterface {

    private final File csvFile;
    private final pokemonFetcher api = new pokemonFetcher();   // reuse existing API class

    private static final String SAVE_FILE = "resources/progress.csv";
    private static final String HEADER = "currentLevel,highScore,activeIndex,teamData";

    public FileGameProgressDataAccessObject() {
        csvFile = new File(SAVE_FILE);
        if (csvFile.length() == 0) {
            writeHeader();
        }
    }

    private void writeHeader() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(csvFile))) {
            w.write(HEADER);
            w.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Cannot create save file", e);
        }
    }

    @Override
    public void saveProgress(Trainer playerTrainer, int currentTowerLevel, int highScore) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(csvFile))) {
            w.write(HEADER);
            w.newLine();

            String teamCsv = serializeTeam(playerTrainer);
            int activeIdx = playerTrainer.getTeam().indexOf(playerTrainer.getActivePokemon());

            String line = String.format("%d,%d,%d,%s",
                    currentTowerLevel, highScore, activeIdx, teamCsv);
            w.write(line);
            w.newLine();

        } catch (IOException e) {
            throw new RuntimeException("Failed to save progress", e);
        }
    }

    @Override
    public Optional<ProgressData> loadProgress() {
        if (csvFile.length() == 0) return Optional.empty();

        try (BufferedReader r = new BufferedReader(new FileReader(csvFile))) {
            r.readLine(); // header
            String line = r.readLine();
            if (line == null) return Optional.empty();

            String[] parts = line.split(",", 4);
            int level = Integer.parseInt(parts[0]);
            int highScore = Integer.parseInt(parts[1]);
            int activeIdx = Integer.parseInt(parts[2]);
            String teamCsv = parts[3];

            Trainer trainer = deserializeTeam(teamCsv, activeIdx);
            return Optional.of(new ProgressData(trainer, level, highScore));

        } catch (Exception e) {
            System.err.println("Load failed: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Format: name~currentHp~move1=pp1~move2=pp2;name~...
    private String serializeTeam(Trainer trainer) {
        StringBuilder sb = new StringBuilder();
        for (Pokemon p : trainer.getTeam()) {
            if (sb.length() > 0) sb.append(";");
            sb.append(p.getName()).append("~").append(p.getCurrentHp()).append("~");
            Move[] moves = p.getMoves();
            for (int i = 0; i < moves.length; i++) {
                if (moves[i] != null) {
                    if (i > 0) sb.append("~");
                    sb.append(moves[i].getName())
                            .append("=")
                            .append(moves[i].getCurrentPp());
                }
            }
        }
        return sb.toString();
    }

    private Trainer deserializeTeam(String csv, int activeIdx) throws Exception {
        Trainer trainer = new Trainer("Player");
        String[] pokes = csv.split(";");

        for (String poke : pokes) {
            if (poke.isBlank()) continue;
            String[] main = poke.split("~", 3); // name ~ hp ~ moves
            String name = main[0].trim();
            int hp = Integer.parseInt(main[1]);

            // Fetch fresh Pokémon from API (uses your teammate's pokemonFetcher)
            JSONObject json = api.getPokemon(name);
            Pokemon fresh = PokemonCreator.fromApiJson(json);   // ← small helper below

            // Restore HP and PP
            fresh.setCurrentHp(hp);
            if (main.length > 2) {
                String[] movePairs = main[2].split("~");
                for (String pair : movePairs) {
                    String[] kv = pair.split("=");
                    String moveName = kv[0];
                    int pp = Integer.parseInt(kv[1]);
                    for (Move m : fresh.getMoves()) {
                        if (m != null && m.getName().equalsIgnoreCase(moveName)) {
                            m.setCurrentPp(pp);
                            break;
                        }
                    }
                }
            }
            trainer.addToTeam(fresh);
        }

        if (activeIdx >= 0 && activeIdx < trainer.getTeam().size()) {
            trainer.switchToPokemon(activeIdx);
        }
        return trainer;
    }
}