package up.csp;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import up.csp.constraint.Constraint;

/**
 * Test all the constraints classes
 * @author Chloé LEMAIRE
 */
public class TestConstraint{

    @Test
    public void testUnary(){
        Variable a = new Variable("a",new Domain(0,10));
        Constraint equality = Constraint.equal(a,6);
        assertFalse(("a domain "+a.getDomain()),equality.check());
        equality.set();
        assertTrue(("a domain "+a.getDomain()+" a value : "+a.getValue()),equality.check());
        Constraint different = Constraint.different(a,5);
        different.set();
        assertTrue(different.check());
        a.getDomain().restoreValue(5);
        assertFalse(different.check());
    }

    @Test
    public void testBinary(){
        Variable a = new Variable("a",new Domain(0,20));
        Variable b = new Variable("b", new Domain(0,20));
        int constant = 0;
        Constraint equals = Constraint.equal(a,b);
        Constraint different = Constraint.different(a,b, constant);
        Constraint under = Constraint.under(a,b,constant);
        equals.set();
        different.set();
        under.set();
        assertTrue((a.toString()+"\n"+b.toString()),equals.check());
        assertTrue((a.toString()+"\n"+b.toString()),different.check());
        assertTrue((a.toString()+"\n"+b.toString()),under.check());


        Constraint set = Constraint.equal(a,1);
        set.set();
        set = Constraint.equal(b,5);
        set.set();

        assertFalse((a.toString()+"\n"+b.toString()+"\n"+equals.check()),equals.check());
        assertTrue((a.toString()+"\n"+b.toString()),different.check());
        assertTrue((a.toString()+"\n"+b.toString()),under.check());

        a.getDomain().restoreValue(6);
        set = Constraint.equal(a,6);
        set.set();
        assertFalse((a.toString()+"\n"+b.toString()),equals.check());
        assertTrue((a.toString()+"\n"+b.toString()),different.check());
        assertFalse((a.toString()+"\n"+b.toString()),under.check());

        constant = 3;
        different = Constraint.different(a,b, constant);
        under = Constraint.under(a,b,constant);
        assertFalse(("a domain "+a.getDomain()+"\n b domain"+b.getDomain()),equals.check());
        assertTrue(("a domain "+a.getDomain()+"\n b domain"+b.getDomain()),different.check());
        assertTrue(("a domain "+a.getDomain()+"\n b domain"+b.getDomain()),under.check());

        constant = -3;
        different = Constraint.different(a,b, constant);
        under = Constraint.under(a,b,constant);
        assertFalse(("a domain "+a.getDomain()+"\n b domain"+b.getDomain()),equals.check());
        assertTrue(("a domain "+a.getDomain()+"\n b domain"+b.getDomain()),different.check());
        assertFalse(("a domain "+a.getDomain()+"\n b domain"+b.getDomain()),under.check());
    }   

    @Test
    public void testAllDifferent(){
        ArrayList<Variable> vars = new ArrayList<>();
        for(int i=0;i<10;i++){
            Variable v = new Variable(Integer.toString(i),new Domain(0,20));
            vars.add(v);
            v.assign(i);
        }
        Constraint diff = Constraint.allDifferent(vars);
        assertTrue(diff.check());
        vars.get(9).assign(1);
        assertFalse(diff.check());
    }
}