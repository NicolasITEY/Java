package core.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import core.Board;
import core.Position;

/**
 * Represents a pawn piece in the game.
 */
public class Pawn extends Pieces implements Serializable {

    /** Indicates whether the pawn has made its first move. */
    private boolean isFirstMove = true;

    /*
    =============================
    -------- Constructor --------
    =============================
     */

    /**
     * Constructor for a Pawn piece
     * 
     * @param board the board to set
     * @param isWhite if a piece is white
     */
    public Pawn(Board board, boolean isWhite) {
        super(board, isWhite, 1, setRep(isWhite), setPatternPawn(isWhite));
    }

    /*
    =============================
    ---------- Getters ----------
    =============================
     */

    /**
     * Getter for the isFirstMove property, which indicates whether the pawn has made its first move (used for en passant rules).
     * 
	 * @return the boolean isFirstMove
	 */
	public boolean isFirstMove() {
		return isFirstMove;
	}

    /*
    =============================
    ---------- Setters ----------
    =============================
     */

	/**
     * Setter for the isFirstMove property, which indicates whether the pawn has made its first move (used for en passant rules).
     * 
	 * @param isFirstMove the boolean isFirstMove to set
	 */
	public void setFirstMove(boolean isFirstMove) {
		this.isFirstMove = isFirstMove;
	}

    /*
    =============================
    ---------- Methods ----------
    =============================
     */

    /**
     * Method to set the string representation of a piece based on its color
     * 
     * @param isWhite boolean that represent the color of a piece
     * 
     * @return String representation of a piece based of the color
     */
    private static String setRep(boolean isWhite) {
        if (isWhite) {
            return "P";
        } else {
            return "p";
        }
    }
    
    /**
     * Method to set the pattern of a Pawn piece based on its color, which includes the possible moves and captures for the pawn.
     * 
     * @param isWhite boolean that represent the color of a piece
     * 
     * @return List containing the pattern for a piece
     */
    private static List<Position> setPatternPawn(boolean isWhite){
    	ArrayList<Position> p_pattern = new ArrayList<>();
    	if (isWhite) {
            p_pattern.add(new Position(-1, -1));  // Seulement quand un pion est sur la case
            p_pattern.add(new Position(0, -1));
            p_pattern.add(new Position(0, -2));
            p_pattern.add(new Position(1, -1));  // Seulement quand un pion est sur la case
            
        } else {
            p_pattern.add(new Position(-1, 1));  // Seulement quand un pion est sur la case
            p_pattern.add(new Position(0, 1));
            p_pattern.add(new Position(0, 2));
            p_pattern.add(new Position(1, 1));  // Seulement quand un pion est sur la case
        }
        return p_pattern;
    }

    /**
     * Moves the pawn to the specified position.
     * 
     * @param x the x-coordinate of the destination position
     * @param y the y-coordinate of the destination position
     * 
     * @return true if the move was successful, false otherwise
     */
    @Override
    public boolean move(int x, int y) {
        int previousY = getPosition().getY();
        //int prevX = getPosition().getX();
        boolean moved = super.move(x, y);
        int delta = previousY - y;
        if (delta < 0) {
            delta = -delta;
        }
        if (moved && delta == 2) {
            getBoard().setEnpassant(new Position(getPosition().getX(), isWhite() ? getPosition().getY() + 1 : getPosition().getY() - 1), this);
        }

        setFirstMove(false);
        return moved;
    }
    
}
