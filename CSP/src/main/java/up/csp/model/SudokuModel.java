package up.csp.model;

import up.csp.mvc.Model;
import up.csp.CSP;
import up.csp.Domain;
import up.csp.Variable;
import up.csp.constraint.Constraint;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

public class SudokuModel implements Model {
    private CSP csp;
    private Variable[][] grid;
    private int difficulty;
    private List<String> terminalLogs;
    private int[][] initialGrid;

    public SudokuModel() {
        terminalLogs = new ArrayList<>();
        initialGrid = new int[9][9];
        initCSP();
    }

    public static class SudokuStep {
        public int[][] gridState;
        public String logMessage;
        public SudokuStep(int[][] gridState, String logMessage) {
            this.gridState = gridState;
            this.logMessage = logMessage;
        }
    }

    private List<SudokuStep> history = new ArrayList<>();
    private int currentStepIndex = 0;

    public void initCSP() {
        csp = new CSP();
        grid = new Variable[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = new Variable("V_" + i + "_" + j, new Domain(1, 9));
                csp.addVariable(grid[i][j]);
            }
        }

        for (int i = 0; i < 9; i++) {
            ArrayList<Variable> ligne = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                ligne.add(grid[i][j]);
            }
            csp.addConstraint(Constraint.allDifferent(ligne));
        }

        for (int j = 0; j < 9; j++) {
            ArrayList<Variable> colonne = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                colonne.add(grid[i][j]);
            }
            csp.addConstraint(Constraint.allDifferent(colonne));
        }

        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                ArrayList<Variable> bloc = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {

                        int realRow = (blockRow * 3) + i;
                        int realCol = (blockCol * 3) + j;
                        bloc.add(grid[realRow][realCol]);
                    }
                }
                csp.addConstraint(Constraint.allDifferent(bloc));
            }
        }
        csp.setStepListener(this::saveStateToHistory);
    }

    private void saveStateToHistory(String message) {
        int[][] currentState = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                currentState[i][j] = grid[i][j].isAssigned() ? grid[i][j].getValue() : 0;
            }
        }
        history.add(new SudokuStep(currentState, message));
    }

    public boolean nextStep() {
        if (currentStepIndex < history.size() - 1) {
            currentStepIndex++;
            log(history.get(currentStepIndex).logMessage);
            return true;
        }
        return false;
    }

    public boolean prevStep() {
        if (currentStepIndex > 0) {
            currentStepIndex--;
            log("Retour arrière manuel...");
            return true;
        }
        return false;
    }

    public SudokuStep getCurrentStep() {
        if (history.isEmpty()) return null;
        return history.get(currentStepIndex);
    }

    public void setDifficulty(int diff) {
        this.difficulty = diff;
        log("Génération d'une grille aléatoire " + (diff == 0 ? "Facile" : diff == 1 ? "Moyen" : "Difficile") + "...");

        initCSP();

        csp.setVarStrategy(1);
        csp.setValStrategy(1);

        csp.solve();

        int[][] grilleGeneree = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grilleGeneree[i][j] = grid[i][j].getValue();
            }
        }

        int casesAVider;
        if (diff == 0) {
            casesAVider = 35;
        } else if (diff == 1) {
            casesAVider = 48;
        } else {
            casesAVider = 58;
        }

        Random rand = new Random();
        int trousFaits = 0;
        while (trousFaits < casesAVider) {
            int r = rand.nextInt(9);
            int c = rand.nextInt(9);
            if (grilleGeneree[r][c] != 0) {
                grilleGeneree[r][c] = 0;
                trousFaits++;
            }
        }

        initCSP();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                initialGrid[i][j] = grilleGeneree[i][j];
                int valeur = grilleGeneree[i][j];
                if (valeur != 0) {
                    grid[i][j].assign(valeur);
                }
            }
        }

        csp.setVarStrategy(0);
        csp.setValStrategy(0);
        csp.forwardCheck();
        log("Grille générée avec succès !");
    }

    public void resetGrid() {
        log("Réinitialisation de la grille à son état de départ...");
        initCSP();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int valeur = initialGrid[i][j];
                if (valeur != 0) {
                    grid[i][j].assign(valeur);
                }
            }
        }
        csp.forwardCheck();
        log("Grille réinitialisée !");
    }

    public void jumpToLastStep() {
        if (!history.isEmpty()) {
            currentStepIndex = history.size() - 1;
            log("Saut direct au résultat final ! (" + history.size() + " étapes calculées)");
        }
    }

    public int[][] getInitialGrid() {
        return initialGrid;
    }

    public void setStrategies(int varStrategy, int valStrategy) {
        csp.setVarStrategy(varStrategy);
        csp.setValStrategy(valStrategy);
    }

    public void log(String message) {
        terminalLogs.add(message);
    }

    public List<String> getLogs() {
        return terminalLogs;
    }

    public Variable[][] getGrid() {
        return grid;
    }

    public CSP getCsp() {
        return csp;
    }

    @Override
    public void run() {
        log("Précalcul de la résolution...");
        history.clear();
        saveStateToHistory("État initial");
        csp.solve();
        currentStepIndex = 0;
        log("Précalcul terminé ! Utilisez les flèches pour avancer.");
    }
}