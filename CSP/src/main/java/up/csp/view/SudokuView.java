package up.csp.view;

import up.csp.mvc.View;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SudokuView implements View {
    private Stage stage;
    private BorderPane root;

    public VBox difficultyScreen;
    public VBox configScreen;
    public BorderPane resolutionScreen;

    public Button btnFacile, btnMoyen, btnDifficile;

    public ComboBox<String> comboVarStrategy, comboValStrategy;
    public Button btnSuivantConfig;

    public TextField[][] gridFields;
    public TextArea terminalArea;
    public Button btnStart, btnNextStep, btnPrevStep, btnReset, btnSolveImmediate;

    public SudokuView(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Satisfy - Sudoku Solver");

        root = new BorderPane();
        Scene scene = new Scene(root, 850, 600);
        this.stage.setScene(scene);

        buildDifficultyScreen();
        buildConfigScreen();
        buildResolutionScreen();

        showScreen(difficultyScreen);
    }

    private void buildDifficultyScreen() {
        difficultyScreen = new VBox(20);
        difficultyScreen.setAlignment(Pos.CENTER);

        Label lbl = new Label("Choisissez le niveau de difficulté :");
        lbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        btnFacile = new Button("Facile");
        btnMoyen = new Button("Moyen");
        btnDifficile = new Button("Difficile");

        difficultyScreen.getChildren().addAll(lbl, btnFacile, btnMoyen, btnDifficile);
    }

    private void buildConfigScreen() {
        configScreen = new VBox(20);
        configScreen.setAlignment(Pos.CENTER);

        comboVarStrategy = new ComboBox<>();
        comboVarStrategy.getItems().addAll("Première non assignée", "Plus petit domaine", "Aléatoire");
        comboVarStrategy.getSelectionModel().selectFirst();

        comboValStrategy = new ComboBox<>();
        comboValStrategy.getItems().addAll("Ordre croissant", "Aléatoire");
        comboValStrategy.getSelectionModel().selectFirst();

        btnSuivantConfig = new Button("Suivant");

        configScreen.getChildren().addAll(
                new Label("Stratégie de sélection des variables :"), comboVarStrategy,
                new Label("Stratégie de sélection des valeurs :"), comboValStrategy,
                btnSuivantConfig
        );
    }

    private void buildResolutionScreen() {
        resolutionScreen = new BorderPane();
        resolutionScreen.setPadding(new Insets(20));

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(2); gridPane.setVgap(2);

        gridFields = new TextField[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField tf = new TextField();
                tf.setPrefSize(45, 45);
                tf.setAlignment(Pos.CENTER);
                tf.setEditable(false);
                tf.setStyle("-fx-font-size: 16px; -fx-border-color: lightgray;");
                gridFields[i][j] = tf;
                gridPane.add(tf, j, i);
            }
        }
        resolutionScreen.setCenter(gridPane);

        terminalArea = new TextArea();
        terminalArea.setEditable(false);
        terminalArea.setPrefWidth(250);
        terminalArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 12px;");
        resolutionScreen.setRight(terminalArea);

        HBox btnPanel = new HBox(15);
        btnPanel.setAlignment(Pos.CENTER);
        btnPanel.setPadding(new Insets(20, 0, 0, 0));

        btnSolveImmediate = new Button("Résolution Immédiate");
        btnStart = new Button("Démarrer Pas-à-Pas");
        btnNextStep = new Button("Étape suivante");
        btnPrevStep = new Button("Étape précédente");
        btnReset = new Button("Reset");

        btnPanel.getChildren().addAll(btnStart, btnPrevStep, btnNextStep, btnReset, btnSolveImmediate);
        resolutionScreen.setBottom(btnPanel);
    }

    // Méthode utilitaire pour naviguer entre les écrans
    public void showScreen(Pane screen) {
        root.setCenter(screen);
    }

    @Override
    public void hide() {
        stage.hide();
    }

    @Override
    public void show() {
        stage.show();
    }
}
