import bagel.Font;
import bagel.Input;

/**
 * Represents a base class for game levels. Provides common attributes and methods for managing the level.
 * The Level class serves as an abstract representation of a game level, capturing shared functionalities such
 * as score tracking, level status (paused, finished), and ability to update game state.
 */
public abstract class Level {
    private static final String FONT_FILE = "res/FSO8BITR.ttf";
    /**
     * Location for displaying scores.
     */
    public static final int SCORE_LOCATION = 35;
    /**
     * Initial frame counter is always set to 0
     */
    public static final int INITIAL_FRAME = 0;
    /**
     * Font object for score display.
     */
    public final Font SCORE_FONT = new Font(FONT_FILE, 30);
    private int clearScore;
    private int score;
    private boolean paused = false;
    private boolean finished = false;

    /**
     * Constructor for the Level class.
     */
    public Level() {}

    /**
     * Provides game update logic for the level.
     * @param input Player input.
     */
    public abstract void update(Input input);

    /**
     * Retrieves the paused status of the level.
     * @return {@code true} if the level is paused, otherwise {@code false}.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Sets the paused status of the level.
     * @param paused New paused status.
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Retrieves the finished status of the level.
     * @return {@code true} if the level is finished, otherwise {@code false}.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Sets the finished status of the level.
     * @param finished New finished status.
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Retrieves the current score of the level.
     * @return Current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the current score for the level.
     * @param score New score.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Retrieves the clear score threshold for the level.
     * @return Clear score threshold.
     */
    public int getClearScore() {
        return clearScore;
    }

    /**
     * Sets the clear score threshold for the level.
     * @param clearScore New clear score threshold.
     */
    public void setClearScore(int clearScore) {
        this.clearScore = clearScore;
    }

    /**
     * Reads game data from a CSV file for the level.
     */
    public abstract void readCsv();
}