package Music;
public interface musicPlayer {

    // Start (or restart) playing the MP3
    void playMusic();

    // Stop playing the MP3
    void stopMusic();

    // For information only (not important)
    boolean isPlaying();
}