package core;

import java.io.Serializable;

/** 
 * Represents a position on the game board.
 */
public class Position implements Serializable {

    /** The x-coordinate of the position. */
    private int x;
    /** The y-coordinate of the position. */
    private int y;

    /*
    =============================
    -------- Constructor --------
    =============================
     */

    /**
     * Constructor for a Position object, which represents a specific location on the game board defined by x and y coordinates.
     * 
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
    =============================
    ---------- Getters ----------
    =============================
     */

    /**
     * Getter for the x-coordinate of the position.
     * 
	 * @return the x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
     * Getter for the y-coordinate of the position.
     * 
	 * @return the y-coordinate
	 */
	public int getY() {
		return y;
	}

    /*
    =============================
    ---------- Setters ----------
    =============================
     */

    /**
     * Method to set the position's coordinates to the specified x and y values.
     * 
     * @param x the x-coordinate to set
     * @param y the y-coordinate to set
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a string representation of the position.
     * 
     * @return a string representing the position
     */
    @Override
    public String toString() {
        return "( x = " + x + "; y = " + y + " )";
    }
    
}
