package up.csp;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import up.csp.model.SudokuModel;

public class TestSudokuPerformance {

    // Nombre d'exécutions par stratégie pour lisser le temps moyen
    private static final int ITERATIONS = 100;

    @Test
    public void testCompareStrategiesPerformance() {
        System.out.println("\n=== Benchmark des Stratégies de Résolution de Sudoku ===");
        SudokuModel model = new SudokuModel();

        // Générer une grille Facile (pour éviter que l'aléatoire ne tourne à l'infini)
        model.setDifficulty(0);

        // Noms des stratégies pour faciliter la lecture des logs
        String[] varStratNames = {"Première non assignée", "Plus petit domaine", "Aléatoire"};
        String[] valStratNames = {"Ordre croissant", "Aléatoire"};

        for (int varStrat = 0; varStrat < 3; varStrat++) {
            for (int valStrat = 0; valStrat < 2; valStrat++) {
                double totalTimeMs = 0;

                for (int i = 0; i < ITERATIONS; i++) {
                    // Réinitialiser la grille au même état initial pour une comparaison juste
                    model.resetGrid();
                    
                    // Désactiver l'enregistrement de l'historique pour éviter de saturer la RAM (OutOfMemoryError)
                    model.getCsp().setStepListener(msg -> {});
                    
                    model.setStrategies(varStrat, valStrat);

                    long startTime = System.nanoTime();
                    // On appelle solve directement sur le CSP
                    boolean solved = model.getCsp().solve();
                    long endTime = System.nanoTime();

                    assertTrue("Le sudoku devrait être résolu", solved);
                    totalTimeMs += (endTime - startTime) / 1_000_000.0;
                }

                double avgTimeMs = totalTimeMs / ITERATIONS;
                System.out.println(String.format("Var: %-25s | Val: %-15s -> Temps moyen (%d itérations): %8.2f ms",
                        varStratNames[varStrat], valStratNames[valStrat], ITERATIONS, avgTimeMs));
            }
        }
        System.out.println("========================================================\n");
    }
}
