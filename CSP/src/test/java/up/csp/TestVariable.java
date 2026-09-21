package up.csp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the {@link Variable} class
 * @author Nicolas ITEY
 */
public class TestVariable {

    private Domain domain; //Domain of the Variable
    private Variable var; // The variable 

    @Before
    public void setUp() {
        domain = new Domain(10, 20);
        var = new Variable("Myname", domain);

    }

    /**
     * Return {@code false}f for the function {@code isAssigned} because the
     * function has never been assigned
     */
    @Test
    public void shouldReturnFalseWhenNotAssigned() {

        assertFalse(var.isAssigned());
    }

    /**
     * Verify that the function {@code assign()} assigned a value to
     * the {@link Variable}
     */
    @Test
    public void shouldBeAssignedAfterAssignCall() {
        var.assign(15);
        assertTrue(var.isAssigned());
    }

    
    @Test
    public void shouldThrowExceptionWhenValueNotInDomain() {
        assertThrows(IllegalArgumentException.class, () -> {
            var.assign(5);
        });
    }

    /**
     * Verify {@code assign()}. Test if the value after being
     * assigned is unassigned if it truly dissapear and if the value is now {@code null}
     */
    @Test
    public void shouldUnassignTheValue() {
        var.assign(15);
        var.unassign();
        assertFalse(var.isAssigned());
        assertNull(var.getValue());

    }

    /**
     * Verify {@code unassign()}. Verifiy if unassign make something
     * bug when no value has been assigned before
     */
    @Test
    public void shouldRemainUnassignedIfAlreadyUnassigned() {
        // jamais assignée avant
        var.unassign();

        assertFalse(var.isAssigned());
        assertNull(var.getValue());
    }

    /**
     * Verify {@code getName()}. Verify if the name in the variable
     * is saved as the same as the String "My name" by comparing them
     */
    @Test
    public void shouldReturnCorrectName() {
        String expected_name = "Myname";
        String actualName = var.getName();

        assertEquals("The name must be the same",expected_name, actualName);
    }

    /**
     * Verify the methode {@code getDomain()} Verify if the Domain
     * (10,20) is saved in the {@link Variable}
     */
    @Test
    public void shouldReturnCorrectDomain() {
        assertSame("The domain must be the same",domain, var.getDomain());
    }

    /**
     *Verify the method {@code getValue()} Verify that if there is
     * no value assigned, it must be {@code null}
     */
    @Test
    public void shouldReturnNullWhenNotAssigned() {
        Integer actualValue = var.getValue();

        assertNull("Value should be null when variable is not assigned", actualValue);
    }

    /**
     * Verify the method {@code getValue()} We assign the value 15
     * before comparing if the {@link Variable} now has the value 15 in it with an
     * assert equal
     */
    @Test
    public void shouldReturnAssignedValue() {
        var.assign(15);

        Integer actualValue = var.getValue();

        assertEquals("Value should be the assigned value",actualValue.intValue(),15);
    }

    // =========================================================================
    // NEW TESTS (QA) - Black & White Box
    // =========================================================================

    /**
     * White Box Test: Verify the protected method {@link Variable#setDomain(Domain)}}.
     * This is crucial because it's used during the backtracking phase in the {@link CSP} class.
     * @author Evan RIBOULEAU
     */
    @Test
    public void testSetDomainUpdatesDomainReference() {
        Domain newDomain = new Domain(30, 40); // Assuming Domain constructor takes min, max
        var.setDomain(newDomain);

        assertSame("setDomain should replace the existing domain with the new one", newDomain, var.getDomain());
    }

    /**
     * Black Box Test: Verify that {@link Variable#toString()} returns a well-formatted string
     * containing both the name and the current value.
     * @author Evan RIBOULEAU
     */
    @Test
    public void testToStringFormatWhenAssigned() {
        var.assign(12);
        String output = var.toString();

        assertTrue("toString should contain the variable's name", output.contains("Myname"));
        assertTrue("toString should contain the assigned value", output.contains("12"));
        assertTrue("toString should mention the domain", output.contains("domain"));
    }

    /**
     * Black Box Test: Verify that {@link Variable#toString()} handles null values correctly
     * without throwing a {@link NullPointerException}.
     * @author Evan RIBOULEAU
     */
    @Test
    public void testToStringFormatWhenUnassigned() {
        String output = var.toString();

        assertTrue("toString should contain the variable's name even if unassigned", output.contains("Myname"));
        assertTrue("toString should print 'null' or handle the lack of value gracefully", output.contains("null"));
    }
}
