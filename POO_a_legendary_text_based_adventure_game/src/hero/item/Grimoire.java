package src.hero.item;

import src.game.World;
import src.hero.Spell;

public class Grimoire extends Item {

	private int grimoireId; // part of the grimoire
	private Spell newSpell;

	/**
	 *
	 * Constructor of Grimoire
	 *
	 * @author Kamardine
	 * @param id   id of the grimoire part
	 * @param name name of the spell
	 *
	 */
	public Grimoire(int id,String description, Spell spell) {
		this.grimoireId = id;
		this.desc = description;
		this.newSpell = spell;
	}

	public int getId() {
		return grimoireId;
	}

	public Spell getSpell() {
		return newSpell;
	}

	/**
	 * method that consume grimoire
	 *
	 * @author Kamardine
	 */
	@Override
	public void consume() {
		if (this.theBag == null) {
			System.out.println("this grimoire piece are not in a bag");
		} else {
			if (!(this.theBag.isOntheBag(this))) {
				System.out.println("this part " + this.grimoireId + " hasn't been found yet");
			} else if (theBag.grimoireInBag(grimoireId)) {
				System.out.println("you already found this grimoire");
			} else {
				(this.theBag).addGrimoire(grimoireId);
				World.getPlayer().upgradeSpell(this);
				(this.theBag).removeItem(this);
			}
		}
	}

}