package core.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import core.Board;
import core.Position;

/** 
 * Represents a rook piece on the game board.
 */
public class Rook extends Pieces implements Serializable {

    /** Indicates whether the rook has made its first move. */
    private boolean isFirstMove = true;

    /*
    =============================
    -------- Constructor --------
    =============================
    */

    /**
     * Constructor for a Rook object.
     * 
     * @param board the game board
     * @param isWhite a boolean indicating whether the rook is white or black
     */
    public Rook(Board board, boolean isWhite) {
        super(board, isWhite, Integer.max(board.getHeight(), board.getWidth()), setRep(isWhite), setPatternRook());
    }

    /*
    =============================
    ---------- Getters ----------
    =============================
    */

    /**
     * Getter for the isFirstMove property, which indicates whether the rook has made its first move (used for castling rules).
     * 
     * @return true if the rook has not moved yet, false otherwise
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
     * Setter for the isFirstMove property, which indicates whether the rook has made its first move (used for castling rules).
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
     * Method to set the string representation of a piece based on its color.
     * 
     * @param isWhite boolean that represent the color of a piece
     * 
     * @return String representation of a piece based of the color
     */
    private static String setRep(boolean isWhite) {
        if (isWhite) {
            return "R";
        } else {
            return "r";
        }
    }

	/**
     * Method to set the pattern of a Rook piece, which includes the possible moves in the four cardinal directions (horizontal and vertical).
     * 
     * @return List containing the pattern for a piece
     */
    private static List<Position> setPatternRook(){
    	ArrayList<Position> p_pattern = new ArrayList<>();
    	p_pattern.add(new Position(-1, 0));
        p_pattern.add(new Position(0, -1));
        p_pattern.add(new Position(0, 1));
        p_pattern.add(new Position(1, 0));
        return p_pattern;
    }
}
