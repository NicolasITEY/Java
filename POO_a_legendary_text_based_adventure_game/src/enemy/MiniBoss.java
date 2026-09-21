package src.enemy;

import src.hero.item.Item;
import src.zone.ElementType;

public abstract class MiniBoss extends Enemy {

	public MiniBoss(int health, int attackPower, ElementType element, Item[] itemDroped, int coinsDropMin,
			int coinsDropMax, String name) {

		super(health, attackPower, element, itemDroped, coinsDropMin, coinsDropMax, name);
	}

	public abstract void specialAbility();

	@Override
	public abstract void attack();

	public abstract void resetAbility();

	public abstract boolean useSpecialAbility();

}