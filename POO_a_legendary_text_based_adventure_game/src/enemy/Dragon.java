package src.enemy;

import src.game.World;
import src.hero.item.Item;
import src.zone.ElementType;

public class Dragon extends MiniBoss {

	protected final static boolean DEFAULT_TARGETABLE = true;
	protected boolean targetable;

	public Dragon(int health, int attackPower, ElementType element, Item[] itemDroped, int coinsDropMin,
			int coinsDropMax, String name) {

		super(150, attackPower, element, itemDroped, coinsDropMin, coinsDropMax, name);
		this.targetable = DEFAULT_TARGETABLE;
	}

	public boolean gettargetable() {
		return this.targetable;
	}

	public void settargetable(boolean theBool) {
		this.targetable = theBool;
	}

	@Override
	public boolean useSpecialAbility() {
		int succed = (int) (Math.random() * (100));
		return succed >= 30;
	}

	@Override
	public void resetAbility() {
		targetable = true;

	}

	@Override
	public void takeDamage(int amount) {
		if (targetable) {
			if (this.health - amount < 0) {
				this.health = 0;
			} else {
				this.health -= amount;

			}
		}

	}

	@Override
	public void specialAbility() {
		if (this.health < 140) {
			this.health += 10;
		} else {
			this.health = 150;
		}

		this.targetable = false;

	}

	@Override
	public void attack() {
		World.getPlayer().takeDamage(attackPower);
	}
}