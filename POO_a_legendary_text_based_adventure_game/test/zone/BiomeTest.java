package test.zone;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.enemy.Direwolf;
import src.enemy.Dragon;
import src.enemy.Enemy;
import src.enemy.FinalBoss;
import src.enemy.Litch;
import src.enemy.SwampMonster;
import src.zone.Biome;
import src.zone.ElementType;

public class BiomeTest {

	private Enemy[] enemies;
	private Direwolf direwolf;
	private Dragon dragon;
	private Litch litch;
	private SwampMonster swampMonster;
	private FinalBoss finalBoss;

	private Biome biome1;
	private Biome biome2;
	private Biome biome3;
	private Biome biome4;
	private Biome biome5;

	@Before
	public void setUp() {
		enemies = new Enemy[] { new Enemy(10, 10, ElementType.CURSED, null, 10, 100, "skeleton"),
				new Enemy(10, 10, ElementType.CURSED, null, 10, 100, "Monster2") };

		direwolf = new Direwolf(0, 0, ElementType.LIFE, null, 0, 0, null);
		dragon = new Dragon(0, 0, ElementType.FIRE, null, 0, 0, null);
		litch = new Litch(0, 0, ElementType.CURSED, null, 0, 0, null);
		swampMonster = new SwampMonster(0, 0, ElementType.MUD, null, 0, 0, null);
		finalBoss = new FinalBoss(0, 0, null);
		biome1 = new Biome(enemies, direwolf, "Forest", ElementType.LIFE);
		biome2 = new Biome(enemies, litch, "Cemetary", ElementType.CURSED);
		biome3 = new Biome(enemies, dragon, "Volcano", ElementType.FIRE);
		biome4 = new Biome(enemies, swampMonster, "Swamp", ElementType.MUD);
		biome5 = new Biome(enemies, finalBoss, "Neutral Zone", ElementType.NEUTRAL);

	}

	@Test
	public void testBiome1HasMiniBoss() {
		assertNotNull(biome1.getBiomeBoss());
		assertNull(biome1.getBiomeFinalBoss());
	}

	@Test
	public void testBiome2HasFinalBoss() {
		assertNotNull(biome5.getBiomeFinalBoss());
		assertNull(biome5.getBiomeBoss());
	}

	@Test
	public void testEnemiesStoredCorrectly() {
		assertEquals(2, biome1.getEnemies().length);
		assertEquals("skeleton", biome1.getEnemies()[0].getName());
		assertEquals("Monster2", biome1.getEnemies()[1].getName());
	}

	@Test
	public void testBiomeElement() {
		assertEquals(ElementType.LIFE, biome1.getBiomeElement());
		assertEquals(ElementType.CURSED, biome2.getBiomeElement());
		assertEquals(ElementType.FIRE, biome3.getBiomeElement());
		assertEquals(ElementType.MUD, biome4.getBiomeElement());
		assertEquals(ElementType.NEUTRAL, biome5.getBiomeElement());

	}

	@Test
	public void testNameofBiome() {
		assertEquals("Forest", biome1.getBiomeName());
		assertEquals("Cemetary", biome2.getBiomeName());
		assertEquals("Volcano", biome3.getBiomeName());
		assertEquals("Swamp", biome4.getBiomeName());
		assertEquals("Neutral Zone", biome5.getBiomeName());
	}

	@Test
	public void testBiome1HasExactDirewolfInstance() {
		assertEquals(direwolf, biome1.getBiomeBoss());
	}

	@Test
	public void testBiome2HasExactLitchInstance() {
		assertEquals(litch, biome2.getBiomeBoss());
	}

	@Test
	public void testBiome1HasExactDragonInstance() {
		assertEquals(dragon, biome3.getBiomeBoss());
	}

	@Test
	public void testBiome1HasExactSwampInstance() {
		assertEquals(swampMonster, biome4.getBiomeBoss());
	}

	@Test
	public void testBiome1HasExactFinalBossInstance() {
		assertEquals(finalBoss, biome5.getBiomeFinalBoss());
	}

}
