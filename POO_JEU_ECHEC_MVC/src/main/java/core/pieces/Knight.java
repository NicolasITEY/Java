package core.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import core.Board;
import core.Position;

/**
 * Class representing a Knight piece
 */
public class Knight extends Pieces implements Serializable {

    /*
    =============================
    -------- Constructor --------
    =============================
    */

    /**
     * Constructor for a Knight piece
     * 
     * @param board the board to set
     * @param isWhite if a piece is white
     */
    public Knight(Board board, boolean isWhite) {
    	super(board, isWhite, 1, setRep(isWhite), setPatternKnight());
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
            return "N";
        } else {
            return "n";
        }
    }
    
    /**
     * Method to set the pattern of a Knight piece
     * 
     * @return List containing the pattern for a piece
     */
    private static List<Position> setPatternKnight(){
    	ArrayList<Position> p_pattern = new ArrayList<>();
    	p_pattern.add(new Position(-2, -1));
        p_pattern.add(new Position(-2, 1));
        p_pattern.add(new Position(-1, -2));
        p_pattern.add(new Position(-1, 2));
        p_pattern.add(new Position(1, -2));
        p_pattern.add(new Position(1, 2));
        p_pattern.add(new Position(2, -1));
        p_pattern.add(new Position(2, 1));
        return p_pattern;
    }

}
