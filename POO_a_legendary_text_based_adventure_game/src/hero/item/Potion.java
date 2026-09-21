package src.hero.item;

import src.hero.Hero;

public class Potion extends Item {

	private int addedManaAmount; // number of mana that regen

	public int getAddedManaAmount() {
		return this.addedManaAmount;
	}

	public Potion(String theName,String description ,int amountMana) {
		this.addedManaAmount = amountMana;
		this.desc = description;
		this.name = theName;
	}

	/**
	 *
	 * @author Kamardine
	 * @param theHero theHero that the potion will operate
	 */
	public void consume(Hero theHero) {
		if (this.theBag == null || !(this.theBag).isOntheBag(this)) {
			System.out.println("this potion are not in your bag");
		} else {
			if (theHero.getMana() == theHero.getMaxMana()) {
				System.out.println("je ne vais pas boire ma mana est deja au maximum");
			} else {
				theHero.regen(this);
				(this.theBag).removeItem(this);
				this.theBag = null;
			}
		}
	}

}