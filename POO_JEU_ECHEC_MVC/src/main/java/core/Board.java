package core;

import java.io.Serializable;
import java.util.ArrayList;

import core.pieces.Bishop;
import core.pieces.King;
import core.pieces.Knight;
import core.pieces.Pawn;
import core.pieces.Pieces;
import core.pieces.Queen;
import core.pieces.Rook;

/**
 * Class representing the game board
 */
public class Board implements Serializable {

    //private String fen;
    /**
     * The board is represented as a 2D array of Pieces, where each cell can either be null (indicating an empty square) 
     * or contain a reference to a Pieces object representing the piece occupying that square.
     */
    private Pieces[][] pieceMatrix;
    /** The en passant square is a special position on the board that can be captured by a pawn under specific conditions. */
    private Position enpassantSquare;
    /** The en passant piece is the pawn that can be captured en passant, it is used to remove the pawn from the board when an en passant capture is made. */
    private Pawn enpassantPiece;
    /** The width of the board, which is determined by the FEN string used to initialize the board. */
    private int width;
    /** The height of the board, which is determined by the FEN string used to initialize the board. */
    private final int height;
    /** The move number, which starts at 1 and increments after Black's turn. */
    private int moveNumber;
    /** A boolean indicating whether it is currently White's turn. */
    private boolean turnWhite;
    /** The half-clock timer is used to track the number of half-moves since the last pawn move or capture. */
    private int halfClockTimer;
    /** Castle rights are represented as a string containing the characters 'K', 'Q', 'k', and 'q', where:
        * 'K' indicates that White can still castle kingside,
        * 'Q' indicates that White can still castle queenside,
        * 'k' indicates that Black can still castle kingside,
        * 'q' indicates that Black can still castle queenside.
     */
    private String castleRights;
    //private Boolean whiteMated = false;
    //private Boolean blackMated = false;

    /**
     * Transient reference to the parent Game instance, used for accessing game-level functionality and state. 
     * This field is marked as transient to avoid serialization issues, and should be set after deserialization using the setGame method.
     */
    private transient Game game = null;
    /*
    =============================
    -------- Constructor --------
    =============================
     */

    /**
     * Constructor for the board, initializes the board with a fen string
     * 
     * @param fen the fen string to initialize the board with
     * @param g the parent Game instance to set
     */
    public Board(String fen,Game g) {
        game = g;
        //this.fen = fen;
        String[] info = fen.split(" ");
        String[] lines = info[0].split("/");
        height = lines.length;

        turnWhite = info[1].equals("w");

        moveNumber = Integer.parseInt(info[5]);

        halfClockTimer = Integer.parseInt(info[4]);

        castleRights = info[2];

        width = 0;
        //il sufit ici de regarder la longueur de la première ligne
        //puisqu'on a déja vérifier dans le factory que tout le paramètre est bien écrit
        //la longueur de la ligne trouvé sera donc la largueur de l'échequier

        for (int i = 0; i < lines[0].length(); i++) {
            char c = lines[0].charAt(i);
            if (Character.isDigit(c)) {
                width += (int) lines[0].charAt(i) - (int) '0';
            } else {

                width++;
            }
        }
        pieceMatrix = new Pieces[height][width];

        for (int y = 0; y < height; y++) {
            String line = lines[y];

            int nextPosLine = 0;
            for (int x = 0; x < line.length(); x++) {
                //une fois la largeur de la ligne récuperé on prend la piece correspondant à un character si c'est une lettre, 
                //sinon on passe au prochain charactere si c'est un nombre indiquant le nombre d'espace libre
                char c = line.charAt(x);
                if (!Character.isDigit(c)) {
                    Pieces piece = getPieceFromChar(c);
                    if (piece != null) {
                        if (piece instanceof Pawn pawn) {
                            if (piece.isWhite()) {
                                pawn.setFirstMove(y >= height - 2);
                            } else {

                                pawn.setFirstMove(y < 2);
                            }
                        }
                        piece.getPosition().setPosition(nextPosLine, y);
                        pieceMatrix[y][nextPosLine] = piece;
                        nextPosLine++;
                    }
                } else {
                    nextPosLine += (int) line.charAt(x) - (int) '0';
                }
            }
        }

    }
    
   

