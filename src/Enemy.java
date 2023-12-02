import bagel.*;
import bagel.util.Vector2;
import java.util.Random;

/**
 * Represents an enemy in the game.
 * Enemies have a position, speed, direction, and image.
 */
public class Enemy extends GameEntity {
    private static final int SWAP_DIRECTION = -1;
    private static final Image ENEMY_IMAGE = new Image("res/enemy.png");
    private static final int LEFT_BOUND = 100;
    private static final int RIGHT_BOUND = 900;
    private static final int NOTE_COLLISION_THRESHOLD = 104;
    private static final int ENEMY_SPEED = 1;
    private int direction;

    /**
     * Constructor for Enemy class.
     * Initializes the position of the enemy, its initial direction, and sets its speed.
     */
    public Enemy() {
        Random rand = new Random();
        this.setPositionVector(new Vector2(rand.nextInt(801) + 100, rand.nextInt(401) + 100));
        setSpeed(ENEMY_SPEED);
        // choosing randomly between going left or right
        this.direction = rand.nextInt(2) * 2 - 1;
        setActive(true);
    }

    /**
     * Updates the enemy's position and draws it on the screen.
     * If the enemy reaches the screen bounds, it reverses its direction.
     */
    public void update() {
        if (isActive()) {
            // if the enemy is out of bounds, then reverse its direction
            if (getPositionVector().x <= LEFT_BOUND || getPositionVector().x >= RIGHT_BOUND) {
                direction *= SWAP_DIRECTION;
            }
            setPositionVector(new Vector2(getPositionVector().x + getSpeed() * direction, getPositionVector().y));
            ENEMY_IMAGE.draw(getPositionVector().x, getPositionVector().y);
        }
    }

    /**
     * Checks if the enemy collides with a given position based on a threshold.
     * @param otherPosition The position to check for collision.
     * @param threshold     The distance threshold for collision.
     * @return              True if the distance between enemy and the given position
     *                      is less than or equal to the threshold.
     */
    public boolean collidesWith(Vector2 otherPosition, double threshold) {
        return distanceBetween(getPositionVector(), otherPosition) <= threshold;
    }

    /**
     * Checks if the enemy collides with a note based on a given x-coordinate of the note.
     * @param noteX The x-coordinate of the note to check for collision.
     * @param note  The note instance to check for its y-coordinate.
     * @return      True if the distance between enemy and the note is less than
     *              or equal to the NOTE_COLLISION_THRESHOLD.
     */
    public boolean collidesWithNote(int noteX, NormalNote note) {
        double distance = distanceBetween(getPositionVector(), new Vector2(noteX, note.getY()));
        return distance <= NOTE_COLLISION_THRESHOLD;
    }

    /**
     * Calculates the distance between two position vectors.
     * @param positionVector1 The first position vector.
     * @param positionVector2 The second position vector.
     * @return                The Euclidean distance between the two position vectors.
     */
    public double distanceBetween(Vector2 positionVector1, Vector2 positionVector2) {
        double dx = positionVector2.x - positionVector1.x;
        double dy = positionVector2.y - positionVector1.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
