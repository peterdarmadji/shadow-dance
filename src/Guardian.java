import bagel.*;
import bagel.util.Vector2;
import java.util.ArrayList;

/**
 * Represents the Guardian in the game.
 * The Guardian is an entity that can shoot projectiles towards enemies. The Guardian always targets the nearest active enemy.
 */
public class Guardian extends GameEntity {
    private static final int POSITION_X = 800;
    private static final int POSITION_Y = 600;
    private final Image GUARDIAN_IMAGE = new Image("res/guardian.png");
    private final ArrayList<Projectile> projectiles = new ArrayList<>();

    /**
     * Constructs a new Guardian and sets its position.
     */
    public Guardian() {
        setPositionVector(new Vector2(POSITION_X, POSITION_Y));
    }

    /**
     * Updates the state of the Guardian.
     * This method draws the Guardian image, updates all its projectiles, and checks if the player has given the command to shoot a projectile at the nearest enemy.
     * @param input   The player's input.
     * @param enemies The list of enemies in the game.
     */
    public void update(Input input, ArrayList<Enemy> enemies) {
        GUARDIAN_IMAGE.draw(POSITION_X, POSITION_Y);
        for (Projectile projectile: projectiles) {
            projectile.update(enemies);
        }
        if (input.wasPressed(Keys.LEFT_SHIFT)) {
            Enemy nearestEnemy = findNearestEnemy(enemies);
            if (nearestEnemy != null) {
                Projectile projectile = new Projectile(getPositionVector(), nearestEnemy.getPositionVector());
                projectile.setActive(true);
                projectiles.add(projectile);
            }
        }
    }

    /**
     * Finds the nearest active enemy to the Guardian.
     * @param enemies The list of enemies in the game.
     * @return The nearest active enemy or null if no active enemy is found.
     */
    private Enemy findNearestEnemy(ArrayList<Enemy> enemies) {
        double minDistance = Double.MAX_VALUE;
        Enemy nearestEnemy = null;
        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                double distance = enemy.distanceBetween(enemy.getPositionVector(), getPositionVector());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestEnemy = enemy;
                }
            }
        }
        return nearestEnemy;
    }
}
