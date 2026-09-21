package src.hero.item;

import src.hero.Hero;

public class Food extends Item {

	protected int addedHealthAmount; // number of health that regen
	
	protected boolean consumable = true;
	
	public int getAddedHealthAmount() {
		return this.addedHealthAmount;
	}

	public Food(String theName,String description ,int amountHealth) {
		
		this.addedHealthAmount = amountHealth;
		this.desc = description;
		this.name = theName;
	}

	/**
	 *
	 * @author Kamardine
	 * @param theHero theHero that the food will operate
	 */
	public void consume(Hero theHero) {
		if (this.theBag == null || !(this.theBag).isOntheBag(this)) {
			System.out.println("this food are not in your bag");
		} else {
			if (theHero.getHealth() == theHero.getMaxHealth()) {
				System.out.println("je ne vais pas manger ma vie est deja au maximum");
			} else {
				theHero.regen(this);
				(this.theBag).removeItem(this);
			}
		}
	}

}