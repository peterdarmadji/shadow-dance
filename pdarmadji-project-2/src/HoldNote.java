import bagel.*;

/**
 * Represents a hold note in the game.
 * Hold notes are a type of note that requires players to hold a key for a duration rather than just pressing it.
 * Scoring a hold note involves assessing player's performance at both the start and end of the hold.
 */
public class HoldNote extends Note {
    private static final int HEIGHT_OFFSET = 82;
    private static final int HOLD_NOTE_INITIAL_Y = 24;
    private final Image image;
    private boolean holdStarted = false;

    /**
     * Constructs a new HoldNote with a specified direction and appearance frame.
     * @param dir             The direction (or type) of the lane this note belongs to.
     * @param appearanceFrame The frame in which this note should appear.
     */
    public HoldNote(String dir, int appearanceFrame) {
        image = new Image("res/holdNote" + dir + ".png");
        this.setAppearanceFrame(appearanceFrame);
        this.setY(HOLD_NOTE_INITIAL_Y);
    }

    /**
     * Indicates that the hold action for this note has started.
     */
    public void startHold() {
        holdStarted = true;
    }

    /**
     * Draws the hold note at the specified x-coordinate if the note is active.
     * @param x The x-coordinate to draw the note.
     */
    public void draw(int x) {
        if (isActive()) {
            image.draw(x, getY());
        }
    }

    /**
     * Checks and calculates the score for this hold note.
     * This note can be scored twice: once at the start of the hold and once at the end.
     * @param input         The player's input.
     * @param accuracy      The accuracy metric for the game.
     * @param targetHeight  The target height for notes in the lane.
     * @param relevantKey   The key associated with the lane this note belongs to.
     * @return              The score value for this hold note.
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive() && !holdStarted) {
            int score = accuracy.evaluateScore(getBottomHeight(), targetHeight, input.wasPressed(relevantKey));
            score *= accuracy.getDoubleScoreMultiplier();

            if (score == Accuracy.MISS_SCORE) {
                deactivate();
                return score;
            } else if (score != Accuracy.NOT_SCORED) {
                startHold();
                return score;
            }
        } else if (isActive() && holdStarted) {
            int score = accuracy.evaluateScore(getTopHeight(), targetHeight, input.wasReleased(relevantKey));
            score *= accuracy.getDoubleScoreMultiplier();

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            } else if (input.wasReleased(relevantKey)) {
                deactivate();
                accuracy.setAccuracy(Accuracy.MISS);
                return Accuracy.MISS_SCORE;
            }
        }

        return 0;
    }

    /**
     * Retrieves the y-coordinate representing the start (bottom) of the hold note.
     * @return The y-coordinate of the start of the note.
     */
    public int getBottomHeight() {
        return getY() + HEIGHT_OFFSET;
    }

    /**
     * Retrieves the y-coordinate representing the end (top) of the hold note.
     * @return The y-coordinate of the end of the note.
     */
    public int getTopHeight() {
        return getY() - HEIGHT_OFFSET;
    }
}
