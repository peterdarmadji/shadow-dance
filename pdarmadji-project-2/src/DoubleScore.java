import bagel.Input;
import bagel.Keys;

/**
 * Represents a special note of type DoubleScore in the game.
 * DoubleScore notes, when activated, can potentially double the player's score for a certain duration.
 */
public class DoubleScore extends SpecialNote {
    /**
     * Constructs a new DoubleScore note with a specified direction and appearance frame.
     * @param dir             The direction (or type) of the lane this note belongs to.
     * @param appearanceFrame The frame in which this note should appear.
     */
    public DoubleScore(String dir, int appearanceFrame) {
        super(dir, appearanceFrame);
        setSpecialType(Accuracy.DOUBLE_SCORE);
    }

    /**
     * Checks and calculates the score for the DoubleScore note based on the player's input and its position relative to the target.
     * @param input         The player's input.
     * @param accuracy      The accuracy metric for the game.
     * @param targetHeight  The target height for notes in the lane.
     * @param relevantKey   The key associated with the lane this note belongs to.
     * @return              The score value for this DoubleScore note.
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), Accuracy.DOUBLE_SCORE);
            if (score != Accuracy.NOT_SCORED) {
                if (score == Accuracy.SPECIAL_SCORE) accuracy.activateDoubleScore();
                deactivate();
                return 0;
            }
        }
        return 0;
    }

}
