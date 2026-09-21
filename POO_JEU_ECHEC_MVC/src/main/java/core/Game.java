package core;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import com.google.gson.Gson;

import core.pieces.Pieces;

/**
 * Class representing a chess game, it contains the board, the move number, the timer for both players and the turn of the game.
 * It also contains methods to move pieces, check if the game is finished, save and load the game state, and manage the timers for both players.
 */
public class Game implements Serializable{

	/** The timed game status */
    private final boolean timedGame;
	/** The move number */
    private int moveNumber;
	/** The timer for the white player */
    private Chrono timeWhite;
	/** The timer for the black player */
    private Chrono timeBlack;
	/** The turn of the game */
    private boolean turnWhite;
	/** The board for the game */
    private Board board;

    /*
    =============================
    -------- Constructor --------
    =============================
    */

    
    /**
     * Constructor for the Game class that initializes the game state based on a FEN string without timer values.
	 * 
     * @param fen the FEN string representing the initial board position and game state
	 * 
     * @throws Exception if there is an error during the initialization of the game state from the FEN string (e.g., invalid FEN format)
     */
    public Game(String fen) throws Exception {
        this(fen, 0 , 0);
    }

	/**
	 * Constructor for the Game class that initializes the game state based on a FEN string and a timer value for both players.
	 * 
	 * @param fen the FEN string representing the initial board position and game state
	 * @param timer the timer value in seconds for both players (if the game is timed, otherwise it will be ignored)
	 * 
	 * @throws Exception if there is an error during the initialization of the game state from the FEN string (e.g., invalid FEN format)
	 */
	public Game(String fen, int timer) throws Exception {
		this(fen, timer, timer);
    }

	/**
	 * Constructor for the Game class that initializes the game state based on a FEN string and timer values for each players.
	 * 
	 * @param fen the FEN string representing the initial board position and game state
	 * @param timerWhite the timer value in seconds for the white player (if the game is timed, otherwise it will be ignored)
	 * @param timerBlack the timer value in seconds for the black player (if the game is timed, otherwise it will be ignored)
	 * 
	 * @throws Exception if there is an error during the initialization of the game state from the FEN string (e.g., invalid FEN format)
	 */
	public Game(String fen, int timerWhite, int timerBlack) throws Exception {
		this.timedGame = timerWhite != 0 && timerBlack != 0;
		if(timedGame){
			timeWhite = new Chrono();
			timeBlack = new Chrono();
			timeWhite.start(timerWhite);
			timeBlack.start(timerBlack);
		}
        this.moveNumber = 0;
        this.turnWhite = true;
        board = BoardFactory.makeBoard(fen,this);
    }

