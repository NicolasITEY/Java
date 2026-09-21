package test.enemy;

import static org.junit.Assert.*;

import org.junit.Test;

import src.enemy.Enemy;
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

public class EnemyTest {
	Item[] items = {};
	int aEnemyHealth = 45;
	int aEnemyAttackPower = 20;
	ElementType aEnemyType = ElementType.LIFE;
	int aEnemycoinsDropMin = 65;
	int aEnemycoinsDropMax = 75;
	String aEnemyName = "kennyyyyyy";

	@Test
	public void TestConsEnemy() {

		Enemy aEnemy = new Enemy(aEnemyHealth, aEnemyAttackPower, aEnemyType, items, aEnemycoinsDropMin,
				aEnemycoinsDropMax, aEnemyName);
		Enemy bEnemy = new Enemy(aEnemy);

		assertEquals(aEnemy.getHealth(), aEnemyHealth);
		assertEquals(aEnemy.getAttackPower(), aEnemyAttackPower);
		assertEquals(aEnemy.getElement(), aEnemyType);
		assertSame(aEnemy.getItemDroped(), items);
		assertEquals(aEnemy.getCoinsDropMin(), aEnemycoinsDropMin);
		assertEquals(aEnemy.getCoinsDropMax(), aEnemycoinsDropMax);
		assertEquals(aEnemy.getName(), aEnemyName);

		assertEquals(bEnemy.getHealth(), aEnemyHealth);
		assertEquals(bEnemy.getAttackPower(), aEnemyAttackPower);
		assertEquals(bEnemy.getElement(), aEnemyType);
		assertSame(bEnemy.getItemDroped(), items);
		assertEquals(bEnemy.getCoinsDropMin(), aEnemycoinsDropMin);
		assertEquals(bEnemy.getCoinsDropMax(), aEnemycoinsDropMax);
		assertEquals(bEnemy.getName(), aEnemyName);

	}

	/*
	 * @Test public void TestResetAbility(){ Enemy aEnemy = new
	 * Enemy(aEnemyHealth,aEnemyAttackPower,aEnemyType,items,aEnemycoinsDropMin,
	 * aEnemycoinsDropMax,aEnemyName); aEnemy.resetAbility();
	 * assertFalse(aEnemy.getStack()); }
	 */

	@Test
	public void TestAttack() {
		Enemy aEnemy = new Enemy(aEnemyHealth, aEnemyAttackPower, aEnemyType, items, aEnemycoinsDropMin,
				aEnemycoinsDropMax, aEnemyName);
		World.startGame();
		int heroDefaultHealth = World.getPlayer().getHealth();
		aEnemy.attack();
		assertEquals(World.getPlayer().getHealth(), heroDefaultHealth - aEnemy.getAttackPower());
	}

	@Test
	public void TestTakeDamage() {
		Enemy aEnemy = new Enemy(aEnemyHealth, aEnemyAttackPower, aEnemyType, items, aEnemycoinsDropMin,
				aEnemycoinsDropMax, aEnemyName);
		int damage = 20;
		int initialHealth = aEnemy.getHealth();

		aEnemy.takeDamage(damage);

		int newHealth = aEnemy.getHealth();

		assertEquals(newHealth, initialHealth - damage);
	}

	@Test
	public void TestTakeDamage0Health() {
		Enemy aEnemy = new Enemy(15, aEnemyAttackPower, aEnemyType, items, aEnemycoinsDropMin, aEnemycoinsDropMax,
				aEnemyName);
		int damage = 20000;

		aEnemy.takeDamage(damage);

		int newHealth = aEnemy.getHealth();

		assertEquals(newHealth, 0);
	}

	@Test
	public void TestIsDead1True() {
		Enemy aEnemy = new Enemy(0, aEnemyAttackPower, aEnemyType, items, aEnemycoinsDropMin, aEnemycoinsDropMax,
				aEnemyName);

		assertTrue(aEnemy.isDead());
	}

	@Test
	public void TestIsDead1False() {
		Enemy aEnemy = new Enemy(aEnemyHealth, aEnemyAttackPower, aEnemyType, items, aEnemycoinsDropMin,
				aEnemycoinsDropMax, aEnemyName);

		assertFalse(aEnemy.isDead());
	}

	@Test
	public void TestIsDead2() {
		Enemy aEnemy = new Enemy(aEnemyHealth, aEnemyAttackPower, aEnemyType, items, aEnemycoinsDropMin,
				aEnemycoinsDropMax, aEnemyName);

		aEnemy.takeDamage(200000);

		assertTrue(aEnemy.isDead());
	}
}
