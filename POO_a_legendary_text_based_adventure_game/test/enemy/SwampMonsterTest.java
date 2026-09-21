package test.enemy;

import static org.junit.Assert.*;

import org.junit.Test;

import src.enemy.Litch;
import src.enemy.SwampMonster;
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
public class SwampMonsterTest {

	// SM = Swamp Monster

	Item[] items = {};
	int aSMHealth = 45;
	int aSMAttackPower = 20;
	ElementType aSMType = ElementType.LIFE;
	int aSMcoinsDropMin = 65;
	int aSMcoinsDropMax = 75;
	String aSMName = "Mr Slimy";

	@Test
	public void TestConsLitch() {

		Litch aSM = new Litch(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax, aSMName);

		assertEquals(aSM.getHealth(), aSMHealth);
		assertEquals(aSM.getAttackPower(), aSMAttackPower);
		assertEquals(aSM.getElement(), aSMType);
		assertSame(aSM.getItemDroped(), items);
		assertEquals(aSM.getCoinsDropMin(), aSMcoinsDropMin);
		assertEquals(aSM.getCoinsDropMax(), aSMcoinsDropMax);
		assertEquals(aSM.getName(), aSMName);

	}

	@Test
	public void TestSetCanUseDotTrue() {
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		aSM.setCanUseDot(true);
		boolean theBool = aSM.getCanUseDot();
		assertTrue("assert true setCanUseDot", theBool);
	}

	@Test
	public void TestSetCanUseDotFalse() {
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		aSM.setCanUseDot(false);
		boolean theBool = aSM.getCanUseDot();
		assertFalse("assert false setCanUseDot", theBool);
	}

	@Test
	public void TestGetCanUseDotTrue() {
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		aSM.setCanUseDot(true);
		boolean theBool = aSM.getCanUseDot();
		assertTrue("assert true getStack", theBool);

	}

	@Test
	public void TestGetCanUseDotFalse() {
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		aSM.setCanUseDot(false);
		boolean theBool = aSM.getCanUseDot();
		assertFalse("assert false getStack", theBool);

	}

	@Test
	public void TestGetCountdownDOT() {
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		assertEquals(aSM.getCountdownDOT(), 3);
	}

	@Test
	public void TestResetAbility1() {
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		aSM.setCanUseDot(true);
		aSM.setCountdownDOT(3);
		aSM.resetAbility();
		assertEquals(aSM.getCountdownDOT(), 2);
	}

	@Test
	public void TestResetAbility2() {
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		aSM.setCountdownDOT(0);
		aSM.resetAbility();
		assertTrue(aSM.getCanUseDot());
	}

	@Test
	public void TestTakeDamage() {
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		int damage = 20;
		int initialHealth = aSM.getHealth();

		aSM.takeDamage(damage);

		int newHealth = aSM.getHealth();

		assertEquals(newHealth, initialHealth - damage);
	}

	@Test
	public void TestTakeDamage0Health() {
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		int damage = 20000;

		aSM.takeDamage(damage);

		int newHealth = aSM.getHealth();

		assertEquals(newHealth, 0);
	}

	@Test
	public void TestSpecialAbility() {
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		aSM.specialAbility();
		assertTrue(aSM.getCanUseDot());

	}

	@Test
	public void TestAttackAbilitySuccessTrue() {
		World.startGame();
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		int heroDefaultHealth = World.getPlayer().getHealth(); // 100
		aSM.specialAbility();
		aSM.attack();
		assertEquals(World.getPlayer().getHealth(), heroDefaultHealth);

	}

	@Test
	public void TestAttackAbilitySuccessFalse() {
		World.startGame();
		SwampMonster aSM = new SwampMonster(aSMHealth, aSMAttackPower, aSMType, items, aSMcoinsDropMin, aSMcoinsDropMax,
				aSMName);
		int heroDefaultHealth = World.getPlayer().getHealth(); // 100
		aSM.setCanUseDot(false);
		aSM.attack();
		assertEquals(World.getPlayer().getHealth(), heroDefaultHealth - 5);

	}
}
