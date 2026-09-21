package src.enemy;

import src.game.World;
import src.hero.item.Item;
import src.zone.ElementType;

public class Witch extends Enemy {

	public Witch(int health, int attackPower, ElementType element, Item[] itemDroped, int coinsDropMin,
			int coinsDropMax, String name) {

		super(health, attackPower, element, itemDroped, coinsDropMin, coinsDropMax, name);

	}

	public boolean useSpecialAbility() {
		int succed = (int) (Math.random() * (100));
		return succed >= 50;
	}

	@Override
	public void takeDamage(int amount) {
		if (this.health - amount < 0) {
			this.health = 0;
		} else {
			this.health -= amount;

		}
	}

	public void throwPotion() {
		World.getPlayer().drainMana(10);
	}

	@Override
	public void attack() {
		if (useSpecialAbility()) {
			throwPotion();
			World.getPlayer().takeDamage(attackPower / 2);
		} else {
			World.getPlayer().takeDamage(attackPower);
		}
	}
}
