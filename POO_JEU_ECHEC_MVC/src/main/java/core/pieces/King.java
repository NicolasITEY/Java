package core.pieces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import core.Board;
import core.Position;

/**
 * Represents a king piece in the chess game.
 */
public class King extends Pieces implements Serializable {

    /** Indicates whether the king has made its first move (used for castling rules). */
    private boolean isFirstMove = true;

    /*
    =============================
    -------- Constructor --------
    =============================
     */

    /**
     * Constructor for a King piece
     * 
     * @param board the board to set
     * @param isWhite if a piece is white
     * @param castleKing a boolean indicating whether the king can castle on the king's side (used for castling rules)
     * @param castleQueen a boolean indicating whether the king can castle on the queen's side (used for castling rules)
     */
    public King(Board board, boolean isWhite, boolean castleKing, boolean castleQueen) {
        super(board, isWhite, 1, setRep(isWhite), setPatternKing());
    }

    /*
    =============================
    ---------- Getters ----------
    =============================
     */

    /**
     * Getter for the isFirstMove property, which indicates whether the king has made its first move (used for castling rules).
     * 
     * @return true if the king has not moved yet, false otherwise
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
     * Setter for the isFirstMove property, which indicates whether the king has made its first move (used for castling rules).
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
            return "K";
        } else {
            return "k";
        }
    }
    
    /**
     * Method to set the pattern of a King piece
     * 
     * @return List containing the pattern for a piece
     */
    private static List<Position> setPatternKing(){
    	ArrayList<Position> p_pattern = new ArrayList<>();
        p_pattern.add(new Position(-2, 0));
        p_pattern.add(new Position(-1, -1));
        p_pattern.add(new Position(-1, 0));
        p_pattern.add(new Position(-1, 1));
        p_pattern.add(new Position(0, -1));
        p_pattern.add(new Position(0, 1));
        p_pattern.add(new Position(1, -1));
        p_pattern.add(new Position(1, 0));
        p_pattern.add(new Position(1, 1));
        p_pattern.add(new Position(2, 0));
        return p_pattern;
    }

    /**
     * Overrides the move method to handle the king's first move
     * 
     * @param x the x coordinate to move to
     * @param y the y coordinate to move to
     * 
     * @return true if the move is valid, false otherwise
     */
    @Override
    public boolean move(int x, int y) {
        boolean canMove = super.move(x, y);

        if (canMove && isFirstMove) {

            setFirstMove(false);
        }
        return canMove;
    }
    
    /**
     * Method to check if the king is in check by any enemy piece. 
     * It creates temporary Queen and Knight pieces at the king's position and checks if any of the legal moves for those pieces would capture the king. 
     * It also checks for enemy pieces that could capture the king based on their movement patterns (e.g., Rooks, Bishops, Pawns, and Kings).
     * 
     * @return true if the king is in check, false otherwise
     */
    public boolean isKingInCheck() {
        Queen tmpQueen = new Queen(getBoard(), isWhite());
        tmpQueen.getPosition().setPosition(getPosition().getX(), getPosition().getY());
        Knight tmpKnight = new Knight(getBoard(), isWhite());
        tmpKnight.getPosition().setPosition(getPosition().getX(), getPosition().getY());
        List<Position> legalMovesQ = this.getBoard().getLegalMoveCheckmate(tmpQueen);
        List<Position> legalMovesN = this.getBoard().getLegalMoveCheckmate(tmpKnight);
        boolean hasEnemyPiece = false;
        for (Position p : legalMovesN) {
            Pieces piece = getBoard().pieceAt(p.getX(), p.getY());
            if (piece != null) {
                if (piece instanceof Knight) {
                    hasEnemyPiece = true;
                    break;
                }
            }
        }
        if (!hasEnemyPiece) {
            for (Position p : legalMovesQ) {
                Pieces piece = getBoard().pieceAt(p.getX(), p.getY());
                if (piece != null) {
                    int dx = p.getX() - this.getPosition().getX();
                    int dy = p.getY() - this.getPosition().getY();
                    int range = 0;
                    if (dx == 0) {
                        range = dy < 0 ? -dy : dy;
                    } else {
                        range = dx < 0 ? -dx : dx;
                    }
                
                    hasEnemyPiece = hasEnemyPiece || piece instanceof Queen;
                    if(hasEnemyPiece){
                        break;
                    }
                    hasEnemyPiece = (dy != 0 && dx != 0 && piece instanceof Bishop) || hasEnemyPiece;
                    if(hasEnemyPiece){
                        break;
                    }
                    hasEnemyPiece = (((dy != 0 && dx == 0) || (dy == 0 && dx != 0)) && piece instanceof Rook) || hasEnemyPiece;
                    if(hasEnemyPiece){
                        break;
                    }
                    if(range==1){
                        hasEnemyPiece = (piece instanceof King && range<=2 && piece.isWhite()!=this.isWhite()) || hasEnemyPiece;
                        if(hasEnemyPiece){
                        break;
                    }
                        hasEnemyPiece = (dx != 0 && (isWhite() && dy < 0 || !isWhite() && dy > 0) && piece instanceof Pawn) || hasEnemyPiece;
                        if(hasEnemyPiece){
                        break;
                    }
                    }
                    if (hasEnemyPiece) {
                        break;
                    }
                }

            }
        }
        
        return hasEnemyPiece;
    }
}
