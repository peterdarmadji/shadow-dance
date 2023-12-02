import javax.sound.sampled.*;

/**
 * Represents a music track that can be played in the background.
 * The Track class provides functionality for loading, playing, and pausing background music
 * using the Java Sound API. It extends Thread, enabling concurrent playback of music while
 * other operations are ongoing.
 */
public class Track extends Thread {
    private AudioInputStream stream;
    private Clip clip;

    /**
     * Constructs a new Track by loading audio from the specified file.
     * @param file The path to the audio file to be loaded.
     */
    public Track(String file) {
        try {
            stream = AudioSystem.getAudioInputStream(new java.io.File(file));
            clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class,stream.getFormat()));
            clip.open(stream);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Pauses the playback of the music track.
     */
    public void pause() {
        try {
            clip.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Begins or resumes playback of the music track.
     */
    @Override
    public void run(){
        try {
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}