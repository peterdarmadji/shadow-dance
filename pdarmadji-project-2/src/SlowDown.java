import bagel.Input;
import bagel.Keys;

/**
 * Represents the SlowDown special note in the game.
 * The SlowDown note, when hit with accuracy, triggers the slow down effect in the game.
 */
public class SlowDown extends SpecialNote {
    /**
     * Value for slowDown speed modifier
     */
    public static int SLOW_DOWN_MODIFIER = -1;
    /**
     * Constructs a new SlowDown note with a specified direction and appearance frame.
     * @param dir             The direction (or type) of the lane this note belongs to.
     * @param appearanceFrame The frame in which this note should appear.
     */
    public SlowDown(String dir, int appearanceFrame) {
        super(dir, appearanceFrame);
        setSpecialType(Accuracy.SLOW_DOWN);
    }

    /**
     * Checks and calculates the score for this SlowDown note based on the player's input and the note's position relative to the target.
     * If scored with maximum accuracy, the slow down effect is triggered in the game.
     * @param input         The player's input.
     * @param accuracy      The accuracy metric for the game.
     * @param targetHeight  The target height for notes in the lane.
     * @param relevantKey   The key associated with the lane this note belongs to.
     * @return              The score value for this SlowDown note.
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), Accuracy.SLOW_DOWN);
            if (score != Accuracy.NOT_SCORED) {
                if (score == Accuracy.SPECIAL_SCORE) accuracy.setSlowDown(true);
                deactivate();
                return score;
            }
        }
        return 0;
    }
}
