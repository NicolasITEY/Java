package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Standalone puzzle view for the N-Queens problem.
 * The user places queens by clicking on squares; conflicts are highlighted in red.
 * The "Résoudre automatiquement" (Solve) button runs a backtracking algorithm to calculate 
 * and display all valid solutions sequentially.
 */
public class NQueensView extends StackPane {

    // ── State ──────────────────────────────────────────────────────────────────
    /** The size of the board (N x N). */
    private int n = 8;
    /** A 2D boolean array indicating where queens are currently placed on the board. */
    private boolean[][] placed;           // placed[row][col]

    /** A cached list of all valid solutions for the current N, where each solution is an array of column indices for the queens. */
    private java.util.List<int[]> allSolutions = null;
    /** The index of the currently displayed solution in the 'allSolutions' list. */
    private int currentSolutionIndex = 0;

    // ── UI nodes refreshed on each rebuild ────────────────────────────────────
    /** The grid pane representing the chess board. */
    private final GridPane boardGrid    = new GridPane();
    /** The label displaying the status message. */
    private final Label    statusLabel  = new Label();
    /** The label displaying the result message. */
    private final Label    resultLabel  = new Label();

    // ── Colours ───────────────────────────────────────────────────────────────
    /** The colour for the background. */
    private static final String BG      = "#2c3e50";
    /** The colour for muted elements. */
    private static final String MUTED   = "#bdc3c7";
    /** The colour for green elements. */
    private static final String GREEN   = "#2ecc71";
    /** The colour for red elements. */
    private static final String RED     = "#e74c3c";
    /** The colour for blue elements. */
    private static final String BLUE    = "#2980b9";
    /** The colour for grey elements. */
    private static final String GREY    = "#7f8c8d";
    /** The colour for dimmed buttons. */
    private static final String BTN_DIM = "#455a64";

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Constructor for the NQueensView. Initializes the UI components and sets up event handlers for user interaction.
     * 
     * @param manager The ViewManager used to handle scene transitions, allowing navigation back to the main menu.
     */
    public NQueensView(ViewManager manager) {
        this.setStyle("-fx-background-color: " + BG + ";");
        placed = new boolean[n][n];

        VBox root = new VBox(14);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(28, 50, 20, 50));
        root.setPrefSize(ViewManager.W, ViewManager.H);
        root.setStyle("-fx-background-color: " + BG + ";");

        // ── Title ──────────────────────────────────────────────────────────────
        Label title = new Label("Problème des N-Dames");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        title.setTextFill(Color.WHITE);

        Label desc = new Label(
            "Placez exactement N dames sur un échiquier NxN\n de sorte qu'aucune dame n'en menace une autre."
        );
        desc.setTextFill(Color.web(MUTED));
        desc.setFont(Font.font("Verdana", 13));
        desc.setTextAlignment(TextAlignment.CENTER);

        // ── Size selector ─────────────────────────────────────────────────────
        Label sizeHint = new Label("Taille :");
        sizeHint.setTextFill(Color.WHITE);
        sizeHint.setFont(Font.font(13));

        ToggleGroup sizeGroup = new ToggleGroup();
        HBox sizeRow = new HBox(8, sizeHint);
        sizeRow.setAlignment(Pos.CENTER);

        for (int s : new int[]{4, 5, 6, 7, 8, 10, 12, 14}) {
            ToggleButton tb = buildSizeButton(s, sizeGroup);
            if (s == 8) tb.setSelected(true);
            sizeRow.getChildren().add(tb);
        }

        // ── Board ─────────────────────────────────────────────────────────────
        boardGrid.setAlignment(Pos.CENTER);
        rebuildBoard();

