package up.csp;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import up.csp.constraint.Constraint;

public class TestBoiteNoir {

    private Domain domain; //Domain of the Variable

    private Variable var; // The variable 

        private Variable var2;
            private Variable var3;
    private Labelling labelling;

    @Before
    public void setUp() {
        domain = new Domain(10, 20);
        var = new Variable("Myname", domain);
        var2 = new Variable("papate", domain);
        var3 = new Variable("bouboule",domain);
        		labelling = new Labelling();

    }

    @Test
    public void testAddVariable() {
        CSP csp = new CSP();

        csp.addVariable(var);

        // Vérifie que la variable a été ajoutée
        assertEquals(1, csp.getVariables().size());
        assertTrue(csp.getVariables().contains(var));
    }

    @Test
    public void testAddConstraint() {
        CSP csp = new CSP();

        Constraint c = Constraint.equal(var, 5);

        csp.addConstraint(c);
        assertEquals(1, csp.getConstraints().size());
        assertTrue(csp.getConstraints().contains(c));

    }

    @Test
    public void testGetVariables() {
        CSP csp = new CSP();
        csp.addVariable(var);

        ArrayList<Variable> vars = csp.getVariables();

        assertEquals(1, vars.size());
        assertTrue(vars.contains(var));

        csp.addVariable(var2);
        vars = csp.getVariables();

        assertEquals(2, vars.size());
        assertTrue(vars.contains(var2));
    }



@Test 
public void testSolvebacktracking(){
        
    CSP csp = new CSP();

    csp.addVariable(var);
    csp.addVariable(var2);
    csp.addVariable(var3);

csp.addConstraint(Constraint.under(var, var2, 0)); 
    csp.addConstraint(Constraint.under(var2, var3, 0));
    csp.addConstraint(Constraint.different(var, 15)); 

    boolean result = csp.solve();

    assertTrue("Le solveur devrait trouver une solution (ex: 11, 12, 13)", result);

}

@Test
public void testSolveNoSolution() {
    CSP csp = new CSP();

     csp.addVariable(var);
    csp.addVariable(var2);
    csp.addVariable(var3);

    csp.addConstraint(Constraint.equal(var, 21));
    csp.addConstraint(Constraint.under(var, var2, 0));

    boolean result = csp.solve();

    assertFalse("Le solveur devrait renvoyer FALSE car 21 < var2 est impossible ici", result);
}
}