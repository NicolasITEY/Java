package core.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import core.Board;
import core.Position;

/**
 * Class representing a Bishop piece
 */
public class Bishop extends Pieces implements Serializable {

    /*
    =============================
    -------- Constructor --------
    =============================
    */

    /**
     * Constructor for a Bishop piece
     * 
     * @param board the board to set
     * @param isWhite if a piece is white
     */
    public Bishop(Board board, boolean isWhite) {
        super(board, isWhite, Integer.max(board.getHeight(), board.getWidth()), setRep(isWhite), setPatternBishop());
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
            return "B";
        } else {
            return "b";
        }
    }
    
    /**
     * Method to set the pattern of a Bishop piece
     * 
     * @return List containing the pattern for a piece
     */
    private static List<Position> setPatternBishop(){
    	ArrayList<Position> p_pattern = new ArrayList<>();
        p_pattern.add(new Position(-1, -1));
        p_pattern.add(new Position(-1, 1));
        p_pattern.add(new Position(1, -1));
        p_pattern.add(new Position(1, 1));
        return p_pattern;
    }

}
