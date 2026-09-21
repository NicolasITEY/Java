package up.csp;

import java.util.ArrayList;
import java.util.List;

import up.csp.constraint.Constraint;

/**
 * Orchestrates the Constraint Satisfaction Problem (CSP) model and its resolution.
 * <p>
 * This class maintains the collection of variables and constraints that define the problem.
 * It implements the Backtracking search algorithm combined with Forward-Checking
 * propagation to find valid assignments.
 * </p>
 * @authors Vanyla MAIKOOUVA
 * @authors Chloé LEMAIRE
 */
public class CSP {

	private final ArrayList<Variable> variables;
	private final ArrayList<Constraint> constraints;
	private final Labelling labelling;
    private java.util.function.Consumer<String> stepListener;

	public CSP() 
	{
		variables = new ArrayList<>();
		constraints = new ArrayList<>();
		labelling = new Labelling();
	}

    public void setStepListener(java.util.function.Consumer<String> listener) {
        this.stepListener = listener;
    }

	/**
	 * Adds a {@link Variable} to this CSP.
	 *
	 * @param v variable to add
	 */
	public void addVariable(Variable v)
	{
		variables.add(v);
	}

	/**
	 * Adds a {@link Constraint} to this CSP.
	 *
	 * @param c constraint to add
	 */
	public void addConstraint(Constraint c)
	{
		constraints.add(c);
	}

	/**
	 * loops through all constraints and sets variables according to them
	 */
	public void forwardCheck() {
        for (Constraint constraint : constraints) {
            constraint.set();
        }
    }
	/**
	 * Returns a copy of variables.
	 *
	 * @return {@code ArrayList<Variable>} modifiable copy of current variables
	 */
	public ArrayList<Variable> getVariables()
	{
		return new ArrayList<>(variables);
	}

	/**
	 * Returns constraints as an unmodifiable collection.
	 *
	 * @return {@code ArrayList<Constraint>} an array list of constraints
	 */
	public ArrayList<Constraint> getConstraints()
	{
		return new ArrayList<>(constraints);
	}

	/**
	 * Sets variable selection strategy used by labelling.
	 *
	 * @param strategy from {@link Labelling}
	 */
	public void setVarStrategy(int strategy)
	{
		labelling.setVarStrategy(strategy);
	}

	/**
	 * Sets value ordering strategy used by labelling.
	 *
	 * @param strategy from {@link Labelling}
	 */
	public void setValStrategy(int strategy)
	{
		labelling.setValStrategy(strategy);
	}

	/**
	 * Checks whether all variables in the CSP are assigned.
	 *
	 * @return {@code true} if all variables are assigned, otherwise {@code false}
	 */
	public boolean isComplete()
	{
		for (Variable v : variables) {
			if (!v.isAssigned()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks whether every constraint is satisfied.
	 *
	 * @return {@code true} if all constraints are satisfied, otherwise {@code false}
	 */
	public boolean isSatisfied()
	{
		for (Constraint c : constraints) {
			if (!c.check()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Solves the CSP with recursive backtracking.
	 * 
	 * This method does not perform forward checking; it assigns values,
     * checks constraints only when a complete assignment is reached, and backtracks if the assignment 
     * does not satisfy all constraints.
	 * @return {@code true} if a complete satisfying assignment is found, otherwise {@code false}
	 */
    
	public boolean solve()
	{
		if (isComplete()) {
			return isSatisfied();
		} else {
			Variable variable = labelling.selectVariable(this);
			if (variable == null) {
				return false;
			} else {
				List<Integer> values = labelling.orderValues(variable);
				for (int value : values) {
					ArrayList<Domain> temp = new ArrayList<>();
					for(Variable v : variables){
						temp.add(v.getDomain().copy());
					}
					if(!variable.getDomain().contains(value)){
						continue;
					}
					BacktrackStep step = new BacktrackStep(variable, value, "Tentative assignment in solve");
					variable.assign(value);
					forwardCheck();

                    if(stepListener != null) stepListener.accept("Assignation : " + variable.getName() + " = " + value);

					if (solve()) {
						return true;
					} else {
                        if(stepListener != null) stepListener.accept("Backtrack sur : " + variable.getName() + " (retrait du " + value + ")");
						for(int i=0;i<temp.size();i++){
							Variable v = variables.get(i);
							Domain newDomain = temp.get(i);
							v.setDomain(newDomain);
						}
						step.undo();
						variable.getDomain().removeValue(value);
					}
				}
				return false;
			}
		}
	}
	

}
