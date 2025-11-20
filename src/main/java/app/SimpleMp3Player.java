package app;
// SimpleMp3Player.java
import Music.musicPlayer;
import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

public class SimpleMp3Player implements musicPlayer {

    private final String filePath;   // path to your single MP3
    private volatile boolean playing;
    private Thread playThread;
    private Player player;

    public SimpleMp3Player(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public synchronized void play() {
        // If already playing, do nothing
        if (playing) {
            return;
        }

        playing = true;

        // New thread so GUI does not freeze
        playThread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(filePath);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {

                player = new Player(bis);
                player.play();  // plays until the end or until close() is called

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                playing = false;
            }
        });

        playThread.start();
    }

    @Override
    public synchronized void stop() {
        // Tell the player to stop
        if (player != null) {
            player.close();   // this makes player.play() return
            player = null;
        }

        playing = false;

        // Interrupt the thread if still running
        if (playThread != null && playThread.isAlive()) {
            playThread.interrupt();
        }
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }
}