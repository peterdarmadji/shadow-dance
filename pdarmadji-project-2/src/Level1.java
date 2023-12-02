import bagel.Input;
import bagel.Keys;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Represents Level 1 of the game. Extends the base Level class to add specific functionalities
 * and configurations pertaining to the first level.
 * This class encompasses the behavior, note patterns, and music track of the first game level.
 * It handles note spawning based on a CSV file, tracks the player's score, and updates game entities accordingly.
 */
public class Level1 extends Level {
    private static final int LEVEL1_CLEAR_SCORE = 150;
    private final String csvFile;
    private final Accuracy accuracy = new Accuracy();
    private final Lane[] lanes = new Lane[4];
    private int numLanes = 0;
    private final Track track;

    /**
     * Constructs a new Level 1 instance. Initializes the track, sets the current frame,
     * and reads the note patterns from the CSV file.
     */
    public Level1() {
        csvFile = "res/level1.csv";
        //csvFile = "res/test1.csv";
        readCsv();
        setClearScore(LEVEL1_CLEAR_SCORE);
        track = new Track("res/track1.wav");
        track.start();
        ShadowDance.setCurrFrame(INITIAL_FRAME);
    }

    /* Checks if all notes in all lanes have been deactivated, indicating the level's completion. */
    private boolean checkFinished() {
        for (int i = 0; i < numLanes; i++) {
            if (!lanes[i].isFinished()) {
                return false;
            }
        }
        return true;
    }

    /* Manages the behavior when the game is paused, such as rendering static lanes. */
    private void pauseGameBehaviour(Input input) {
        if (input.wasPressed(Keys.TAB)) {
            setPaused(false);
            track.run();
        }

        // draw normal lanes
        for (int i = 0; i < numLanes; i++) {
            lanes[i].draw();
        }
    }

    /**
     * Reads note patterns from the CSV file and populates the lanes with corresponding notes.
     * This method determines note types, spawn timings, and their associated lanes.
     */
    @Override
    public void readCsv() {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    String laneType = splitText[1];
                    int pos = Integer.parseInt(splitText[2]);
                    // add to lane array
                    Lane lane = new Lane(laneType, pos);
                    lanes[numLanes++] = lane;
                } else {
                    // reading notes
                    String dir = splitText[0];
                    Lane lane = null;
                    for (int i = 0; i < numLanes; i++) {
                        if (lanes[i].getType().equals(dir)) {
                            lane = lanes[i];
                        }
                    }

                    if (lane != null) {
                        switch (splitText[1]) {
                            case "Normal":
                                NormalNote note = new NormalNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(note);
                                break;
                            case "Hold":
                                HoldNote holdNote = new HoldNote(dir, Integer.parseInt(splitText[2]));
                                lane.addHoldNote(holdNote);
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    /**
     * Provides the update logic for Level 1, handling note movement, user input, score computation,
     * and game state changes (e.g., pausing).
     * @param input Player input.
     */
    @Override
    public void update(Input input) {
        // gameplay
        SCORE_FONT.drawString("Score " + getScore(), SCORE_LOCATION, SCORE_LOCATION);

        // when pausing the screen behaviour
        if (isPaused()) {
            pauseGameBehaviour(input);
        }

        // main game behaviour
        else {
            ShadowDance.setCurrFrame(ShadowDance.getCurrFrame() + 1);

            // update the normal and hold notes behaviour
            for (int i = 0; i < numLanes; i++) {
                // calculate scoring for normal notes and hold notes
                setScore(getScore() + lanes[i].update(input, accuracy));
            }
            accuracy.update();
            setFinished(checkFinished());

            // toggle pausing the game
            if (input.wasPressed(Keys.TAB)) {
                setPaused(true);
                track.pause();
            }
        }
    }
}
