package src.enemy;

import src.game.World;
import src.zone.ElementType;

public class FinalBoss extends Enemy {
	protected boolean stealCapacity = false;
	protected boolean targetable = true;
	protected int countdownDOT = 0;
	private int typeIndex = 0;

	public FinalBoss(int health, int attackPower, String name) {

		super(health, attackPower, ElementType.NEUTRAL, null, 0, 0, name);
	}

	public boolean useSpecialAbility() {
		int succed = (int) (Math.random() * (100));
		return succed > 80;
	}

	public boolean getStealCapacity() {
		return this.stealCapacity;
	}

	public void setStealCapacity(boolean bool) {
		this.stealCapacity = bool;
	}

	public boolean getTargetable() {
		return this.targetable;
	}

	public void setTargetable(boolean bool) {
		this.targetable = bool;
	}

	public int getCountDownDOT() {
		return this.countdownDOT;
	}

	public int getTypeIndex() {
		return this.typeIndex;
	}

	/**
	 *
	 * @param amount
	 */
	@Override
	public void takeDamage(int amount) {
		if (targetable) {
			if (this.health - amount < 0) {
				this.health = 0;
			} else {
				this.health -= amount;
				ElementType[] types = ElementType.values();
				int next_type = (int) (Math.random() * types.length - 1);
				if (next_type >= typeIndex) {
					typeIndex = next_type + 1;
					element = types[typeIndex];
				} else {
					typeIndex = next_type;
					element = types[typeIndex];
				}
			}

		}
	}

	public double specialAbility() {
		if (useSpecialAbility()) {
			int choose = (int) (Math.random() * (4));
			switch (choose) {
			case 0:
				return 1.5;
			case 1:
				if (this.health < 280) {
					this.health = this.health + 20;
				} else {
					this.health = 300;
				}
				return 1;
			case 2:
				return 2;
			case 3:

				countdownDOT = 3;
				return 1;
			}
		}
		return 1;
	}

	@Override
	public void attack() {
		double res = specialAbility();
		World.getPlayer().takeDamage((int) (attackPower * res));
		if (countdownDOT > 0) {
			countdownDOT--;
			World.getPlayer().takeDamage(5);
		}
	}
}

// a lattaque du joueur c'est un type aleatoire choisit cacher a part dans le texte ecrit genre "on a sentie une odeur de mort dans son attaque"
// donc type mort cacher (ducoup moins de degat si on lance un type weak contre un autre type)