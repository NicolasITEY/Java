package test.enemy;

import static org.junit.Assert.*;

import org.junit.Test;

import src.enemy.Direwolf;
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

public class DireWolfTest {

	Item[] items = {};
	int aDireWolfHealth = 45;
	int aDireWolfAttackPower = 20;
	ElementType aDireWolfType = ElementType.LIFE;
	int aDireWolfcoinsDropMin = 65;
	int aDireWolfcoinsDropMax = 75;
	String aDireWolfName = "loulou";

	@Test
	public void TestConsDireWolf() {

		Direwolf aDireWolf = new Direwolf(aDireWolfHealth, aDireWolfAttackPower, aDireWolfType, items,
				aDireWolfcoinsDropMin, aDireWolfcoinsDropMax, aDireWolfName);

		assertEquals(aDireWolf.getHealth(), aDireWolfHealth);
		assertEquals(aDireWolf.getAttackPower(), aDireWolfAttackPower);
		assertEquals(aDireWolf.getElement(), aDireWolfType);
		assertSame(aDireWolf.getItemDroped(), items);
		assertEquals(aDireWolf.getCoinsDropMin(), aDireWolfcoinsDropMin);
		assertEquals(aDireWolf.getCoinsDropMax(), aDireWolfcoinsDropMax);
		assertEquals(aDireWolf.getName(), aDireWolfName);

	}

	/*
	 * @Test public void TestUseSpecialAbilityTrue(){
	 *
	 * Direwolf aDireWolf = new
	 * Direwolf(aDireWolfHealth,aDireWolfAttackPower,aDireWolfType,items,
	 * aDireWolfcoinsDropMin,aDireWolfcoinsDropMax,aDireWolfName); boolean res =
	 * aDireWolf.useSpecialAbility();
	 *
	 * assertFalse("res return true",res); }
	 *
	 * @Test public void TestUseSpecialAbilityFalse(){
	 *
	 * Direwolf aDireWolf = new
	 * Direwolf(aDireWolfHealth,aDireWolfAttackPower,aDireWolfType,items,
	 * aDireWolfcoinsDropMin,aDireWolfcoinsDropMax,aDireWolfName);
	 *
	 * boolean res = aDireWolf.useSpecialAbility();
	 *
	 * assertFalse("res return false",res); }
	 */

	@Test
	public void TestSetIsAbilitySuccessTrue() {
		Direwolf aDireWolf = new Direwolf(aDireWolfHealth, aDireWolfAttackPower, aDireWolfType, items,
				aDireWolfcoinsDropMin, aDireWolfcoinsDropMax, aDireWolfName);
		aDireWolf.setIsAbilitySuccess(true);
		boolean theBool = aDireWolf.getIsAbilitySuccess();
		assertTrue("assert true setIsAbilitySuccess", theBool);
	}

	@Test
	public void TestSetIsAbilitySuccessFalse() {
		Direwolf aDireWolf = new Direwolf(aDireWolfHealth, aDireWolfAttackPower, aDireWolfType, items,
				aDireWolfcoinsDropMin, aDireWolfcoinsDropMax, aDireWolfName);
		aDireWolf.setIsAbilitySuccess(false);
		boolean theBool = aDireWolf.getIsAbilitySuccess();
		assertFalse("assert false setIsAbilitySuccess", theBool);
	}

	@Test
	public void TestResetAbility() {
		Direwolf aDireWolf = new Direwolf(aDireWolfHealth, aDireWolfAttackPower, aDireWolfType, items,
				aDireWolfcoinsDropMin, aDireWolfcoinsDropMax, aDireWolfName);
		boolean theBool = aDireWolf.getIsAbilitySuccess();
		aDireWolf.resetAbility();
		assertFalse(theBool);
	}

	@Test
	public void TestTakeDamage() {
		Direwolf aDireWolf = new Direwolf(aDireWolfHealth, aDireWolfAttackPower, aDireWolfType, items,
				aDireWolfcoinsDropMin, aDireWolfcoinsDropMax, aDireWolfName);
		int damage = 20;
		int initialHealth = aDireWolf.getHealth();

		aDireWolf.takeDamage(damage);

		int newHealth = aDireWolf.getHealth();

		assertEquals(newHealth, initialHealth - damage);
	}

	@Test
	public void TestTakeDamage0Health() {
		Direwolf aDireWolf = new Direwolf(15, aDireWolfAttackPower, aDireWolfType, items, aDireWolfcoinsDropMin,
				aDireWolfcoinsDropMax, aDireWolfName);
		int damage = 20;

		aDireWolf.takeDamage(damage);

		int newHealth = aDireWolf.getHealth();

		assertEquals(newHealth, 0);
	}

	@Test
	public void TestAttackAbilitySuccessTrue() {
		World.startGame();
		Direwolf aDireWolf = new Direwolf(aDireWolfHealth, aDireWolfAttackPower, aDireWolfType, items,
				aDireWolfcoinsDropMin, aDireWolfcoinsDropMax, aDireWolfName);
		int heroDefaultHealth = World.getPlayer().getHealth(); // 100
		aDireWolf.setIsAbilitySuccess(true);
		aDireWolf.attack();
		assertEquals(World.getPlayer().getHealth(), heroDefaultHealth - (2 * aDireWolf.getAttackPower()));

	}

	@Test
	public void TestAttackAbilitySuccessFalse() {
		World.startGame();
		Direwolf aDireWolf = new Direwolf(aDireWolfHealth, aDireWolfAttackPower, aDireWolfType, items,
				aDireWolfcoinsDropMin, aDireWolfcoinsDropMax, aDireWolfName);
		int heroDefaultHealth = World.getPlayer().getHealth(); // 100
		aDireWolf.setIsAbilitySuccess(false);
		aDireWolf.attack();
		assertEquals(World.getPlayer().getHealth(), heroDefaultHealth - (aDireWolf.getAttackPower()));

	}

}