package view;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import controller.ChessActionListener;
import core.Board;
import core.Position;
import core.pieces.Pieces;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

/**
 * Main chess game view (900 × 760).
 * Handles the display of the chessboard, player timers, move history, and overlays.
 *
 * Layout structure (inside the StackPane):
 * - Layer 1 (Bottom): Main game layout (Board, History, Player Bars).
 * - Layer 2: Return to summary button (Visible only when observing the board after a game).
 * - Layer 3: Overlays (Pause Menu, Promotion Chooser, End Game Summary).
 */
public class GameView extends StackPane implements ChessView {

    // ── Colours ───────────────────────────────────────────────────────────────
    /** The background colour for the game view. */
    private static final String BG       = "#2c3e50";
    /** The colour for idle player bars. */
    private static final String BAR_IDLE = "#34495e";
    /** The colour for active player bars. */
    private static final String BAR_ACT  = "#1a252f";
    /** The colour for muted elements. */
    private static final String MUTED    = "#bdc3c7";
    /** The colour for green elements. */
    private static final String GREEN    = "#27ae60";
    /** The colour for blue elements. */
    private static final String BLUE     = "#2980b9";
    /** The colour for grey elements. */
    private static final String GREY     = "#7f8c8d";
    /** The colour for red elements. */
    private static final String RED      = "#e74c3c";
    /** The colour for orange elements. */
    private static final String ORANGE   = "#e67e22";

    // ── Board layer ───────────────────────────────────────────────────────────
    /** The GridPane representing the chessboard. Each cell will contain a StackPane with the square and piece image. */
    private final GridPane boardGrid = new GridPane();

    // ── Player bars ───────────────────────────────────────────────────────────
    /** The label for the white player's name. */
    private final Label whiteNameLabel  = new Label("Joueur 1");
    /** The label for the black player's name. */
    private final Label blackNameLabel  = new Label("Joueur 2");
    /** The label for the white player's timer. */
    private final Label whiteTimerLabel = new Label("");
    /** The label for the black player's timer. */
    private final Label blackTimerLabel = new Label("");
    /** The container for the white player's bar. */
    private final HBox  whiteBar = new HBox();
    /** The container for the black player's bar. */
    private final HBox  blackBar = new HBox();

    // ── History panel ─────────────────────────────────────────────────────────
    /** The list view for displaying the move history. */
    private final ListView<String> historyList = new ListView<>();
    /** Flag indicating whether the game is in keyboard mode. */
    private boolean keyboardMode = false;
    /** The text field for entering keyboard commands (e.g., "e2e4"). */
    private final TextField commandInput = new TextField();

    // ── Overlays ──────────────────────────────────────────────────────────────
    /** The overlay for the pause menu. */
    private final VBox pauseOverlay     = new VBox(18);
    /** The overlay for piece promotion. */
    private final StackPane promotionOverlay = new StackPane();
    /** The overlay for the end-game summary. */
    private final StackPane endGameOverlay   = new StackPane();
    /** The button to return to the game summary. */
    private final Button returnToSummaryBtn = new Button("RETOUR AU RÉSUMÉ");

    // Dynamic labels inside end-game overlay
    /** The label for the end-game title. */
    private final Label endTitleLabel  = new Label();
    /** The label for the end-game reason. */
    private final Label endReasonLabel = new Label();

    // Promotion pieces box (rebuilt for each colour)
    /** The container for the promotion pieces. */
    private final HBox promoPiecesBox = new HBox(16);
    /** The callback to notify when a piece is selected for promotion. */
    private Consumer<Character> promoCallback;

    /** The view manager to handle navigation between views. */
    private final ViewManager manager;

    /**
     * The action listener to notify of user interactions (square clicks, keyboard commands, save/load requests).
     * Must be set for the view to be interactive.
     */
    private ChessActionListener actionListener;

    
    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Constructor for the GameView class, initializes the view components and layout.
     * 
     * @param manager the ViewManager instance to handle navigation between views. Must not be null.
     */
    public GameView(ViewManager manager) {
        this.manager = manager;
        this.setStyle("-fx-background-color: " + BG + ";");

        buildPlayerBar(blackBar, blackNameLabel, blackTimerLabel, false);
        buildPlayerBar(whiteBar, whiteNameLabel, whiteTimerLabel, true);
        // initGrid();

        // Centre: board (left) + history (right)
        HBox centre = new HBox();
        centre.getChildren().addAll(boardGrid, buildHistoryPanel());

        BorderPane gameLayout = new BorderPane();
        gameLayout.setTop(blackBar);
        gameLayout.setCenter(centre);
        gameLayout.setBottom(whiteBar);
        gameLayout.setStyle("-fx-background-color: " + BG + ";");

        buildPauseOverlay();
        buildPromotionOverlay();
        buildEndGameOverlay();
        buildReturnButton();

        this.getChildren().addAll(gameLayout, pauseOverlay, promotionOverlay, endGameOverlay, returnToSummaryBtn);
    }

