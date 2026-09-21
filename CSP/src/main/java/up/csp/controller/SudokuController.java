package up.csp.controller;

import up.csp.model.SudokuModel;
import up.csp.mvc.Controller;
import up.csp.mvc.Model;
import up.csp.mvc.View;
import up.csp.Variable;
import up.csp.view.SudokuView;

public class SudokuController extends Controller {

    public SudokuController(Model p_model, View p_view) {
        super(p_model, p_view);
        initController();
    }

    private void initController() {
        SudokuView v = (SudokuView) view;
        SudokuModel m = (SudokuModel) model;

        v.btnFacile.setOnAction(e -> { m.setDifficulty(0); v.showScreen(v.configScreen); });
        v.btnMoyen.setOnAction(e -> { m.setDifficulty(1); v.showScreen(v.configScreen); });
        v.btnDifficile.setOnAction(e -> { m.setDifficulty(2); v.showScreen(v.configScreen); });

        v.btnSuivantConfig.setOnAction(e -> {
            int varStrat = v.comboVarStrategy.getSelectionModel().getSelectedIndex();
            int valStrat = v.comboValStrategy.getSelectionModel().getSelectedIndex();
            m.setStrategies(varStrat, valStrat);
            updateGridDisplay();
            v.showScreen(v.resolutionScreen);
        });

        v.btnStart.setOnAction(e -> {
            m.run();
            updateGridFromHistory();
            updateTerminal();
        });

        v.btnReset.setOnAction(e -> {
            m.resetGrid();

            int varStrat = v.comboVarStrategy.getSelectionModel().getSelectedIndex();
            int valStrat = v.comboValStrategy.getSelectionModel().getSelectedIndex();
            m.setStrategies(varStrat, valStrat);

            v.terminalArea.clear();
            updateGridDisplay();
        });

        v.btnNextStep.setOnAction(e -> {
            if (m.nextStep()) {
                updateGridFromHistory();
                updateTerminal();
            }
        });

        v.btnPrevStep.setOnAction(e -> {
            if (m.prevStep()) {
                updateGridFromHistory();
                updateTerminal();
            }
        });

        v.btnSolveImmediate.setOnAction(e -> {
            v.btnSolveImmediate.setDisable(true);
            v.btnStart.setDisable(true);

            m.log("Calcul de la résolution immédiate en cours...");
            updateTerminal();

            new Thread(() -> {

                m.run();
                m.jumpToLastStep();

                javafx.application.Platform.runLater(() -> {
                    updateGridFromHistory();
                    updateTerminal();

                    v.btnSolveImmediate.setDisable(false);
                    v.btnStart.setDisable(false);
                });

            }).start();
        });
    }

    private void updateGridDisplay() {
        SudokuView v = (SudokuView) view;
        SudokuModel m = (SudokuModel) model;
        Variable[][] grid = m.getGrid();
        int[][] initialGrid = m.getInitialGrid();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j].isAssigned()) {
                    v.gridFields[i][j].setText(String.valueOf(grid[i][j].getValue()));

                    if (initialGrid[i][j] != 0) {
                        v.gridFields[i][j].setStyle("-fx-text-fill: black; -fx-alignment: center; -fx-font-weight: bold; -fx-border-color: lightgray; -fx-background-color: #f4f4f4;");
                    }
                    else {
                        v.gridFields[i][j].setStyle("-fx-text-fill: red; -fx-alignment: center; -fx-font-weight: bold; -fx-border-color: lightgray;");
                    }
                } else {
                    v.gridFields[i][j].setText("");
                    v.gridFields[i][j].setStyle("-fx-alignment: center; -fx-border-color: lightgray; -fx-background-color: white;");
                }
            }
        }
    }

    private void updateGridFromHistory() {
        SudokuView v = (SudokuView) view;
        SudokuModel m = (SudokuModel) model;

        SudokuModel.SudokuStep currentStep = m.getCurrentStep();
        if (currentStep == null) return;

        int[][] state = currentStep.gridState;
        int[][] initial = m.getInitialGrid();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int val = state[i][j];
                if (val != 0) {
                    v.gridFields[i][j].setText(String.valueOf(val));
                    if (initial[i][j] != 0) {
                        v.gridFields[i][j].setStyle("-fx-text-fill: black; -fx-alignment: center; -fx-font-weight: bold; -fx-background-color: #f0f0f0;");
                    } else {
                        v.gridFields[i][j].setStyle("-fx-text-fill: red; -fx-alignment: center; -fx-font-weight: bold; -fx-background-color: white;");
                    }
                } else {
                    v.gridFields[i][j].setText("");
                    v.gridFields[i][j].setStyle("-fx-background-color: white;");
                }
            }
        }
    }

    private void updateTerminal() {
        SudokuView v = (SudokuView) view;
        SudokuModel m = (SudokuModel) model;
        v.terminalArea.setText(String.join("\n", m.getLogs()));
        v.terminalArea.setScrollTop(Double.MAX_VALUE);
    }
}
