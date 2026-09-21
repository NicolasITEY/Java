package src.zone;

import src.enemy.Enemy;
import src.enemy.FinalBoss;
import src.enemy.MiniBoss;

public class Biome {

	private final Enemy[] enemies;
	private final MiniBoss biomeBoss;
	public final FinalBoss bossfinal;
	private final String name;
	private final ElementType element;

	public Biome(Enemy[] biomeEnemies, MiniBoss boss, String biomeName, ElementType biomeElement) {
		enemies = biomeEnemies;
		this.bossfinal = null;
		name = biomeName;
		biomeBoss = boss;
		element = biomeElement;

	}

	public Biome(Enemy[] biomeEnemies, FinalBoss boss, String biomeName, ElementType biomeElement) {
		enemies = biomeEnemies;
		this.biomeBoss = null;
		name = biomeName;
		bossfinal = boss;
		element = biomeElement;

	}

	public Enemy[] getEnemies() {
		return enemies;
	}

	public String getBiomeName() {
		return name;
	}

	public MiniBoss getBiomeBoss() {
		return biomeBoss;
	}

	public FinalBoss getBiomeFinalBoss() {
		return bossfinal;
	}

	public ElementType getBiomeElement() {
		return element;
	}
}
