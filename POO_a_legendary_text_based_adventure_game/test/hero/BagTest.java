package test.hero;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.game.World;
import src.hero.Spell;
import src.hero.item.Bag;
import src.hero.item.Food;
import src.hero.item.Grimoire;
import src.hero.item.Staff;
import src.zone.ElementType;

public class BagTest {
	private Bag bag;
	private Food f1;
	private Food f2;
	private Grimoire g;
	private Staff s;

	@Before
	public void init() {
		World.startGame();
		bag = new Bag();
		f1 = new Food("numbah 9","da numba 9", 9);
		f2 = new Food("numbah 15","da numba 15", 15);
		g = new Grimoire(0,
				"grimoire containing the spell incinerate " ,
				new Spell(ElementType.FIRE, 25, 7, "incinerate","release a ball of fire within your hand and incinerate your foes")
				);
		s = new Staff(0,"default staff");
	}

	@Test
	public void testAdd() {

		bag.addItem(f1);
		bag.addItem(f2);
		assertEquals(bag.isOntheBag(f2), true);
		assertEquals(bag.isOntheBag(f1), true);
	}
	
	@Test
	public void testAddGrimoire() {
		bag.addItem(g);
		assertEquals(bag.isOntheBag(g), true);
		(bag.getItem(0)).consume();
		assertNotEquals(true, bag.isOntheBag(g));
	}
	
	@Test
	public void testAddStaff() {
		bag.addItem(s);
		assertEquals(bag.isOntheBag(s), true);
		(bag.getItem(0)).consume();
		assertNotEquals(true, bag.isOntheBag(s));
	}
}
