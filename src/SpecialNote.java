/**
 * Represents a special type of note in the game, derived from the standard NormalNote.
 * The SpecialNote class is intended to represent notes that have special effects or functionalities
 * that differentiate them from regular notes.
 */
public class SpecialNote extends NormalNote {
    private String specialType;
    /**
     * Constructs a new SpecialNote with a specified direction and appearance frame.
     * @param dir             The direction (or type) of the lane this note belongs to.
     * @param appearanceFrame The frame in which this note should appear.
     */
    public SpecialNote(String dir, int appearanceFrame) {
        super(dir, appearanceFrame);
    }

    /**
     * Retrieves the special type of this note.
     * @return The special type of the note.
     */
    public String getSpecialType() {
        return specialType;
    }

    /**
     * Sets the special type of this note.
     * @param specialType The special type to set for this note.
     */
    public void setSpecialType(String specialType) {
        this.specialType = specialType;
    }
}
