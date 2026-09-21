package view;

import java.util.List;
import java.util.function.Consumer;

import controller.ChessActionListener;
import core.Board;
import core.Position;
import mvc.View;

/**
 * View interface: defines every visual update the Controller may trigger.
 */
public interface ChessView extends View {

    // ── Board ─────────────────────────────────────────────────────────────────

    /**
     * Redraws all pieces from the given board state.
     * 
     * @param board The current state of the chessboard.
     */
    void updateBoard(Board board);

    /**
     * Highlights reachable squares for the selected piece.
     * 
     * @param positions List of legal move coordinates.
     */
    void highlightMoves(List<Position> positions);

    /**
     * Marks the selected square (pass null to clear the selection).
     * 
     * @param position The coordinate of the selected square.
     */
    void selectSquare(Position position);

    /**
     * Updates the active-player indicator (e.g., highlighting the active player's bar).
     * 
     * @param isWhiteTurn true if it is White's turn, false otherwise.
     */
    void setTurn(boolean isWhiteTurn);

    // ── Players ───────────────────────────────────────────────────────────────

    /**
     * Sets display names for both players (must be called before the first render).
     * 
     * @param whiteName Name of the white player.
     * @param blackName Name of the black player.
     */
    void setPlayerNames(String whiteName, String blackName);

    /**
     * Refreshes the on-screen clock labels.
     * Pass empty strings for untimed games.
     * 
     * @param whiteTime Formatted time string for White.
     * @param blackTime Formatted time string for Black.
     */
    void updateTimers(String whiteTime, String blackTime);

    // ── History ───────────────────────────────────────────────────────────────

    /**
     * Replaces the move-history list displayed on the screen.
     * Each entry should be one formatted line (e.g., "1.  e2e4   e7e5").
     * 
     * @param entries The full list of move history strings.
     */
    void setMoveHistory(List<String> entries);

    // ── Game events ───────────────────────────────────────────────────────────

    /**
     * Shows the end-of-game overlay (checkmate, timeout, etc).
     *
     * @param title  e.g. "Victoire des Blancs !"
     * @param reason e.g. "Échec et mat" or "Temps écoulé"
     */
    void showEndGame(String title, String reason);

    /**
     * Shows the pawn-promotion chooser overlay.
     *
     * @param isWhite  true if it is White's pawn being promoted
     * @param callback receives the chosen piece character: 'Q', 'R', 'B' or 'N'
     */
    void showPromotionChoice(boolean isWhite, Consumer<Character> callback);

    /**
     * Displays a simple informational alert popup.
     * 
     * @param message The message to display.
     */
    void showMessage(String message);

    /**
     * Checks if the pause menu overlay is currently visible on the screen.
     *
     * @return true if the pause menu is displayed, false otherwise.
     */
    boolean isPauseMenuVisible();
    
    /**
     * Shows or hides the pause menu overlay.
     *
     * @param visible true to show the pause menu, false to hide it.
     */
    void setPauseMenuVisible(boolean visible);

    // ── Wiring ────────────────────────────────────────────────────────────────

    /**
     * Injects the controller listener to handle user inputs (mouse clicks, keyboard).
     * 
     * @param listener The controller acting as the action listener.
     */
    void setActionListener(ChessActionListener listener);
}