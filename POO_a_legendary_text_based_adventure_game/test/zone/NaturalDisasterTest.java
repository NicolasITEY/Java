package test.zone;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.enemy.Direwolf;
import src.enemy.Enemy;
import src.game.World;
import src.hero.Hero;
import src.hero.item.Food;
import src.hero.item.Item;
import src.zone.Biome;
import src.zone.ElementType;
import src.zone.FindStaffPiece;
import src.zone.Location;
import src.zone.NaturalDisaster;

;

public class NaturalDisasterTest {
	private Enemy[] enemies;
	private Direwolf direwolf;
	private Item[] items;
	private Biome biome1;
	private FindStaffPiece event;

	@Before
	public void setUp() {
		World.startGame();
		items = new Item[] { new Food("patate","a tater", 100) };
		enemies = new Enemy[] { new Enemy(10, 10, ElementType.CURSED, items, 10, 100, "skeleton"),
				new Enemy(10, 10, ElementType.CURSED, items, 10, 100, "Monster2") };
		direwolf = new Direwolf(1000, 1000, ElementType.LIFE, items, 100000, 1000000000, "String");
		biome1 = new Biome(enemies, direwolf, "Forest", ElementType.LIFE);
		event = new FindStaffPiece();
	}

	@Test
	public void testEventAddedIfUnderStepLimit() {
		World.startGame();
		Hero hero = World.getPlayer();

		while (hero.getStepCount() > 10) {
			hero.takeStep();
		}

		int[] exitIndices = { 1, 2 };
		String[] exitNames = { "North", "South" };
		String texts = "it's a location alright";
		Location loc = new Location(event, true, biome1, exitIndices, exitNames, texts);
		NaturalDisaster disaster = new NaturalDisaster(10,loc);

		disaster.doEvent();

		assertNotNull(loc.getEvent());
		assertTrue(loc.getEvent() instanceof FindStaffPiece);
	}

	@Test
	public void testEventNotAddedIfAboveStepLimit() {
		World.startGame();
		Hero hero = World.getPlayer();
		while (hero.getStepCount() > 31) {
			hero.takeStep();
		}
		int[] exitIndices = { 1, 2 };
		String[] exitNames = { "North", "South" };
		String texts = "it's a location alright";
		Location loc = new Location(null, true, biome1, exitIndices, exitNames, texts);
		NaturalDisaster disaster = new NaturalDisaster(30,loc);
		disaster.doEvent();

		assertNull(loc.getEvent());
	}

	@Test
	public void testStaffPieceAddedToBagWhenEventTriggered() {
		World.startGame();
		Hero hero = World.getPlayer();

		while (hero.getStepCount() > 10) {
			hero.takeStep();
		}
		int[] exitIndices = { 1, 2 };
		String[] exitNames = { "North", "South" };
		String texts = "it's a location alright";
		Location loc = new Location(null, true, biome1, exitIndices, exitNames, texts);
		NaturalDisaster disaster = new NaturalDisaster(30,loc);

		disaster.doEvent();
		loc.enterLocation();

		assertTrue(hero.getBag().staffInBag(3));
	}

}
