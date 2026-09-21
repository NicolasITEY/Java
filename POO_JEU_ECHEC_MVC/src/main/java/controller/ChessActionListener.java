package controller;

import core.Position;

/**
 * Listener for user actions in the chess view.
 */
public interface ChessActionListener {

	/**
     * Triggered by the View when a square on the board is clicked.
     * 
     * @param position The coordinates of the selected square. Must not be null.
     */
    void onSquareSelected(Position position);

    /**
     * Triggered by the View when the user requests to start a new game.
     */
    void onNewGameRequested();

    /**
     * Triggered by the View when the user requests to save the current game.
     * 
     * @param filename The destination file name.
     */
    void onSaveRequested(String filename);
    
     /**
	 * Triggered by the View when the user requests to load a game from a file.
     * 
	 * @param filename The source file name.
	 */
    void onLoadRequested(String filename);
    
     /**
	  * Triggered by the View when the user requests to pause the game.
	  */
    void onPauseRequested();

    /** 
     * Triggered when the user types a move in the keyboard input.
     * 
     * @param command The command entered by the user, expected to be in PGN format.
     */
    void onKeyboardCommand(String command);
}
