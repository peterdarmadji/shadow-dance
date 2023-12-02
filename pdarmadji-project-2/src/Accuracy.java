import bagel.*;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents the accuracy of pressing the notes in the game.
 * This class determines the score and feedback based on how accurately a note is pressed
 * in relation to its target position.
 */
public class Accuracy {
    /* Various constants for score values and feedback messages */
    /**
     * Special constant for score
     */
    public static final int SPECIAL_SCORE = 15;
    private static final int PERFECT_SCORE = 10;
    private static final int GOOD_SCORE = 5;
    private static final int BAD_SCORE = -1;
    /**
     * Score decrement when a note is missed.
     */
    public static final int MISS_SCORE = -5;
    /**
     * Default score representing an un-scored event or action.
     */
    public static final int NOT_SCORED = 0;
    private static final String PERFECT = "PERFECT";
    private static final String GOOD = "GOOD";
    private static final String BAD = "BAD";
    /**
     * Label representing a missed note.
     */
    public static final String MISS = "MISS";
    /**
     * Label for a special note that doubles the score for a certain time frame.
     */
    public static final String DOUBLE_SCORE = "DOUBLE SCORE";
    /**
     * Label for a special note that slows down other notes for a certain time frame.
     */
    public static final String SLOW_DOWN = "SLOW DOWN";
    /**
     * Label for a special note that speeds up other notes for a certain time frame.
     */
    public static final String SPEED_UP = "SPEED UP";
    /**
     * Label for a special note which has a bomb effect in gameplay.
     */
    public static final String BOMB = "BOMB";
    private static final String LANE_CLEAR = "LANE CLEAR";
    private static final int PERFECT_RADIUS = 15;
    private static final int GOOD_RADIUS = 50;
    private static final int SPECIAL_RADIUS = 50;
    private static final int BAD_RADIUS = 100;
    private static final int MISS_RADIUS = 200;

    private static final Font ACCURACY_FONT = new Font(ShadowDance.FONT_FILE, 40);
    private static final int RENDER_FRAMES = 30;
    private String currAccuracy = null;
    private int frameCount = 0;

    /* Boolean flags for various power-ups and effects */
    private boolean SlowDown = false;
    private boolean SpeedUp = false;
    private boolean Bomb = false;

    /* Double score properties */
    private int doubleScoreMultiplier = 1;
    private int doubleScoreStackCount = 0;
    private Queue<Integer> doubleScoreEndFrames = new LinkedList<>();

    /**
     * Activates the Double Score effect, updating the multiplier and end frame accordingly.
     */
    public void activateDoubleScore() {
        doubleScoreMultiplier *= 2;
        doubleScoreStackCount += 1;
        doubleScoreEndFrames.add(ShadowDance.getCurrFrame() + 450);
    }

    /**
     * @return The current multiplier value for the score.
     */
    public int getDoubleScoreMultiplier() {
        return doubleScoreMultiplier;
    }

    /**
     * Sets the current multiplier value for the score.
     * @param doubleScoreMultiplier The new multiplier value.
     */
    public void setDoubleScoreMultiplier(int doubleScoreMultiplier) {
        this.doubleScoreMultiplier = doubleScoreMultiplier;
    }

    /**
     * @return The current count of stacked "Double Score" effects.
     */
    public int getDoubleScoreStackCount() {
        return doubleScoreStackCount;
    }

    /**
     * Sets the count of stacked "Double Score" effects.
     * @param doubleScoreStackCount The new stack count value.
     */
    public void setDoubleScoreStackCount(int doubleScoreStackCount) {
        this.doubleScoreStackCount = doubleScoreStackCount;
    }

    /**
     * @return The queue containing end frames for all active "Double Score" effects.
     */
    public Queue<Integer> getDoubleScoreEndFrames() {
        return doubleScoreEndFrames;
    }

    /**
     * Retrieves the status of the SlowDown effect.
     * @return True if SlowDown effect is active, false otherwise.
     */
    public boolean isSlowDown() {
        return SlowDown;
    }

    /**
     * Sets the status of the SlowDown effect.
     * @param slowDown True to activate the SlowDown effect, false to deactivate.
     */
    public void setSlowDown(boolean slowDown) {
        SlowDown = slowDown;
    }

    /**
     * Retrieves the status of the SpeedUp effect.
     * @return True if SpeedUp effect is active, false otherwise.
     */
    public boolean isSpeedUp() {
        return SpeedUp;
    }

    /**
     * Sets the status of the SpeedUp effect.
     * @param speedUp True to activate the SpeedUp effect, false to deactivate.
     */
    public void setSpeedUp(boolean speedUp) {
        SpeedUp = speedUp;
    }

    /**
     * Retrieves the status of the Bomb effect.
     * @return True if Bomb effect is active, false otherwise.
     */
    public boolean isBomb() {
        return Bomb;
    }

    /**
     * Sets the status of the Bomb effect.
     * @param bomb True to activate the Bomb effect, false to deactivate.
     */
    public void setBomb(boolean bomb) {
        Bomb = bomb;
    }

    /**
     * Sets the current accuracy feedback message.
     * @param accuracy The accuracy feedback message to set.
     */
    public void setAccuracy(String accuracy) {
        currAccuracy = accuracy;
        frameCount = 0;
    }

    /**
     * Evaluates the score based on the distance between the note's height and target height.
     * @param height       The height at which the note was pressed.
     * @param targetHeight The target height of the note.
     * @param triggered    Whether the note was triggered by player input.
     * @return             The score value corresponding to the accuracy.
     */
    public int evaluateScore(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= PERFECT_RADIUS) {
                setAccuracy(PERFECT);
                return PERFECT_SCORE;
            } else if (distance <= GOOD_RADIUS) {
                setAccuracy(GOOD);
                return GOOD_SCORE;
            } else if (distance <= BAD_RADIUS) {
                setAccuracy(BAD);
                return BAD_SCORE;
            } else if (distance <= MISS_RADIUS) {
                setAccuracy(MISS);
                return MISS_SCORE;
            }

        } else if (height >= (Window.getHeight())) {
            setAccuracy(MISS);
            return MISS_SCORE;
        }

        return NOT_SCORED;

    }

    /**
     * Evaluates the special score for notes that have power-ups/effects.
     * @param height       The height at which the note was pressed.
     * @param targetHeight The target height of the note.
     * @param triggered    Whether the note was triggered by player input.
     * @param specialType  The type of power-up/effect the note has.
     * @return             The special score value.
     */
    public int evaluateSpecialScore(int height, int targetHeight, boolean triggered, String specialType) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= SPECIAL_RADIUS) {
                switch (specialType) {
                    case SLOW_DOWN:
                        setAccuracy(SLOW_DOWN);
                        break;
                    case SPEED_UP:
                        setAccuracy(SPEED_UP);
                        break;
                    case DOUBLE_SCORE:
                        setAccuracy(DOUBLE_SCORE);
                        break;
                    case BOMB:
                        setAccuracy(LANE_CLEAR);
                        break;
                }
                return SPECIAL_SCORE;
            }
        } else if (height >= (Window.getHeight())) {
            return MISS_SCORE;
        }
        return NOT_SCORED;
    }

    /**
     * Updates the display of the accuracy feedback message.
     * If the feedback message has been displayed for RENDER_FRAMES, it will be cleared.
     */
    public void update() {
        frameCount++;
        if (currAccuracy != null && frameCount < RENDER_FRAMES) {
            ACCURACY_FONT.drawString(currAccuracy,
                    Window.getWidth()/2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                    Window.getHeight()/2);
        }
    }
}
