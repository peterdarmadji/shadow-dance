import bagel.*;

/**
 * Represents a normal note in the game.
 * Normal notes are basic type of note that players need to hit in the game.
 */
public class NormalNote extends Note {
    private final Image image;
    private static final int NORMAL_NOTE_INITIAL_Y = 100;

    /**
     * Constructs a new NormalNote with a specified direction and appearance frame.
     * @param dir             The direction (or type) of the lane this note belongs to.
     * @param appearanceFrame The frame in which this note should appear.
     */
    public NormalNote(String dir, int appearanceFrame) {
        image = new Image("res/note" + dir + ".png");
        this.setAppearanceFrame(appearanceFrame);
        this.setY(NORMAL_NOTE_INITIAL_Y);
    }

    /**
     * Draws the normal note at the specified x-coordinate if the note is active.
     * @param x The x-coordinate to draw the note.
     */
    public void draw(int x) {
        if (isActive()) {
            image.draw(x, getY());
        }
    }

}
