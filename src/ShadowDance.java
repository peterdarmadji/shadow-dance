import bagel.*;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2023
 * Please enter your name below
 * @author Peter Darmadji 1360675
 */
public class ShadowDance extends AbstractGame  {
    /**
     * The ShadowDance game window's width.
     */
    public final static int WINDOW_WIDTH = 1024;
    /**
     * The ShadowDance game window's height.
     */
    public final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    /**
     * General type of font that are being used throughout all the classes.
     */
    public final static String FONT_FILE = "res/FSO8BITR.ttf";
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 100;
    private final static int INS_Y_OFFSET = 190;
    private final static int INS_LEVELS_X_OFFSET = 50;
    private final static int INS_LEVELS_Y_OFFSET = 75;
    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private static final String INSTRUCTIONS = "Select Levels With\nNumber keys";
    private static final String INSTRUCTIONS_LEVELS = "1       2       3";
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private static final String RETURN_TO_BEGINNING = "PRESS SPACE TO RETURN TO LEVEL SELECTION";
    private static int currFrame = 0;
    private boolean started = false;
    private Level level;

    /**
     * Constructor for ShadowDance.
     */
    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     * @param input the human input.
     */
    @Override
    public void update(Input input) {
        // force close the game at any time if escape key is pressed
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        // draw the background image of ShadowDance
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        // starting screen
        if (!started) {
            startingScreen(input);
        }
        // end screen
        else if (level.isFinished()) {
            endScreen(input);
        }
        // main gameplay
        else {
            System.out.println(currFrame);
            level.update(input);
        }
    }

    /* Level selection behaviour on the beginning with corresponding level object initialization */
    private void levelSelection(Input input) {
        if (input.wasPressed(Keys.NUM_1)) {
            level = new Level1();
            started = true;
        }
        else if (input.wasPressed(Keys.NUM_2)) {
            level = new Level2();
            started = true;
        }
        else if (input.wasPressed(Keys.NUM_3)) {
            level = new Level3();
            started = true;
        }
    }

    /* Starting screen behaviour */
    private void startingScreen(Input input) {
        TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
        INSTRUCTION_FONT.drawString(INSTRUCTIONS,
                TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);

        INSTRUCTION_FONT.drawString(INSTRUCTIONS_LEVELS,
                TITLE_X + INS_X_OFFSET + INS_LEVELS_X_OFFSET, TITLE_Y + INS_Y_OFFSET + INS_LEVELS_Y_OFFSET);

        // level selections
        levelSelection(input);
    }

    /* End screen behaviour */
    private void endScreen(Input input) {
        if (level.getScore() >= level.getClearScore()) {
            TITLE_FONT.drawString(CLEAR_MESSAGE,
                    WINDOW_WIDTH / 2 - TITLE_FONT.getWidth(CLEAR_MESSAGE) / 2, WINDOW_HEIGHT / 2);
        } else {
            TITLE_FONT.drawString(TRY_AGAIN_MESSAGE,
                    WINDOW_WIDTH / 2 - TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE) / 2, WINDOW_HEIGHT / 2);
        }
        INSTRUCTION_FONT.drawString(RETURN_TO_BEGINNING,
                WINDOW_WIDTH / 2 - INSTRUCTION_FONT.getWidth(RETURN_TO_BEGINNING) / 2, 500);
        if (input.wasPressed(Keys.SPACE)) {
            started = false;
        }
    }

    /**
     * Getter for current frame attribute.
     * @return current frame of the game.
     */
    public static int getCurrFrame() {
        return currFrame;
    }

    /**
     * Setter for current frame attribute.
     * @param currFrame current frame of the game.
     */
    public static void setCurrFrame(int currFrame) {
        ShadowDance.currFrame = currFrame;
    }
}