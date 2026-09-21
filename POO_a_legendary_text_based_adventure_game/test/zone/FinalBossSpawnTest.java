package test.zone;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.enemy.FinalBoss;
import src.game.World;
import src.zone.Biome;
import src.zone.ElementType;
import src.zone.FinalBossSpawn;

public class FinalBossSpawnTest {

	private FinalBoss boss;

	private Biome biome;
	@Before
	public void setUp() {
		World.startGame(); // initializes World.hero
		boss = new FinalBoss(0, 0, "BBEG");
		biome = new Biome(null, boss, "Forest", ElementType.LIFE);

	}

	@Test
	public void testDoEventAssignsBoss() {

		FinalBossSpawn spawn = new FinalBossSpawn(0);
		spawn.setBiome(biome);
		spawn.doEvent();

		assertNotNull(spawn.getBoss());
		assertEquals(biome.getBiomeFinalBoss(), spawn.getBoss());
	}

	@Test
	public void testBossDead() {

		FinalBossSpawn spawn = new FinalBossSpawn(3);

		spawn.setBiome(biome);
		spawn.doEvent();

		boss.takeDamage(10000);
		spawn.handleBoss();

		assertNull(spawn.getBoss());
	}

	@Test
	public void testHandleBossAlive() {

		FinalBossSpawn spawn = new FinalBossSpawn(2);
		spawn.setBiome(biome);
		spawn.doEvent();

		int healthBefore = World.getPlayer().getHealth();

		spawn.handleBoss();
		assertTrue(World.getPlayer().getHealth() < healthBefore);

	}

	@Test
	public void testCooldownResetsAfterSpecialAbility() {

		FinalBossSpawn spawn = new FinalBossSpawn(3);
		spawn.setBiome(biome);
		spawn.doEvent();

		spawn.setCooldownTimer(0); // setter temporaire pour test
		spawn.handleBoss();

		assertEquals(3, spawn.getCooldownTimer());
	}

}
