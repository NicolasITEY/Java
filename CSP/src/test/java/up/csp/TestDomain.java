package up.csp;

import org.junit.Test;
import org.junit.Assert;

/**
 * Test the {@link Domain} class
 * @author Evan Ribouleau
 * */

public class TestDomain {
    @Test
    public void testDomainCreation() {
        Domain domain = new Domain(1, 3);
        Assert.assertEquals(3, domain.size());
    }

    @Test
    public void testDomainContains() {
        Domain domain = new Domain(10, 20);
        Assert.assertTrue(domain.contains(10));
        Assert.assertTrue(domain.contains(15));
        Assert.assertFalse(domain.contains(30));
    }

    @Test
    public void testDomainCreationNegative() {
        Domain domain = new Domain(-4, -10);
        Assert.assertEquals(7, domain.size());
    }

    @Test
    public void testDomainRemoveValue() {
        Domain domain = new Domain(1, 10);
        domain.removeValue(5);
        Assert.assertFalse(domain.contains(5));
    }

    @Test
    public void testDomainRestoreValue() {
        Domain domain = new Domain(1, 10);
        domain.removeValue(5);
        domain.restoreValue(5);
        Assert.assertTrue(domain.contains(5));
    }

    @Test
    public void testDomainIntersection() {
        Domain domain1 = new Domain(1, 10);
        Domain domain2 = new Domain(5, 15);
        domain1.intersection(domain2);
        Assert.assertEquals(6, domain1.size());
        Assert.assertTrue(domain1.contains(5));
        Assert.assertTrue(domain1.contains(10));
        Assert.assertFalse(domain1.contains(1));
        Assert.assertFalse(domain1.contains(4));
        Assert.assertFalse(domain1.contains(11));
        Assert.assertFalse(domain1.contains(15));
    }

    @Test
    public void testDomainIntersectionEmpty() {
        Domain domain1 = new Domain(1, 10);
        Domain domain2 = new Domain(15, 20);
        domain1.intersection(domain2);
        Assert.assertEquals(0, domain1.size());
        Assert.assertFalse(domain1.contains(15));
        Assert.assertFalse(domain1.contains(20));
        Assert.assertFalse(domain1.contains(1));
        Assert.assertFalse(domain1.contains(10));
        Assert.assertFalse(domain1.contains(11));
    }

    @Test
    public void testDomainGetMin() {
        Domain domain = new Domain(1, 10);
        Assert.assertEquals(1, domain.getMin());
    }

    @Test
    public void testDomainGetMax() {
        Domain domain = new Domain(1, 10);
        Assert.assertEquals(10, domain.getMax());
    }
}
