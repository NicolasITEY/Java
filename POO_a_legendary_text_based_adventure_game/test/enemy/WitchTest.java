package test.enemy;

import static org.junit.Assert.*;

import org.junit.Test;

import src.enemy.Witch;
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

public class WitchTest {

	Item[] items = {};
	int aWitchHealth = 45;
	int aWitchAttackPower = 20;
	ElementType aWitchType = ElementType.LIFE;
	int aWitchcoinsDropMin = 65;
	int aWitchcoinsDropMax = 75;
	String aWitchName = "blanche neige";

	@Test
	public void TestConsWitch() {

		Witch aWitch = new Witch(aWitchHealth, aWitchAttackPower, aWitchType, items, aWitchcoinsDropMin,
				aWitchcoinsDropMax, aWitchName);

		assertEquals(aWitch.getHealth(), aWitchHealth);
		assertEquals(aWitch.getAttackPower(), aWitchAttackPower);
		assertEquals(aWitch.getElement(), aWitchType);
		assertSame(aWitch.getItemDroped(), items);
		assertEquals(aWitch.getCoinsDropMin(), aWitchcoinsDropMin);
		assertEquals(aWitch.getCoinsDropMax(), aWitchcoinsDropMax);
		assertEquals(aWitch.getName(), aWitchName);

	}

	@Test
	public void TestTakeDamage() {
		Witch aWitch = new Witch(aWitchHealth, aWitchAttackPower, aWitchType, items, aWitchcoinsDropMin,
				aWitchcoinsDropMax, aWitchName);
		int damage = 20;
		int initialHealth = aWitch.getHealth();

		aWitch.takeDamage(damage);

		int newHealth = aWitch.getHealth();

		assertEquals(newHealth, initialHealth - damage);
	}

	@Test
	public void TestTakeDamage0Health() {
		Witch aWitch = new Witch(15, aWitchAttackPower, aWitchType, items, aWitchcoinsDropMin, aWitchcoinsDropMax,
				aWitchName);
		int damage = 20000;

		aWitch.takeDamage(damage);

		int newHealth = aWitch.getHealth();

		assertEquals(newHealth, 0);
	}

	@Test
	public void TestThrowPotion() {
		World.startGame();
		int manaHero = World.getPlayer().getMana();
		Witch aWitch = new Witch(aWitchHealth, aWitchAttackPower, aWitchType, items, aWitchcoinsDropMin,
				aWitchcoinsDropMax, aWitchName);
		aWitch.throwPotion();
		assertEquals(World.getPlayer().getMana(), manaHero - 10);
	}

	@Test
	public void TestAttackAbilitySuccessTrue() {
		World.startGame();
		Witch aWitch = new Witch(aWitchHealth, aWitchAttackPower, aWitchType, items, aWitchcoinsDropMin,
				aWitchcoinsDropMax, aWitchName);
		int heroDefaultHealth = World.getPlayer().getHealth(); // 100
		aWitch.attack();
		assertEquals(World.getPlayer().getHealth(), heroDefaultHealth - (aWitch.getAttackPower() / 2));

	}

	@Test
	public void TestAttackAbilitySuccessFalse() {
		World.startGame();
		Witch aWitch = new Witch(aWitchHealth, aWitchAttackPower, aWitchType, items, aWitchcoinsDropMin,
				aWitchcoinsDropMax, aWitchName);
		int heroDefaultHealth = World.getPlayer().getHealth(); // 100
		aWitch.attack();
		assertEquals(World.getPlayer().getHealth(), heroDefaultHealth - (aWitch.getAttackPower()));

	}
}
