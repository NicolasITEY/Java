package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Configuration screen for setting up a new game.
 * Allows users to choose between a classic setup (standard board, custom names/timers)
 * or a custom board editor (variable size, manual piece placement).
 */
public class SetupView {

    // ── Board-editor state ────────────────────────────────────────────────────
    
    /** 2D array representing the custom board: [row][col], 0 = empty square. */
    private char[][] boardState;
    /** Current board dimension (e.g., 8 for 8x8). Array used for lambda mutability. */
    private final int[] editorSize = {8};
    /** Currently selected piece in the palette (0 = eraser). Array used for lambda mutability. */
    private final char[] selPiece = {'Q'};

    /** Grid pane for the custom board editor. */
    private final GridPane editorGrid = new GridPane();
    /** Label for displaying errors in the custom board editor. */
    private final Label editorError = new Label();
    
    /** Flag to determine if the game should load the custom FEN file. */
    private boolean isCustomModeActive = false; 
    
    /** Dynamic content area to switch between the sub-menu and setup forms. */
    private final StackPane contentArea;
    
    // ── Root ──────────────────────────────────────────────────────────────────
    /** The main container for the setup view. */
    private final VBox root;

    // ── Colours ───────────────────────────────────────────────────────────────
    /** Main background color. */
    private static final String BG_DARK  = "#2c3e50";
    /** Card and panel background color. */
    private static final String BG_CARD  = "#34495e";
    /** Muted text and accents color. */
    private static final String MUTED    = "#bdc3c7";
    /** Green color for action buttons. */
    private static final String GREEN    = "#27ae60";
    /** Grey color for secondary buttons. */
    private static final String GREY     = "#7f8c8d";
    /** Blue color for palette selection highlight. */
    private static final String BLUE     = "#2980b9";
    /** Red color for errors and eraser selection. */
    private static final String RED      = "#e74c3c";
    /** Button background color for disabled or dimmed states. */
    private static final String BTN_DIM  = "#455a64";
    
    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Constructor for the SetupView, initializes the UI components and layout for the game configuration screen.
     * 
     * @param manager The ViewManager used to handle scene transitions and game launching. 
     * It is passed to button actions for navigation and starting the game with the configured settings.
     */
    public SetupView(ViewManager manager) {

        // Init board state
        boardState = new char[editorSize[0]][editorSize[0]];

        root = new VBox(0);
        root.setStyle("-fx-background-color: " + BG_DARK + ";");
        root.setPrefSize(ViewManager.W, ViewManager.H);

        // Header
        buildHeader(manager);

        // Central zone
        contentArea = new StackPane();
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        // Display the submenu on startup
        showSubMenu(manager);

        root.getChildren().add(contentArea);
    }


    // ── Header bar Builder ──────────────────────────────────────────────────

    /**
     * Helper method to build the header section of the setup view, which includes the title and a back button to return to the main menu.
     * 
     * @param manager The ViewManager used to handle the back button action for navigating to the main menu.
     */
    private void buildHeader(ViewManager manager) {
        Label title = new Label("CONFIGURATION DE PARTIE");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);

        Button backBtn = actionBtn("RETOUR", GREY);
        backBtn.setMinWidth(120);
        backBtn.setOnAction(e -> manager.showMainMenu());

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setStyle("-fx-background-color: #1a252f;");

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        header.getChildren().addAll(backBtn, leftSpacer, title, rightSpacer);
        
