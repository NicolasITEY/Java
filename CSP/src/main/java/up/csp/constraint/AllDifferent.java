package up.csp.constraint;


import java.util.ArrayList;
import java.util.Objects;

import up.csp.Variable;

/**
 * Implementation of the <b>AllDifferent</b> constraint
 * <p>
 * This constraint ensures that all variables within a specified list hold distinct values.
 * </p>
 * * <p><b>Usage example:</b></p>
 * <pre>
 * List&lt;Variable&gt; vars = Arrays.asList(v1, v2, v3);
 * AllDifferent constraint = new AllDifferent(vars);
 * boolean isValid = constraint.check();
 * </pre>
 * @author Chloé Lemaire
 * @see Variable
 */
public class AllDifferent extends Constraint {
    private final ArrayList<Variable> variables;
    
    /**
    * @param differentVariables list of variables that needs to be different
    */
    protected AllDifferent(ArrayList<Variable> differentVariables){
        variables=differentVariables;
    }

    /**
     * Checks whether the constraint is currently satisfied.
     * <p>
     * This implementation uses a cross-comparison (double loop) to ensure that
     * no two variables share the same value.
     * </p>
     * @return {@code true} if all variables have distinct values (or if the list
     * contains fewer than two variables); {@code false} otherwise.
     */
    @Override
    public boolean check(){
        boolean isDifferent = true;
        for(int i=0;i<variables.size()-1 && isDifferent;i++){
            for(int j=i+1;j<variables.size() && isDifferent;j++){
                isDifferent=!Objects.equals(variables.get(i).getValue(), variables.get(j).getValue());
            }
        }
        return isDifferent;
    }

    @Override
    public void set(){
        for (Variable v : variables){
            if(v.isAssigned()){
                int assignedValue = v.getValue();
                for(Variable rest : variables){
                    if(rest!=v && rest.getDomain().contains(assignedValue)){
                        rest.getDomain().removeValue(assignedValue);
                    }
                }
            }
        }

    }
}