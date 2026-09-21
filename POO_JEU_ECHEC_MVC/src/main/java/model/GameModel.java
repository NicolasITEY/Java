package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.Board;
import core.Game;
import core.Position;
import core.pieces.Pieces;
import mvc.Model;

/**
 * Model layer: wraps {@link Game} and notifies listeners on every state change.
 */
public class GameModel implements Model {

    /** The underlying game instance. */
    private Game game;
    /** A list of listeners to notify of changes to the game state. */
    private final List<GameModelListener> listeners = new ArrayList<>();

    // ── Constructors ──────────────────────────────────────────────────────────

    /** 
     * Untimed game, standard starting position. 
     */
    public GameModel() {
        this.game = new Game();
    }

    /** 
     * Untimed game from a FEN string. 
     * 
     * @param fen the FEN string representing the initial board position and game state. 
     * Must be valid according to chess rules, otherwise an exception will be thrown.
     * 
     * @throws Exception if the FEN string is not valid, an exception will be thrown with a message describing the error.
     */
    public GameModel(String fen) throws Exception {
        this.game = new Game(fen);
    }

    /** 
     * Timed game – same clock for both players (minutes). 
     * 
     * @param fen the FEN string representing the initial board position and game state.
     * Must be valid according to chess rules, otherwise an exception will be thrown.
     * @param timerMinutes the number of minutes on the clock for each player at the start of the game. 
     * Must be a positive integer, otherwise an exception will be thrown.
     * 
     * @throws Exception if the FEN string is not valid or if the timerMinutes is not a positive integer, 
     * an exception will be thrown with a message describing the error.
     */
    public GameModel(String fen, int timerMinutes) throws Exception {
        this.game = new Game(fen, timerMinutes);
    }

    /** 
     * Timed game – independent clocks (minutes each). 
     * 
     * @param fen the FEN string representing the initial board position and game state.
     * Must be valid according to chess rules, otherwise an exception will be thrown.
     * @param whiteMinutes the number of minutes on the clock for the white player at the start of the game. 
     * Must be a positive integer, otherwise an exception will be thrown.
     * @param blackMinutes the number of minutes on the clock for the black player at the start of the game. 
     * Must be a positive integer, otherwise an exception will be thrown.
     * 
     * @throws Exception if the FEN string is not valid or if the timerMinutes is not a positive integer, 
     * an exception will be thrown with a message describing the error.
     */
    public GameModel(String fen, int whiteMinutes, int blackMinutes) throws Exception {
        this.game = new Game(fen, whiteMinutes, blackMinutes);
    }

    // ── Model ─────────────────────────────────────────────────────────────────

    /** 
     * Starts the game loop.
     */
    @Override
    public void run() {
        notifyListeners();
    }

    // ── Game actions ──────────────────────────────────────────────────────────

    /**
     * Attempts to move a piece from one position to another, and notifies listeners of the change.
     * 
     * @param from the starting position of the piece to move
     * @param to the target position to move the piece to
     */
    public void move(Position from, Position to) {
        game.move(from, to);
        notifyListeners();
    }

    /**
     * Attempts to move a piece from one position to another with promotion, and notifies listeners of the change.
     * 
     * @param from the starting position of the piece to move
     * @param to the target position to move the piece to
     * @param pieceRep the character representing the piece to promote to (e.g., 'Q' for Queen, 'R' for Rook, 'B' for Bishop, 'N' for Knight)
     */
    public void movePromote(Position from, Position to, char pieceRep) {
        game.movePromote(from, to, pieceRep);
        notifyListeners();
    }

    /**
     * Pauses the game.
     */
    public void pauseGame() { 
        game.pauseGame();
    }

    /**
     * Saves the current game state to a file with the given filename.
     * 
     * @param filename the name of the file to save the game state to. The file will be created in the "saves" directory.
     * 
     * @throws IOException if an I/O error occurs while writing to the file. The exception will be propagated to the caller for handling.
     */
    public void saveGame(String filename) throws IOException {
        game.saveGame(filename);
    }

    /**
     * Loads a game state from a file with the given filename, replacing the current game state with the loaded one.
     * 
     * @param filename the name of the file to load the game state from. The file should be located in the "saves" directory and should contain a valid saved game state.
     * 
     * @throws Exception if the file is invalid or cannot be loaded. The exception will be propagated to the caller for handling, with a message describing the error.
     */
    public void loadGame(String filename) throws Exception {
        Game loadedGame = this.game.loadGame(filename);
        if (loadedGame != null) {
            this.game = loadedGame;
            notifyListeners();
        } else {
            throw new Exception("Le fichier de sauvegarde est invalide ou corrompu.");
        }
    }

    // ── State queries ─────────────────────────────────────────────────────────

    /**
     * Retrieves the current board state of the game.
     * 
     * @return the Board object representing the current state of the chess board, 
     * including the positions of all pieces and any relevant game information.
     */
    public Board getBoard() {
        return game.getBoard();
    }

    /**
     * Checks if it is currently White's turn to move.
     * 
     * @return true if it is White's turn, false if it is Black's turn.
     */
    public boolean isTurnWhite() {
        return game.isTurnWhite();
    }

    /**
     * Checks if the game has finished.
     * 
     * @return true if the game is finished, false otherwise.
     */
    public boolean isGameFinished() {
        return game.gameFinished();
    }

