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
import src.zone.EnemySpawn;

public class EnemySpawnTest {

	private Enemy[] enemies;
	private Direwolf direwolf;
	private Item[] items;

	private Biome biome1;
	private Hero hero;

	@Before
	public void setUp() {
		World.startGame(); // initializes World.hero
		hero = World.getPlayer();
		items = new Item[] { new Food("patate","une patate", 100) };
		enemies = new Enemy[] { new Enemy(10, 10, ElementType.CURSED, items, 10, 100, "skeleton"),
				new Enemy(10, 10, ElementType.CURSED, items, 10, 100, "Monster2") };
		direwolf = new Direwolf(1000, 1000, ElementType.LIFE, items, 100000, 1000000000, "String");
		biome1 = new Biome(enemies, direwolf, "Forest", ElementType.LIFE);

	}

	@Test
	public void testDoEventCreatesEnemies() {
		EnemySpawn spawn = new EnemySpawn(1);
		spawn.setBiome(biome1); // Il faudra ajouter un setter pour le biome
		int amount = 5;
		spawn.doEvent();

		assertEquals(amount, spawn.getEnemies().length);
		assertEquals(amount, spawn.getEnemyAmount());

		for (Enemy e : spawn.getEnemies()) {
			assertNotNull(e);
		}
	}

	@Test
	public void testHandleEnemiesEndsEventWhenAllDead() {
		EnemySpawn spawn = new EnemySpawn(1);
		spawn.setBiome(biome1);
		spawn.doEvent();

		// Simule que tous les ennemis sont morts
		for (Enemy e : spawn.getEnemies()) {
			e.setHealth(0);
		}

		spawn.handleEnemies();

		assertTrue(spawn.hasEventEnded());
	}

	@Test
	public void testHandleEnemiesAttacksPlayer() {
		EnemySpawn spawn = new EnemySpawn(1);
		spawn.setBiome(biome1);
		spawn.doEvent();

		Enemy enemy = spawn.getEnemies()[0];
		enemy.takeDamage(1);

		int healthBefore = hero.getHealth();

		spawn.handleEnemies();

		assertTrue(hero.getHealth() < healthBefore); // le joueur a été attaqué
	}

	@Test
	public void testBiomeGetters() {
		assertEquals("Forest", biome1.getBiomeName());
		assertEquals(ElementType.LIFE, biome1.getBiomeElement());
		assertNotNull(biome1.getEnemies());
		assertEquals(direwolf, biome1.getBiomeBoss());
		assertNull(biome1.getBiomeFinalBoss());
	}

	@Test
	public void testBiomeHasDirewolf() {
		assertTrue(biome1.getBiomeBoss() instanceof Direwolf);
	}

}
