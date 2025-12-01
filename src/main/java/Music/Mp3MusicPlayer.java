package Music;

import javazoom.jl.player.Player;
import java.io.FileInputStream;

public class Mp3MusicPlayer implements MusicPlayer {

    private Player player;
    private Thread playThread;
    private final String filePath;
    private volatile boolean keepPlaying = false;  // controls continuous play

    public Mp3MusicPlayer(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public synchronized void playMusic() {
        // If already playing, do nothing
        if (keepPlaying) {
            return;
        }

        keepPlaying = true;

        playThread = new Thread(this::playLoop);
        playThread.start();
    }

    // This keeps looping the track until stopMusic() is called
    private void playLoop() {
        while (keepPlaying) {
            try (FileInputStream fis = new FileInputStream(filePath)) {
                player = new Player(fis);
                player.play();  // plays once; when done, loop repeats if keepPlaying is still true
            } catch (Exception e) {
                e.printStackTrace();
                break; // break on error so we don't spin forever
            }
        }
    }

    @Override
    public synchronized void stopMusic() {
        keepPlaying = false;     // tell the loop to stop

        try {
            if (player != null) {
                player.close();
            }
            if (playThread != null && playThread.isAlive()) {
                playThread.interrupt();
            }
        } catch (Exception ignored) {}
    }
}
