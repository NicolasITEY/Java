package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Represents the main landing screen of the application.
 * Provides navigation options to start a single-player puzzle (N-Queens),
 * a local 2-player chess game, or to exit the application.
 */
public class MainMenuView {

    /** The root layout container for this view. */
    private final VBox root;

    /**
     * Constructs the Main Menu View and initializes its UI components.
     * 
     * @param manager The ViewManager used to handle scene transitions.
     */
    public MainMenuView(ViewManager manager) {
        root = new VBox(22);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2c3e50;");
        root.setPrefSize(ViewManager.W, ViewManager.H);

        // ── Title Section ──────────────────────────────────────────────────────
        Label title = new Label("ÉCHECS");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 56));
        title.setTextFill(Color.WHITE);

        Label pieces = new Label("♚  ♛  ♜  ♝  ♞  ♟");
        pieces.setStyle("-fx-text-fill: #bdc3c7; -fx-font-size: 22px;");
        VBox.setMargin(pieces, new Insets(0, 0, 12, 0));

        // ── Navigation Buttons ─────────────────────────────────────────────────
        Button soloBtn  = createMenuButton("1 JOUEUR  (SOLO)");
        Button localBtn = createMenuButton("2 JOUEURS  (LOCAL)");
        Button quitBtn  = createMenuButton("  QUITTER");

        // Set button actions routing through the ViewManager
        soloBtn.setOnAction(e  -> manager.showSoloSetup());
        localBtn.setOnAction(e -> manager.showGameSetup());
        quitBtn.setOnAction(e  -> System.exit(0));

        // Assemble the view
        root.getChildren().addAll(title, pieces, soloBtn, localBtn, quitBtn);
    }

    /**
     * Helper method to create consistently styled menu buttons with hover effects.
     * 
     * @param text The label text to display on the button.
     * 
     * @return A styled JavaFX Button instance.
     */
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMinWidth(290);
        btn.setMinHeight(52);
        
        String base  = "-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; " +
                       "-fx-font-size: 16px; -fx-font-weight: bold; " +
                       "-fx-cursor: hand; -fx-background-radius: 8;";
        
        String hover = "-fx-background-color: #bdc3c7; -fx-text-fill: #2c3e50; " +
                       "-fx-font-size: 16px; -fx-font-weight: bold; " +
                       "-fx-cursor: hand; -fx-background-radius: 8;";

        btn.setStyle(base);
        // Apply dynamic hover effects
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        
        return btn;
    }

    /**
     * Retrieves the root node of this view to be displayed in the scene.
     * 
     * @return The VBox containing the main menu layout.
     */
    public VBox getRoot() { 
        return root; 
    }
}