    // ── Player bars ───────────────────────────────────────────────────────────

    /**
     * Helper method to construct the player information bars displayed at the top and bottom of the screen.
     * Each bar includes a colored dot indicating player color, the player's name, and their timer.
     * 
     * @param bar the HBox container for the player bar
     * @param nameLabel the Label to display the player's name
     * @param timerLabel the Label to display the player's timer
     * @param isWhite true if this bar is for the white player, false for black (used to set the color of the dot)
     */
    private void buildPlayerBar(HBox bar, Label nameLabel, Label timerLabel,
                                boolean isWhite) {
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 20, 0, 20));
        bar.setPrefHeight(55);
        bar.setStyle("-fx-background-color: " + BAR_IDLE + ";");

        Circle dot = new Circle(11);
        dot.setFill(isWhite ? Color.web("#f0d9b5") : Color.web("#333"));
        dot.setStroke(Color.web(MUTED));
        dot.setStrokeWidth(1.5);

        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        timerLabel.setTextFill(Color.web(MUTED));
        timerLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: monospace;");

        bar.getChildren().addAll(dot, nameLabel, spacer, timerLabel);
        HBox.setMargin(dot, new Insets(0, 12, 0, 0));
    }

    // ── History panel ─────────────────────────────────────────────────────────

    /**
     * Helper method to construct the move history panel on the right side of the screen.
     * Includes a header label, a ListView to display the move history, and a TextField for keyboard command input (hidden by default).
     * 
     * @return a VBox containing the fully constructed history panel
     */
    private VBox buildHistoryPanel() {
        Label header = new Label("HISTORIQUE");
        header.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + MUTED + ";");
        header.setPadding(new Insets(10, 12, 6, 12));

        historyList.setStyle(
            "-fx-background-color: " + BAR_IDLE + ";" +
            "-fx-control-inner-background: " + BAR_IDLE + ";" +
            "-fx-background-insets: 0; -fx-padding: 0;"
        );

        historyList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: " + BAR_IDLE + ";");
                } else {
                    setText(item);
                    setStyle(
                        "-fx-background-color: " + BAR_IDLE + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: monospace;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 4 12;"
                    );
                }
            }
        });

        // Exact height = board (640) + two bars (55+55)
        historyList.setPrefHeight(640);

        // --- Configuration du champ de texte ---
        commandInput.setPromptText("Ex: e2e4, e2-e4");
        commandInput.setStyle("-fx-background-color: #1a252f; -fx-text-fill: white; -fx-font-family: monospace; -fx-padding: 12; -fx-font-size: 14px; -fx-border-color: transparent;");
        commandInput.setVisible(false); // Caché par défaut
        commandInput.setManaged(false);

        // Action quand on appuie sur Entrée
        commandInput.setOnAction(e -> {
            String cmd = commandInput.getText().trim();
            if (!cmd.isEmpty() && actionListener != null) {
                actionListener.onKeyboardCommand(cmd);
                commandInput.clear(); // On vide le champ après l'envoi
            }
        });

        VBox panel = new VBox(0, header, historyList, commandInput);
        panel.setPrefWidth(220);
        panel.setStyle("-fx-background-color: " + BAR_IDLE + ";");
        VBox.setVgrow(historyList, Priority.ALWAYS);
        return panel;
    }

    // ── Pause overlay ─────────────────────────────────────────────────────────

    /**
     * Builds the pause overlay panel.
     */
    private void buildPauseOverlay() {
        pauseOverlay.setAlignment(Pos.CENTER);
        pauseOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.72);");
        pauseOverlay.setVisible(false);

        Label title = overlayTitle("PAUSE");

        Button resume = overlayButton("Reprendre",    GREEN);
        Button save   = overlayButton("Sauvegarder",  BLUE);
        Button load   = overlayButton("Charger", ORANGE);
        Button quit   = overlayButton("Menu principal", GREY);

        resume.setOnAction(e -> {
            setPauseMenuVisible(false);
            if (actionListener != null) actionListener.onSquareSelected(null);
        });
        save.setOnAction(e -> {
            if (actionListener != null) actionListener.onSaveRequested("autosave.json");
            showMessage("Partie sauvegardée dans autosave.json !");
        });

        load.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Charger une sauvegarde");
            
            File savesDir = new File("saves");
            if (!savesDir.exists()) savesDir.mkdirs(); 
            fileChooser.setInitialDirectory(savesDir);
            
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON", "*.json"));
            
            // Affiche la fenêtre de sélection
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (file != null) {
                if (actionListener != null) actionListener.onLoadRequested(file.getName());
            }
        });

        quit.setOnAction(e -> {
            javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Retourner au menu ? La progression non sauvegardée sera perdue.");
            alert.showAndWait().ifPresent(r -> {
                if (r == javafx.scene.control.ButtonType.OK) manager.showMainMenu();
            });
        });

        // --- Création du Toggle Button ---
        javafx.scene.control.ToggleButton kbToggle = new javafx.scene.control.ToggleButton("Mode Clavier : OFF");
        kbToggle.setMinWidth(230);
        kbToggle.setMinHeight(44);
        kbToggle.setStyle("-fx-background-color: " + GREY + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        kbToggle.setOnAction(e -> {
            keyboardMode = kbToggle.isSelected();
            if (keyboardMode) {
                kbToggle.setText("Mode Clavier : ON");
                kbToggle.setStyle(kbToggle.getStyle().replace(GREY, BLUE)); // Passe en bleu
                commandInput.setVisible(true);
                commandInput.setManaged(true);
            } else {
                kbToggle.setText("Mode Clavier : OFF");
                kbToggle.setStyle(kbToggle.getStyle().replace(BLUE, GREY)); // Repasse en gris
                commandInput.setVisible(false);
                commandInput.setManaged(false);
            }
        });

        pauseOverlay.getChildren().addAll(title, resume, kbToggle, save, load, quit);
    }

    /**
     * Checks if the pause menu overlay is currently visible on the screen.
     *
     * @return true if the pause menu is displayed, false otherwise.
     */
    @Override
    public boolean isPauseMenuVisible() {
        return pauseOverlay.isVisible();
    }

    // ── Promotion overlay ─────────────────────────────────────────────────────

    /**
     * Builds the promotion overlay panel.
     */
    private void buildPromotionOverlay() {
        promotionOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.78);");
        promotionOverlay.setVisible(false);
        promotionOverlay.setAlignment(Pos.CENTER);

        Label title = overlayTitle("Promotion du pion");
        Label sub   = new Label("Choisissez la pièce de remplacement :");
        sub.setTextFill(Color.web(MUTED));

        promoPiecesBox.setAlignment(Pos.CENTER);

        VBox content = new VBox(18, title, sub, promoPiecesBox);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        content.setMaxWidth(480);
        content.setStyle(
            "-fx-background-color: #2c3e50;" +
            "-fx-background-radius: 14;"
        );

        promotionOverlay.getChildren().add(content);
    }

    /** 
     * Rebuilds the piece buttons for the correct colour, then shows the overlay. 
     * 
     * @param isWhite true if it is White's pawn being promoted, false for Black
     * @param callback receives the chosen piece character: 'Q', 'R', 'B' or 'N'
     */
    @Override
    public void showPromotionChoice(boolean isWhite, Consumer<Character> callback) {
        this.promoCallback = callback;
        promoPiecesBox.getChildren().clear();

        char[]   chars  = {'Q', 'R', 'B', 'N'};
        String[] labels = {"Dame", "Tour", "Fou", "Cavalier"};
        String   color  = isWhite ? "white" : "black";

        for (int i = 0; i < chars.length; i++) {
            char   c    = chars[i];
            Button btn  = new Button(labels[i]);
            btn.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
            btn.setStyle(
                "-fx-background-color: #34495e;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13px;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-min-width: 90;" +
                "-fx-min-height: 90;"
            );

            try {
                String name = switch (c) {
                    case 'Q' -> "Queen";
                    case 'R' -> "Rook";
                    case 'B' -> "Bishop";
                    default  -> "Knight";
                };
                Image img = new Image(
                    getClass().getResourceAsStream("/images/" + name + "_" + color + ".png")
                );
                ImageView iv = new ImageView(img);
                iv.setFitWidth(56);
                iv.setFitHeight(56);
                btn.setGraphic(iv);
            } catch (Exception ignored) { }

            btn.setOnAction(e -> {
                promotionOverlay.setVisible(false);
                if (promoCallback != null) promoCallback.accept(c);
            });
            promoPiecesBox.getChildren().add(btn);
        }

        promotionOverlay.setVisible(true);
    }

    // ── End-game overlay ──────────────────────────────────────────────────────

    /**
     * Builds the end-game overlay panel.
     */
    private void buildEndGameOverlay() {
        endGameOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.78);");
        endGameOverlay.setVisible(false);
        endGameOverlay.setAlignment(Pos.CENTER);

        Label trophy = new Label("🏆");
        trophy.setStyle("-fx-font-size: 54px;");

        endTitleLabel.setStyle(
            "-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;"
        );
        endReasonLabel.setStyle(
            "-fx-font-size: 15px; -fx-text-fill: " + MUTED + ";"
        );

        Button replay = overlayButton("Nouvelle partie", GREEN);
        Button menu   = overlayButton("Menu principal",  GREY);
        Button viewBoard = overlayButton("Voir l'échiquier", BLUE);

        replay.setOnAction(e -> manager.showGameSetup());
        menu.setOnAction(e -> manager.showMainMenu());
        viewBoard.setOnAction(e -> {
            endGameOverlay.setVisible(false);
            returnToSummaryBtn.setVisible(true); // Affiche le bouton flottant de retour
        });

        VBox content = new VBox(16, trophy, endTitleLabel, endReasonLabel, viewBoard, replay, menu);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));
        content.setMaxWidth(420);
        content.setStyle(
            "-fx-background-color: #2c3e50;" +
            "-fx-background-radius: 14;"
        );

        endGameOverlay.getChildren().add(content);
    }

    /**
     * Displays the end-game overlay with the specified title and reason.
     *
     * @param title the title of the end-game message
     * @param reason the reason for the end of the game
     */
    @Override
    public void showEndGame(String title, String reason) {
        endTitleLabel.setText(title);
        endReasonLabel.setText(reason);
        endGameOverlay.setVisible(true);
    }

    // ── ChessView implementation ──────────────────────────────────────────────

    /**
     * Sets the names of the players.
     *
     * @param whiteName the name of the white player
     * @param blackName the name of the black player
     */
    @Override
    public void setPlayerNames(String whiteName, String blackName) {
        whiteNameLabel.setText(whiteName);
        blackNameLabel.setText(blackName);
    }

    /**
     * Refreshes the on-screen clock labels.
     * Pass empty strings for untimed games.
     * 
     * @param whiteTime Formatted time string for White.
     * @param blackTime Formatted time string for Black.
     */
    @Override
    public void updateTimers(String whiteTime, String blackTime) {
        whiteTimerLabel.setText(whiteTime);
        blackTimerLabel.setText(blackTime);
        applyTimerColour(whiteTimerLabel, whiteTime);
        applyTimerColour(blackTimerLabel, blackTime);
    }

    /**
     * Helper method to apply color coding to timer labels based on remaining time.
     * - Muted color for empty or non-second formats (e.g., "5min", "1h").
     * - Red for less than 10 seconds, Orange for less than 60 seconds, Muted otherwise.
     * 
     * @param label the Label to update
     * @param txt the formatted time string to analyze
     */
    private void applyTimerColour(Label label, String txt) {
        if (txt.isEmpty() || txt.contains("h") || txt.contains("min")) {
            label.setTextFill(Color.web(MUTED));
            return;
        }
        try {
            long secs = Long.parseLong(txt.split("sec")[0].replaceAll("[^0-9]", "").strip());
            if (secs < 10)      label.setTextFill(Color.web(RED));
            else if (secs < 60) label.setTextFill(Color.web(ORANGE));
            else                label.setTextFill(Color.web(MUTED));
        } catch (NumberFormatException ignored) {
            label.setTextFill(Color.web(MUTED));
        }
    }

    /**
     * Sets the move history list.
     *
     * @param entries the list of move entries
     */
    @Override
    public void setMoveHistory(List<String> entries) {
        historyList.getItems().setAll(entries);
        if (!entries.isEmpty())
            historyList.scrollTo(entries.size() - 1);
    }

    /**
     * Updates the game board display.
     *
     * @param board the current board state
     */
    @Override
    public void updateBoard(Board board) {
        // Clear the previous board state
        boardGrid.getChildren().clear();

        int w = board.getWidth();
        int h = board.getHeight();
        
        // Dynamically adjust tile size based on board dimensions to fit the screen
        int dynamicTile = Math.min(80, 640 / Math.max(w, h));

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                // Create the background square
                Rectangle sq = new Rectangle(dynamicTile, dynamicTile);
                sq.setFill((y + x) % 2 == 0 ? Color.web("#f0d9b5") : Color.web("#b58863"));
                
                // Fixed border (inside) to prevent grid "jumping" during selection
                sq.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
                sq.setStroke(Color.TRANSPARENT);
                sq.setStrokeWidth(4);

                final int curX = x, curY = y;
                sq.setOnMouseClicked(e -> {
                    // Ignore mouse clicks if keyboard mode is active
                    if (!keyboardMode && actionListener != null) {
                        actionListener.onSquareSelected(new Position(curX, curY));
                    }
                });
                boardGrid.add(sq, x, y);

                // --- Coordinate Labels (A-P, 1-16) ---
                // Determine text color based on square color for contrast
                String textColor = (y + x) % 2 == 0 ? "#b58863" : "#f0d9b5";
                
                // Left column numbers
                if (x == 0) { 
                    Label numLbl = new Label(String.valueOf(h - y));
                    numLbl.setStyle("-fx-font-weight: bold; -fx-font-size: " + (dynamicTile * 0.25) + "px; -fx-text-fill: " + textColor + ";");
                    numLbl.setPadding(new Insets(2, 0, 0, 4));
                    GridPane.setHalignment(numLbl, javafx.geometry.HPos.LEFT);
                    GridPane.setValignment(numLbl, javafx.geometry.VPos.TOP);
                    numLbl.setMouseTransparent(true); // Prevent blocking clicks
                    boardGrid.add(numLbl, x, y);
                }
                
                // Bottom row letters
                if (y == h - 1) { 
                    Label letLbl = new Label(String.valueOf((char)('A' + x)));
                    letLbl.setStyle("-fx-font-weight: bold; -fx-font-size: " + (dynamicTile * 0.22) + "px; -fx-text-fill: " + textColor + ";");
                    letLbl.setPadding(new Insets(0, 4, 2, 0));
                    GridPane.setHalignment(letLbl, javafx.geometry.HPos.RIGHT);
                    GridPane.setValignment(letLbl, javafx.geometry.VPos.BOTTOM);
                    letLbl.setMouseTransparent(true);
                    boardGrid.add(letLbl, x, y);
                }

                // Chess piece (ASCII)
                Pieces p = board.pieceAt(x, y);
                if (p != null) {
                    // Create a label for the Unicode chess symbol
                    Label visualPiece = new Label(pieceToSymbol(p));

                    // Style the piece: dynamic font size based on tile, and color based on player
                    visualPiece.setStyle(
                        "-fx-font-size: " + (dynamicTile * 0.75) + "px; " +
                        "-fx-text-fill: " + (p.isWhite() ? "white" : "black") + ";"
                    );

                    // Center the piece in the cell
                    visualPiece.setMouseTransparent(true);
                    GridPane.setHalignment(visualPiece, javafx.geometry.HPos.CENTER);
                    GridPane.setValignment(visualPiece, javafx.geometry.VPos.CENTER);

                    boardGrid.add(visualPiece, x, y);
                }
            }
        }
    }

    /**
     * Highlights the legal moves for the selected piece.
     *
     * @param positions the list of positions to highlight
     */
    @Override
    public void highlightMoves(List<Position> positions) {
        for (Node n : boardGrid.getChildren()) {
            if (n instanceof Rectangle r) {
                r.setStroke(Color.TRANSPARENT);
            }
        }
        for (Position pos : positions) {
            Rectangle r = squareAt(pos.getX(), pos.getY());
            if (r != null) {
                r.setStroke(Color.web("#2ecc71"));
            }
        }
    }

    /**
     * Selects a square on the board.
     *
     * @param pos the position of the square to select
     */
    @Override
    public void selectSquare(Position pos) {
        if (pos == null) return;
        Rectangle r = squareAt(pos.getX(), pos.getY());
        if (r != null) {
            r.setStroke(Color.YELLOW);
        }
    }

    /**
     * Sets the current turn.
     *
     * @param isWhiteTurn true if it's the white player's turn, false otherwise
     */
    @Override
    public void setTurn(boolean isWhiteTurn) {
        whiteBar.setStyle("-fx-background-color: " + (isWhiteTurn  ? BAR_ACT : BAR_IDLE) + ";");
        blackBar.setStyle("-fx-background-color: " + (!isWhiteTurn ? BAR_ACT : BAR_IDLE) + ";");
        String boldW = isWhiteTurn  ? "bold" : "normal";
        String boldB = !isWhiteTurn ? "bold" : "normal";
        whiteNameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: " + boldW + ";");
        blackNameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: " + boldB + ";");
    }

    /**
     * Displays a message to the user.
     *
     * @param msg the message to display
     */
    @Override
    public void showMessage(String msg) {
        javafx.scene.control.Alert a =
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    /**
     * Sets the action listener for the game.
     *
     * @param l the action listener
     */
    @Override
    public void setActionListener(ChessActionListener l) { this.actionListener = l; }

    /**
     * Shows the game view.
     */
    @Override public void show() {
        setVisible(true);
    }

    /**
     * Hides the game view.
     */
    @Override public void hide() {
        setVisible(false);
    }

    /**
     * Sets the visibility of the pause menu.
     *
     * @param v true if the pause menu should be visible, false otherwise
     */
    @Override
    public void setPauseMenuVisible(boolean v) { 
        pauseOverlay.setVisible(v); 
        if (!v && keyboardMode) {
            commandInput.requestFocus();
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Helper method to find the Rectangle node corresponding to a given board position (x, y).
     * Iterates through the children of the boardGrid to find the matching square based on GridPane column and row indices.
     * 
     * @param x the x-coordinate (column index) of the square
     * @param y the y-coordinate (row index) of the square
     * 
     * @return the Rectangle node representing the square at the given position, or null if not found
     */
    private Rectangle squareAt(int x, int y) {
        for (Node n : boardGrid.getChildren()) {
            if (n instanceof Rectangle
                && Integer.valueOf(x).equals(GridPane.getColumnIndex(n))
                && Integer.valueOf(y).equals(GridPane.getRowIndex(n)))
                return (Rectangle) n;
        }
        return null;
    }

    // ── Overlay UI helpers ────────────────────────────────────────────────────

    /**
     * Helper method to create styled title labels for overlays.
     * 
     * @param text the text for the title label
     * 
     * @return the created title label
     */
    private Label overlayTitle(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        return l;
    }

    /**
     * Helper method to create styled buttons for overlays.
     * 
     * @param text the text to display on the button
     * @param bg the background color of the button
     * 
     * @return a styled Button instance
     */
    private Button overlayButton(String text, String bg) {
        Button b = new Button(text);
        b.setMinWidth(230);
        b.setMinHeight(44);
        b.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        return b;
    }

    /**
     * Helper method to convert a Pieces instance to its corresponding Unicode chess symbol.
     * Uses the piece's representation character to determine the type and color of the piece.
     * 
     * @param p the Pieces instance to convert
     * 
     * @return a String containing the Unicode symbol for the piece, or the original representation if not recognized
     */
    private String pieceToSymbol(Pieces p) {
        return switch (Character.toLowerCase(p.getRep().charAt(0))) {
            case 'k' -> "♚";
            case 'q' -> "♛";
            case 'r' -> "♜";
            case 'b' -> "♝";
            case 'n' -> "♞";
            case 'p' -> "♟";
            default  -> p.getRep();
        };
    }

    /**
     * Helper method to build the return button for the summary overlay.
     */
    private void buildReturnButton() {
        returnToSummaryBtn.setVisible(false); // Caché par défaut
        returnToSummaryBtn.setMinWidth(200);
        returnToSummaryBtn.setMinHeight(40);
        // Style flottant en bas de l'écran
        returnToSummaryBtn.setStyle(
            "-fx-background-color: " + BLUE + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 10, 0, 0, 0);"
        );
        
        StackPane.setAlignment(returnToSummaryBtn, Pos.BOTTOM_CENTER);
        StackPane.setMargin(returnToSummaryBtn, new Insets(0, 0, 30, 0));

        returnToSummaryBtn.setOnAction(e -> {
            endGameOverlay.setVisible(true);
            returnToSummaryBtn.setVisible(false);
        });
    }

}