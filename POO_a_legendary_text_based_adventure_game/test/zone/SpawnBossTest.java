package test.zone;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.enemy.Direwolf;
import src.enemy.Enemy;
import src.game.World;
import src.hero.item.Food;
import src.hero.item.Item;
import src.zone.Biome;
import src.zone.ElementType;
import src.zone.SpawnBoss;

public class SpawnBossTest {

	private Enemy[] enemies;
	private Direwolf direwolf;
	private Item[] items;

	private Biome biome1;

	@Before
	public void setUp() {
		World.startGame(); // initializes World.hero
		items = new Item[] { new Food("patate","a tater", 100) };
		enemies = new Enemy[] { new Enemy(10, 10, ElementType.CURSED, items, 10, 100, "skeleton"),
				new Enemy(10, 10, ElementType.CURSED, items, 10, 100, "Monster2") };
		direwolf = new Direwolf(1000, 1000, ElementType.LIFE, items, 100000, 1000000000, "String");
		biome1 = new Biome(enemies, direwolf, "Forest", ElementType.LIFE);

	}

	@Test
	public void testDoEventAssignsBoss() {
		SpawnBoss spawn = new SpawnBoss(3);
		spawn.setBiome(biome1);

		spawn.doEvent();

		assertNotNull(spawn.getBoss());
		assertEquals(direwolf, spawn.getBoss());
	}

	@Test
	public void testBossDead() {
		direwolf.setHealth(0);
		SpawnBoss spawn = new SpawnBoss(3);
		spawn.setBiome(biome1);
		spawn.doEvent();

		spawn.handleBoss();

		assertNull(spawn.getBoss());
	}

	@Test
	public void testHandleBossAlive() {
		SpawnBoss spawn = new SpawnBoss(2);
		spawn.setBiome(biome1);
		spawn.doEvent();

		int healthBefore = World.getPlayer().getHealth();

		spawn.handleBoss();
		assertTrue(World.getPlayer().getHealth() < healthBefore);

	}

	@Test
	public void testCooldownResetsAfterSpecialAbility() {
		SpawnBoss spawn = new SpawnBoss(3);
		spawn.setBiome(biome1);
		spawn.doEvent();

		spawn.setCooldownTimer(0); // setter temporaire pour test
		spawn.handleBoss();

		assertEquals(3, spawn.getCooldownTimer());
	}

}
