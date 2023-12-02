import bagel.*;

/**
 * Represents a special note of type Bomb in the game.
 * Bomb notes, when activated, have unique effects in comparison to regular notes.
 */
public class Bomb extends SpecialNote {

    /**
     * Constructs a new Bomb note with a specified direction and appearance frame.
     * @param dir             The direction (or type) of the lane this note belongs to.
     * @param appearanceFrame The frame in which this note should appear.
     */
    public Bomb(String dir, int appearanceFrame) {
        super(dir, appearanceFrame);
        this.setSpecialType(Accuracy.BOMB);
    }

    /**
     * Checks and calculates the score for the Bomb note based on the player's input and its position relative to the target.
     * @param input         The player's input.
     * @param accuracy      The accuracy metric for the game.
     * @param targetHeight  The target height for notes in the lane.
     * @param relevantKey   The key associated with the lane this note belongs to.
     * @return              The score value for this Bomb note. Always returns 0 because bomb note does not have any score.
     */
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            int score = accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), getSpecialType());
            if (score != Accuracy.NOT_SCORED) {
                if (score == Accuracy.SPECIAL_SCORE) accuracy.setBomb(true);
                deactivate();
                return 0;
            }
        }
        return 0;
    }

}

