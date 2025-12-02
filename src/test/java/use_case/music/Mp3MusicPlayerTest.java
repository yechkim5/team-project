package use_case.music;

import Music.Mp3MusicPlayer;
import Music.MusicPlayer;
import org.junit.Test;

public class Mp3MusicPlayerTest {

    /**
     * Adjust this path so it matches where your MP3 actually is.
     * Example: "main/java/interface_adapter/Music/BattleMusic.mp3"
     */
    private static final String MP3_PATH =
            "main/java/interface_adapter/Music/BattleMusic.mp3";

    @Test
    public void testPlayThenStopDoesNotCrash() throws InterruptedException {
        MusicPlayer player = new Mp3MusicPlayer(MP3_PATH);

        // Should start playing without throwing exceptions
        player.playMusic();

        // Let it run a bit so the thread actually starts
        Thread.sleep(500);

        // Should stop without throwing exceptions
        player.stopMusic();
    }

    @Test
    public void testStopWithoutPlayDoesNotCrash() {
        MusicPlayer player = new Mp3MusicPlayer(MP3_PATH);

        // Calling stop before play should be safe
        player.stopMusic();
    }

    @Test
    public void testMultiplePlayCallsDoNotCrash() throws InterruptedException {
        MusicPlayer player = new Mp3MusicPlayer(MP3_PATH);

        // First play
        player.playMusic();
        Thread.sleep(300);

        // Second play – your code uses keepPlaying / playThread,
        // so this should not cause errors or start multiple players
        player.playMusic();
        Thread.sleep(300);

        // Stop at the end
        player.stopMusic();
    }
}
