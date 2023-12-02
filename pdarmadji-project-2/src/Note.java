import bagel.Input;
import bagel.Keys;

/**
 * Represents the foundational structure of a note in the game.
 * This is an abstract class that provides the common functionalities and attributes for its child classes,
 * which are the different types of notes.
 */
public abstract class Note {
    // Change to 2 for 120hz
    private int speed = 2;
    private int appearanceFrame;
    private int y;
    private boolean active = false;
    private boolean completed = false;

    /**
     * Sets the note to an inactive state and marks it as completed.
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /**
     * Updates the state and position of the note.
     * Activates the note when it's time for it to appear.
     */
    public void update() {
        if (active) {
            y += speed;
        }

        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    /**
     * Checks and calculates the score based on the player's input and the note's position relative to the target.
     * @param input         The player's input.
     * @param accuracy      The accuracy metric for the game.
     * @param targetHeight  The target height for notes in the lane.
     * @param relevantKey   The key associated with the lane this note belongs to.
     * @return              The score value for this note.
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateScore(getY(), targetHeight, input.wasPressed(relevantKey));
            score *= accuracy.getDoubleScoreMultiplier();
            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            }
        }
        return 0;
    }

    /**
     * Adjusts the speed of the note.
     * @param speed The amount to adjust the speed.
     */
    public void setSpeed(int speed) {
        this.speed += speed;
    }

    /**
     * Sets the frame at which the note should appear.
     * @param appearanceFrame The new appearance frame for the note.
     */
    public void setAppearanceFrame(int appearanceFrame) {
        this.appearanceFrame = appearanceFrame;
    }

    /**
     * Retrieves the y-coordinate position of the note.
     * @return The y-coordinate of the note.
     */
    public int getY() {
        return y;
    }

    /**
     * Adjusts the y-coordinate position of the note.
     * @param y The amount to adjust the y-coordinate.
     */
    public void setY(int y) {
        this.y += y;
    }

    /**
     * Checks if the note is currently active.
     * @return True if the note is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Checks if the note has been completed/hit.
     * @return True if the note is completed, false otherwise.
     */
    public boolean isCompleted() {
        return completed;
    }

}
