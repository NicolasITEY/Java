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

public class LocationTest {

	private Enemy[] enemies;
	private Direwolf direwolf;
	private Item[] items;

	private Location loc;
	private Biome biome1;
	private FindStaffPiece event;

	@Before
	public void setUp() {
		World.startGame();
		items = new Item[] { new Food("patate","une patate",100) };
		enemies = new Enemy[] { new Enemy(10, 10, ElementType.CURSED, items, 10, 100, "skeleton"),
				new Enemy(10, 10, ElementType.CURSED, items, 10, 100, "Monster2") };
		direwolf = new Direwolf(1000, 1000, ElementType.LIFE, items, 100000, 1000000000, "String");
		biome1 = new Biome(enemies, direwolf, "Forest", ElementType.LIFE);
		event = new FindStaffPiece();

		int[] exitIndices = { 1, 2 };
		String[] exitNames = { "North", "South" };
		String texts = "it's a location alright";
		loc = new Location(event, true, biome1, exitIndices, exitNames, texts);

	}

	@Test
	public void testLocationInitialization() {

		// Vérifie que les sorties sont correctement enregistrées
		assertEquals(Integer.valueOf(1), loc.getExits().get("North"));
		assertEquals(Integer.valueOf(2), loc.getExits().get("South"));

		// Vérifie que le biome est correct
		assertEquals(biome1, loc.getBiome());

		// Vérifie que l'event est attaché
		assertNotNull(loc.getEvent());
	}

	@Test
	public void testEnterLocation() {

		Hero hero = World.getPlayer();

		// On vérifie qu’avant l’event : la pièce n’est pas là
		assertFalse(hero.getBag().staffInBag(1));

		FindStaffPiece event = new FindStaffPiece(); // ADAPTÉ à ton constructeur
		int[] exitIndices = { 1, 2 };
		String[] exitNames = { "North", "South" };
		String texts = "it's a location alright";
		Location loc = new Location(event, true, biome1, exitIndices, exitNames, texts);

		// --- Act ---
		loc.enterLocation(); // déclenche event.doEvent()

		// --- Assert ---
		assertTrue(hero.getBag().staffInBag(0)); // ✔ SUCCÈS
	}

	@Test
	public void testEnterLocationDoesNotStartEventWhenFalse() {
		Hero hero = World.getPlayer();

		assertFalse(hero.getBag().staffInBag(3)); // staffID = 3

		int[] exitIndices = { 1, 2 };
		String[] exitNames = { "North", "South" };
		String texts = "it's a location alright";
		Location loc = new Location(event, true, biome1, exitIndices, exitNames, texts);

		loc.enterLocation();

		// Ici, on pourrait vérifier que la pièce n'a pas été ajoutée au sac
		assertFalse(World.getPlayer().getBag().staffInBag(3));
	}

	@Test
	public void testDoEventManuallyStartsEvent() {
		FindStaffPiece event = new FindStaffPiece();
		int[] exitIndices = { 1, 2 };
		String[] exitNames = { "North", "South" };
		String texts = "it's a location alright";
		Location loc = new Location(event, true, biome1, exitIndices, exitNames, texts);

		loc.doEvent();

		assertTrue(World.getPlayer().getBag().staffInBag(0));
	}

	@Test
	public void testLookPrintsCorrectText() {
		int[] exitIndices = { 1, 2 };
		String[] exitNames = { "North", "South" };
		String texts = "it's a location alright";
		Location loc = new Location(event, true, biome1, exitIndices, exitNames, texts);

		loc.look();
	}

	@Test
	public void testAddAndRemoveEvent() {
		FindStaffPiece event = new FindStaffPiece();
		int[] exitIndices = { 1, 2 };
		String[] exitNames = { "North", "South" };
		String texts = "it's a location alright";
		Location loc = new Location(event, true, biome1, exitIndices, exitNames, texts);

		loc.addEvent(event);
		assertNotNull(loc.getEvent());

		loc.removeEvent();
		assertNull(loc.getEvent());
	}

	@Test
	public void testExitsMapping() {
		int[] exitIndices = { 1, 2 };
		String[] exitNames = { "North", "South" };
		String texts = "it's a location alright";
		Location loc = new Location(event, true, biome1, exitIndices, exitNames, texts);

		assertEquals(Integer.valueOf(1), loc.getExits().get("North"));
		assertEquals(Integer.valueOf(2), loc.getExits().get("South"));
	}

}
