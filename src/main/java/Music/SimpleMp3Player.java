package Music;

import javazoom.jl.player.Player;
import java.io.FileInputStream;

public class Mp3MusicPlayer implements MusicPlayer {

    private Player player;
    private Thread playThread;
    private final String filePath;

    public Mp3MusicPlayer(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void playMusic() {
        stopMusic();
        playThread = new Thread(this::playTask);
        playThread.start();
    }

    private void playTask() {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            player = new Player(fis);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopMusic() {
        try {
            if (player != null) player.close();
            if (playThread != null && playThread.isAlive()) playThread.interrupt();
        } catch (Exception ignored) {}
    }
}