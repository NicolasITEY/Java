package up.csp.constraint;

import up.csp.Variable;

/**
 * Implementation of BinaryConstraint constraint
 * @author Chloé Lemaire
 */
public class BinaryConstraint extends Constraint{
    private final Variable varA ;
    private final Variable varB ;
    private final int constant ;
    private final char mode;

    /**@author Chloé Lemaire
    @param a left variable in : a=b,a!=b+c and a<b+c
    @param b right variable in :  a=b,a!=b+c and a<b+c
    @param c constant in : a=b,a!=b+c and a<b+c
    @param mode character describing how to compare the variables e for equals d for different and u for under
    */
    protected BinaryConstraint(Variable a,Variable b,int constant,char mode){
        varA=a;
        varB=b;
        this.constant = constant;
        String allModes = "edu";
        if(allModes.indexOf(mode)!=-1){
            this.mode = mode;
        }else{
            this.mode = ' ';
        }
    }

    @Override
    public boolean check() {
        boolean res = false;
        switch (mode){
            case 'e' -> {
                    if(varA.isAssigned() && varB.isAssigned()){
                        return varA.getValue()==varB.getValue();
                    }
                    int min = varA.getDomain().getMin()<varB.getDomain().getMin()? varA.getDomain().getMin() : varB.getDomain().getMin();
                    int max= varA.getDomain().getMax()>varB.getDomain().getMax()? varA.getDomain().getMax() : varB.getDomain().getMax();
                    for(int i=min;i<max;i++){
                        if(varA.getDomain().contains(i) && varB.getDomain().contains(i)){
                            return true;
                        }
                        
                    }
                    return false;
                }
            case 'd' -> {
                    if(varA.isAssigned() && varB.isAssigned()){
                        return varA.getValue()!=varB.getValue()+constant;
                    }
                    int min = varA.getDomain().getMin()<varB.getDomain().getMin()? varA.getDomain().getMin() : varB.getDomain().getMin();
                    int max= varA.getDomain().getMax()>varB.getDomain().getMax()? varA.getDomain().getMax() : varB.getDomain().getMax();
                    int countSame = 0; 
                    for(int i=min;i<max;i++){
                        if(varA.getDomain().contains(i) && varB.getDomain().contains(i+constant)){
                            countSame++;
                        }
                    }
                    
                    return !(countSame==1 && varA.getDomain().size()==1  && varB.getDomain().size()==1);
                }
            case 'u' -> { //A<B+i
                    if(varA.isAssigned() && varB.isAssigned()){
                        return varA.getValue()<varB.getValue()+constant;
                    }
                    else if(varA.isAssigned()){
                        for(int i=varA.getValue()-constant+1;i<=varB.getDomain().getMax();i++){
                            if (varB.getDomain().contains(i)){
                                return true;
                            }
                        }
                    }
                    else if(varB.isAssigned()){
                        for(int i = varB.getValue()+constant-1;i>=varA.getDomain().getMin();i--){
                            if(varA.getDomain().contains(i)){
                                return true;
                            }
                        }
                    }else{
                        return !(varB.getDomain().getMax()+constant<=varA.getDomain().getMin()); 
                    }

                    return false;
                }
        }
        return res;
    }

    @Override 
    public void set(){
        switch(mode){
            case 'e' ->{varA.getDomain().intersection(varB.getDomain());varB.getDomain().intersection(varA.getDomain());}
            case 'd' ->{if(varA.isAssigned()){
                            varB.getDomain().removeValue(varA.getValue()-constant);
                        }
                        if(varB.isAssigned()){
                            varA.getDomain().removeValue(varB.getValue()+constant);
                        }
                    }
            case 'u' ->{
                if(varA.isAssigned()){
                    for(int i=varB.getDomain().getMin();i<varA.getValue()-constant;i++){
                        varB.getDomain().removeValue(i-constant);
                    }
                }
                if(varB.isAssigned()){
                    for(int i = varA.getDomain().getMax();i>=varB.getValue()+constant;i--){
                        varA.getDomain().removeValue(i);
                    }
                }

            }
        }
    }

}