import bagel.Input;
import bagel.Keys;

/**
 * Represents a SpeedUp type of special note in the game, derived from the SpecialNote class.
 * The SpeedUp class is a unique type of SpecialNote that, when activated, affects the speed of the gameplay.
 * The effects and conditions for activation are determined by the game's scoring and accuracy mechanics.
 */
public class SpeedUp extends  SpecialNote {
    /**
     * Value for speedUp speed modifier
     */
    public static int SPEED_UP_MODIFIER = 1;
    /**
     * Constructs a new SpeedUp note with a specified direction and appearance frame.
     * @param dir             The direction (or type) of the lane this note belongs to.
     * @param appearanceFrame The frame in which this note should appear.
     */
    public SpeedUp(String dir, int appearanceFrame) {
        super(dir, appearanceFrame);
        setSpecialType(Accuracy.SPEED_UP);
    }

    /**
     * Evaluates and returns the score based on the user's input accuracy for this special note.
     * Additionally, if the accuracy meets a certain threshold, the game's speed will be affected.
     * @param input         The player's input.
     * @param accuracy      The accuracy metric for the game.
     * @param targetHeight  The target height or position for optimal note activation.
     * @param relevantKey   The key that corresponds to this note's activation.
     * @return              The score value for this special note based on user's input accuracy.
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), Accuracy.SPEED_UP);
            if (score != Accuracy.NOT_SCORED) {
                if (score == Accuracy.SPECIAL_SCORE) accuracy.setSpeedUp(true);
                deactivate();
                return score;
            }
        }
        return 0;
    }
}
