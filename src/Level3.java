import bagel.Input;
import bagel.Keys;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Represents Level 3 of the game, extending the base Level class.
 * In this level, apart from regular lanes, there's a special lane handling various
 * types of special notes. Moreover, the level introduces enemies and the guardian
 * character that can interact with the game's notes and the environment.
 */
public class Level3 extends Level {
    private static final int ENEMIES_SPAWN_FRAME = 600;
    private static final int LEVEL3_CLEAR_SCORE = 350;
    private final String csvFile;
    private final Accuracy accuracy = new Accuracy();
    private final Lane[] lanes = new Lane[4];
    private SpecialLane specialLanes;
    private int numLanes = 0;
    private final Track track;
    private final Guardian guardian = new Guardian();
    private final ArrayList<Enemy> enemies = new ArrayList<>();

    /**
     * Constructs a new instance of Level 3. Reads note patterns from the CSV file,
     * initializes the music track, and starts the gameplay.
     */
    public Level3() {
        csvFile = "res/level3.csv";
        //csvFile = "res/test3.csv";
        readCsv();
        setClearScore(LEVEL3_CLEAR_SCORE);
        track = new Track("res/track3.wav");
        track.start();
        ShadowDance.setCurrFrame(INITIAL_FRAME);
    }

    /* Check if the all the notes is already deactivated */
    private boolean checkFinished() {
        for (int i = 0; i < numLanes; i++) {
            if (!lanes[i].isFinished()) {
                return false;
            }
        }

        if (!specialLanes.isFinished()) {
            return false;
        }

        return true;
    }

    /* The behaviour when pausing the game */
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
     * Reads note patterns from the CSV file and initializes the lanes, special lane,
     * and corresponding notes. This method determines note types, spawn timings, and
     * their associated lanes or special lane.
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
                    // if encountering special lane then create new instance of SpecialLane type
                    if (laneType.equals("Special")) {
                        specialLanes = new SpecialLane(laneType, pos);
                        continue;
                    }
                    // else, add to lane array
                    Lane lane = new Lane(laneType, pos);
                    lanes[numLanes++] = lane;
                } else {
                    // reading notes
                    String dir = splitText[0];
                    if (splitText[0].equals("Special")) {
                        dir = splitText[1];
                    }

                    Lane lane = null;
                    for (int i = 0; i < numLanes; i++) {
                        if (lanes[i].getType().equals(dir)) {
                            lane = lanes[i];
                        }
                    }

                    if (lane != null && !splitText[0].equals("Special")) {
                        switch (splitText[1]) {
                            case "Normal":
                                NormalNote note = new NormalNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(note);
                                break;
                            case "Hold":
                                HoldNote holdNote = new HoldNote(dir, Integer.parseInt(splitText[2]));
                                lane.addHoldNote(holdNote);
                                break;
                            case "Bomb":
                                Bomb bombNote = new Bomb("Bomb", Integer.parseInt(splitText[2]));
                                lane.addNote(bombNote);
                                break;
                        }
                    }

                    else if (specialLanes != null && splitText[0].equals("Special")) {
                        switch (splitText[1]) {
                            case "DoubleScore":
                                String strDoubleScore = "2x";
                                DoubleScore doubleScore = new DoubleScore(strDoubleScore, Integer.parseInt(splitText[2]));
                                specialLanes.addSpecialNote(doubleScore);
                                break;
                            case "SpeedUp":
                                SpeedUp speedUp = new SpeedUp(dir, Integer.parseInt(splitText[2]));
                                specialLanes.addSpecialNote(speedUp);
                                break;
                            case "SlowDown":
                                SlowDown slowDown = new SlowDown(dir, Integer.parseInt(splitText[2]));
                                specialLanes.addSpecialNote(slowDown);
                                break;
                            case "Bomb":
                                Bomb bombNote = new Bomb("Bomb", Integer.parseInt(splitText[2]));
                                specialLanes.addSpecialNote(bombNote);
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
     * Provides the update logic for Level 3, managing note movement, user input, score
     * computations, and game state changes (e.g., pausing). It also updates the game's state
     * based on the effects of special notes and introduces interactions with enemies and
     * the guardian character.
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

                // normal note special effect update
                for (int j = 0; j < lanes[i].getNotes().size(); j++) {
                    if (accuracy.isSpeedUp()) {
                        lanes[i].getNotes().get(j).setSpeed(SpeedUp.SPEED_UP_MODIFIER);
                    }
                    else if (accuracy.isSlowDown()) {
                        lanes[i].getNotes().get(j).setSpeed(SlowDown.SLOW_DOWN_MODIFIER);
                    }
                }

                // hold note special effect update
                for (int j = 0; j < lanes[i].getHoldNotes().size(); j++) {
                    if (accuracy.isSpeedUp()) {
                        lanes[i].getHoldNotes().get(j).setSpeed(SpeedUp.SPEED_UP_MODIFIER);
                    }
                    else if (accuracy.isSlowDown()) {
                        lanes[i].getHoldNotes().get(j).setSpeed(SlowDown.SLOW_DOWN_MODIFIER);
                    }
                }

                // calculate scoring for normal notes and hold notes
                setScore(getScore() + lanes[i].update(input, accuracy));
            }

            // special note special effect update
            for (int i = 0; i < specialLanes.getSpecialNotes().size(); i++) {
                if (accuracy.isSpeedUp()) {
                    specialLanes.getSpecialNotes().get(i).setSpeed(SpeedUp.SPEED_UP_MODIFIER);
                }
                else if (accuracy.isSlowDown()) {
                    specialLanes.getSpecialNotes().get(i).setSpeed(SlowDown.SLOW_DOWN_MODIFIER);
                }
            }

            // turn off some special notes after certain periods
            accuracy.setSpeedUp(false);
            accuracy.setSlowDown(false);

            // when encountering double score note
            if (!accuracy.getDoubleScoreEndFrames().isEmpty() && ShadowDance.getCurrFrame()
                    > accuracy.getDoubleScoreEndFrames().peek()) {
                accuracy.getDoubleScoreEndFrames().poll();
                accuracy.setDoubleScoreStackCount(accuracy.getDoubleScoreStackCount() - 1);
                accuracy.setDoubleScoreMultiplier(accuracy.getDoubleScoreMultiplier() / 2);
            }

            // calculate scoring for special notes
            int currScore = specialLanes.update(input, accuracy);
            if (currScore != Accuracy.MISS_SCORE) {
                setScore(getScore() + currScore);
            }

            guardian.update(input, enemies);
            for (Enemy enemy : enemies) {
                enemy.update();
                for (int i = 0; i < numLanes; i++) {
                    lanes[i].checkCollisions(enemy);
                }
            }
            if (ShadowDance.getCurrFrame() % ENEMIES_SPAWN_FRAME == 0) {
                enemies.add(new Enemy());
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
