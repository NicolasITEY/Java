package src.hero;

import src.enemy.Enemy;
import src.hero.item.Bag;
import src.hero.item.Food;
import src.hero.item.Grimoire;
import src.hero.item.Item;
import src.hero.item.Potion;
import src.zone.ElementType;
import src.zone.TypeChart;

public class Hero {

	protected Bag bag;
	protected Spell[] spells;
	protected Item Item;
	protected int stepCount;
	protected int mana;
	protected boolean hasBag;
	protected int money = 0;
	protected int health;
	protected final int MAXHEALTH = 100;
	protected final int MAXMANA = 20;

	/**
	 *
	 * Default Constructor of the hero
	 *
	 * @author Kamardine
	 *
	 */
	public Hero() {
		this.stepCount = 50;
		this.mana = 20;
		this.hasBag = true;
		this.bag = new Bag();
		this.money = 10;
		this.health = 100;
		this.spells = new Spell[4];
		spells[0] = new Spell(ElementType.FIRE, 15, 5, "fire bolt", "a small bolt of fire");
		spells[1] = new Spell(ElementType.CURSED, 15, 5, "curse", "places a curse on an enemy");
		spells[2] = new Spell(ElementType.LIFE, 15, 5, "roots", "ensnares an enemy in roots");
		spells[3] = new Spell(ElementType.MUD, 15, 5, "unsuspecting puddle",
				"places a seemingly tiny puddle over a deep sink hole");
	}

	public int getStepCount() {
		return this.stepCount;
	}

	public int getHealth() {
		return this.health;
	}

	public int getMaxHealth() {
		return this.MAXHEALTH;
	}

	public int getMana() {
		return this.mana;
	}

	public int getMaxMana() {
		return this.MAXMANA;
	}

	public void drainMana(int amount) {
		if (mana - amount > 0) {
			mana -= amount;
		} else {
			mana = 0;
		}
	}

	/**
	 * @author Kamardine
	 * @param theEnemy target
	 * @param theSpell the spell the hero will use
	 *
	 */
	public void useSpell(Enemy theEnemy, Spell theSpell) {
		if (this.mana > theSpell.getCost()) {
			if (TypeChart.isStrongAgainst(theSpell.getType(), theEnemy.getElement())) {
				System.out.println("it was super effective");
				theEnemy.takeDamage(theSpell.damage * 2);
			} else if (TypeChart.isWeakTo(theSpell.getType(), theEnemy.getElement())) {
				System.out.println("it wasn't effective");
				theEnemy.takeDamage(theSpell.damage / 2);
			} else {
				System.out.println("it was effective");
				theEnemy.takeDamage(theSpell.damage);
			}
			drainMana(theSpell.getCost());
		} else {
			System.out.println("not enough mana , hitting like a baby");
			theEnemy.takeDamage(5);
		}

	}

	public int getOccupiedSpace() {
		return this.bag.getOccupiedSpace();

	}

	/**
	 * Function that remove 1 step for the hero
	 *
	 * @author Kamardine
	 *
	 */
	public void takeStep() {
		if (this.stepCount == 1) {
			this.stepCount = 0;
			this.usedUpStep();
		} else {
			this.stepCount -= 1;
		}
	}

	/**
	 *
	 * @author Kamardine
	 * @param amount
	 */
	public void gainMoney(int amount) {
		this.money += amount;
	}

	/**
	 * @author Kamardine
	 * @param item item that will be added in the hero's bag
	 */
	public void gainItem(Item item) {
		if((this.bag).getBagCapacity()==0) {
			(this.bag).addItem(item);
		}
	}

	public Bag getBag() {
		return bag;
	}

	/**
	 *
	 * @author Kamardine
	 * @param amount money will be removed
	 */
	public boolean spendMoney(int amount) {
		if (money - amount >= 0) {
			this.money -= amount;
			return true;
		} else {
			System.out.println("couldn't spend " + amount + " monies");
			return false;
		}
	}

	public int getMoney() {
		return money;
	}

	/**
	 *
	 * @author Kamardine
	 * @param amount health will be removed
	 */
	public void takeDamage(int amount) {
		if ((this.health - amount) < 0) {
			this.health = 0;
			this.die();
		} else {
			this.health -= amount;
		}
	}

	public Spell getSpell(int i) {
		return this.spells[i];
	}
	
	public Spell getSpell(String name) {
		boolean found = false;
		int i;
		for(i=0;i<4||!found;i++) {
			if(spells[i].getName().equals(name)) {
				found=true;
			}
		}
		if(found) {
			return spells[i];
		}else {
			return null;
		}
	}

	/**
	 * method that call Game.World.gameOver()
	 *
	 * @author Kamardine
	 *
	 */
	public void die() {
		System.out.println("i am dead");
		// World.gameOver();
		// throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @author Kamardine
	 * @param item health will be added
	 */
	public void regen(Item item) {
		if (item instanceof Food) {
			int amountHP = ((Food) item).getAddedHealthAmount();
			if ((this.health + amountHP) > this.MAXHEALTH) {
				this.health = this.MAXHEALTH;
			} else {
				this.health += amountHP;
			}
		} else if (item instanceof Potion) {
			int amountMana = ((Potion) item).getAddedManaAmount();
			if ((this.mana + amountMana) > this.MAXMANA) {
				this.mana = this.MAXMANA;
			} else {
				this.mana += amountMana;
			}
		} else {
			System.out.println("ce n'est ni une potion ni une nourriture");
		}
	}

	public void upgradeSpell(Grimoire g) {
		spells[g.getId()] = g.getSpell();
	}

	/**
	 * Function called when all steps were used
	 *
	 * @author Kamardine
	 *
	 */
	public void usedUpStep() {
		System.out.println("All the steps were used");
		// World.gameOver();
	}

}