    /*
    =============================
    ---------- Getters ----------
    =============================
     */

    /**
     * Getter for the game reference
     * 
     * @return the parent Game instance
     */
    public Game getGame(){
        return game;
    }

    /**
     * Getter for the pieces on the board
     * 
	 * @return the pieces
	 */
	public Pieces[][] getPieceMatrix() {
		return pieceMatrix;
	}

	/**
     * Getter for the en passant square
     * 
	 * @return the enpassantSquare
	 */
	public Position getEnpassantSquare() {
		return enpassantSquare;
	}

	/**
	 * Getter for the width of the board
     * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

    /**
     * Getter for the height of the board
     * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

    /**
     * Getter for the move number
     * 
	 * @return the moveNumber
	 */
	public int getMoveNumber() {
		return moveNumber;
	}

	/**
     * Getter for the turn color
     * 
	 * @return the turnWhite
	 */
	public boolean isTurnWhite() {
		return turnWhite;
	}

	/**
     * Getter for the half clock timer
     * 
	 * @return the halfClockTimer
	 */
	public int getHalfClockTimer() {
		return halfClockTimer;
	}

	/**
     * Getter for the castle rights
     * 
	 * @return the castleRights
	 */
	public String getCastleRights() {
		return castleRights;
	}

    /*
    =============================
    ---------- Setters ----------
    =============================
     */

	/**
     * Setter for the pieces on the board
     * 
	 * @param pieces the pieces to set
	 */
	public void setPieces(Pieces[][] pieces) {
		this.pieceMatrix = pieces;
	}

	/**
     * Setter for the en passant square
     * 
	 * @param enpassantSquare the enpassantSquare to set
	 */
	public void setEnpassantSquare(Position enpassantSquare) {
		this.enpassantSquare = enpassantSquare;
	}

	/**
     * Setter for the en passant piece
     * 
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
     * Setter for the move number
     * 
	 * @param moveNumber the moveNumber to set
	 */
	public void setMoveNumber(int moveNumber) {
		this.moveNumber = moveNumber;
	}

	/**
     * Setter for the turn color
     * 
	 * @param turnWhite the turnWhite to set
	 */
	public void setTurnWhite(boolean turnWhite) {
		this.turnWhite = turnWhite;
	}

	/**
     * Setter for the half clock timer
     * 
	 * @param halfClockTimer the halfClockTimer to set
	 */
	public void setHalfClockTimer(int halfClockTimer) {
		this.halfClockTimer = halfClockTimer;
	}

	/**
     * Setter for the castle rights
     * 
	 * @param castleRights the castleRights to set
	 */
	public void setCastleRights(String castleRights) {
		this.castleRights = castleRights;
	}

    /**
     * Sets the game reference after deserialization.
     *
     * @param game The parent Game instance.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /*
    =============================
    ---------- Methods ----------
    =============================
    */

