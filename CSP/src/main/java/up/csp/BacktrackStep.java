package up.csp;

import java.util.ArrayList;

/**
 * Provides functionality to create and manage backtracking stack elements
 * <p>
 * Each stack elements is composed of a {@link Variable}, an integer {@link #value} and a {@link #reason}.
 * </p>
 * @author Antoine GIRET
 */
public class BacktrackStep {

	private final Variable var;
	private final int value;
	private final String reason;
    private final ArrayList<Variable> impactedVariables;

    /**
     * Constructor for BacktrackStep class.
     * @param var modified {@link Variable}
     * @param val tested value
     * @param reason reason for adding this step to the stack
     */
    public BacktrackStep(Variable var, int val, String reason) {
        this.var = var;
        this.value = val;
        this.reason = reason;
        this.impactedVariables = new ArrayList<>();
    }

    /**
     * Records a variable that was impacted by Forward-Checking during this step.
     * @param v the {@link Variable} to add to the impact list
     */
    public void addImpactedVariable(Variable v) {
        this.impactedVariables.add(v);
    }

    /**
     * Reverts the changes made during this step.
     * Unassigns the main variable and restores the domains of all impacted variables.
     */
    public void undo() {
        // 1. Unassign the main variable
        this.var.unassign();

        // 2. Restore the value in all neighbor domains that were filtered by FC
        for (Variable v : impactedVariables) {
            v.getDomain().restoreValue(this.value);
        }
    }

    // --- Getters ---

    public Variable getVar(){
        return this.var;
    }

    public int getValue(){
        return  this.value;
    }

    public String getReason(){
        return this.reason;
    }
}