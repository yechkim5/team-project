package Music;
public interface musicPlayer {

    // Start (or restart) playing the MP3
    void play();

    // Stop playing the MP3
    void stop();

    // For information only - you can ignore if not needed
    boolean isPlaying();
}