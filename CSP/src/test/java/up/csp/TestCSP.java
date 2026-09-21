package up.csp;

import org.junit.Test;
import up.csp.constraint.Constraint;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Test the {@link CSP} class
 * @author Chloé LEMAIRE
 */


public class TestCSP {

    @Test
    public void testReturnFalseWhenNotComplete() {
        CSP csp = new CSP();
        Variable x = new Variable("x", new Domain(1, 2));
        csp.addVariable(x);
        assertFalse(csp.isComplete());
    }

    @Test
    public void testSolveSimpleUnaryConstraint() {
        CSP csp = new CSP();
        Variable x = new Variable("x", new Domain(1, 3));
        csp.addVariable(x);
        csp.addConstraint(Constraint.equal(x, 2));
        assertTrue(csp.solve());
        assertTrue(csp.isComplete());
        assertTrue(csp.isSatisfied());
    }

    @Test
    public void testFailWhenConstraintsAreContradictory() {
        CSP csp = new CSP();
        Variable x = new Variable("x", new Domain(1, 1));
        csp.addVariable(x);
        csp.addConstraint(Constraint.equal(x, 1));
        csp.addConstraint(Constraint.different(x, 1));
        assertFalse("Solver should fail when constraints are contradictory", csp.solve());
    }

    @Test
    public void testForwardChecking(){
        CSP csp = new CSP();
        Variable x = new Variable("x", new Domain(0, 2));
        Variable y = new Variable("y", new Domain(0, 1));
        Variable z = new Variable("z", new Domain(0, 1));
        csp.addVariable(x);
        csp.addVariable(y);
        csp.addVariable(z);
        Constraint diff = Constraint.allDifferent(csp.getVariables());
        csp.addConstraint(diff);
        assertTrue("Solver failed to find a solution for allDifferent constraint", csp.solve());
        assertTrue("all variables aren't different : \n x = "+x.toString()+
        "\nvalue y = "+y.toString()+
        "\nvalue Z = "+z.toString(),diff.check());
        assertTrue("value x = "+x.getValue().toString(),x.isAssigned());
        assertTrue("value y = "+y.getValue().toString(),y.isAssigned());
        assertTrue("value Z = "+z.getValue().toString(),z.isAssigned());
    }


    /**
     * Tests a scenario that has no possible solution.
     * The solver should return false without crashing.
     *
     * @author Evan Ribouleau
     */
    @Test
    public void testUnsolvableCSP() {
        CSP csp = new CSP();
        Variable x = new Variable("x", new Domain(0, 1));
        Variable y = new Variable("y", new Domain(0, 1));
        Variable z = new Variable("z", new Domain(0, 1));
        csp.addVariable(x);
        csp.addVariable(y);
        csp.addVariable(z);
        csp.addConstraint(Constraint.allDifferent(csp.getVariables()));
        assertFalse("CSP should not be solvable", csp.solve());
    }


    /**
     * Black Box Test: What happens if we try to solve an empty {@link CSP}?
     *
     * @author Evan Ribouleau
     * */
    @Test
    public void testEmptyCSPSolvesAutomatically() {
        CSP csp = new CSP();
        assertTrue("A CSP with no variables should be considered complete", csp.isComplete());
        assertTrue("A CSP with no constraints should be considered satisfied", csp.isSatisfied());
        assertTrue("An empty CSP should return true upon resolution", csp.solve());
    }

    /**
     * Black Box Test: Verify that encapsulation is respected.
     * Modifying the list returned by {@code getVariables()} should not affect the CSP.
     * @author Evan Ribouleau
     */
    @Test
    public void testGettersReturnCopies() {
        CSP csp = new CSP();
        csp.addVariable(new Variable("x", new Domain(1, 2)));

        ArrayList<Variable> externalList = csp.getVariables();
        externalList.clear(); // Maliciously attempting to clear the list

        assertEquals("Encapsulation failed, the internal list was modified!", 1, csp.getVariables().size());
    }

    /**
     * White Box Test: Ensure the for loop in {@code isComplete} stops
     * as soon as it finds an unassigned variable among assigned ones.
     * @author Evan Ribouleau
     */
    @Test
    public void testPartialCompletenessReturnsFalse() {
        CSP csp = new CSP();
        Variable v1 = new Variable("v1", new Domain(1, 1));
        Variable v2 = new Variable("v2", new Domain(2, 2));

        csp.addVariable(v1);
        csp.addVariable(v2);

        v1.assign(1); // v1 is assigned, but not v2

        assertFalse("isComplete should return false if at least one variable is unassigned", csp.isComplete());
    }

    /**
     * White Box Test: Ensure {@code isSatisfied} returns false upon the
     * first unmet constraint, even if preceding ones are met.
     * @author Evan Ribouleau
     */
    @Test
    public void testPartialSatisfactionReturnsFalse() {
        CSP csp = new CSP();
        Variable v = new Variable("v", new Domain(1, 1));
        csp.addVariable(v);
        v.assign(1);

        // The first constraint passes, the second fails
        csp.addConstraint(Constraint.equal(v, 1));
        csp.addConstraint(Constraint.different(v, 1));

        assertFalse("isSatisfied should return false as soon as one constraint fails", csp.isSatisfied());
    }

    /**
     * White Box Test: Cover the for loop in {@code solve()} and the backtracking process.
     * Testing the block where {@code solve()} fails deep down and must clean up (restore domain/unassign).
     * @author Evan Ribouleau
     */
    @Test
    public void testSolveBacktrackingRestoresDomains() {
        CSP csp = new CSP();
        Variable x = new Variable("x", new Domain(1, 2));
        Variable y = new Variable("y", new Domain(2, 2));

        csp.addVariable(x);
        csp.addVariable(y);

        // x and y must be different, and y = 2.
        // If the strategy chooses to assign x=2 first, it will fail for y, triggering a backtrack.
        csp.addConstraint(Constraint.allDifferent(csp.getVariables()));

        assertTrue("The CSP should find the solution (x=1, y=2) via backtracking", csp.solve());
        assertEquals("After backtracking, x should be equal to 1", Integer.valueOf(1), x.getValue());
    }
}
