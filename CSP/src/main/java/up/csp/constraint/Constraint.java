package up.csp.constraint;

import java.util.ArrayList;

import up.csp.Variable;

/**
 * Implementation of Constraint
 * @author Chloé Lemaire
 */
public abstract class Constraint {


	public abstract boolean check();
	public abstract void set();

	/** @author Chloé lemaire
	 * @param var variable which we want to be equal
	 * @param i value the variable needs to be equal to
	 * @return a constraint correspondig to var=i
	 */
	public static UnaryConstraint equal(Variable var,int i){
		return new UnaryConstraint(var, i, true);
	} 

	/** @author Chloé lemaire
	 * @param var variable we want to be different to 
	 * @param i the value that needs to be different
	 * @return a constraint representing var != i
	 */
	public static UnaryConstraint different(Variable var,int i){
		return new UnaryConstraint(var, i, false);
	} 
	
	/**@author Chloé lemaire
	 * @param a first variable for the equality
	 * @param b second variable for the equality
	 * @return constraint representing a=b
	 */
	public static BinaryConstraint equal(Variable a,Variable b){
		return new BinaryConstraint(a, b, 0, 'e');
	}
	
	/**@author Chloé lemaire
	 * @param a first variable in the difference 	 
	 * @param b second variable in the difference
	 * @param c the constant to add to b
	 * @return binary constraint representing a!=b+c
	 */
	public static BinaryConstraint different(Variable a,Variable b,int c){
		return new BinaryConstraint(a, b, c, 'd');
	}

	/**@author Chloé lemaire
	 * @param a first variable in the inequality 
	 * @param b second variable in the inequality
	 * @param c constant added to b
	 * @return constraint representing a<b+c
	 */
	public static BinaryConstraint under(Variable a,Variable b,int c){
		return new BinaryConstraint(a, b, c, 'u');
	}

	/** @author Chloé lemaire
	 * @param v list of variables that needs to be different
	 * @return returns a constraint where all the variables are different
	 */
	public static Constraint allDifferent(ArrayList<Variable> v){
		return new AllDifferent(v);
	}
}