package src.zone;

import src.enemy.Enemy;
import src.game.World;

public class EnemySpawn extends Event {

	private Biome biome;
	private Enemy[] enemies;
	private int enemyAmount = 0;

	public Enemy[] getEnemies() {
		return enemies;
	}

	public int getEnemyAmount() {
		return enemyAmount;
	}

	public void setBiome(Biome b) {
		biome = b;
	}

	public EnemySpawn(int amount) {
		enemyAmount = amount;
	}

	/**
	 *
	 * @param amount
	 */
	@Override
	public void doEvent() {
		enemies = new Enemy[enemyAmount];
		for (int i = 0; i < enemyAmount; i++) {
			if (Math.random() > 0.5) {
				enemies[i] = new Enemy(biome.getEnemies()[1]);
			} else {
				enemies[i] = new Enemy(biome.getEnemies()[0]);
			}
		}
	}

	public void handleEnemies() {
		int numEnemiesDead = 0;
		for (int i = 0; i < enemies.length; i++) {
			if (enemies[i] != null) {
				if (enemies[i].isDead()) {
					
					numEnemiesDead++;
				} else {
					enemies[i].attack(World.getPlayer());
				}
			} else {
				numEnemiesDead++;
			}
		}
		
		if (numEnemiesDead == enemyAmount) {
			for(Enemy enemy : enemies) {
				World.getPlayer().gainMoney(enemy.coinDrop());
				World.getPlayer().gainItem(enemy.giveitem());
			}
			endEvent();
			
		}
	}

}
