package src.zone;

import src.enemy.MiniBoss;
import src.game.World;

public class SpawnBoss extends Event {

	private Biome biome;
	private MiniBoss boss;
	private int cooldownTimer;
	private final int cooldownStart;

	@Override
	public void doEvent() {
		boss = biome.getBiomeBoss();
	}

	public SpawnBoss(int timerStart) {
		super();
		cooldownStart = timerStart;
		cooldownTimer = timerStart;
	}

	public void setCooldownTimer(int timer) {
		this.cooldownTimer = timer;
	}

	public int getCooldownTimer() {
		return this.cooldownTimer;
	}

	public MiniBoss getBoss() {
		return boss;
	}

	public void setBiome(Biome b) {
		biome = b;
	}

	public void handleBoss() {
		if (boss.isDead()) {
			World.getPlayer().gainMoney(boss.coinDrop());
			World.getPlayer().gainItem(boss.giveitem());
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
			boss.attack();
			boss.resetAbility();
		}
	}

}
