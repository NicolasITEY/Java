package test.enemy;

import static org.junit.Assert.*;

import org.junit.Test;

import src.enemy.FinalBoss;
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

public class FinalBossTest {

	Item[] items = {};
	int aFinalBossHealth = 300;
	int aFinalBossAttackPower = 20;
	ElementType aFinalBossType = ElementType.LIFE;
	int aFinalBosscoinsDropMin = 65;
	int aFinalBosscoinsDropMax = 75;
	String aFinalBossName = "big boss";

	@Test
	public void TestConsFinalBoss() {

		FinalBoss aFinalBoss = new FinalBoss(aFinalBossHealth, aFinalBossAttackPower, aFinalBossName);

		assertEquals(aFinalBoss.getHealth(), aFinalBossHealth);
		assertEquals(aFinalBoss.getAttackPower(), aFinalBossAttackPower);
		assertSame(aFinalBoss.getItemDroped(), null);
		assertEquals(aFinalBoss.getName(), aFinalBossName);

	}

	@Test
	public void TestTakeDamage() {
		FinalBoss aFinalBoss = new FinalBoss(aFinalBossHealth, aFinalBossAttackPower, aFinalBossName);
		int damage = 20;
		int initialHealth = aFinalBoss.getHealth();

		aFinalBoss.takeDamage(damage);

		int newHealth = aFinalBoss.getHealth();

		assertEquals(newHealth, initialHealth - damage);
	}

	@Test
	public void TestTakeDamageChangeType() {
		FinalBoss aFinalBoss = new FinalBoss(aFinalBossHealth, aFinalBossAttackPower, aFinalBossName);
		int damage = 20;
		int initialHealth = aFinalBoss.getHealth();
		ElementType initialElement = aFinalBoss.getElement();

		aFinalBoss.takeDamage(damage);

		ElementType newElement = aFinalBoss.getElement();
		int newHealth = aFinalBoss.getHealth();

		assertNotEquals(newElement, initialElement);
		assertEquals(newHealth, initialHealth - damage);
	}

	@Test
	public void TestTakeDamage0Health() {
		FinalBoss aFinalBoss = new FinalBoss(aFinalBossHealth, aFinalBossAttackPower, aFinalBossName);
		int damage = 20000;

		aFinalBoss.takeDamage(damage);

		int newHealth = aFinalBoss.getHealth();

		assertEquals(newHealth, 0);
	}

}