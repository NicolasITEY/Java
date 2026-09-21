package up.csp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;


/**
 * Test the {@link Labelling} class
 * @author Chloé LEMAIRE
 */
public class TestLabelling {

    private Labelling labelling;
    private CSP csp;
    private Variable x;
    private Variable y;

    @Before
    public void setUp() {
        labelling = new Labelling();
        csp = new CSP();
        x = new Variable("x", new Domain(1, 5));
        y = new Variable("y", new Domain(1, 2));
        csp.addVariable(x);
        csp.addVariable(y);
    }

    @Test
    public void testSelectFirstUnassignedVariable() {
        x.assign(1);
        labelling.setVarStrategy(Labelling.VAR_FIRST_UNASSIGNED);
        assertSame(y, labelling.selectVariable(csp));
    }

    @Test
    public void testSelectVariableWithSmallestDomain() {
        labelling.setVarStrategy(Labelling.VAR_MIN);
        assertSame(y, labelling.selectVariable(csp));
    }

    @Test
    public void testSelectFirstAvailableValueInOrder() {
        y.getDomain().removeValue(1);
        labelling.setValStrategy(Labelling.VAL_IN_ORDER);
        int selectedValue = labelling.selectValue(y);
        assertEquals(2, selectedValue);
    }

    // =========================================================================
    // NEW TESTS (QA) - Black & White Box
    // =========================================================================

    /**
     * Black Box Test: Verify that passing an unknown variable strategy
     * throws an {@link IllegalArgumentException}.
     * @author Evan RIBOULEAU
     */
    @Test
    public void testSetVarStrategyThrowsExceptionForInvalidInput() {
        assertThrows("Setting an invalid variable strategy should throw an exception",
                IllegalArgumentException.class, () -> {
                    labelling.setVarStrategy(99);
                });
    }

    /**
     * Black Box Test: Verify that passing an unknown value strategy
     * throws an {@link IllegalArgumentException}.
     * @author Evan RIBOULEAU
     */
    @Test
    public void testSetValStrategyThrowsExceptionForInvalidInput() {
        assertThrows("Setting an invalid value strategy should throw an exception",
                IllegalArgumentException.class, () -> {
                    labelling.setValStrategy(-1);
                });
    }

    /**
     * White Box Test: Ensure the {@link Labelling#selectVariable(CSP)} method safely handles a null {@link CSP} object.
     * @author Evan RIBOULEAU
     */
    @Test
    public void testSelectVariableReturnsNullWhenCspIsNull() {
        assertNull("Selecting a variable from a null CSP should return null", labelling.selectVariable(null));
    }

    /**
     * White Box Test: Ensure {@link Labelling#selectVariable(CSP)} returns null when all variables
     * in the CSP are already assigned (the problem is complete).
     * @author Evan RIBOULEAU
     */
    @Test
    public void testSelectVariableReturnsNullWhenAllAssigned() {
        x.assign(1);
        y.assign(2);
        assertNull("Should return null if there are no unassigned variables left", labelling.selectVariable(csp));
    }

    /**
     * White Box Test: Verify that {@link Labelling#VAR_RANDOM} strategy returns a valid,
     * unassigned variable without crashing.
     * @author Evan RIBOULEAU
     */
    @Test
    public void testSelectVariableRandomStrategy() {
        labelling.setVarStrategy(Labelling.VAR_RANDOM);
        Variable selected = labelling.selectVariable(csp);

        assertFalse("The randomly selected variable should not be assigned", selected.isAssigned());
        assertTrue("The randomly selected variable should be either x or y", selected == x || selected == y);
    }

    /**
     * White Box Test: Verify that {@link Labelling#selectValue(Variable)} throws an {@link IllegalArgumentException}
     * if there are no values left in the domain.
     * @author Evan RIBOULEAU
     */
    @Test
    public void testSelectValueThrowsExceptionWhenNoValuesAvailable() {
        Variable z = new Variable("z", new Domain(1, 1));
        z.getDomain().removeValue(1); // Domain is now empty

        assertThrows("Should throw an IllegalStateException if no values can be selected",
                IllegalStateException.class, () -> {
                    labelling.selectValue(z);
                });
    }

    /**
     * White Box Test: {@link Labelling#orderValues(Variable)} should return an empty list if the variable
     * or its domain is null.
     * @author Evan RIBOULEAU
     */
    @Test
    public void testOrderValuesReturnsEmptyListForNullVariable() {
        List<Integer> values = labelling.orderValues(null);
        assertTrue("Ordering values of a null variable should return an empty list", values.isEmpty());
    }

    /**
     * Black Box Test: Verify that {@link Labelling#VAL_RANDOM} strategy returns exactly the same
     * available values, just potentially in a different order.
     * @author Evan RIBOULEAU
     */
    @Test
    public void testOrderValuesRandomStrategyContainsAllValues() {
        labelling.setValStrategy(Labelling.VAL_RANDOM);
        List<Integer> orderedValues = labelling.orderValues(x); // x has domain 1 to 5

        assertEquals("The list should contain exactly 5 elements", 5, orderedValues.size());
        assertTrue("The list must contain the value 1", orderedValues.contains(1));
        assertTrue("The list must contain the value 5", orderedValues.contains(5));
    }
}
