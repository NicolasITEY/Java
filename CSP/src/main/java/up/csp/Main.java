package up.csp;

import up.csp.constraint.Constraint;

public class Main{

    public static void main(String[] args) {
        CSP csp = new CSP();
        for(int i=0;i<9;i++){
            csp.addVariable(new Variable(Integer.toString(i),new Domain(1,9)));
        }
        Constraint diff = Constraint.allDifferent(csp.getVariables());
        csp.addConstraint(Constraint.equal(csp.getVariables().get(5), 4));
        csp.addConstraint(Constraint.equal(csp.getVariables().get(7), 3));
        csp.addConstraint(diff);
        csp.solve();
        for(Variable V : csp.getVariables()){
            System.out.println(V);
        }
    }
}