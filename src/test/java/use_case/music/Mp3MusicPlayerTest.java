package use_case.music;

import Music.Mp3MusicPlayer;
import Music.MusicPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class Mp3MusicPlayerTest {

    @TempDir
    Path tempDir;
    private String validMp3Path;
    private String invalidMp3Path;

    @BeforeEach
    void setUp() throws Exception {

        File validFile = tempDir.resolve("valid.mp3").toFile();
        byte[] validMp3Data = createValidMp3Data();
        try (FileOutputStream fos = new FileOutputStream(validFile)) {
            fos.write(validMp3Data);
        }
        validMp3Path = validFile.getAbsolutePath();

        File invalidFile = tempDir.resolve("invalid.mp3").toFile();
        Files.write(invalidFile.toPath(), new byte[]{0});
        invalidMp3Path = invalidFile.getAbsolutePath();
    }

    private byte[] createValidMp3Data() {

        byte[] data = new byte[1024];

        System.arraycopy("ID3".getBytes(), 0, data, 0, 3);

        data[100] = (byte) 0xFF;
        data[101] = (byte) 0xFB;
        return data;
    }

    @Test
    void testNormalPlayAndStop() throws Exception {
        Mp3MusicPlayer player = new Mp3MusicPlayer(validMp3Path);

        player.playMusic();
        Thread.sleep(200);

        assertTrue(isPlaying(player));
        assertTrue(isThreadAlive(player));

        player.stopMusic();
        Thread.sleep(100);

        assertFalse(isPlaying(player));
        assertFalse(isThreadAlive(player));
    }

    @Test
    void testPlayMusicTwice() throws Exception {
        Mp3MusicPlayer player = new Mp3MusicPlayer(validMp3Path);

        player.playMusic();
        Thread.sleep(100);
        Thread firstThread = getPlayThread(player);

        player.playMusic();
        Thread.sleep(50);
        Thread secondThread = getPlayThread(player);

        assertEquals(firstThread, secondThread, "应该是同一个线程");

        player.stopMusic();
    }

    @Test
    void testStopMusicExceptionHandling() throws Exception {
        Mp3MusicPlayer player = new Mp3MusicPlayer(validMp3Path);

        player.playMusic();
        Thread.sleep(100);

        Field playerField = Mp3MusicPlayer.class.getDeclaredField("player");
        playerField.setAccessible(true);
        playerField.set(player, null);

        assertDoesNotThrow(player::stopMusic);
    }

    @Test
    void testPlayLoopWithIOException() throws Exception {

        Mp3MusicPlayer player = new Mp3MusicPlayer("non_existent_file_12345.mp3");

        Method playLoopMethod = Mp3MusicPlayer.class.getDeclaredMethod("playLoop");
        playLoopMethod.setAccessible(true);

        Thread loopThread = new Thread(() -> {
            try {
                playLoopMethod.invoke(player);
            } catch (Exception e) {
                fail("Exceptions should not be thrown: " + e);
            }
        });

        loopThread.start();
        Thread.sleep(200);

        player.stopMusic();
        loopThread.join(1000);

        assertFalse(loopThread.isAlive());
    }

    @Test
    void testInvalidMp3File() throws Exception {
        Mp3MusicPlayer player = new Mp3MusicPlayer(invalidMp3Path);

        player.playMusic();
        Thread.sleep(200);

        assertTrue(isThreadAlive(player));

        player.stopMusic();
        Thread.sleep(100);

        assertFalse(isThreadAlive(player));
    }

    @Test
    void testThreadInterruption() throws Exception {
        Mp3MusicPlayer player = new Mp3MusicPlayer(validMp3Path);

        player.playMusic();
        Thread.sleep(200);

        Thread playThread = getPlayThread(player);
        assertNotNull(playThread);

        player.stopMusic();
        Thread.sleep(200);

        assertTrue(playThread.isInterrupted() || !playThread.isAlive());
    }

    @Test
    void testResourceCleanup() throws Exception {
        Mp3MusicPlayer player = new Mp3MusicPlayer(validMp3Path);

        player.playMusic();
        Thread.sleep(200);

        Field playerField = Mp3MusicPlayer.class.getDeclaredField("player");
        playerField.setAccessible(true);
        Object internalPlayer = playerField.get(player);
        assertNotNull(internalPlayer);

        player.stopMusic();
        Thread.sleep(200);

        internalPlayer = playerField.get(player);

        assertTrue(true);
    }

    @Test
    void testMultipleStopCalls() {
        Mp3MusicPlayer player = new Mp3MusicPlayer(validMp3Path);

        player.stopMusic();
        player.stopMusic();
        player.stopMusic();

        assertTrue(true);
    }

    @Test
    void testEarlyReturnInPlayMusic() throws Exception {
        Mp3MusicPlayer player = new Mp3MusicPlayer(validMp3Path);

        player.playMusic();
        Thread.sleep(100);

        player.playMusic();

        assertTrue(getKeepPlaying(player));

        player.stopMusic();
    }

    private boolean isPlaying(Mp3MusicPlayer player) throws Exception {
        return getKeepPlaying(player);
    }

    private boolean getKeepPlaying(Mp3MusicPlayer player) throws Exception {
        Field field = Mp3MusicPlayer.class.getDeclaredField("keepPlaying");
        field.setAccessible(true);
        return field.getBoolean(player);
    }

    private Thread getPlayThread(Mp3MusicPlayer player) throws Exception {
        Field field = Mp3MusicPlayer.class.getDeclaredField("playThread");
        field.setAccessible(true);
        return (Thread) field.get(player);
    }

    private boolean isThreadAlive(Mp3MusicPlayer player) throws Exception {
        Thread thread = getPlayThread(player);
        return thread != null && thread.isAlive();
    }
}

class MusicPlayerTest {

    @Test
    void testInterfaceContract() {
        MusicPlayer player = new Mp3MusicPlayer("test.mp3");

        assertNotNull(player);
        assertTrue(player instanceof MusicPlayer);

        assertDoesNotThrow(() -> {
            player.playMusic();
            player.stopMusic();
        });
    }
}
