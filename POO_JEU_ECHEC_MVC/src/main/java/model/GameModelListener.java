package model;

/**
 * Observer interface: The Model notifies the Controller of a state change.
 */
public interface GameModelListener {

	/**
     * Called whenever the game state has changed (e.g., a move is played, game over).
     * 
     * @param model The model that triggered the change. Must not be null.
     */
    void onModelChanged(GameModel model);
}
