package core.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import core.Board;
import core.Position;

/** 
 * Represents a queen piece on the game board.
 */
public class Queen extends Pieces implements Serializable {

    /*
    =============================
    -------- Constructor --------
    =============================
    */

    /**
     * Constructor for a Queen object.
     * 
     * @param board the game board
     * @param isWhite a boolean indicating whether the queen is white or black
     */
    public Queen(Board board, boolean isWhite) {
        super(board, isWhite, Integer.max(board.getHeight(), board.getWidth()), setRep(isWhite), setPatternQueen());
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
            return "Q";
        } else {
            return "q";
        }
    }
    
    /**
     * Method to set the pattern of a Queen piece, which includes the possible moves in all eight directions (horizontal, vertical, and diagonal).
     * 
     * @return List containing the pattern for a piece
     */
    private static List<Position> setPatternQueen(){
    	ArrayList<Position> p_pattern = new ArrayList<>();
    	p_pattern.add(new Position(-1, -1));
        p_pattern.add(new Position(-1, 0));
        p_pattern.add(new Position(-1, 1));
        p_pattern.add(new Position(0, -1));
        p_pattern.add(new Position(0, 1));
        p_pattern.add(new Position(1, -1));
        p_pattern.add(new Position(1, 0));
        p_pattern.add(new Position(1, 1));
        return p_pattern;
    }

}