    /**
     * Retrieves the number of moves made in the game.
     * 
     * @return the move number.
     */
    public int getMoveNumber() {
        return game.getMoveNumber();
    }

    // ── Timer queries ─────────────────────────────────────────────────────────

    /**
     * Checks if the game uses countdown clocks.
     * 
     * @return true if the game uses countdown clocks, false otherwise.
     */
    public boolean isTimedGame() {
        return game.isTimedGame();
    }

    /**
     * Remaining time for White as a formatted string.
     * Returns "" if the game is untimed.
     * 
     * @return the remaining time for White, formatted as a string.
     */
    public String showTimeWhite() {
        return game.isTimedGame() ? game.showTimeWhite() : "";
    }

    /**
     * Remaining time for Black as a formatted string.
     * Returns "" if the game is untimed.
     * 
     * @return the remaining time for Black, formatted as a string.
     */
    public String showTimeBlack() {
        return game.isTimedGame() ? game.showTimeBlack() : "";
    }

    /**
     * Checks if either player has run out of time. Returns 0 if Black timed out, 1 if White timed out, and 2 if neither player has timed out.
     * 
     * @return the timeout status.
     */
    public int timeOut() {
        return game.timeOut();
    }

    /**
     * Start/resume White's clock, pause Black's.
     */
    public void countdownWhite() {
        game.countdownWhite();
    }

    /**
     * Start/resume Black's clock, pause White's.
     */
    public void countdownBlack() {
        game.countdownBlack();
    }

    // ── Listeners ─────────────────────────────────────────────────────────────

    /**
     * Adds a listener to the game model.
     * 
     * @param l the listener to add
     */
    public void addListener(GameModelListener l) {
        listeners.add(l);
    }

    /**
     * Removes a listener from the game model.
     * 
     * @param l the listener to remove
     */
    public void removeListener(GameModelListener l) {
        listeners.remove(l);
    }

    /**
     * Notifies all registered listeners that the game model has changed.
     */
    private void notifyListeners() {
        for (GameModelListener l : listeners) l.onModelChanged(this);
    }

    // ── Display Information ───────────────────────────────────────────────────

    /**
     * Retrieves the algebraic string representation of the piece at a given position for the move history.
     * In standard algebraic chess notation, Pawns do not return a letter.
     * 
     * @param pos the position of the piece to get the representation for
     * 
     * @return the algebraic string representation of the piece at the given position, or an empty string if there is no piece at that position.
     */
    public String getPieceRepresentation(Position pos) {
        Pieces piece = game.getBoard().pieceAt(pos.getX(), pos.getY());
        
        if (piece == null) {
            return "";
        }
        
        // If it's a pawn (representation 'p' or 'P'), return no letter
        if (piece.getRep().equalsIgnoreCase("p")) {
            return "";
        }
        
        // For other pieces, return the uppercase letter (K, Q, R, B, N)
        return piece.getRep().toUpperCase();
    }

    // ── Controller Queries (MVC Encapsulation) ────────────────────────────────

    /**
     * Checks if there is a piece at the specified position on the board.
     * 
     * @param pos the position to check for the presence of a piece
     * 
     * @return true if there is a piece at the specified position, false otherwise
     */
    public boolean hasPiece(Position pos) {
        return game.getBoard().pieceAt(pos.getX(), pos.getY()) != null;
    }

    /**
     * Checks if the piece at the specified position is white.
     * 
     * @param pos the position of the piece to check
     * 
     * @return true if the piece at the specified position is white, false otherwise
     */
    public boolean isPieceWhite(Position pos) {
        Pieces piece = game.getBoard().pieceAt(pos.getX(), pos.getY());
        return piece != null && piece.isWhite();
    }

    /**
     * Checks if there is a piece at the specified position that belongs to the player whose turn it is.
     * 
     * @param pos the position of the piece to check
     * 
     * @return true if there is a piece at the specified position that belongs to the current player, false otherwise
     */
    public boolean isPieceBelongingToCurrentPlayer(Position pos) {
        Pieces piece = game.getBoard().pieceAt(pos.getX(), pos.getY());
        return piece != null && piece.isWhite() == game.isTurnWhite();
    }

    /**
     * Retrieves the list of legal moves for a piece at the given position.
     * 
     * @param pos the position of the piece for which to retrieve legal moves
     * 
     * @return the list of legal moves for the piece at the given position
     */
    public List<Position> getLegalMovesFor(Position pos) {
        Pieces piece = game.getBoard().pieceAt(pos.getX(), pos.getY());
        if (piece == null) return new ArrayList<>();
        return game.getBoard().getLegalMove(piece);
    }

    /**
     * Determines if a move from a starting position to a target position results in a pawn promotion.
     * 
     * @param from the starting position of the move
     * @param to the target position of the move
     * 
     * @return true if the move results in a pawn promotion, false otherwise
     */
    public boolean isPromotionMove(Position from, Position to) {
        Pieces piece = game.getBoard().pieceAt(from.getX(), from.getY());
        if (piece == null) return false;
        
        int promRank = piece.isWhite() ? 0 : game.getBoard().getHeight() - 1;
        return piece.getClass().getSimpleName().equals("Pawn") && to.getY() == promRank;
    }
}