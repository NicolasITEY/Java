package src.enemy;

import src.game.World;
import src.hero.item.Item;
import src.zone.ElementType;

public class Direwolf extends MiniBoss {
	private boolean isAbilitySucces;
	private final static boolean DEFAULT_ABILITY_SUCCES = false;

	public Direwolf(int health, int attackPower, ElementType element, Item[] itemDroped, int coinsDropMin,
			int coinsDropMax, String name) {

		super(health, attackPower, element, itemDroped, coinsDropMin, coinsDropMax, name);
		this.isAbilitySucces = DEFAULT_ABILITY_SUCCES;

	}

	@Override
	public boolean useSpecialAbility() {
		int succed = (int) (Math.random() * (100));
		return succed > 50;
	}

	public boolean getIsAbilitySuccess() {
		return this.isAbilitySucces;
	}

	public void setIsAbilitySuccess(boolean theBool) {
		this.isAbilitySucces = theBool;
	}

	@Override
	public void resetAbility() {
		isAbilitySucces = false;

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
		// dée 20 entre 10 -20 sa marche
		int succed = (int) (Math.random() * (20));
		if (succed >= 10) {
			this.isAbilitySucces = true;

		}
	}

	@Override
	public void attack() {
		if (this.isAbilitySucces) {
			System.out.println("oulala il a reussi son lancer");
			World.getPlayer().takeDamage((2 * (this.attackPower)));

		} else {
			World.getPlayer().takeDamage(attackPower);

		}

	}
}