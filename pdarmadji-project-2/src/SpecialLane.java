import bagel.*;
import java.util.ArrayList;
import bagel.Image;

/**
 * Represents the lane in the game that contains special notes.
 * SpecialLane is an extension of the Lane class that handles special notes. These notes require different handling,
 * especially in their interaction mechanics and scoring.
 */
public class SpecialLane extends Lane {
    private final Image image = new Image("res/laneSpecial.png");
    private final ArrayList<SpecialNote> specialNotes = new ArrayList<>();
    private int currSpecialNote = 0;
    private final Keys specialRelevantKey = Keys.SPACE;

    /**
     * Constructs a new SpecialLane with a specified direction and location.
     * @param dir      The direction (or type) of the lane.
     * @param location The location (or x-coordinate) of the lane on the screen.
     */
    public SpecialLane(String dir, int location) {
        super(dir, location);
    }

    /**
     * Draws the lane and the special notes on it.
     */
    @Override
    public void draw() {
        image.draw(getLocation(), HEIGHT);
        for (int i = currSpecialNote; i < specialNotes.size(); i++) {
            specialNotes.get(i).draw(getLocation());
        }
    }

    /**
     * Deactivate all currently active special notes in the screen
     */
    @Override
    public void deactivateAffectedNotes() {
        // Deactivating special notes
        for (SpecialNote specialNote: specialNotes) {
            if (specialNote.isActive()) {
                specialNote.deactivate();
            }
        }
    }

    /**
     * Updates the state of the special lane, handling interactions and scoring for the special notes.
     * This method will also handle unique effects of special notes, such as bombs.
     * @param input    The player's input.
     * @param accuracy The accuracy metric for the game.
     * @return         The score value for the notes in this special lane.
     */
    @Override
    public int update(Input input, Accuracy accuracy) {
        draw();

        for (int i = currSpecialNote; i < specialNotes.size(); i++) {
            specialNotes.get(i).update();
        }

        if (currSpecialNote < specialNotes.size()) {
            int score = specialNotes.get(currSpecialNote).checkScore(input, accuracy, TARGET_HEIGHT, specialRelevantKey);

            // deactivate special notes when encountering bomb and set to not count bomb score
            if (specialNotes.get(currSpecialNote) instanceof Bomb && accuracy.isBomb()) {
                System.out.println("Done");
                deactivateAffectedNotes();
                score = Accuracy.NOT_SCORED;
                accuracy.setBomb(false);
            }

            if (specialNotes.get(currSpecialNote).isCompleted()) {
                currSpecialNote++;
                return score;
            }
        }
        return Accuracy.NOT_SCORED;
    }

    /**
     * Adds a new special note to this lane.
     * @param n The special note to be added.
     */
    public void addSpecialNote(SpecialNote n) {
        specialNotes.add(n);
    }


    /**
     * Retrieves the list of special notes in this lane.
     * @return The list of special notes.
     */
    public ArrayList<SpecialNote> getSpecialNotes() {
        return specialNotes;
    }

    /**
     * Checks for collisions between the special notes and a specified enemy.
     * @param enemy The enemy to check collisions against.
     */
    @Override
    public void checkCollisions(Enemy enemy) {
        for (SpecialNote specialNote : specialNotes) {
            if (enemy.isActive() && specialNote.isActive() && enemy.collidesWithNote(getLocation(), specialNote)) {
                specialNote.deactivate();
            }
        }
    }

    /**
     * Checks if all the special notes in this lane are completed (either hit or missed).
     * @return True if all special notes are completed, otherwise false.
     */
    @Override
    public boolean isFinished() {
        for (int i = 0; i < specialNotes.size(); i++) {
            if (!specialNotes.get(i).isCompleted()) {
                return false;
            }
        }

        return true;
    }
}
