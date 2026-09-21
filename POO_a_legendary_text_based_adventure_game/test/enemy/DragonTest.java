package test.enemy;

import static org.junit.Assert.*;

import org.junit.Test;

import src.enemy.Dragon;
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

public class DragonTest {

	Item[] items = {};
	int aDragonHealth = 150;
	int aDragonAttackPower = 80;
	ElementType aDragonType = ElementType.FIRE;
	int aDragoncoinsDropMin = 165;
	int aDragoncoinsDropMax = 175;
	String aDragonName = "drogon";

	@Test
	public void TestConsDragon() {
		Dragon aDragon = new Dragon(aDragonHealth, aDragonAttackPower, aDragonType, items, aDragoncoinsDropMin,
				aDragoncoinsDropMax, aDragonName);

		assertEquals(aDragon.getHealth(), aDragonHealth);
		assertEquals(aDragon.getAttackPower(), aDragonAttackPower);
		assertEquals(aDragon.getElement(), aDragonType);
		assertEquals(aDragon.getCoinsDropMin(), aDragoncoinsDropMin);
		assertEquals(aDragon.getCoinsDropMax(), aDragoncoinsDropMax);
		assertEquals(aDragon.getName(), aDragonName);
	}

	@Test
	public void TestSetTargetableTrue() {
		Dragon aDragon = new Dragon(aDragonHealth, aDragonAttackPower, aDragonType, items, aDragoncoinsDropMin,
				aDragoncoinsDropMax, aDragonName);
		aDragon.settargetable(true);
		boolean theBool = aDragon.gettargetable();
		assertTrue("assert true setTargetable", theBool);
	}

	@Test
	public void TestSetTargatableFalse() {
		Dragon aDragon = new Dragon(aDragonHealth, aDragonAttackPower, aDragonType, items, aDragoncoinsDropMin,
				aDragoncoinsDropMax, aDragonName);
		aDragon.settargetable(false);
		boolean theBool = aDragon.gettargetable();
		assertFalse("assert false setTargetable", theBool);
	}

	@Test
	public void TestGetTargatableTrue() {
		Dragon aDragon = new Dragon(aDragonHealth, aDragonAttackPower, aDragonType, items, aDragoncoinsDropMin,
				aDragoncoinsDropMax, aDragonName);
		aDragon.settargetable(true);
		boolean theBool = aDragon.gettargetable();
		assertTrue("assert true getTargetable", theBool);

	}

	@Test
	public void TestResetAbility() {
		Dragon aDragon = new Dragon(aDragonHealth, aDragonAttackPower, aDragonType, items, aDragoncoinsDropMin,
				aDragoncoinsDropMax, aDragonName);
		boolean theBool = aDragon.gettargetable();
		aDragon.resetAbility();
		assertTrue(theBool);
	}

	@Test
	public void TestTakeDamage() {
		Dragon aDragon = new Dragon(aDragonHealth, aDragonAttackPower, aDragonType, items, aDragoncoinsDropMin,
				aDragoncoinsDropMax, aDragonName);
		int damage = 20;
		int initialHealth = aDragon.getHealth();

		aDragon.takeDamage(damage);

		int newHealth = aDragon.getHealth();

		assertEquals(newHealth, initialHealth - damage);
	}

	@Test
	public void TestTakeDamage0Health() {
		Dragon aDragon = new Dragon(aDragonHealth, aDragonAttackPower, aDragonType, items, aDragoncoinsDropMin,
				aDragoncoinsDropMax, aDragonName);
		int damage = 2000;

		aDragon.takeDamage(damage);

		int newHealth = aDragon.getHealth();

		assertEquals(newHealth, 0);
	}

	@Test
	public void TestSpecialAbilityTargetableFalse() {
		Dragon aDragon = new Dragon(aDragonHealth, aDragonAttackPower, aDragonType, items, aDragoncoinsDropMin,
				aDragoncoinsDropMax, aDragonName);

		aDragon.specialAbility();
		boolean isTargetable = aDragon.gettargetable();

		assertFalse(isTargetable);

	}

	@Test
	public void TestSpecialAbilityTargetable150() {
		Dragon aDragon = new Dragon(aDragonHealth, aDragonAttackPower, aDragonType, items, aDragoncoinsDropMin,
				aDragoncoinsDropMax, aDragonName);

		aDragon.takeDamage(5);
		aDragon.specialAbility();

		assertEquals(aDragon.getHealth(), 150);
	}

	@Test
	public void TestSpecialAbilityTargetableUnder140() {
		Dragon aDragon = new Dragon(aDragonHealth, aDragonAttackPower, aDragonType, items, aDragoncoinsDropMin,
				aDragoncoinsDropMax, aDragonName);

		aDragon.takeDamage(50);
		aDragon.specialAbility();

		assertEquals(aDragon.getHealth(), 110);
	}

	@Test
	public void TestAttackAbilitySuccessFalse() {
		World.startGame();
		Dragon aDragon = new Dragon(aDragonHealth, aDragonAttackPower, aDragonType, items, aDragoncoinsDropMin,
				aDragoncoinsDropMax, aDragonName);
		int heroDefaultHealth = World.getPlayer().getHealth();
		aDragon.attack();
		assertEquals(World.getPlayer().getHealth(), heroDefaultHealth - (aDragon.getAttackPower()));
	}

}