        root.getChildren().add(header);
    }

    // Central Menu

    /**
     * Helper method to create a styled action button with consistent appearance for primary actions in the setup view 
     * (e.g., "Start Game", "Validate Board").
     * 
     * @param manager The ViewManager used to handle the button's action when clicked, allowing for navigation or game launching.
     */
    private void showSubMenu(ViewManager manager) {
        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        
        Label subTitle = new Label("CHOISISSEZ VOTRE MODE");
        subTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        subTitle.setTextFill(Color.web(MUTED));

        Button btnClassic = createMenuButton("ÉCHECS CLASSIQUES");
        Button btnCustom  = createMenuButton("ÉDITEUR DE PLATEAU");

        btnClassic.setOnAction(e -> {
            this.isCustomModeActive = false;
            contentArea.getChildren().setAll(buildClassicContent(manager));
        });

        btnCustom.setOnAction(e -> {
            contentArea.getChildren().setAll(buildCustomContent(manager));
        });
        menu.getChildren().addAll(subTitle, btnClassic, btnCustom);
        contentArea.getChildren().setAll(menu);
    }

    /**
     * Helper method to create a styled button with consistent appearance for menu options in the setup view (e.g., "Classic Chess", "Board Editor").
     * 
     * @param text The text to display on the button.
     * 
     * @return The created button. The caller is responsible for setting the button's action handler.
     */
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMinWidth(320);
        btn.setMinHeight(60);
        btn.setStyle("-fx-background-color: " + BG_CARD + "; -fx-text-fill: white; " +
                    "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand;");
        return btn;
    }

    // ── Tab 1 : Classic 2-player setup ───────────────────────────────────────

    /**
     * Helper method to build the content for the classic chess setup, which includes player name inputs, time control options, and a start button.
     * 
     * @param manager The ViewManager used to handle the start button's action when clicked.
     * 
     * @return The built content node for the classic chess setup.
     */
    private Node buildClassicContent(ViewManager manager) {
        VBox pane = new VBox(18);
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setPadding(new Insets(28, 60, 28, 60));
        pane.setStyle("-fx-background-color: " + BG_DARK + ";");

        // Player cards
        TextField whiteNameField = styledTextField("Joueur 1");
        TextField blackNameField = styledTextField("Joueur 2");

        HBox playersRow = new HBox(24,
            playerCard("♔  Blancs", "#f0d9b5", whiteNameField),
            playerCard("♚  Noirs",  "#333333", blackNameField)
        );
        playersRow.setAlignment(Pos.CENTER);

        // Time section
        Label timeTitle = sectionTitle("CONTRÔLE DU TEMPS");
        ToggleGroup timeGroup = new ToggleGroup();
        String[] presets = {"INFINI", "3 min", "5 min", "10 min", "15 min", "30 min", "Autre…"};

        HBox presetsBox = new HBox(8);
        presetsBox.setAlignment(Pos.CENTER);
        ToggleButton customTimeBtn = null;

        for (String label : presets) {
            ToggleButton tb = toggleBtn(label, timeGroup);
            if (label.equals("10 min")) tb.setSelected(true);
            if (label.equals("Autre…")) customTimeBtn = tb;
            presetsBox.getChildren().add(tb);
        }

        TextField wCustom = styledNumberField("10");
        TextField bCustom = styledNumberField("10");

        HBox customTimeRow = new HBox(10,
            inlineLabel("Blancs :"), wCustom,
            inlineLabel("min   Noirs :"), bCustom, inlineLabel("min")
        );
        customTimeRow.setAlignment(Pos.CENTER);
        customTimeRow.setVisible(false);
        customTimeRow.setManaged(false);

        if (customTimeBtn != null) {
            customTimeBtn.selectedProperty().addListener((o, ov, v) -> {
                customTimeRow.setVisible(v);
                customTimeRow.setManaged(v);
            });
        }

        // ── Error label ───────────────────────────────────────────────────────
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.web(RED));
        errorLabel.setFont(Font.font(13));

        ToggleGroup finalTimeGroup = timeGroup;

        Button startBtn = actionBtn("LANCER LA PARTIE", GREEN);
        startBtn.setMinWidth(240);
        
        startBtn.setOnAction(e -> {
            errorLabel.setText("");
            try {
                int wTime = 0, bTime = 0;
                Toggle sel = finalTimeGroup.getSelectedToggle();

                if (sel != null) {
                    String txt = ((ToggleButton) sel).getText().trim();
                    switch (txt) {
                        case "Autre…" -> {
                            wTime = Integer.parseInt(wCustom.getText().trim());
                            bTime = Integer.parseInt(bCustom.getText().trim());
                            if (wTime <= 0 || bTime <= 0) throw new NumberFormatException("must be > 0");
                        }
                        case "INFINI" -> {
                            wTime = 0;
                            bTime = 0;
                        }
                        default -> {
                            int val = Integer.parseInt(txt.replace(" min", "").trim());
                            wTime = bTime = val;
                        }
                    }
                }
                String wName = orDefault(whiteNameField.getText(), "Joueur 1");
                String bName = orDefault(blackNameField.getText(), "Joueur 2");

                // Standard starting position FEN
                String fenToUse = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

                // If coming from the board editor, attempt to load the saved custom FEN
                if (this.isCustomModeActive) {
                    try {
                        java.nio.file.Path path = java.nio.file.Paths.get("saves/custom_layout.txt");
                        if (java.nio.file.Files.exists(path)) {
                            fenToUse = java.nio.file.Files.readString(path).trim();
                        }
                    } catch (java.io.IOException ex) {
                        System.err.println("Impossible de lire le plateau personnalisé, utilisation du défaut.");
                    }
                    // Reset flag for future classic games
                    this.isCustomModeActive = false; 
                }

                manager.launchGame(wTime, bTime, wName, bName, fenToUse);

            } catch (NumberFormatException ex) {
                errorLabel.setText("Temps invalide. Entrez un nombre entier de minutes.");
            }
        });

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + BTN_DIM + ";");

        pane.getChildren().addAll(
            playersRow, sep, timeTitle, presetsBox,
            customTimeRow, errorLabel, startBtn
        );
        return pane;
    }

    // ── Tab 2 : Custom board editor ───────────────────────────────────────────

    /**
     * Helper method to create a styled TextField specifically for numeric input, used in the custom time configuration and board size input.
     * 
     * @param manager The ViewManager used to handle the start button's action when clicked.
     * 
     * @return A TextField instance with predefined styling and a set preferred width, intended for numeric input in the setup view.
     */
    private Node buildCustomContent(ViewManager manager) {
        VBox pane = new VBox(14);
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setPadding(new Insets(20, 40, 20, 40));
        pane.setStyle("-fx-background-color: " + BG_CARD + ";");

        // ── Board size input ──────────────────────────────────────────────────
        Label sizeLabel = inlineLabel("Taille du plateau :");
        TextField sizeField = styledNumberField(String.valueOf(editorSize[0]));
        sizeField.setPrefWidth(60);
        Button sizeBtn = actionBtn("Redimensionner", BTN_DIM);
        sizeBtn.setMinWidth(160);
        sizeBtn.setMinHeight(34);

        HBox sizeRow = new HBox(10, sizeLabel, sizeField, sizeBtn);
        sizeRow.setAlignment(Pos.CENTER);

        Label sizeHint = new Label("(min 3 · max 16 colonnes)");
        sizeHint.setTextFill(Color.web(MUTED));
        sizeHint.setFont(Font.font(11));

        sizeBtn.setOnAction(e -> {
            editorError.setText("");
            try {
                int s = Integer.parseInt(sizeField.getText().trim());
                if (s < 3 || s > 16) throw new NumberFormatException();
                editorSize[0] = s;
                boardState = new char[s][s];
                rebuildEditorGrid();
            } catch (NumberFormatException ex) {
                editorError.setText("Taille invalide. Entrez un entier entre 3 et 16.");
            }
        });

        // ── Piece palette ─────────────────────────────────────────────────────
        Label palLabel = sectionTitle("PALETTE DE PIÈCES");

        ToggleGroup paletteGroup = new ToggleGroup();

        // White pieces
        Object[][] whites = {
            {'K', "♔", "Roi"}, {'Q', "♕", "Dame"}, {'R', "♖", "Tour"},
            {'B', "♗", "Fou"}, {'N', "♘", "Cav."}, {'P', "♙", "Pion"}
        };
        // Black pieces
        Object[][] blacks = {
            {'k', "♚", "Roi"}, {'q', "♛", "Dame"}, {'r', "♜", "Tour"},
            {'b', "♝", "Fou"}, {'n', "♞", "Cav."}, {'p', "♟", "Pion"}
        };

        HBox whiteRow = new HBox(6);
        whiteRow.setAlignment(Pos.CENTER);
        whiteRow.getChildren().add(inlineLabel("Blancs :"));
        for (Object[] p : whites) {
            ToggleButton tb = pieceToggle((char) p[0], (String) p[1], (String) p[2],paletteGroup, "#a2a2a2");
            if ((char) p[0] == 'K') tb.setSelected(true);
            whiteRow.getChildren().add(tb);
        }

        HBox blackRow = new HBox(6);
        blackRow.setAlignment(Pos.CENTER);
        blackRow.getChildren().add(inlineLabel("Noirs  :"));
        for (Object[] p : blacks) {
            ToggleButton tb = pieceToggle((char) p[0], (String) p[1], (String) p[2], paletteGroup, "#333333");
            blackRow.getChildren().add(tb);
        }

        // Eraser button (char 0)
        ToggleButton eraserBtn = new ToggleButton("Gomme");
        eraserBtn.setToggleGroup(paletteGroup);
        String eBase = paletteToggleStyle(GREY, true);
        String eSel  = paletteToggleStyle(RED,  true);
        eraserBtn.setStyle(eBase);
        eraserBtn.selectedProperty().addListener((o, ov, v) -> {
            eraserBtn.setStyle(v ? eSel : eBase);
            if (v) selPiece[0] = 0;
        });

        // Wire palette group -> selPiece (for pieces; eraser handled above)
        paletteGroup.selectedToggleProperty().addListener((o, ov, nv) -> {
            if (nv != null) {
                Object ud = ((ToggleButton) nv).getUserData();
                if (ud != null && ud.getClass() == Character.class) {
                    selPiece[0] = (Character) ud;
                } else {
                    selPiece[0] = 0;
                }
            }
        });
        // Set initial
        selPiece[0] = 'Q';

        VBox palette = new VBox(6, whiteRow, blackRow, eraserBtn);
        palette.setAlignment(Pos.CENTER);

        // ── Board grid ────────────────────────────────────────────────────────
        editorGrid.setAlignment(Pos.CENTER);
        rebuildEditorGrid();

        // ── Error + validation ────────────────────────────────────────────────
        editorError.setTextFill(Color.web(RED));
        editorError.setFont(Font.font(13));

        Label rulesHint = new Label(
            "Le plateau doit contenir exactement 1 roi blanc (♔) et 1 roi noir (♚) pour être lancé."
        );
        rulesHint.setTextFill(Color.web(MUTED));
        rulesHint.setFont(Font.font(11));
        rulesHint.setWrapText(true);

        // ── Action buttons ────────────────────────────────────────────────────
        Button clearBtn = actionBtn("Effacer tout",    GREY);
        Button startBtn = actionBtn("VALIDER LE PLATEAU", GREEN);

        clearBtn.setOnAction(e -> {
            boardState = new char[editorSize[0]][editorSize[0]];
            editorError.setText("");
            rebuildEditorGrid();
        });

        startBtn.setOnAction(e -> {
            String err = validateBoard();
            if (err != null) { editorError.setText(err); return; }

            String fen = buildFen();
            try {
                // Save the custom board layout to a dedicated file
                java.nio.file.Path path = java.nio.file.Paths.get("saves/custom_layout.txt");
                java.nio.file.Files.createDirectories(path.getParent());
                java.nio.file.Files.writeString(path, fen);

                // Activate the custom mode flag for the next step
                this.isCustomModeActive = true;

                // Switch to the classic setup view to configure timers and names
                contentArea.getChildren().setAll(buildClassicContent(manager));
            } catch (java.io.IOException ex) {
                editorError.setText("Erreur lors de la sauvegarde du fichier.");
            }
        });

        HBox actionRow = new HBox(20, clearBtn, startBtn);
        actionRow.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(
            sizeRow, sizeHint,
            palLabel, palette,
            editorGrid,
            editorError, rulesHint, actionRow
        );

        ScrollPane scroll = new ScrollPane(pane);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + BG_DARK + "; -fx-background: " + BG_DARK + ";");
        return scroll;
    }

    // ── Board editor logic ────────────────────────────────────────────────────

    /**
     * Rebuilds the editor grid based on the current board state.
     */
    private void rebuildEditorGrid() {
        editorGrid.getChildren().clear();
        int size = editorSize[0];
        int tile = Math.max(22, Math.min(52, 416 / size));

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                boolean light = (row + col) % 2 == 0;
                Rectangle bg  = new Rectangle(tile, tile);
                bg.setFill(light ? Color.web("#f0d9b5") : Color.web("#b58863"));

                char pieceChar = boardState[row][col];
                Label pieceLbl = new Label(fenToSymbol(pieceChar));
                pieceLbl.setStyle(
                    "-fx-font-size: " + (int)(tile * 0.75) + "px;" +
                    "-fx-text-fill: " + (Character.isUpperCase(pieceChar) ? "white" : "black") + ";" // Blanc si majuscule (FEN), sinon noir
                );

                StackPane cell = new StackPane(bg, pieceLbl);
                cell.setPrefSize(tile, tile);
                cell.setStyle("-fx-cursor: hand;");

                final int r = row, c = col;
                cell.setOnMouseClicked(ev -> onCellClick(r, c));

                editorGrid.add(cell, col, row);
            }
        }
    }

    /**
     * Handles clicks on the editor grid cells to place or remove pieces based on the currently selected piece in the palette.
     * 
     * @param row The row index of the clicked cell in the editor grid.
     * @param col The column index of the clicked cell in the editor grid.
     */
    private void onCellClick(int row, int col) {
        editorError.setText("");
        char piece = selPiece[0];

        switch (piece) {
            case 0 -> {
                // Eraser selected: clear the cell
                boardState[row][col] = 0;
            }
            case 'K', 'k' -> {
                // King selected: ensure only one per color
                int size = editorSize[0];
                for (int r = 0; r < size; r++) {
                    for (int c = 0; c < size; c++) {
                        if (r == row && c == col) continue;
                        if (boardState[r][c] == piece) {
                            editorError.setText("Un seul roi " +
                                (piece == 'K' ? "blanc ♔" : "noir ♚") + " est autorisé !");
                            return;
                        }
                    }
                }
                boardState[row][col] = piece;
            }
            default -> {
                // Other piece selected: place it directly (multiple allowed)
                boardState[row][col] = piece;
            }
        }
        
        rebuildEditorGrid();
    }

    // ── FEN helpers ───────────────────────────────────────────────────────────

    /**
     * Builds the FEN string representing the current board state.
     * 
     * @return The FEN string.
     */
    private String buildFen() {
        int size = editorSize[0];
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < size; y++) {
            int empty = 0;
            for (int x = 0; x < size; x++) {
                char c = boardState[y][x];
                if (c == 0) {
                    empty++;
                } else {
                    // Flush empty run (FEN only allows single digits)
                    while (empty > 9) { sb.append('9'); empty -= 9; }
                    if (empty > 0)    { sb.append(empty); empty = 0; }
                    sb.append(c);
                }
            }
            while (empty > 9) { sb.append('9'); empty -= 9; }
            if (empty > 0) sb.append(empty);
            if (y < size - 1) sb.append('/');
        }
        sb.append(" w - - 0 1");
        return sb.toString();
    }

    /**
     * Validates the current board state to ensure it meets the requirements for launching a game (exactly one white king and one black king).
     * 
     * @return An error message if the board is invalid, or null if it is valid.
     */
    private String validateBoard() {
        int size = editorSize[0], wK = 0, bK = 0;
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++) {
                if (boardState[y][x] == 'K') wK++;
                if (boardState[y][x] == 'k') bK++;
            }
        if (wK == 0) return "Il manque le roi blanc (♔). Placez-en un.";
        if (bK == 0) return "Il manque le roi noir (♚). Placez-en un.";
        if (wK > 1)  return "Trop de rois blancs (maximum 1 ♔).";
        if (bK > 1)  return "Trop de rois noirs (maximum 1 ♚).";
        return null;
    }

    // ── UI helpers ────────────────────────────────────────────────────────────

    /**
     * Helper method to create a ToggleButton for a piece in the palette, with appropriate styling and behavior when selected.
     * 
     * @param fenChar The FEN character representing the piece (e.g., 'K' for white king, 'p' for black pawn). 
     * This is used as user data for selection handling.
     * @param symbol The Unicode symbol to display on the button (e.g., "♔" for white king).
     * @param name The name of the piece to display below the symbol (e.g., "Roi" for king).
     * @param group The ToggleGroup to which this button belongs, ensuring only one piece can be selected at a time.
     * @param bgColor The background color for the button, typically a shade of grey for white pieces and a darker shade for black pieces, 
     * to visually differentiate them in the palette.
     * 
     * @return The created ToggleButton for the piece, with event handling to update the selected piece in the palette when clicked.
     */
    private ToggleButton pieceToggle(char fenChar, String symbol, String name, ToggleGroup group, String bgColor) {
        ToggleButton tb = new ToggleButton(symbol + "\n" + name);
        tb.setUserData(fenChar);
        tb.setToggleGroup(group);
        tb.setMinWidth(62);
        tb.setMinHeight(52);
        tb.setContentDisplay(ContentDisplay.TOP);
        String base = paletteToggleStyle(bgColor, false);
        String sel  = paletteToggleStyle(BLUE,    false);
        tb.setStyle(base);
        tb.selectedProperty().addListener((o, ov, v) -> {
            tb.setStyle(v ? sel : base);
            if (v) selPiece[0] = fenChar;
        });
        return tb;
    }

    /**
     * Helper method to generate the CSS style string for palette toggle buttons, allowing for consistent 
     * styling and easy updates to the appearance of these buttons.
     * 
     * @param bg The background color for the button.
     * @param wide Whether the button should have a wide width.
     * 
     * @return The CSS style string for the button.
     */
    private String paletteToggleStyle(String bg, boolean wide) {
        return "-fx-background-color: " + bg + "; -fx-text-fill: white; " +
               "-fx-background-radius: 6; -fx-cursor: hand; -fx-font-size: 13px;" +
               (wide ? " -fx-min-width: 100;" : "");
    }

    /**
     * Helper method to create a styled player card for the classic setup, which includes a colored circle, 
     * a header, and a text field for the player's name.
     * 
     * @param headerText The text to display in the header of the player card (e.g., "♔  Blancs" or "♚  Noirs").
     * @param circleColor The color to use for the circle next to the header, typically a light color for white 
     * pieces and a dark color for black pieces,
     * @param nameField The text field for the player's name.
     * 
     * @return The created player card.
     */
    private VBox playerCard(String headerText, String circleColor, TextField nameField) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16, 22, 16, 22));
        card.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 10;" +
                      "-fx-min-width: 210; -fx-max-width: 240;");

        Circle circle = new Circle(13);
        circle.setFill(Color.web(circleColor));
        circle.setStroke(Color.web(MUTED));
        circle.setStrokeWidth(1.5);

        Label header = new Label(headerText);
        header.setTextFill(Color.WHITE);
        header.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        HBox hdr = new HBox(10, circle, header);
        hdr.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label("Nom du joueur :");
        nameLabel.setTextFill(Color.web(MUTED));
        nameLabel.setFont(Font.font(12));

        card.getChildren().addAll(hdr, nameLabel, nameField);
        return card;
    }

    /**
     * Helper method to create a styled ToggleButton for the time control presets, with appropriate styling and behavior when selected.
     * 
     * @param text The text to display on the button (e.g., "3 min", "5 min", "Autre…").
     * @param group The ToggleGroup to which this button belongs, ensuring only one time control option can be selected at a time.
     * 
     * @return The created ToggleButton.
     */
    private ToggleButton toggleBtn(String text, ToggleGroup group) {
        ToggleButton tb = new ToggleButton(text);
        tb.setToggleGroup(group);
        tb.setMinWidth(70);
        tb.setMinHeight(34);
        String base = "-fx-background-color: " + BTN_DIM + "; -fx-text-fill: white;" +
                      "-fx-background-radius: 6; -fx-cursor: hand; -fx-font-size: 13px;";
        String sel  = "-fx-background-color: " + BLUE   + "; -fx-text-fill: white;" +
                      "-fx-background-radius: 6; -fx-cursor: hand; -fx-font-size: 13px;";
        tb.setStyle(base);
        tb.selectedProperty().addListener((o, ov, v) -> tb.setStyle(v ? sel : base));
        return tb;
    }

    /**
     * Helper method to create a styled action button with consistent appearance for primary actions in the setup view 
     * (e.g., "Start Game", "Validate Board").
     * 
     * @param text The text to display on the button.
     * @param bg The background color for the button.
     * 
     * @return The created action button. The caller is responsible for setting the button's action handler.
     */
    private Button actionBtn(String text, String bg) {
        Button btn = new Button(text);
        btn.setMinHeight(42);
        btn.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: white; " +
                   "-fx-font-size: 14px; -fx-font-weight: bold; " +
                   "-fx-background-radius: 8; -fx-cursor: hand;");
        return btn;
    }

    /**
     * Helper method to create a styled section title label for different sections of the setup view, such as "Time Control" or "Piece Palette".
     * 
     * @param text The text to display in the section title.
     * 
     * @return The created section title label.
     */
    private Label sectionTitle(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web(MUTED));
        l.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        return l;
    }

    /**
     * Helper method to create a styled inline label with white text, used for labeling inputs in the setup view (e.g., "Blancs:", "Noirs:").
     * 
     * @param text The text to display in the label.
     * 
     * @return The created inline label.
     */
    private Label inlineLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }

    /**
     * Helper method to create a styled TextField with consistent appearance for player name inputs and custom time inputs in the setup view.
     * 
     * @param def The default text to display in the TextField.
     * 
     * @return The created styled TextField.
     */
    private TextField styledTextField(String def) {
        TextField tf = new TextField(def);
        tf.setMaxWidth(190);
        tf.setStyle("-fx-background-color: " + BTN_DIM + "; -fx-text-fill: white;" +
                    "-fx-border-color: transparent; -fx-background-radius: 6; -fx-padding: 7 10;");
        return tf;
    }

    /**
     * Helper method to create a styled TextField specifically for numeric input, used in the custom time configuration and board size input.
     * 
     * @param defaultValue The default text to display in the TextField when it is created. 
     * This should typically be a valid number (e.g., "10" for 10 minutes or "8" for an 8x8 board) to guide the user on the expected input format.
     * 
     * @return A TextField instance with predefined styling and a set preferred width, intended for numeric input in the setup view. 
     * The caller is responsible for adding any necessary input validation to ensure only numbers are entered.
     */
    private TextField styledNumberField(String defaultValue) {
        TextField tf = styledTextField(defaultValue);
        tf.setPrefWidth(70);
        return tf;
    }

    /**
     * Helper method to convert a FEN character to its corresponding Unicode chess symbol for display in the board editor.
     * The method takes a single character as input, which represents a chess piece in FEN notation (e.g., 'K' for white king,
     * 'p' for black pawn), and returns the corresponding Unicode symbol that can be displayed in the JavaFX UI.
     * 
     * @param c The FEN character representing a chess piece (e.g., 'K' for white king, 'p' for black pawn).
     * 
     * @return A string containing the Unicode symbol for the piece, or an empty string if the character is not recognized.
     */
    private static String fenToSymbol(char c) {
        return switch (Character.toLowerCase(c)) {
            case 'k' -> "♚";
            case 'q' -> "♛";
            case 'r' -> "♜";
            case 'b' -> "♝";
            case 'n' -> "♞";
            case 'p' -> "♟";
            default  -> "";
        };
    }

    /**
     * Utility method to return a default value if the input string is null or empty after trimming.
     * 
     * @param s The input string to check.
     * @param def The default value to return if the input is null or empty.
     * 
     * @return The trimmed input string if it is not null/empty, otherwise the provided default value.
     */
    private static String orDefault(String s, String def) {
        String t = (s == null) ? "" : s.trim();
        return t.isEmpty() ? def : t;
    }

    // ── Public getter ─────────────────────────────────────────────────────────

    /**
     * Public getter for the root node of the setup view, which can be used by the ViewManager to display this scene.
     * 
     * @return The root VBox containing all UI components of the setup view.
     */
    public VBox getRoot() { return root; }
}