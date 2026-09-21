package test.hero;

import static org.junit.Assert.*;

import org.junit.Test;

import src.hero.Hero;
import src.hero.Spell;
import src.hero.item.Food;
import src.hero.item.Grimoire;
import src.hero.item.Potion;

import src.zone.ElementType;
public class HeroTest {
	@Test
	public void init() {
		Hero hero = new Hero();
		assertEquals(hero.getHealth(), 100);
		assertEquals(hero.getStepCount(), 10);
		assertEquals(hero.getMaxHealth(), 100);
		assertEquals(hero.getMana(), 20);
		assertEquals(hero.getMaxMana(), 20);
		assertNotEquals(hero.getBag(), null);
	}

	@Test
	public void testDrainMana() {
		Hero hero = new Hero();
		int mana = hero.getMana();
		hero.drainMana(5);
		assertEquals(hero.getMana(), mana - 5);
	}

	@Test
	public void testTakeStep() {
		Hero hero = new Hero();
		int steps = hero.getStepCount();
		hero.takeStep();
		assertEquals(hero.getStepCount(), steps - 1);
	}

	@Test
	public void testHeroDrainedMana() {
		Hero hero = new Hero();
		hero.drainMana(hero.getMana() * 2);
		assertEquals(hero.getMana(), 0);
	}

	@Test
	public void testHeroDie() {
		Hero hero = new Hero();
		hero.takeDamage(hero.getHealth() * 2);
		assertEquals(hero.getHealth(), 0);
	}

	@Test
	public void testGainMoney() {
		Hero hero = new Hero();
		int curmoney = hero.getMoney();
		hero.gainMoney(15);
		assertEquals(hero.getMoney(), 15 + curmoney);
	}

	@Test
	public void testDrainMoney() {
		Hero hero = new Hero();
		hero.gainMoney(15);
		boolean success = hero.spendMoney(15);
		assertTrue(success);
		assertEquals(hero.getMoney(), 5);
	}

	@Test
	public void testDrainTooMuchMoney() {
		Hero hero = new Hero();
		boolean success = hero.spendMoney(25);
		assertFalse(success);
		assertEquals(hero.getMoney(), 5);
	}

	@Test
	public void testHealFromItem() {
		Hero hero = new Hero();
		hero.takeDamage(15);
		Food f2 = new Food("numbah 15","numbah 15", 15);
		hero.regen(f2);
		assertEquals(hero.getHealth(), hero.getMaxHealth());
	}

	@Test
	public void testHealManaFromItem() {
		Hero hero = new Hero();
		hero.drainMana(15);
		Potion f2 = new Potion("numbah 15","numbah 15", 15);
		hero.regen(f2);
		assertEquals(hero.getMana(), hero.getMaxMana());
	}

	@Test
	public void testUpgradeSpell() {
		Hero hero = new Hero();
		Spell spell = hero.getSpell(0);
		Grimoire g = new Grimoire(0,"a grimoire" ,new Spell(ElementType.FIRE, 25, 7, "incinerate",
				"release a ball of fire within your hand and incinerate your foes"));
		hero.upgradeSpell(g);
		assertEquals(hero.getSpell(0), g.getSpell());
		assertNotEquals(hero.getSpell(0), spell);
	}
}
