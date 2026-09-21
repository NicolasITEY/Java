package src.enemy;

import src.game.World;
import src.hero.item.Item;
import src.zone.ElementType;

public class Litch extends MiniBoss {
	protected final int MULTIPLICATOR = 2;
	protected boolean stacked = false;

	public Litch(int health, int attackPower, ElementType element, Item[] itemDroped, int coinsDropMin,
			int coinsDropMax, String name) {

		super(health, attackPower, element, itemDroped, coinsDropMin, coinsDropMax, name);

	}

	public int getMULTIPLICATOR() {
		return this.MULTIPLICATOR;
	}

	public boolean getStack() {
		return this.stacked;
	}

	public void setStack(boolean theBool) {
		this.stacked = theBool;
	}

	@Override
	public boolean useSpecialAbility() {
		int succed = (int) (Math.random() * (100));
		return succed >= 35;
	}

	@Override
	public void resetAbility() {
		stacked = false;

	}

	@Override
	public void takeDamage(int amount) {
		if (this.health - amount < 0) {
			this.health = 0;
		} else {
			this.health -= amount;

		}
	}

	@Override
	public void specialAbility() {
		this.stacked = true;
	}

	@Override
	public void attack() {
		if (stacked) {
			World.getPlayer().takeDamage((MULTIPLICATOR * (this.attackPower)));
		} else {
			World.getPlayer().takeDamage(attackPower);
		}
	}
}