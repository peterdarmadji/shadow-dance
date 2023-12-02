import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Vector2;
import java.util.ArrayList;

/**
 * Represents the projectile entity in the game.
 * This class models projectiles that are shot towards enemies. A projectile follows a specified direction and has the ability to eliminate enemies upon collision.
 */
public class Projectile extends GameEntity{
    private static final int PROJECTILE_SPEED = 6;
    private static final Image PROJECTILE_IMAGE = new Image("res/arrow.png");
    private static final DrawOptions drawOptions = new DrawOptions();
    private final Vector2 directionVector;

    /**
     * Constructs a new Projectile with a starting position and a target position.
     * @param startPositionVector The starting position of the projectile.
     * @param targetPositionVector The position of the target towards which the projectile is aimed.
     */
    public Projectile(Vector2 startPositionVector, Vector2 targetPositionVector) {
        this.setPositionVector(new Vector2(startPositionVector.x, startPositionVector.y));
        this.directionVector = targetPositionVector.sub(getPositionVector()).normalised();
        setSpeed(PROJECTILE_SPEED);
    }

    /**
     * Updates the state and position of the projectile.
     * This method checks for collisions with enemies and also for going out of screen bounds.
     * Upon a collision with an enemy, both the projectile and the enemy are deactivated.
     * @param enemies The list of enemies in the game.
     */
    public void update(ArrayList<Enemy> enemies) {
        if (isActive()) {
            setPositionVector(getPositionVector().add(directionVector.mul(getSpeed())));

            // set the rotation direction for projectiles
            double rotation = Math.atan2(directionVector.y, directionVector.x);
            drawOptions.setRotation(rotation);
            PROJECTILE_IMAGE.draw(getPositionVector().x, getPositionVector().y, drawOptions.setRotation(rotation));

            // remove projectile and enemy when colliding with each other
            for (Enemy enemy : enemies) {
                if (enemy.collidesWith(getPositionVector(), 62) && enemy.isActive()) {
                    deactivate();
                    enemy.deactivate();
                }
            }

            // remove projectile if it hits the screen bounds
            if ((getPositionVector().x >= ShadowDance.WINDOW_WIDTH || getPositionVector().x <= 0)
                    || (getPositionVector().y >= ShadowDance.WINDOW_HEIGHT || getPositionVector().y <= 0)) {
                deactivate();
            }
        }
    }
}
