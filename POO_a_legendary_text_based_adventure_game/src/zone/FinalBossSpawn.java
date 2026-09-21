package src.zone;

import src.enemy.FinalBoss;
import src.game.World;

public class FinalBossSpawn extends Event {

	private Biome biome;
	private FinalBoss boss;
	private int cooldownTimer;
	private final int cooldownStart;

	@Override
	public void doEvent() {
		boss = biome.getBiomeFinalBoss();
	}

	public void setBiome(Biome b) {
		biome = b;
	}

	public void setCooldownTimer(int timer) {
		this.cooldownTimer = timer;
	}

	public int getCooldownTimer() {
		return this.cooldownTimer;
	}

	public FinalBoss getBoss() {
		return boss;
	}

	public FinalBossSpawn(int timerStart) {
		cooldownStart = timerStart;
		cooldownTimer = timerStart;
	}

	public void handleBoss() {
		if (boss.isDead()) {
			boss = null;
		} else {
			if (cooldownTimer == 0) {
				if (boss.useSpecialAbility()) {
					boss.specialAbility();
					cooldownTimer = cooldownStart;
				}
			} else {
				cooldownTimer -= 1;
			}
			boss.attack(World.getPlayer());
		}
	}
}
