package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.Position;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.GameModel;
import model.GameModelListener;
import mvc.Controller;
import view.ChessView;

/**
 * Bridges the View (user actions) and the Model (game state).
 *
 * Responsibilities:
 *  - Two-click move selection
 *  - Pawn-promotion dialog
 *  - Move-history notation (algebraic-like)
 *  - Clock management (200 ms Timeline)
 *  - End-of-game detection (checkmate + timeout)
 */
public class GameController extends Controller
        implements ChessActionListener, GameModelListener {

    // ── State ─────────────────────────────────────────────────────────────────

    /** First-click selection (null = nothing selected yet). */
    private Position pendingFrom = null;

    /** Blocks board clicks while the promotion chooser is open. */
    private boolean awaitingPromotion = false;

    // ── Clock ─────────────────────────────────────────────────────────────────

    /** The timeline for the game clock. */
    private Timeline clockTimeline;

    // ── Move history ──────────────────────────────────────────────────────────

    /** One entry per full move; e.g. "1.  ♙e2–e4   ♟e7–e5". */
    private final List<String> historyLines = new ArrayList<>();

    /** Counts half-moves (increments after every single move). */
    private int halfMoveCount = 0;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Constructor for the GameController, initializes the controller with the given model and view.
     * 
     * @param model the GameModel to be controlled by this controller
     * @param view the ChessView that this controller will update based on the model's state and user interactions
     */
    public GameController(GameModel model, ChessView view) {
        super(model, view);
    }

    // ── Initialisation ────────────────────────────────────────────────────────

    /**
     * Initializes the game controller.
     */
    public void init() {
        cv().setActionListener(this);
        gm().addListener(this);
        gm().run();

        if (gm().isTimedGame()) {
            startClockTimeline();
            gm().countdownWhite();   // White always goes first
        }
    }

    // ── Clock ─────────────────────────────────────────────────────────────────

    /**
     * Starts the clock timeline for timed games.
     */
    private void startClockTimeline() {
        GameModel gm = gm();
        ChessView cv = cv();

        clockTimeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            cv.updateTimers(gm.showTimeWhite(), gm.showTimeBlack());

            int status = gm.timeOut();
            if (status != 2) {           // someone timed out
                stopClock();
                if (status == 0) {       // Black's clock hit 0
                    cv.showEndGame("Victoire des Blancs !",
                                   "Temps écoulé — les Noirs ont perdu au temps.");
                } else {                  // White's clock hit 0
                    cv.showEndGame("Victoire des Noirs !",
                                   "Temps écoulé — les Blancs ont perdu au temps.");
                }
            }
        }));
        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();
    }

    /**
     * Stops the clock timeline.
     */
    private void stopClock() {
        if (clockTimeline != null) {
            clockTimeline.stop();
            clockTimeline = null;
        }
    }

    // ── Model listener ────────────────────────────────────────────────────────

    /**
     * Called when the game model changes.
     * 
     * @param model the updated game model
     */
    @Override
    public void onModelChanged(GameModel model) {
        refreshView();
        // Switch active clock only if the game is still going
        if (model.isTimedGame() && !model.isGameFinished()) {
            if (model.isTurnWhite()) model.countdownWhite();
            else                     model.countdownBlack();
        }
    }

    /**
     * Called when a square is selected in the view.
     * Handles both the first click (selecting a piece) and the second click (attempting a move).
     * The method also manages the promotion dialog if a pawn promotion move is detected.
     * The method ensures that only legal moves are executed and updates the move history and end game status accordingly.
     * The method also ignores clicks when waiting for a promotion choice and handles the case where the user clicks the same square twice (deselecting).
     * 
     * @param pos the position of the selected square
     */
    @Override
    public void onSquareSelected(Position pos) {
        if (awaitingPromotion) return;   // wait for promotion choice

        GameModel gm = gm();
        ChessView cv = cv();
        
        // null = resume signal from pause menu
        if (pos == null) {
            pendingFrom = null;
            cv.highlightMoves(new ArrayList<>());
            if (gm.isTimedGame() && !gm.isGameFinished()) {
                // Restarts the UI clock display loop
                startClockTimeline();
                
                // Resumes the active player's internal timer
                if (gm.isTurnWhite()) {
                    gm.countdownWhite(); 
                } else {
                    gm.countdownBlack();
                }
            }
            return;
        }

        if (pendingFrom == null) {
            // ── First click: select a piece ──────────────────────────────────
            if (!gm.isPieceBelongingToCurrentPlayer(pos)) return;

            pendingFrom = pos;
            cv.highlightMoves(gm.getLegalMovesFor(pos));
            cv.selectSquare(pos);

        } else {
            // ── Second click: attempt a move ─────────────────────────────────
            Position from = pendingFrom;
            pendingFrom = null;
            cv.selectSquare(null);
            cv.highlightMoves(new ArrayList<>());

            if (from.getX() == pos.getX() && from.getY() == pos.getY()) return;

            if (!gm.hasPiece(from)) return;

            // Build notation BEFORE the move
            String notation = buildNotation(from, pos);
            boolean isWhite = gm.isPieceWhite(from);

            if (gm.isPromotionMove(from, pos)) {
                // ── Promotion path ───────────────────────────────────────────
                awaitingPromotion = true;
                cv.showPromotionChoice(isWhite, chosen -> {
                    awaitingPromotion = false;
                    gm.movePromote(from, pos, chosen);

                    String promoNotation = notation + "=" + promotionSymbol(chosen);
                    recordHalfMove(promoNotation, isWhite);

                    if (gm.isGameFinished()) {
                        stopClock();
                        cv.showEndGame(winnerTitle(gm), "Promotion suivie d'échec et mat !");
                    }
                });

            } else {
                // ── Normal move path ─────────────────────────────────────────
                gm.move(from, pos);
                recordHalfMove(notation, isWhite);

                if (gm.isGameFinished()) {
                    stopClock();
                    cv.showEndGame(winnerTitle(gm), "Échec et mat !");
                }
            }
        }
    }

    /**
     * Called when a keyboard command is received.
     * Parses the command, validates it against the current game state, executes legal moves, handles promotions, 
     * updates move history, and checks for end game conditions.
     * 
     * @param command the command string
     */
    @Override
    public void onKeyboardCommand(String command) {
        // Ignore keyboard input while waiting for promotion choice
        if (awaitingPromotion) return;

        GameModel gm = gm();
        String cmd = command.trim().toLowerCase().replaceAll("\\s+", "");

        // Regex to capture e2e4, e2-e4, e2 e4, with optional promotion (e.g., e7e8=q or e7e8q)
        // [a-p] covers A through P to support boards up to 16x16
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("^([a-p])(\\d+)[-]?([a-p])(\\d+)[=]?([qrbn]?)$");
        java.util.regex.Matcher m = p.matcher(cmd);

        if (m.matches()) {
            int fromX = m.group(1).charAt(0) - 'a';
            int fromY = gm.getBoard().getHeight() - Integer.parseInt(m.group(2));
            
            int toX = m.group(3).charAt(0) - 'a';
            int toY = gm.getBoard().getHeight() - Integer.parseInt(m.group(4));
            
            String promo = m.group(5);

            Position from = new Position(fromX, fromY);
            Position to = new Position(toX, toY);

            // Check that the starting space contains a piece belonging to the active player
            if (!gm.hasPiece(from) || !gm.isPieceBelongingToCurrentPlayer(from)) {
                cv().showMessage("Mouvement invalide : Aucune pièce à vous sur la case de départ !");
                return;
            }

            // Check if the destination is among the legal moves for this piece
            boolean isLegal = false;
            for (Position pos : gm.getLegalMovesFor(from)) {
                if (pos.getX() == to.getX() && pos.getY() == to.getY()) {
                    isLegal = true;
                    break;
                }
            }

            if (!isLegal) {
                cv().showMessage("Mouvement invalide : Ce coup est illégal pour cette pièce.");
                return;
            }

            // Execute the move
            boolean isWhite = gm.isPieceWhite(from);
            String notation = buildNotation(from, to);

            if (gm.isPromotionMove(from, to)) {
                // If the player hasn't entered a promotion letter, a Queen (Q) is placed by default
                char promoChar = promo.isEmpty() ? 'Q' : promo.toUpperCase().charAt(0);
                gm.movePromote(from, to, promoChar);
                recordHalfMove(notation + "=" + promoChar, isWhite);
            } else {
                gm.move(from, to);
                recordHalfMove(notation, isWhite);
            }

            // Check for game end
            if (gm.isGameFinished()) {
                stopClock();
                cv().showEndGame(winnerTitle(gm), "Échec et mat !");
            }

            // We make sure to clear the mouse's visual selections
            cv().selectSquare(null);
            cv().highlightMoves(new java.util.ArrayList<>());

        } else {
            // If the notation is incorrect or the move is illegal, show an error message
            cv().showMessage("Format attendu : e2e4 ou e2-e4 (case de départ vers case d'arrivée)"
                                + " avec optionnellement =Q/R/B/N pour la promotion (ex: e7e8=Q)"
            );
        }
    }

    /**
     * Called when a new game is requested.
     */
    @Override
    public void onNewGameRequested(){ 
        pendingFrom = null; refreshView();
    }

    /**
     * Handles the request to save the current game.
     * 
     * @param filename The name of the file to save the game to.
     */
    @Override
    public void onSaveRequested(String filename){
        try {
            gm().saveGame(filename);
            cv().showMessage("Partie sauvegardée avec succès !");
        } catch (IOException e) {
            cv().showMessage("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
    
    /**
     * Handles the request to load a game from a file.
     * Loads the game state from the specified file, updates the view, and manages the move history and clock accordingly.
     * Displays success or error messages based on the outcome of the loading process.
     * 
     * @param filename The name of the file to load the game from.
     */
    @Override 
    public void onLoadRequested(String filename) { 
        try {
            // Load the saved game state into the model
            gm().loadGame(filename);
            
            // Clear the move history (as it is not saved in the JSON file)
            historyLines.clear();
            halfMoveCount = gm().getMoveNumber() * 2; // Resumes approximate move numbering
            cv().setMoveHistory(new ArrayList<>());
            
            // Stop current clock, then restart it if the loaded game is timed and ongoing
            stopClock();
            if (gm().isTimedGame() && !gm().isGameFinished()) {
                startClockTimeline();
            }
            
            // Close the pause menu and display a success notification
            cv().setPauseMenuVisible(false);
            cv().showMessage("La partie a été chargée avec succès !");
            
        } catch (Exception e) {
            cv().showMessage("Erreur lors du chargement : " + e.getMessage());
        }
    }

    /**
     * Handles the request to pause the current game.
     */
    @Override
    public void onPauseRequested() {
        stopClock();
        gm().pauseGame();
    }

    // ── History ───────────────────────────────────────────────────────────────

    /**
     * Appends one half-move to the history list and updates the view.
     *
     * @param notation  Formatted string for this half-move
     * @param isWhite   True if White just moved
     */
    private void recordHalfMove(String notation, boolean isWhite) {
        halfMoveCount++;
        if (isWhite) {
            // White move: start a new numbered line
            int num = (halfMoveCount + 1) / 2;
            historyLines.add(String.format("%-3s  %-14s", num + ".", notation));
        } else {
            // Black move: append to the last line
            if (!historyLines.isEmpty()) {
                int last = historyLines.size() - 1;
                historyLines.set(last, historyLines.get(last) + notation);
            } else {
                historyLines.add("...  " + notation);
            }
        }
        cv().setMoveHistory(new ArrayList<>(historyLines));
    }

    // ── Notation helpers ──────────────────────────────────────────────────────

    /**
     * Converts a move into a human-readable algebraic notation string (e.g., "Nf3–e5").
     * 
     * @param from The starting position of the piece.
     * @param to   The destination position of the piece.
     * 
     * @return The formatted move notation.
     */
    private String buildNotation(Position from, Position to) {
        return gm().getPieceRepresentation(from) + toAlg(from) + "–" + toAlg(to);
    }

    /** 
     * Converts grid coordinates to standard chess algebraic notation 
     * (e.g., column 4, row 6 -> "e2").
     * 
     * @param p The position on the board.
     * 
     * @return The corresponding algebraic coordinate string.
     */
    private String toAlg(Position p) {
        return "" + (char) ('a' + p.getX()) + (8 - p.getY());
    }

    /** 
     * Returns the uppercase symbol for the chosen promotion piece (e.g., "Q" for Queen).
     * 
     * @param c The character representing the selected piece.
     * 
     * @return The formatted promotion symbol.
     */
    private String promotionSymbol(char c) {
        return String.valueOf(Character.toUpperCase(c));
    }

    // ── End game helpers ──────────────────────────────────────────────────────

    /**
     * Determines the winner title after the model state has been updated.
     * After the last move: isTurnWhite() == true → it is now White's turn but
     * White is mated → Noirs won. And vice-versa.
     * 
     * @param gm The current game model, used to determine the active player and thus the winner.
     * 
     * @return The title string indicating the winner of the game.
     */
    private String winnerTitle(GameModel gm) {
        return gm.isTurnWhite()
            ? "Victoire des Noirs !"
            : "Victoire des Blancs !";
    }

    // ── Private accessors ─────────────────────────────────────────────────────

    /**
     * Refreshes the view with the current game state.
     */
    private void refreshView() {
        GameModel gm = gm();
        ChessView cv = cv();
        cv.updateBoard(gm.getBoard());
        cv.setTurn(gm.isTurnWhite());
        cv.updateTimers(gm.showTimeWhite(), gm.showTimeBlack());
    }

    /**
     * Private helper to cast the generic model to a GameModel, for easier access to game-specific methods.
     * 
     * @return The GameModel instance associated with this controller.
     */
    private GameModel gm() {
        return (GameModel) this.model;
    }

    /**
     * Private helper to cast the generic view to a ChessView, for easier access to chess-specific UI methods.
     * @return The ChessView instance associated with this controller.
     */
    private ChessView cv() {
        return (ChessView) this.view;
    }
}