    /**
	 * Check if the current position is in checkmate
     * 
	 * @return true if the current position is in checkmate, false otherwise
	 */
	public boolean checkmate() {
        boolean sideHasLegalMoves = false;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pieces p = pieceAt(x, y);
                if (p != null) {
                    if (turnWhite == p.isWhite()) {
                        ArrayList<Position> pieceMoves = getLegalMove(p);
                        if (!pieceMoves.isEmpty()) {
                            ArrayList<Position> simul = new ArrayList<>() ;
                            for (Position pos : pieceMoves){
                                simulateMove(findKing(), p, x, y, pos.getX(), pos.getY(), simul);
                                sideHasLegalMoves = sideHasLegalMoves || !simul.isEmpty();
                                if(sideHasLegalMoves){
                                    break;
                                }
                            }

                        }
                    }
                }
            }
            if (sideHasLegalMoves) {
                break;
            }
        }
        return !sideHasLegalMoves;
    }

	/**
	 * Get the piece at a given position
     * 
	 * @param x the x coordinate of the position
	 * @param y the y coordinate of the position
     * 
	 * @return the piece at the given position, or null if there is no piece at 
	 * the given position or if the position is out of bounds
	 * */
    public Pieces pieceAt(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return pieceMatrix[y][x];
    }

    /**
     * Sets the en passant square and piece.
     * 
     * @param square The position of the en passant square.
     * @param piece The pawn that can be captured en passant.
     */
    public void setEnpassant(Position square, Pawn piece) {
        enpassantPiece = piece;
        enpassantSquare = square;
    }

    /**
     * Method to move a piece from one position to another, if the move is legal, it will return true and move the piece, false otherwise
     * 
     * @param xPiece the x coordinate of the piece to move
     * @param yPiece the y coordinate of the piece to move
     * @param xTarget the x coordinate of the target position
     * @param yTarget the y coordinate of the target position
     * @param promotionPiece the piece to promote to in case of a pawn promotion, 
     * if the move is not a pawn promotion, this parameter will be ignored and can be set to null
     * 
     * @return true if the move is legal and the piece is moved, false otherwise
     */
    protected boolean movePiece(int xPiece, int yPiece, int xTarget, int yTarget,Pieces promotionPiece){
        boolean moveSuccess = movePiece(xPiece, yPiece, xTarget, yTarget);
        if (moveSuccess) {
            promotionPiece.setPos(pieceAt(xTarget,yTarget).getPosition());
            pieceMatrix[yTarget][xTarget] = promotionPiece;
        }
        return moveSuccess;
    }

    /**
     * Method to move a piece from one position to another, if the move is legal, it will return true and move the piece, false otherwise
     * 
     * @param xPiece the x coordinate of the piece to move
     * @param yPiece the y coordinate of the piece to move
     * @param xTarget the x coordinate of the target position
     * @param yTarget the y coordinate of the target position
     * 
     * @return true if the move is legal and the piece is moved, false otherwise
     */
    protected boolean movePiece(int xPiece, int yPiece, int xTarget, int yTarget) {
        Pieces piece = pieceAt(xPiece, yPiece);
        if(piece == null){
            return false;
        }
        boolean moveSuccess = piece.move(xTarget, yTarget);
        if (moveSuccess) {
            pieceMatrix[yPiece][xPiece] = null;
            pieceMatrix[yTarget][xTarget] = piece;
            if (enpassantSquare != null) {
                if (yTarget == enpassantSquare.getY() && xTarget == enpassantSquare.getX() && piece instanceof Pawn) {
                    if (turnWhite) {
                        pieceMatrix[yTarget + 1][xTarget] = null;

                        setEnpassant(null, null);
                    } else {

                        pieceMatrix[yTarget - 1][xTarget] = null;

                        setEnpassant(null, null);
                    }
                }
            }
            int diff = yTarget - yPiece;
            if (diff < 0) {
                diff = -diff;
            }
            if (!(piece instanceof Pawn) || diff != 2) {
                setEnpassant(null, null);
            }
            turnWhite = !turnWhite;
            if (turnWhite) {
                moveNumber++;
            }

        }
        return moveSuccess;
    }

    /**
     * Method to find a piece on the board based on its character representation and potential move patterns, used for parsing PGN moves.
     * 
     * @param boardRep the character representation of the piece to find (e.g., 'K' for King, 'Q' for Queen, etc.)
     * @param x the x coordinate of the target position for the piece to move to
     * @param y the y coordinate of the target position for the piece to move to
     * @param startX the x coordinate of the starting position of the piece to find, or -1 if not specified (used for disambiguating moves in PGN)
     * @param startY the y coordinate of the starting position of the piece to find, or -1 if not specified (used for disambiguating moves in PGN)
     * 
     * @return the position of the found piece, or null if not found
     */
    private Position findPiecePGN(char boardRep,int x,int y,int startX,int startY){
        Pieces tmp = getPieceFromChar(boardRep);
        if (tmp != null) {
            tmp.getPosition().setPosition(x, y);

            ArrayList<Position> potentialPositions = getLegalMoveCheckmate(tmp);
            Position piecePos = new Position(-1, -1);
            for (Position pos : potentialPositions) {
                Pieces piece = pieceAt(pos.getX(), pos.getY());
                if(startX>-1 && pos.getX()!=startX){
                    continue;
                }
                if(startY>-1 && pos.getY()!=startY){
                    continue;
                }
                if (piece != null) {
                    if (piece.toString().toLowerCase().equals(tmp.toString().toLowerCase()) && piece.isWhite() != tmp.isWhite()) { //same inner representation
                        piecePos.setPosition(pos.getX(), pos.getY());
                    }
                }
            }
            return piecePos;
        } else{
            return null;
        }
    }

    /**
     * Method to make a move based on a PGN string, it will return true if the move is legal and has been made, false otherwise
     * 
     * @param e the PGN string representing the move to make
     * 
     * @return true if the move is legal and has been made, false otherwise
     */
    public boolean makePGNmove(String e) {
        // \w\d -> pawn move to col,line 
        // [KQNBR]x?\w\d -> only one of the piece rep can move to col,line piece could be a pawn if the letter P is precised
        // [KQNBR]x?\w\w\d -> piece rep from col to col,line
        // \w\w\d -> pawn on col goes to col,line
        // (+|#)?
        char last = e.charAt(e.length() - 1);
        String pgn = (last != '+' && last != '#') ? e : e.substring(0, e.length() - 1);
        boolean moveSuccess = false;
        //possible moves : e5,exf5,Qb4,Qxc5,a8=Q,Rcd1 (le + dit qu'il y a échec après le coup et le # dit qu'il y a matte)
        if (pgn.matches("[KQNBR]?([a-w0-9])?(x?)[a-w][0-9]+(=[QRBN])?")) {
            char pieceRep = pgn.charAt(0);

            if (Character.isLowerCase(pieceRep)) { //case pawn
                int pieceX = ((int) pieceRep) - ((int) 'a');
                if (pgn.charAt(1) == 'x') { //pawn at pieceXtakes piece on coords
                    int x = ((int) pgn.charAt(2)) - (int) 'a';
                    int promotionIndex = pgn.indexOf("=");
                    int y;
                    if (promotionIndex > 0) {
                        y = height - Integer.parseInt(pgn.substring(3, promotionIndex));
                    } else {
                        y = height - Integer.parseInt(pgn.substring(3));
                    }
                    if ((turnWhite && y == 0) || (!turnWhite && y == height - 1)) { //if taking puts you at the end of the board on either side
                        if (promotionIndex > 0) {
                            //equal exists, we look for the last character
                            char piece = pgn.charAt(pgn.length() - 1);
                            if (turnWhite) {
                                piece = Character.toUpperCase(piece);
                            } else {
                                piece = Character.toLowerCase(piece);
                            }
                            Pieces p = getPieceFromChar(piece);
                            int pieceY = turnWhite ? y + 1 : y - 1;
                            moveSuccess = movePiece(pieceX, pieceY, x, y,p);
                            
                        }
                    } else {
                        if (promotionIndex == -1) {
                            int pieceY = turnWhite ? y + 1 : y - 1;
                            moveSuccess = movePiece(pieceX, pieceY, x, y);
                        }
                    }
                } else { //normal pawn moveint promotionIndex = pgn.indexOf("=");
                    int promotionIndex = pgn.indexOf("=");
                    int y;
                    if (promotionIndex > 0) {
                        y = height - Integer.parseInt(pgn.substring(1, promotionIndex));
                    } else {
                        y = height - Integer.parseInt(pgn.substring(1));
                    }
                    Pieces tmp = getPieceFromChar(turnWhite ? 'r' : 'R');
                    if (tmp != null) {
                        tmp.getPosition().setPosition(pieceX, y);

                        ArrayList<Position> potentialPositions = getLegalMoveCheckmate(tmp);
                        Position piecePos = new Position(-1, -1);
                        for (Position pos : potentialPositions) {
                            if (pos.getX() != pieceX) {
                                continue;
                            }
                            Pieces piece = pieceAt(pos.getX(), pos.getY());
                            if (piece != null) {
                                if (piece instanceof Pawn && piece.isWhite() != tmp.isWhite()) { //same inner representation
                                    piecePos.setPosition(pos.getX(), pos.getY());

                                }
                            }
                        }
                        if ((turnWhite && y == 0) || (!turnWhite && y == height - 1)) {
                            if (promotionIndex > 0) {
                                char piece = pgn.charAt(pgn.length() - 1);
                                if (turnWhite) {
                                    piece = Character.toUpperCase(piece);
                                } else {
                                    piece = Character.toLowerCase(piece);
                                }
                                Pieces p = getPieceFromChar(piece);
                                moveSuccess = movePiece(piecePos.getX(), piecePos.getY(), pieceX, y,p);
                            }
                        } else {
                            if (promotionIndex == -1) {
                                return movePiece(piecePos.getX(), piecePos.getY(), pieceX, y);
                            }
                        }

                    } else {
                        return false;
                    }
                }
            } else { //not a pawn
                if (pgn.charAt(1) == 'x') { //look for a piece that has the following coords as potential legal move

                    int x = ((int) pgn.charAt(2)) - (int) 'a';
                    int y = height - Integer.parseInt(pgn.substring(3));
                    char boardRep = turnWhite ? Character.toLowerCase(pieceRep) : pieceRep;
                    Position piecePos = findPiecePGN(boardRep, x, y,-1,-1);
                    moveSuccess = movePiece(piecePos.getX(), piecePos.getY(), x, y);

                } else if (pgn.charAt(2) == 'x') { //if two of the same type of piece can take
                    int pieceX = -1;
                    int pieceY = -1;
                    if(!Character.isDigit(pgn.charAt(1))){
                        pieceX = ((int) pgn.charAt(1)) - (int) 'a';
                    }else{

                        pieceY = height-Integer.parseInt(pgn.substring(1,2));
                    }
                    
                    int x = ((int) pgn.charAt(3)) - (int) 'a';
                    int y = height - Integer.parseInt(pgn.substring(4));
                    char boardRep = turnWhite ? Character.toLowerCase(pieceRep) : pieceRep;
                    //Pieces tmp = getPieceFromChar(boardRep);
                    Position piecePos = findPiecePGN(boardRep, x, y,pieceX,pieceY);
                    if(pieceX==-1){
                        pieceX=piecePos.getX();
                    }
                    if(pieceY==-1){
                        pieceY=piecePos.getY();
                    }
                    moveSuccess = movePiece(pieceX, pieceY, x, y);
                } else { //nothing being taken 
                    int pieceX = -1;
                    int pieceY = -1;
                    if(!Character.isDigit(pgn.charAt(1))){
                        pieceX = ((int) pgn.charAt(1)) - (int) 'a';
                    }else{

                        pieceY = height-Integer.parseInt(pgn.substring(1,2));
                    }
                    if (Character.isDigit(pgn.charAt(2))) {
                        int x = pieceX;
                        pieceX=-1;
                        int y = height - Integer.parseInt(pgn.substring(2));
                        char boardRep = turnWhite ? Character.toLowerCase(pieceRep) : pieceRep;
                        
                        Position piecePos = findPiecePGN(boardRep, x, y,pieceX,pieceY);
                        pieceX=piecePos.getX();
                        pieceY=piecePos.getY();
                        
                        moveSuccess = movePiece(pieceX, pieceY, x, y);
                    } else {
                        int x = ((int) pgn.charAt(2)) - (int) 'a';

                        int y = height - Integer.parseInt(pgn.substring(3));
                        char boardRep = turnWhite ? Character.toLowerCase(pieceRep) : pieceRep;
                        Position piecePos = findPiecePGN(boardRep, x, y,pieceX,pieceY);
                        if(pieceX==-1){
                            pieceX=piecePos.getX();
                        }
                        if(pieceY==-1){
                            pieceY=piecePos.getY();
                        }
                        moveSuccess = movePiece(pieceX, pieceY, x, y);
                    }
                }
            }
        } else if (pgn.matches("(O-){1,2}O")) {
            King k = findKing();
            Position start = new Position(k.getPosition().getX(), k.getPosition().getY());
            if (pgn.length() == 3) {
                moveSuccess = k.move(k.getPosition().getX() + 2, k.getPosition().getY());
                if (moveSuccess) {
                    ArrayList<Rook> rooks = findRooks();
                    for (Rook r : rooks) {
                        if (r.getPosition().getY() == k.getPosition().getY() && r.getPosition().getX() > k.getPosition().getX()) {
                            pieceMatrix[r.getPosition().getY()][r.getPosition().getX()] = null;
                            pieceMatrix[k.getPosition().getY()][k.getPosition().getX() - 1] = r;
                            pieceMatrix[start.getY()][start.getX()] = null;
                            pieceMatrix[k.getPosition().getY()][k.getPosition().getX()] = k;
                            r.getPosition().setPosition(k.getPosition().getX() - 1, k.getPosition().getY());
                            if (turnWhite) {
                                moveNumber++;
                            }
                            turnWhite = !turnWhite;
                            break;
                        }
                    }
                }
            } else {
                moveSuccess = k.move(k.getPosition().getX() - 2, k.getPosition().getY());
                if (moveSuccess) {
                    ArrayList<Rook> rooks = findRooks();
                    for (Rook r : rooks) {
                        if (r.getPosition().getY() == k.getPosition().getY() && r.getPosition().getX() < k.getPosition().getX()) {
                            pieceMatrix[r.getPosition().getY()][r.getPosition().getX()] = null;
                            pieceMatrix[k.getPosition().getY()][k.getPosition().getX() + 1] = r;
                            pieceMatrix[start.getY()][start.getX()] = null;
                            pieceMatrix[k.getPosition().getY()][k.getPosition().getX()] = k;
                            r.getPosition().setPosition(k.getPosition().getX() + 1, k.getPosition().getY());
                            if (turnWhite) {
                                moveNumber++;
                            }
                            turnWhite = !turnWhite;
                            break;
                        }
                    }
                }
            }
        }

        return moveSuccess;

    }
    
    /**
     * Simulate a move on the board
     * 
     * @param k the king
     * @param piece the piece to move
     * @param startX the starting x position
     * @param startY the starting y position
     * @param possibleX the possible x position
     * @param possibleY the possible y position
     * @param legalMoves the list of legal moves
     */
    private void simulateMove(King k,Pieces piece,int startX,int startY,int possibleX,int possibleY,ArrayList<Position> legalMoves){
       
        Pieces[][] tmp = new Pieces[height][width];
        for (int y = 0; y < height; y++) {
            tmp[y] = pieceMatrix[y].clone();
        }
        pieceMatrix[possibleY][possibleX] = piece;
        pieceMatrix[startY][startX] = null;
        piece.getPosition().setPosition(possibleX, possibleY);
        if (!k.isKingInCheck()) {
            legalMoves.add(new Position(possibleX, possibleY));
        }
        pieceMatrix = tmp;
        piece.getPosition().setPosition(startX, startY);
    }
    
    /**
	 * Get the legal moves for a piece
     * 
	 * @param piece the piece to get the legal moves for
     * 
	 * @return an ArrayList of Position objects representing the legal moves for the piece
	 */
    public ArrayList<Position> getLegalMove(Pieces piece) {
        return getLegalMove(piece, false);
    }

    /**
     * Get the legal moves for a piece without checking for checkmate
     * 
     * @param piece the piece to get the legal moves for
     * 
     * @return an ArrayList of Position objects representing the legal moves for the piece without checking for checkmate
     */
    public ArrayList<Position> getLegalMoveCheckmate(Pieces piece) {
        return getLegalMove(piece, true);
    }

    /**
     * Get the legal moves for a piece, if checks is true, 
     * it will not check for checkmate and will return all the moves that are legal without considering if the king is in check or not, 
     * if checks is false, it will only return the moves that are legal and do not put the king in check
     * 
     * @param piece the piece to get the legal moves for
     * @param checks boolean that indicate if we want to check for checkmate or not
     * 
     * @return an ArrayList of Position objects representing the legal moves for the piece, 
     * if checks is true, it will return all the moves that are legal without considering if the king is in check or not, 
     * if checks is false, it will only return the moves that are legal and do not put the king in check
     */
    private ArrayList<Position> getLegalMove(Pieces piece, boolean checks) {
        ArrayList<Position> legalMoves = new ArrayList<>();
        King k = findKing(); // can not be null
        int startX = piece.getPosition().getX();
        int startY = piece.getPosition().getY();
        switch (piece) {
            case Pawn pawn -> {
                for (Position direction : piece.getPattern()) {
                    int dx = direction.getX();
                    int dy = direction.getY();
                    int possibleX = startX + dx;
                    int possibleY = startY + dy;
                    if (possibleX < 0 || possibleX >= width || possibleY < 0 || possibleY >= height) {
                        continue;
                    }
                    Pieces target = pieceAt(possibleX, possibleY);
                    if (dx != 0) {
                        if (target != null) {
                            if (target.isWhite() != piece.isWhite()) {
                                if (!checks) {
                                    simulateMove(k, piece, startX, startY, possibleX, possibleY, legalMoves);
                                } else {
                                    legalMoves.add(new Position(possibleX, possibleY));
                                }
                            }
                        }
                        if (enpassantSquare != null) {
                            if (enpassantPiece.isWhite() != piece.isWhite() && possibleX == enpassantSquare.getX() && possibleY == enpassantSquare.getY()) {
                                if (!checks) {
                                    simulateMove(k, piece, startX, startY, possibleX, possibleY, legalMoves);
                                } else {
                                    legalMoves.add(new Position(possibleX, possibleY));
                                }
                            }
                        }
                    } else if ((dy == 2 || dy==-2)) {
                        if(pawn.isFirstMove()){
                            if (target == null) {
                                if (!checks) {
                                    simulateMove(k, piece, startX, startY, possibleX, possibleY, legalMoves);
                                } else {
                                    legalMoves.add(new Position(possibleX, possibleY));
                                }
                            }
                        }
                    } else {
                        if (target == null) {
                            if (!checks) {
                                simulateMove(k, piece, startX, startY, possibleX, possibleY, legalMoves);
                            } else {
                                legalMoves.add(new Position(possibleX, possibleY));
                            }
                        }
                    }
                }
            }
            case King king -> {
                    for (Position direction : king.getPattern()) {
                    int dx = direction.getX();
                    int dy = direction.getY();

                    int possibleX = startX + dx;
                    int possibleY = startY + dy;

                    if (possibleX < 0 || possibleX >= width || possibleY < 0 || possibleY >= height) {
                        continue;
                    }
                    Pieces target = pieceAt(possibleX, possibleY);
                    if (dx > -2 && dx < 2) {
                        if (target == null) {
                            if (!checks) {
                                    simulateMove(k, piece, startX, startY, possibleX, possibleY, legalMoves);
                            } else {
                                legalMoves.add(new Position(possibleX, possibleY));
                            }
                        } else {
                            if (target.isWhite() != piece.isWhite()) {
                                if (!checks) {
                                    simulateMove(k, piece, startX, startY, possibleX, possibleY, legalMoves);
                                } else {
                                    legalMoves.add(new Position(possibleX, possibleY));
                                }
                            }

                        }
                    } else if (king.isFirstMove() && !king.isKingInCheck()) { //castle 
                        if (dx > 1 && (turnWhite ? castleRights.contains("K") : castleRights.contains("k"))) {
                            boolean castleRight = true;
                            for (int i = startX + 1; i < width && castleRight; i++) {
                                Pieces p = pieceAt(startX + i, startY);
                                King test = new King(this, king.isWhite(), false, false);
                                test.getPosition().setPosition(i, startY);

                                castleRight = castleRight && !test.isKingInCheck();
                                if (!castleRight) {
                                    break;
                                }
                                if (p instanceof Rook rook) {
                                    castleRight = castleRight && rook.isFirstMove() && p.isWhite() == king.isWhite();
                                    break;
                                } else {
                                    castleRight = castleRight && p == null;
                                }
                            }
                            if (castleRight) {
                                Position pos = new Position(possibleX, possibleY);
                                if (!checks) {
                                    if (!k.isKingInCheck()) {
                                        legalMoves.add(pos);
                                    }
                                } else {
                                    legalMoves.add(pos);
                                }
                            }

                        } else if (dx < 1 && (turnWhite ? castleRights.contains("Q") : castleRights.contains("q"))) {
                            boolean castleRight = true;
                            for (int i = startX - 1; i > 0 && castleRight; i--) {
                                Pieces p = pieceAt(i, startY);
                                King test = new King(this, king.isWhite(), false, false);
                                test.getPosition().setPosition(i, startY);
                                castleRight = castleRight && !test.isKingInCheck();
                                if (!castleRight) {
                                    break;
                                }
                                if (p instanceof Rook rook) {
                                    castleRight = castleRight && rook.isFirstMove() && p.isWhite() == king.isWhite();
                                    break;
                                } else {
                                    castleRight = castleRight && p == null;
                                }
                            }
                            if (castleRight) {
                                Position pos = new Position(possibleX, possibleY);
                                if (!checks) {
                                    if (!k.isKingInCheck()) {
                                        legalMoves.add(pos);
                                    }
                                } else {
                                    legalMoves.add(pos);
                                }
                            }
                        }
                    }
                }
            }
            
            default -> {
                    for (Position direction : piece.getPattern()) {
                    int dx = direction.getX();
                    int dy = direction.getY();

                    for (int i = 1; i <= piece.getRange(); i++) {
                        int possibleX = startX + dx * i;
                        int possibleY = startY + dy * i;

                        if (possibleX < 0 || possibleX >= width || possibleY < 0 || possibleY >= height) {
                            break;
                        }
                        Pieces target = pieceAt(possibleX, possibleY);

                        if (target == null) {
                            if (!checks) {
                                simulateMove(k, piece, startX, startY, possibleX, possibleY, legalMoves);
                            } else {
                                legalMoves.add(new Position(possibleX, possibleY));
                            }
                        } else {
                            if (target.isWhite() != piece.isWhite()) {
                                if (!checks) {
                                    simulateMove(k, piece, startX, startY, possibleX, possibleY, legalMoves);
                                } else {
                                    legalMoves.add(new Position(possibleX, possibleY));
                                }
                            }
                            break;
                        }

                    }
                }
            }     
        }
        return legalMoves;
    }

    /**
     * Find the king of the current player on the board
     * 
     * @return the king of the current player on the board, or null if there is no king of the current player on the board
     */
    private King findKing() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pieceAt(x, y) instanceof King p) {
                    if (p.isWhite() == turnWhite) {
                        return p;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find the rooks of the current player on the board
     * 
     * @return an ArrayList of the rooks of the current player on the board, or an empty ArrayList if there is no rook of the current player on the board
     */
    private ArrayList<Rook> findRooks() {
        ArrayList<Rook> res = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pieceAt(x, y) instanceof Rook p) {
                    if (p.isWhite() == turnWhite) {
                        res.add(p);
                    }
                }
            }
        }
        return res;
    }
    
    /**
     * Get the piece corresponding to a character in the FEN string
     * 
     * @param c the character to convert
     * 
     * @return the piece corresponding to the character, or null if the character is not a valid piece
     */
    public final Pieces getPieceFromChar(char c) {
        switch (c) {
            //on a ici chaque characteres possible pour un échequier
            case 'K' -> {
                return new King(this, true, (castleRights.contains("K")), (castleRights.contains("Q")));
            }
            case 'Q' -> {

                return new Queen(this, true);
            }
            case 'R' -> {

                return new Rook(this, true);
            }
            case 'B' -> {

                return new Bishop(this, true);
            }
            case 'N' -> {

                return new Knight(this, true);
            }
            case 'P' -> {

                return new Pawn(this, true);
            }

            case 'k' -> {

                return new King(this, false, (castleRights.contains("k")), (castleRights.contains("q")));
            }
            case 'q' -> {

                return new Queen(this, false);
            }
            case 'r' -> {

                return new Rook(this, false);
            }
            case 'b' -> {

                return new Bishop(this, false);
            }
            case 'n' -> {

                return new Knight(this, false);
            }
            case 'p' -> {

                return new Pawn(this, false);
            }
        }
        return null;
    }
}
