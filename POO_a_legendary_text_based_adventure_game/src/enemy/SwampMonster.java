package src.enemy;

import src.game.World;
import src.hero.item.Item;
import src.zone.ElementType;

public class SwampMonster extends MiniBoss {

	protected int countdownDOT = 3;
	protected boolean canUseDot = false;

	public SwampMonster(int health, int attackPower, ElementType element, Item[] itemDroped, int coinsDropMin,
			int coinsDropMax, String name) {

		super(health, attackPower, element, itemDroped, coinsDropMin, coinsDropMax, name);

	}

	public int getCountdownDOT() {
		return this.countdownDOT;
	}

	public boolean getCanUseDot() {
		return this.canUseDot;
	}

	public void setCanUseDot(boolean bool) {
		this.canUseDot = bool;
	}

	public void setCountdownDOT(int amount) {
		this.countdownDOT = amount;
	}

	@Override
	public boolean useSpecialAbility() {
		int succed = (int) (Math.random() * (100));
		if (succed < 45) {
			return false;
		} else {
			countdownDOT = 3;
			return true;
		}
	}

	@Override
	public void resetAbility() {
		if (canUseDot && countdownDOT != 0) {
			this.countdownDOT--;
		}
		if (this.countdownDOT == 0) {
			canUseDot = true;
		}

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
		this.canUseDot = true;
	}

	@Override
	public void attack() {
		World.getPlayer().takeDamage(5);
		if (countdownDOT > 0 && canUseDot) {
			countdownDOT--;
			World.getPlayer().takeDamage(5);
		}
	}
}