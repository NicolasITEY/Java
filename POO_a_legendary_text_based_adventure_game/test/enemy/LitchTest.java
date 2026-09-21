package test.enemy;

import static org.junit.Assert.*;

import org.junit.Test;

import src.enemy.Litch;
import src.game.World;
import src.hero.item.Item;
import src.zone.ElementType;

/*    JUnit
assertEquals(Object expected, Object actual)
assertSame(Object expected, Object actual)
assertNotSame(Object expected, Object actual)
assertNull(Object object)
assertNotNull(Object object)
assertTrue(boolean condition)
assertFalse(boolean condition)
fail(String)
*/

public class LitchTest {

	Item[] items = {};
	int aLitchHealth = 45;
	int aLitchAttackPower = 20;
	ElementType aLitchType = ElementType.LIFE;
	int aLitchcoinsDropMin = 65;
	int aLitchcoinsDropMax = 75;
	String aLitchName = "salopard";

	@Test
	public void TestConsLitch() {

		Litch aLitch = new Litch(aLitchHealth, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin,
				aLitchcoinsDropMax, aLitchName);

		assertEquals(aLitch.getHealth(), aLitchHealth);
		assertEquals(aLitch.getAttackPower(), aLitchAttackPower);
		assertEquals(aLitch.getElement(), aLitchType);
		assertSame(aLitch.getItemDroped(), items);
		assertEquals(aLitch.getCoinsDropMin(), aLitchcoinsDropMin);
		assertEquals(aLitch.getCoinsDropMax(), aLitchcoinsDropMax);
		assertEquals(aLitch.getName(), aLitchName);

	}

	@Test
	public void TestSetStackTrue() {
		Litch aLitch = new Litch(aLitchHealth, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin,
				aLitchcoinsDropMax, aLitchName);
		aLitch.setStack(true);
		boolean theBool = aLitch.getStack();
		assertTrue("assert true setStack", theBool);
	}

	@Test
	public void TestSetStackFalse() {
		Litch aLitch = new Litch(aLitchHealth, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin,
				aLitchcoinsDropMax, aLitchName);
		aLitch.setStack(false);
		boolean theBool = aLitch.getStack();
		assertFalse("assert false setStack", theBool);
	}

	@Test
	public void TestGetStackTrue() {
		Litch aLitch = new Litch(aLitchHealth, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin,
				aLitchcoinsDropMax, aLitchName);
		aLitch.setStack(true);
		boolean theBool = aLitch.getStack();
		assertTrue("assert true getStack", theBool);

	}

	@Test
	public void TestGetStackFalse() {
		Litch aLitch = new Litch(aLitchHealth, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin,
				aLitchcoinsDropMax, aLitchName);
		aLitch.setStack(false);
		boolean theBool = aLitch.getStack();
		assertFalse("assert false getStack", theBool);

	}

	@Test
	public void TestGetMultiplicator() {
		Litch aLitch = new Litch(aLitchHealth, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin,
				aLitchcoinsDropMax, aLitchName);
		assertEquals(aLitch.getMULTIPLICATOR(), 2);
	}

	@Test
	public void TestResetAbility() {
		Litch aLitch = new Litch(aLitchHealth, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin,
				aLitchcoinsDropMax, aLitchName);
		aLitch.resetAbility();
		assertFalse(aLitch.getStack());
	}

	@Test
	public void TestTakeDamage() {
		Litch aLitch = new Litch(aLitchHealth, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin,
				aLitchcoinsDropMax, aLitchName);
		int damage = 20;
		int initialHealth = aLitch.getHealth();

		aLitch.takeDamage(damage);

		int newHealth = aLitch.getHealth();

		assertEquals(newHealth, initialHealth - damage);
	}

	@Test
	public void TestTakeDamage0Health() {
		Litch aLitch = new Litch(15, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin, aLitchcoinsDropMax,
				aLitchName);
		int damage = 20000;

		aLitch.takeDamage(damage);

		int newHealth = aLitch.getHealth();

		assertEquals(newHealth, 0);
	}

	@Test
	public void TestSpecialAbility() {
		Litch aLitch = new Litch(aLitchHealth, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin,
				aLitchcoinsDropMax, aLitchName);
		aLitch.specialAbility();
		assertTrue(aLitch.getStack());

	}

	@Test
	public void TestAttackAbilitySuccessTrue() {
		World.startGame();
		Litch aLitch = new Litch(aLitchHealth, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin,
				aLitchcoinsDropMax, aLitchName);
		int heroDefaultHealth = World.getPlayer().getHealth(); // 100
		aLitch.specialAbility();
		aLitch.attack();
		assertEquals(World.getPlayer().getHealth(),
				heroDefaultHealth - (aLitch.getMULTIPLICATOR() * aLitch.getAttackPower()));

	}

	@Test
	public void TestAttackAbilitySuccessFalse() {
		World.startGame();
		Litch aLitch = new Litch(aLitchHealth, aLitchAttackPower, aLitchType, items, aLitchcoinsDropMin,
				aLitchcoinsDropMax, aLitchName);
		int heroDefaultHealth = World.getPlayer().getHealth(); // 100
		aLitch.attack();
		assertEquals(World.getPlayer().getHealth(), heroDefaultHealth - (aLitch.getAttackPower()));

	}

}
