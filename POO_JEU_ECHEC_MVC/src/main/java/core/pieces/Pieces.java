package core.pieces;

import java.util.List;

import core.Board;
import core.Position;

/** 
 * Abstract class representing a piece on the board
 */
public abstract class Pieces {

    /** Indicates whether the piece is white. */
    private final boolean isWhite;
    /** The maximum range of movement for the piece. */
    private final int range;
    /** The board on which the piece is placed. */
    private transient Board board;
    /** The position of the piece on the board. */
    private Position pos;
    /** The pattern of possible moves for the piece. */
    private final List<Position> pattern;
    /** The string representation of the piece. */
    private final String rep;

    /*
    =============================
    -------- Constructor --------
    =============================
     */

    /**
     * Constructor for a piece
     * 
     * @param board the board to set
     * @param isWhite if a piece is white
     * @param range the range of a piece
     * @param representation the string representation of a piece
     * @param p_pattern the pattern of a piece
     */
    protected Pieces(Board board, boolean isWhite, int range, String representation, List<Position> p_pattern) {
        this.board = board;
        this.isWhite = isWhite;
        this.range = range;
        this.rep = representation;
        this.pos = new Position(0, 0);
        this.pattern = p_pattern;
    }

    /*
    =============================
    ---------- Getters ----------
    =============================
    */
    
    /**
     * Getter for the color of a piece
     * 
	 * @return the boolean that represent if a piece is white
	 */
	public boolean isWhite() {
		return isWhite;
	}

	/**
     * Getter for the range of a piece
     * 
	 * @return the range of a piece
	 */
	public int getRange() {
		return range;
	}

	/**
     * Getter for the board of a piece
     * 
	 * @return the board of a piece
	 */
	public Board getBoard() {
		return board;
	}

	/**
     * Getter for the position of a piece
     * 
	 * @return the position of a piece
	 */
	public Position getPosition() {
		return pos;
	}

	/**
     * Getter for the pattern of a piece
     * 
	 * @return the pattern of a piece
	 */
	public List<Position> getPattern() {
		return pattern;
	}

	/**
     * Getter for the string representation of a piece
     * 
	 * @return the string representation of a piece
	 */
	public String getRep() {
		return rep;
	}
    
    /*
    =============================
    ---------- Setters ----------
    =============================
     */

	/**
     * Setter for the board of a piece
     * 
	 * @param board the board to set
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
     * Setter for the position of a piece
     * 
	 * @param pos the position to set
	 */
	public void setPos(Position pos) {
		this.pos = pos;
	}

    /*
    =============================
    ---------- Methods ----------
    =============================
    */

	/**
     * Method to move a piece to a new position if the move is legal
     * 
	 * @param x the desired x-coordinate
	 * @param y the desired y-coordinate
     * 
     * @return true if the move is legal and the piece is moved, false otherwise
	 */
	public boolean move(int x, int y) {
        List<Position> possibleMoves = board.getLegalMove(this);
        if (possibleMoves != null && !possibleMoves.isEmpty()) {
            for (Position position : possibleMoves) {
                if (position.getX() == x && position.getY() == y) {

                    pos.setPosition(x, y);
                    return true;
                }
            }
            return false;

        } else {
            return false;
        }
    }

    
	@Override
    public String toString() {
        return rep;
    }

}