	/**
	 * Default constructor for the Game class that initializes a new game with the default starting position.
	 */
	public Game() {
        this.moveNumber = 0;
		this.timedGame = false;
		turnWhite=true;
		try {
			this.board = BoardFactory.makeBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /*
    =============================
    ---------- Getters ----------
    =============================
    */

    /**
	 * Returns whether the game is timed.
	 * 
	 * @return the time game status
	 */
	public boolean isTimedGame() {
		return timedGame;
	}

	/**
	 * Returns the current move number.
	 * 
	 * @return the moveNumber
	 */
	public int getMoveNumber() {
		return moveNumber;
	}

	/**
	 * Returns the time remaining for the white player.
	 * 
	 * @return the timeWhite
	 */
	public long getTimeWhite() {
        return timeWhite.getDureeMs();
    }

	/**
	 * Returns a string representation of the time remaining for the white player.
	 * 
	 * @return the timeWhite as a string
	 */
    public String showTimeWhite() {
        return timeWhite.getDureeTxt();
    }

	/**
	 * Returns the time remaining for the black player.
	 * 
	 * @return the timeBlack
	 */
    public long getTimeBlack() {
        return timeBlack.getDureeMs();
    }

	/**
	 * Returns a string representation of the time remaining for the black player.
	 * 
	 * @return the timeBlack as a string
	 */
    public String showTimeBlack() {
        return timeBlack.getDureeTxt();
    }

	/**
	 * Returns whether it is currently the white player's turn.
	 * 
	 * @return true if it is the white player's turn, false otherwise
	 */
	public boolean isTurnWhite() {
		return turnWhite;
	}

	/**
	 * Returns the current board state of the game.
	 * 
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}

    /*
    =============================
    ---------- Setters ----------
    =============================
    */

	/**
	 * Add 1 to the move number
	 */
	public void addMoveNumber() {
        this.moveNumber++;
    }

	/**
	 * Sets the time for the white player.
	 * 
	 * @param timeWhite the timeWhite to set
	 */
	public void setTimeWhite(int timeWhite) {
        this.timeWhite.start(timeWhite);
        this.timeWhite.pause();
    }

    /**
     * Sets the time for the black player.
	 * 
     * @param timeBlack the timeBlack to set
     */
    public void setTimeBlack(int timeBlack) {
        this.timeBlack.start(timeBlack);
        this.timeBlack.pause();
    }

	/**
	 * switches who's turn it is
	 */
	public void switchColorTurn() {
        this.turnWhite = !this.turnWhite;
    }

	/**
	 * Setter for the board of the game
	 * 
	 * @param board the board to set
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

    /*
    =============================
    ---------- Methods ----------
    =============================
    */

	/**
	 * Method to move a piece from one position to another, if the move is legal, it will return true and move the piece, false otherwise
	 * 
	 * @param from the position of the piece to move
	 * @param to the position to move the piece to
	 */
	public void move(Position from,Position to){
		if(board.movePiece(from.getX(),from.getY(),to.getX(),to.getY())){
			turnWhite=board.isTurnWhite();
			moveNumber=board.getMoveNumber();
		}
	}

	/**
	 * Method to move a piece from one position to another with promotion, if the move is legal, it will return true and move the piece, false otherwise
	 * 
	 * @param from the position of the piece to move
	 * @param to the position to move the piece to
	 * @param newPiece the piece to promote to
	 */
	private void movePromote(Position from, Position to, Pieces newPiece){
		if(board.movePiece(from.getX(),from.getY(),to.getX(),to.getY(),newPiece)){
			turnWhite=board.isTurnWhite();
			moveNumber=board.getMoveNumber();
		}
	}

	/**
	 * Method to move a piece from one position to another with promotion, if the move is legal, it will return true and move the piece, false otherwise
	 * 
	 * @param from the position of the piece to move
	 * @param to the position to move the piece to
	 * @param pieceRep the character representation of the piece to promote to (e.g., 'Q' for queen, 'R' for rook, 'B' for bishop, 'N' for knight)
	 */
	public void movePromote(Position from, Position to, char pieceRep){
		char colorRep = board.pieceAt(from.getX(), from.getY()).isWhite()? Character.toUpperCase(pieceRep):Character.toLowerCase(pieceRep);
		movePromote(from,to,board.getPieceFromChar(colorRep));
	}

	/**
	 * Method to check if the game has ended due to a timeout for either player.
	 * 
	 * @return 0 if the black player has run out of time, 1 if the white player has run out of time, 2 if neither player has run out of time
	 */
    public int timeOut() {
    	if (this.timeBlack.getDureeMs() < 1 && timedGame) {
            return 0;
        } else if (this.timeWhite.getDureeMs() < 1 && timedGame) {
        	return 1;
        } else {
        	return 2;
        }
    }
    
	/**
	 * Starts the countdown for the white player.
	 */
    public void countdownWhite() {
		timeWhite.resume();   // Blanc tourne
		timeBlack.pause();    // Noir en pause
	}
    
	/**
	 * Starts the countdown for the black player.
	 */
    public void countdownBlack() {
    	timeBlack.resume();   // Noir tourne
   		timeWhite.pause();    // Blanc en pause
    }

	/**
	 * Method to manage the countdown for both players, it will check if any player has run out of time and if not, 
	 * it will continue the countdown for the current player.
	 * 
	 * @throws InterruptedException if the thread is interrupted while sleeping
	 */
    public void countdown() throws InterruptedException {
	    while (timeOut() == 2) {
	        if (isTurnWhite()) {
	            countdownWhite();   // Blanc tourne  
	        } else {
	            countdownBlack();   // Noir tourne
	        }
	
	        Thread.sleep(100);
	    }
    }

	/**
     * Pauses the game clocks if the current game is timed.
     * Safe to call on untimed games (it simply does nothing).
     */
    public void pauseGame() {
		if (this.timedGame && this.timeWhite != null && this.timeBlack != null) {
            this.timeWhite.pause();
            this.timeBlack.pause();
        }
	}
    
	/**
	 * Method to check if the game has ended due to a checkmate condition.
	 * 
	 * @return true if the game has ended due to checkmate, false otherwise
	 */
    public boolean gameFinished() {
        return this.board.checkmate();
    }

    /**
     * Saves the current game state to a JSON file in the "saves" directory.
	 * 
     * @param filename the name of the file to save the game state to (e.g., "save1.json")
	 * 
	 * @throws IOException if there is an error during saving the game state to the file
     */
    public void saveGame(String filename) throws IOException {
	
    	filename = "saves/" + filename;
    	
		Gson gson = GsonGenerate.getChessGson();
		
	    try (FileWriter writer = new FileWriter(filename)) {
	        gson.toJson(this, writer);
		}
    }
    
	/**
	 * Loads a game state from a JSON file in the "saves" directory.
	 * 
	 * @param filename the name of the file to load the game state from (e.g., "save1.json")
	 * 
	 * @return the loaded Game object, or null if an error occurs
	 * 
	 * @throws Exception if there is an error during loading
	 */
    public Game loadGame(String filename) throws Exception {
	
		filename = "saves/" + filename;
	
		Gson gson = GsonGenerate.getChessGson();
	    
	    try (FileReader reader = new FileReader(filename)) {
	        Game game = gson.fromJson(reader, Game.class);
	
	        if (game != null && game.getBoard() != null) {
	            Board b = game.getBoard();
				b.setGame(game);
	            
	            for (Pieces[] row : b.getPieceMatrix()) {
	                for (Pieces p : row) {
	                    if (p != null) {
	                        p.setBoard(b); 
	                    }
	                }
	            }
	        }
	        return game;
	    }
	}


}
