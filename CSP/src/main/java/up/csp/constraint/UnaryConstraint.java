package up.csp.constraint;

import up.csp.Variable;

/**
 * Implementation of UnaryConstraint constraint
 * @author Chloé Lemaire
 */
public class UnaryConstraint extends Constraint{

    private final Variable var ;
    private final boolean equals ;
    private final int value;

    /**
    * @param v compared variable
    * @param value compared value
    * @param isEqual is true when you want the variable V to be equal to the value, false otherwise
    */
    protected UnaryConstraint(Variable v,int value,boolean isEqual){
        var = v;
        equals = isEqual;
        this.value =value; 
    }

    @Override
    public boolean check(){
        if(equals){
            if(var.isAssigned()){
                return var.getDomain().contains(value) || (var.isAssigned() && var.getValue()==value);
            }
        }else{
            if(var.isAssigned()){
                return !var.getDomain().contains(value);
            }
        }
        return false;
    }

    @Override
    public void set(){
        if(equals){
            if (var.getDomain().contains(value)){
                for(int i=var.getDomain().getMin();i<=var.getDomain().getMax();i++){
                    if (i!=value){
                        var.getDomain().removeValue(i);
                    }
                }
                var.assign(value);
            }
        }else{
            if(var.getDomain().contains(value)){
                var.getDomain().removeValue(value);
                var.unassign();
            }
        }
    }
}