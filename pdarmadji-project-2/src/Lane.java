import bagel.*;
import java.util.ArrayList;

/**
 * Represents a lane in which notes fall down.
 * A lane has a type, image, location, relevant key for interaction, and a list of notes.
 */
public class Lane {
    /**
     * Height of the lane.
     */
    public static final int HEIGHT = 384;
    /**
     * Target height for notes in the lane.
     */
    public static final int TARGET_HEIGHT = 657;
    private final String type;
    private final Image image;
    private final ArrayList<NormalNote> notes = new ArrayList<>();
    private final ArrayList<HoldNote> holdNotes = new ArrayList<>();
    private Keys relevantKey;
    private int location;
    private int currNote = 0;
    private int currHoldNote = 0;

    /**
     * Constructs a new lane with a specified direction and location.
     * @param dir      The direction (or type) of the lane.
     * @param location The X-coordinate of the lane.
     */
    public Lane(String dir, int location) {
        this.type = dir;
        this.location = location;
        image = new Image("res/lane" + dir + ".png");
        initRelevantKey(dir);
    }

    /**
     * Initializes the relevant key corresponding to the lane's direction.
     * @param dir The direction (or type) of the lane.
     */
    public void initRelevantKey(String dir) {
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
        }
    }

    /**
     * @return The X-coordinate location of the lane.
     */
    public int getLocation() {
        return location;
    }

    /**
     * @return The type (or direction) of the lane.
     */
    public String getType() {
        return type;
    }


    /**
     * Deactivate all currently active regular notes (normal and hold) in the screen
     */
    public void deactivateAffectedNotes() {
        // Deactivating regular notes
        for (NormalNote note: notes) {
            if (note.isActive()) {
                note.deactivate();
            }
        }

        // Deactivating hold notes
        for (HoldNote holdNote: holdNotes) {
            if (holdNote.isActive()) {
                holdNote.deactivate();
            }
        }
    }

    /**
     * Updates all the notes in the lane and checks their scores based on player input.
     * @param input    The player's input.
     * @param accuracy The accuracy metric for the game.
     * @return         The score for the current note or Accuracy.NOT_SCORED if not scored.
     */
    public int update(Input input, Accuracy accuracy) {
        draw();

        for (int i = currNote; i < notes.size(); i++) {
            notes.get(i).update();
        }

        for (int j = currHoldNote; j < holdNotes.size(); j++) {
            holdNotes.get(j).update();
        }

        if (currNote < notes.size()) {
            int score = notes.get(currNote).checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);

            // deactivate regular and hold notes when encountering Bomb note and set to not count bomb score
            if (notes.get(currNote) instanceof Bomb && accuracy.isBomb()) {
                deactivateAffectedNotes();
                score = 0;
                accuracy.setBomb(false);
            }

            if (notes.get(currNote).isCompleted()) {
                currNote++;
                return score;
            }
        }

        if (currHoldNote < holdNotes.size()) {
            int score = holdNotes.get(currHoldNote).checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);

            if (holdNotes.get(currHoldNote).isCompleted()) {
                currHoldNote++;
            }
            return score;
        }

        return Accuracy.NOT_SCORED;
    }

    /**
     * Adds a regular note to the lane.
     * @param n The regular note to add.
     */
    public void addNote(NormalNote n) {
        notes.add(n);
    }

    /**
     * @return The list of regular notes in the lane.
     */
    public ArrayList<NormalNote> getNotes() {
        return notes;
    }

    /**
     * Adds a hold note to the lane.
     * @param hn The hold note to add.
     */
    public void addHoldNote(HoldNote hn) {
        holdNotes.add(hn);
    }

    /**
     * @return The list of hold notes in the lane.
     */
    public ArrayList<HoldNote> getHoldNotes() {
        return holdNotes;
    }


    /**
     * Checks if all notes (regular and hold) in the lane have been pressed or missed.
     * @return True if all notes have been processed, false otherwise.
     */
    public boolean isFinished() {
        for (NormalNote note : notes) {
            if (!note.isCompleted()) {
                return false;
            }
        }

        for (HoldNote holdNote : holdNotes) {
            if (!holdNote.isCompleted()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Draws the lane and the notes on the screen
     */
    public void draw() {
        image.draw(location, HEIGHT);

        for (int i = currNote; i < notes.size(); i++) {
            notes.get(i).draw(location);
        }

        for (int j = currHoldNote; j < holdNotes.size(); j++) {
            holdNotes.get(j).draw(location);
        }
    }

    /**
     * Checks for collisions between notes in the lane and an enemy.
     * @param enemy The enemy to check collisions with.
     */
    public void checkCollisions(Enemy enemy) {
        for (NormalNote note: notes) {
            if (enemy.isActive() && note.isActive() && enemy.collidesWithNote(location, note)
                && !(note instanceof Bomb)) {
                note.deactivate();
            }
        }
    }
}
