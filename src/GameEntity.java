import bagel.util.Vector2;

/**
 * Represents a basic entity in the game with properties such as position, speed, and active status.
 * It serves as a base class for other game objects/entities that might have these shared properties.
 * Such as Enemy, Guardian, and Projectile
 */
public abstract class GameEntity {
    private boolean active = false;
    private int speed;
    private Vector2 positionVector;

    /**
     * Checks if the entity is currently active.
     * @return True if the entity is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the entity to an inactive state.
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Sets the active status of the entity.
     * @param active True to set the entity as active, false to set as inactive.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Retrieves the position of the entity.
     * @return The position vector of the entity.
     */
    public Vector2 getPositionVector() {
        return positionVector;
    }

    /**
     * Sets the position of the entity.
     * @param positionVector The new position vector for the entity.
     */
    public void setPositionVector(Vector2 positionVector) {
        this.positionVector = positionVector;
    }

    /**
     * Retrieves the speed of the entity.
     * @return The speed of the entity.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of the entity.
     * @param speed The new speed value for the entity.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