        // ── Status area ───────────────────────────────────────────────────────
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + MUTED + ";");
        resultLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + MUTED + ";");
        updateStatus();

        VBox statusBox = new VBox(4, statusLabel, resultLabel);
        statusBox.setAlignment(Pos.CENTER);

        // ── Action buttons ────────────────────────────────────────────────────
        Button backBtn  = actionBtn("Menu principal", BTN_DIM);
        Button clearBtn = actionBtn("Effacer tout", GREY);
        Button solveBtn = actionBtn("Résoudre automatiquement", BLUE);

        backBtn.setOnAction(e  -> manager.showMainMenu());
        clearBtn.setOnAction(e -> { 
            placed = new boolean[n][n]; 
            allSolutions = null;
            rebuildBoard(); 
            updateStatus(); 
        });
        solveBtn.setOnAction(e -> solveAndShow());

        HBox btnRow = new HBox(14, backBtn, clearBtn, solveBtn);
        btnRow.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, desc, sizeRow, boardGrid, statusBox, btnRow);
        this.getChildren().add(root);
    }

    // ── Board ─────────────────────────────────────────────────────────────────

    /**
     * Helper method to build a toggle button for selecting the board size (N) in the N-Queens puzzle.
     * When a button is selected, it updates the board size, resets the placed queens, and rebuilds the board UI.
     * 
     * @param size the size of the board (N)
     * @param group the toggle group to which the button belongs
     * 
     * @return the created toggle button
     */
    private ToggleButton buildSizeButton(int size, ToggleGroup group) {
        ToggleButton tb = new ToggleButton(size + "x" + size);
        tb.setToggleGroup(group);
        String base = toggleStyle(BTN_DIM);
        String sel  = toggleStyle(BLUE);
        tb.setStyle(base);
        tb.selectedProperty().addListener((o, ov, v) -> tb.setStyle(v ? sel : base));
        tb.setOnAction(e -> {
            n = size;
            placed = new boolean[n][n];
            allSolutions = null;
            rebuildBoard();
            updateStatus();
        });

        return tb;
    }

    /**
     * Rebuilds the board UI based on the current 'placed' state.
     */
    private void rebuildBoard() {
        boardGrid.getChildren().clear();
        // Keep tiles between 22 px and 54 px so any board fits in ~440 px
        int tile = Math.max(22, Math.min(54, 440 / n));

        // Create the grid of cells
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                boolean light    = (row + col) % 2 == 0;
                boolean hasQueen = placed[row][col];
                boolean conflict = hasQueen && hasConflict(row, col);

                Rectangle bg = new Rectangle(tile, tile);
                bg.setFill(light ? Color.web("#f0d9b5") : Color.web("#b58863"));
                if (conflict) bg.setFill(Color.web("#7f1d1d"));   // dark red bg for conflict

                Label queenLbl = new Label(hasQueen ? "♛" : "");
                queenLbl.setStyle(
                    "-fx-font-size: " + (int)(tile * 0.65) + "px;" +
                    "-fx-text-fill: " + (conflict ? RED : (light ? "#1a1a1a" : "#f5f5f5")) + ";"
                );

                StackPane cell = new StackPane(bg, queenLbl);
                cell.setPrefSize(tile, tile);
                cell.setStyle("-fx-cursor: hand;");

                final int r = row, c = col;
                cell.setOnMouseClicked(e -> {
                    placed[r][c] = !placed[r][c];
                    allSolutions = null; 
                    rebuildBoard();
                    updateStatus();
                });

                boardGrid.add(cell, col, row);
            }
        }
    }

    // ── Logic ─────────────────────────────────────────────────────────────────

    /**
     * Checks if placing a queen at the given row and column would cause a conflict with any already placed queens.
     * A conflict occurs if another queen is in the same row, column, or diagonal.
     * 
     * @param row the row index of the position to check
     * @param col the column index of the position to check
     * 
     * @return true if there is a conflict, false otherwise
     */
    private boolean hasConflict(int row, int col) {
        for (int r2 = 0; r2 < n; r2++) {
            for (int c2 = 0; c2 < n; c2++) {
                if (r2 == row && c2 == col) continue;
                if (!placed[r2][c2]) continue;
                if (r2 == row || c2 == col) return true;
                if (Math.abs(r2 - row) == Math.abs(c2 - col)) return true;
            }
        }
        return false;
    }

    // ── Status updates ─────────────────────────────────────────────────────────

    /**
     * Updates the status and result labels based on the current board state, counting placed queens and conflicts.
     */
    private void updateStatus() {
        int total = 0, conflicts = 0;
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                if (placed[r][c]) { total++; if (hasConflict(r, c)) conflicts++; }

        statusLabel.setText("Dames placées : " + total + " / " + n);

        if (total == 0) {
            resultLabel.setText("Cliquez sur les cases pour poser ou retirer une dame.");
            resultLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + MUTED + ";");
        } else if (conflicts > 0) {
            resultLabel.setText(conflicts + " dame(s) en conflit (cases rouge) !");
            resultLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + RED + ";");
        } else if (total == n) {
            resultLabel.setText("Solution valide — Bravo !");
            resultLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + GREEN + ";");
        } else {
            resultLabel.setText("Pas de conflit pour l'instant. Continuez !");
            resultLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + GREEN + ";");
        }
    }

    // ── Solver (backtracking) ─────────────────────────────────────────────────

    /**
     * Solves the N-Queens puzzle using a backtracking algorithm.
     * Calculates all possible solutions on the first run, then iterates through them 
     * on subsequent clicks to display different valid configurations.
     */
    private void solveAndShow() {
        // If we haven't calculated solutions for the current N yet, do it now
        if (allSolutions == null) {
            allSolutions = new java.util.ArrayList<>();
            int[] sol = new int[n];
            java.util.Arrays.fill(sol, -1);
            findAllSolutions(sol, 0);
            currentSolutionIndex = 0; // reset index to show the first solution
        }

        // If solutions exist, display the current one and prepare the next index
        if (!allSolutions.isEmpty()) {
            int[] currentSol = allSolutions.get(currentSolutionIndex);
            placed = new boolean[n][n];
            for (int row = 0; row < n; row++) {
                placed[row][currentSol[row]] = true;
            }
            
            rebuildBoard();

            // Update status and result labels
            statusLabel.setText("Dames placées : " + n + " / " + n);
            resultLabel.setText("Solution " + (currentSolutionIndex + 1) + " sur " + allSolutions.size());
            resultLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + BLUE + ";");
            
            // Prepare index for the next solution (wrap around to the first after the last)
            currentSolutionIndex = (currentSolutionIndex + 1) % allSolutions.size();

        // If no solutions exist for the current N, show a message and clear the board
        } else {
            resultLabel.setText("Aucune solution pour N=" + n);
            resultLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + RED + ";");
            rebuildBoard();
            updateStatus();
        }
    }

    /**
     * Recursive backtracking algorithm to find all valid queen placements.
     * Stores every complete valid board state in the 'allSolutions' list.
     * 
     * @param sol The current state of placed queens (index = row, value = col).
     * @param row The current row being evaluated.
     */
    private void findAllSolutions(int[] sol, int row) {
        if (row == n) {
            // When we reach a complete solution, we add a copy of it to the list
            allSolutions.add(sol.clone()); 
            return;
        }
        for (int col = 0; col < n; col++) {
            if (isSafe(sol, row, col)) {
                sol[row] = col;
                findAllSolutions(sol, row + 1); // Recurse to place the next queen
                sol[row] = -1;
            }
        }
    }

    /**
     * Checks if placing a queen at the given row and column would cause a conflict with any already placed queens.
     * A conflict occurs if another queen is in the same row, column, or diagonal.
     *
     * @param sol the current state of placed queens
     * @param row the row index of the position to check
     * @param col the column index of the position to check
     * 
     * @return true if there is a conflict, false otherwise
     */
    private boolean isSafe(int[] sol, int row, int col) {
        for (int r = 0; r < row; r++) {
            if (sol[r] == col) return false;
            if (Math.abs(sol[r] - col) == Math.abs(r - row)) return false;
        }
        return true;
    }

    // ── Style helpers ─────────────────────────────────────────────────────────

    /**
     * Helper method to generate CSS style strings for toggle buttons based on their background color.
     * This is used to create consistent styling for the size selection buttons, with dynamic colors for selected and unselected states.
     * 
     * @param bg The background color to use in the style string.
     * 
     * @return A CSS style string for a toggle button with the specified background color.
     */
    private String toggleStyle(String bg) {
        return "-fx-background-color: " + bg + "; -fx-text-fill: white; " +
               "-fx-background-radius: 6; -fx-cursor: hand; -fx-font-size: 13px; " +
               "-fx-min-width: 66; -fx-min-height: 34;";
    }

    /**
     * Helper method to create action buttons with consistent styling.
     * 
     * @param text The label text to display on the button.
     * @param bg The background color to use in the style string.
     * 
     * @return A styled JavaFX Button instance.
     */
    private Button actionBtn(String text, String bg) {
        Button b = new Button(text);
        b.setMinWidth(180);
        b.setMinHeight(40);
        b.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: white; " +
                   "-fx-font-size: 13px; -fx-font-weight: bold; " +
                   "-fx-background-radius: 8; -fx-cursor: hand;");
        return b;
    }
}